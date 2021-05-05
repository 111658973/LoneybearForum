const Vueapp={
    data(){
        return {
            zonedata:{},
            postdata:{},
            floorlist:[],
            userdata:{},
            floors:[],
            head:{},
            head_uid:null,
            head_iconurl:null,
            head_nick:null,
            isShow:true,
            isZoneFollowed:false,
            isPostCollected:false,
            isPostLiked:false,
            isPostdisliked:false,
            zoneFollowButtonClass:"margin_left_20px like_button",
            postCollectButtonText:"收藏",
            thumbUpText:"点赞",
            thumbDownText:"点踩",
            logined:false,
            notlogined:true,
            pid:null,

        }
    },
    methods:{
        getParam: function(){
            var rawurl = window.location.href;
            // return  rawurl.slice(27,rawurl.length);
            this.pid= rawurl.split("/")[4];
        },
        getResourses: function (){
            var param = this.getParam();
            return axios({
                methods: 'get',
                url: '/post/'+this.pid+'/getresources',
            });
        },
        getFollowInfo: function (){
            var param = this.getParam();
            return axios({
                methods: 'get',
                url: '/post/'+this.pid+"/getButtonsinfo",
            });
        },
        getuserdata: function (){
            return  axios({
                methods: 'get',
                url: '/GetUserFullDataFromSession',
            });
        }
    },
    mounted:function (){
        axios.all([
            this.getuserdata(),this.getResourses(),this.getFollowInfo()
        ])
            .then(axios.spread(function (res1,res2,res3){
                console.log("userdata",res1);
                vuePost.userdata = res1.data.data;
                if(res1.data.code!=-1){
                    vuePost.logined =true;
                    vuePost.notlogined =false;
                }

                console.log("resource",res2);
                vuePost.zonedata=res2.data.zonedata;
                vuePost.postdata = res2.data.postdata;
                vuePost.pid = res2.data.postdata.pid;

                if(res2.data.floors!=null){
                    vuePost.floors = res2.data.floors;
                }
                vuePost.head = res2.data.head;
                vuePost.head_iconurl = res2.data.head.userdata.iconUrl;
                vuePost.head_nick = res2.data.head.userdata.nick;
                vuePost.head_uid = res2.data.head.userdata.uid;


                console.log("followdata",res3);
                if(res3.data.code!=-1){
                    vuePost.isZoneFollowed=res3.data.ZoneFollowedInfo.isZoneFollowed;
                    if(vuePost.isZoneFollowed){
                        $("#like_button").attr("class","margin_left_20px like_button_checked");
                        $("#like_button").text("已关注");
                    }
                    vuePost.isPostCollected=res3.data.PostCollectInfo.isPostCillected;
                    if(vuePost.isPostCollected){
                        vuePost.postCollectButtonText = "已收藏";
                    }
                }


            }))
    }

        // axios({
        //     methods: 'get',
        //     url: '/post/'+param+'/getresources',
        // })
        //     .then(function (response) {
        //         console.log("postdata",response);
        //         vuePost.zonedata=response.data.zonedata;
        //         vuePost.postdata = response.data.postdata;
        //         vuePost.pid = response.data.postdata.pid;
        //
        //         if(response.data.floors!=null){
        //             vuePost.floors = response.data.floors;
        //         }
        //         vuePost.head = response.data.head;
        //         vuePost.head_iconurl = response.data.head.userdata.iconUrl;
        //         vuePost.head_nick = response.data.head.userdata.nick;
        //         vuePost.head_uid = response.data.head.userdata.uid;
        //
        //         // vuePost.isShow = true;
        //         // })
        //
        //     })
        //     .catch(function (error) {
        //         console.log(error);
        //     }),
        // axios({
        //     methods: 'get',
        //     url: '/post/'+param+"/getButtonsinfo",
        // })
        //     .then(function (response) {
        //         console.log("Buttonsinfo",response);
        //         if(response.data.code!=-1){
        //             vuePost.isZoneFollowed=response.data.ZoneFollowedInfo.isZoneFollowed;
        //             if(vuePost.isZoneFollowed){
        //                 $("#like_button").click();
        //             }
        //             vuePost.isPostCollected=response.data.PostCollectInfo.isPostCillected;
        //             if(vuePost.isPostCollected){
        //                 vuePost.postCollectButtonText = "已收藏";
        //             }
        //         }
        //
        //         // vuePost.isPostLiked=response.data.PostCollectInfo.
        //         // vuePost.isPostdisliked=response.data.PostCollectInfo.
        //
        //     })
        //     .catch(function (error) {
        //         console.log(error);
        //     }),
        // axios({
        //         methods: 'get',
        //         url: '/GetUserFullDataFromSession',
        //     })
        //     .then(function (response) {
        //             console.log("userdata",response);
        //             vuePost.userdata = response.data.data;
        //             if(response.data.code!=-1){
        //                 vuePost.logined =true;
        //                 vuePost.notlogined =false;
        //             }
        //             vuePost.isShow = true;
        //             // })
        //
        //         })
        //     .catch(function (error) {
        //             console.log(error);
        //         })



}

