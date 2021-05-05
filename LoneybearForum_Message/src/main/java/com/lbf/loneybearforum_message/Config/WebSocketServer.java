package com.lbf.loneybearforum_message.Config;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lbf.loneybearforum_message.Beans.MessageBean;
import com.lbf.loneybearforum_message.Beans.ResBean;
import com.lbf.loneybearforum_message.Mapper.MessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint("/message/websocket/connect/{uid}")
@Component
public class WebSocketServer {
    public static MessageMapper messageMapper;


    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId="";

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String userId) {
        this.session = session;
        this.userId=userId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
            //加入set中
        }else{
            webSocketMap.put(userId,this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }
        List<WebSocketServer> onlineUsers = getOnlineUsers();
        String onlineUser ="";
        log.info("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());
        for(WebSocketServer uid:onlineUsers){
            onlineUser+=(uid.userId+",");
        }
        log.info("当前在线用户:" + onlineUser );


        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("用户:"+userId+",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
        List<WebSocketServer> onlineUsers = getOnlineUsers();
        String onlineUser ="";
        for(WebSocketServer uid:onlineUsers){
            onlineUser+=(uid.userId+",");
        }
        log.info("当前在线用户:" + onlineUser );
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:"+userId+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //追加发送人(防止串改)
                jsonObject.put("fromUserId",this.userId);
                String toUserId=jsonObject.getString("toUserId");
                //传送给对应toUserId用户的websocket
                if(StringUtils.isNotBlank(toUserId)&&webSocketMap.containsKey(toUserId)){
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                }else{
                    log.error("请求的userId:"+toUserId+"不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

//    public void sendMessageEntry(Map map) throws EncodeException, IOException {
//        this.session.getBasicRemote().sendObject(map);
//    }



    public static void sendAll(String message, String html, String tittle, HttpServletRequest request) throws IOException, EncodeException {
        List<WebSocketServer> onlineUsers = getOnlineUsers();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        log.info("------开始群发------");
        int count = 0;
        for(WebSocketServer uid:onlineUsers){
            log.info(++count+".发送消息到:"+uid.userId+"，报文:"+message);
            if(StringUtils.isNotBlank(uid.userId)&&webSocketMap.containsKey(uid.userId)){
//                webSocketMap.get(uid.userId).sendMessage(message);

                MessageBean messagebean = new MessageBean();
                messagebean.setContent(message);
                messagebean.setClassificationId(0);
                messagebean.setHtml(html);
                messagebean.setTittle(tittle);
                messagebean.setUid(Long.parseLong(uid.userId));
                messagebean.setReceiverId(Long.parseLong(uid.userId));
                messagebean.setSenderId(1L);
                messagebean.setTime(df1.format(new Date()));
                Map<String,Object> map =new HashMap<>();
                map.put("status",new ResBean(200,"发送成功"));
                map.put("message",messagebean);
                if(messageMapper == null){
                    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                    messageMapper = wac.getBean(MessageMapper.class);
                }
                JSONObject json = new JSONObject(map);
                webSocketMap.get(uid.userId).sendMessage(json.toJSONString());
                messageMapper.insert(messagebean);
            }else{
                log.error("用户"+uid.userId+",不在线！");
            }
        }
        log.info("------群发结束------");
    }

    public static List<WebSocketServer> getOnlineUsers(){
        List list =new ArrayList();
        for(WebSocketServer value :webSocketMap.values()){
            list.add(value);
        }
        return list;
    }
    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message, @PathParam("userId") String userId) throws IOException {
        log.info("发送消息到:"+userId+"，报文:"+message);
        if(StringUtils.isNotBlank(userId)&&webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(message);
        }else{
            log.error("用户"+userId+",不在线！");
        }
    }

    public  synchronized int getOnlineCount() {
        return onlineCount;
    }

    public  synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public  synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}
