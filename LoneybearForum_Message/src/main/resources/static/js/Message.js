const Vueapp={
    data(){
        return {
            // show: {
            //     "s0": true,
            //     "s1": false,
            //     "s2": false,
            //     "s3": false,
            //     "s4": false,
            //     "s5": false,
            // }
            show_switch:[
                false,
                false,
                false,
                false,
                false,
            ],
            hint:"我的消息",
            hint_list:[
                "回复我的",
                "@我的",
                "收到的赞",
                "系统通知",
                "我的私信",
            ],
            loading:false,
            messages_reply:[],
            messages_at:[],
            messages_like:[],
            messages_notify:[],
            messages_mailbox:[],
            socket:null,
            userId:1,
            toUserId:'2',
            content:'3'
        }
    },
    methods:{
        getReply: function (){
            return axios({
                methods: 'get',
                url: '/message/getReply',
            });
        },
        getAt: function (){
            return axios({
                methods: 'get',
                url: '/message/getAt',
            });
        },
        getLike: function (){
            return axios({
                methods: 'get',
                url: '/message/getLike',
            });
        },
        getNotify: function (){

            return axios({
                methods: 'get',
                url: '/message/getNotify',
            });
        },
        getMailbox: function (){
            return axios({
                methods: 'get',
                url: '/message/getMailbox',
            });
        },
        openSocket() {
            if (typeof WebSocket == "undefined") {
                console.log("您的浏览器不支持WebSocket");
            } else {
                console.log("您的浏览器支持WebSocket");
                //实现化WebSocket对象，指定要连接的服务器地址与端口  建立连接
                //等同于socket = new WebSocket("ws://localhost:8888/xxxx/im/25");
                //var socketUrl="${request.contextPath}/im/"+$("#userId").val();
                var rawurl = window.location.href;
                var param = rawurl.split("/");
                var host = param[0]+"//"+param[2];
                var socketUrl = host+"/message/websocket/connect/"+ this.userId;
                socketUrl = socketUrl.replace("https", "ws").replace("http", "ws");
                console.log(socketUrl);
                if (this.socket != null) {
                    this.socket.close();
                    this.socket = null;
                }
                this.socket = new WebSocket(socketUrl);
                //打开事件
                this.socket = new WebSocket(socketUrl);
                //打开事件
                this.socket.onopen = function() {
                    console.log("websocket已打开");
                    //socket.send("这是来自客户端的消息" + location.href + new Date());
                };
                //获得消息事件
                this.socket.onmessage = function(msg) {
                    var res =JSON.parse(msg.data)
                    vueMessage.message_notify.push(res.message);
                    console.log("notify",vueMessage.message_notify);
                    vueMessage.show_switch[3]=false;
                    vueMessage.show_switch[3]=true;
                    console.log("获取消息成功");
                    //发现消息进入    开始处理前端触发逻辑
                };
                //关闭事件
                this.socket.onclose = function() {
                    console.log("websocket已关闭");
                };
                //发生了错误事件
                this.socket.onerror = function() {
                    console.log("websocket发生了错误");
                };
            }
        },
        sendMessage() {
            if (typeof WebSocket == "undefined") {
                console.log("您的浏览器不支持WebSocket");
            } else {
                console.log("您的浏览器支持WebSocket");
                console.log(
                    '{"toUserId":"' +
                    this.toUserId +
                    '","contentText":"' +
                    this.content +
                    '"}'
                );
                this.socket.send(
                    '{"toUserId":"' +
                    this.toUserId +
                    '","contentText":"' +
                    this.content +
                    '"}'
                );
            }
    },
        getUserData: function (){
            return axios({
                methods: 'get',
                url: 'csrf/GetUserFullDataFromSession',
            });
        }
    },
    mounted:function (){
        this.openSocket();
        this.loading=true;
        axios.all([
            this.getReply()
        ])
            .then(axios.spread(function (res1,res2){
                vueMessage.loading=false;
                $("#message_wrapper").attr("class","message_wrapper")
                console.log("userdata",res1);

                console.log("messages",res2)
                vueMessage.message_notify=res1.data.messages;
            }))
    }
}

// Vue.createApp(Vueapp).mount("#Vue")
var vueMessage = Vue.createApp(Vueapp).mount("#Vue")