// Vue.createApp(Vueapp).mount("#Vue")
var vuePost = Vue.createApp(Vueapp).mount("#Vue")



function show_hide_reply(obj){
    switch ($(obj).text()){
        case "显示回复":
            $(obj).text("隐藏回复");
            $(obj).next().show();
            break;
        case "隐藏回复":
            $(obj).text("显示回复");
            $(obj).next().hide();
            break;
    }
}

// function show_hide_reply(obj){
//
//     $(obj).next().toggle(100,function (){
//         alert($(obj.text("显示评论")))
//         $(obj.text("显示评论")) ? $(obj).text("隐藏评论") : $(obj).text("显示评论")
//     });
// }

//楼中楼的回复
function reply(obj){
    var floorNumber = Number($(obj).attr("id").slice(1));
    var url = "/post/replyfloor/"+ floorNumber
    var content = $(obj).prev().val();
    var formdata = new FormData();
    formdata.append("content",content);
    formdata.append("pid",vuePost.pid);
    $.ajax({
        cache: false,
        contentType: false,
        processData: false,
        // contentType:"application/json;charset=utf-8",
        type:"post",
        url:url,
        data: formdata,
        async:true,
        success:function(res) {
            console.log(res)
            if(res.code!=-1){
                window.refresh();
            }
            else{
                alert(res.msg)
            }

        },
        error:function(res){
            console.log("error");
        }
    });

}


//主题回复
function ReplyTopic(){
    var content = editor.txt.html();
    var rawurl = window.location.href;
    var param = Number(rawurl.slice(27,rawurl.length));
    var formdata = new FormData();
    var url ="/post/replyTopic/"+param;
    formdata.append("content",content);
    formdata.append("pid",param);
    $.ajax({
        cache: false,
        contentType: false,
        processData: false,
        // contentType:"application/json;charset=utf-8",
        type:"post",
        url:url,
        data: formdata,
        async:true,
        success:function(res) {
            console.log(res)
            if(res.code==200){
                window.refresh();
            }
            else if(res.code==413){
                alert("用户被禁言")
            }
            else{
                alert(res.msg)
            }
        },
        error:function(res){
            console.log("error");
        }
    });

}


function checkIsTextEmpty(obj){
    var isTrue =false;
     $(obj).val()=="" ? isTrue=false:isTrue= true;
    if(isTrue){
        $(obj).next().removeClass("disabled");
    }
    else{
        $(obj).next().addClass("disabled")

    }
}

function followZone(){
    var zid = vuePost.zonedata.zid;
    var formdata = new FormData();
    var url = "/zone/"+zid+"/followzone";
    $.ajax({
        cache: false,
        contentType: false,
        processData: false,
        // contentType:"application/json;charset=utf-8",
        type:"post",
        url:url,
        data: formdata,
        async:true,
        success:function(res) {
            console.log(res)
            if(res.code!=-1){
                window.refresh();
            }
            else{
                alert(res.msg)
            }

        },
        error:function(res){
            console.log("error");
        }
    });

}