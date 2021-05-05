package com.lbf.loneybearforum_message.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbf.loneybearforum_message.Beans.MessageBean;
import com.lbf.loneybearforum_message.Config.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface MessageService extends IService<MessageBean> {

    public List getAllMessagesByUid(Long uid);

    public List<Map<String,Object>> getMessageByUidAndType(Long uid,int type);

}