function switchNav(obj){
    $(".message_sidebar_link").attr("class","message_sidebar_link init")
    $(obj).children(":first").addClass("checked")
    var id=$(obj).attr("id");
    var index = Number(id.slice(1,2));

    for(var i=0;i<vueMessage.show_switch.length;i++){
        vueMessage.show_switch[i]=false;
    }
    vueMessage.show_switch[index]=true;
    vueMessage.loading =false;
    $("#message_wrapper").attr("class","message_wrapper")
    vueMessage.hint=vueMessage.hint_list[index];
}

function showLoading(){
    $("#message_wrapper").attr("class","message_wrapper_loading");
    for(var i=0;i<vueMessage.show_switch.length;i++){
        vueMessage.show_switch[i]=false;
    }
    vueMessage.loading=true;
}

// var socket;
// function openSocket() {
//     if(typeof(WebSocket) == "undefined") {
//         console.log("您的浏览器不支持WebSocket");
//     }else{
//         console.log("您的浏览器支持WebSocket");
//         //实现化WebSocket对象，指定要连接的服务器地址与端口  建立连接
//         //等同于socket = new WebSocket("ws://localhost:8888/xxxx/im/25");
//         //var socketUrl="${request.contextPath}/im/"+$("#userId").val();
//         // var socketUrl="http://localhost:8081/message/notify/"+$("#userId").val();
//
//
//         var rawurl = window.location.href;
//         var param = rawurl.split("/");
//         var host = param[0]+"//"+param[2];
//         var socketUrl = host+"/message/websocket/connect/10";
//         socketUrl=socketUrl.replace("https","ws").replace("http","ws");
//         console.log(socketUrl);
//         if(socket!=null){
//             socket.close();
//             socket=null;
//         }
//         socket = new WebSocket(socketUrl);
//         //打开事件
//         socket.onopen = function() {
//             console.log("websocket已打开");
//             //socket.send("这是来自客户端的消息" + location.href + new Date());
//         };
//         //获得消息事件
//         socket.onmessage = function(msg) {
//             console.log(msg.data);
//             var res =JSON.parse(msg.data);
//             vueMessage.message_notify.push(res.message);
//             console.log("notify",this.message_notify);
//             //发现消息进入    开始处理前端触发逻辑
//         };
//         //关闭事件
//         socket.onclose = function() {
//             console.log("websocket已关闭");
//         };
//         //发生了错误事件
//         socket.onerror = function() {
//             console.log("websocket发生了错误");
//         }
//     }
// }
//
// function sendMessage() {
//     if(typeof(WebSocket) == "undefined") {
//         console.log("您的浏览器不支持WebSocket");
//     }else {
//         console.log("您的浏览器支持WebSocket");
//         console.log('{"toUserId":"'+$("#toUserId").val()+'","contentText":"'+$("#contentText").val()+'"}');
//         socket.send('{"toUserId":"'+$("#toUserId").val()+'","contentText":"'+$("#contentText").val()+'"}');
//     }
// }


document.getElementById("system").onscroll = function() {
//var height = document.getElementById("divData").offsetHeight;
//var height=$("#divData").height();//250
    var scrollHeight = document.getElementById("system").scrollHeight;
    var scrollTop = document.getElementById("system").scrollTop;
    var clientHeight = document.getElementById("system").clientHeight;
    if (scrollHeight - clientHeight == scrollTop) {
//滚动条滚到最底部
        alert("滚到了最底部");
    }
}
document.getElementById("message_wrapper").onscroll = function() {
//var height = document.getElementById("divData").offsetHeight;
//var height=$("#divData").height();//250
    var scrollHeight = document.getElementById("message_wrapper").scrollHeight;
    var scrollTop = document.getElementById("message_wrapper").scrollTop;
    var clientHeight = document.getElementById("message_wrapper").clientHeight;
    if (scrollHeight - clientHeight == scrollTop) {
//滚动条滚到最底部
        alert("滚到了最底部");
    }
}
document.getElementById("message_wrapper_loading").onscroll = function() {
//var height = document.getElementById("divData").offsetHeight;
//var height=$("#divData").height();//250
    var scrollHeight = document.getElementById("message_wrapper").scrollHeight;
    var scrollTop = document.getElementById("message_wrapper").scrollTop;
    var clientHeight = document.getElementById("message_wrapper").clientHeight;
    if (scrollHeight - clientHeight == scrollTop) {
//滚动条滚到最底部
        alert("滚到了最底部");
    }
}