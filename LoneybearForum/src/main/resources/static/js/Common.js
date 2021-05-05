const VueCommon={
    data(){
        return {

        }
    },
    method: {
        // 关注分区
        follow: function () {
            var rawurl = window.location.href;
            var zid = Number(rawurl.slice(27,rawurl.length));
            var formdata = new FormData();
            formdata.append("zid",zid)
            axios({
                methods: 'post',
                url: '/zone/'+zid+"/followzone",
                data:formdata,
            })
                .then(function (response) {
                    console.log(response);

                })
                .catch(function (error) {
                    console.log(error);
                })
        },
        //收藏帖子
        collect: function () {
            var rawurl = window.location.href;
            var pid = Number(rawurl.slice(27,rawurl.length));
            var formdata = new FormData();
            var url = "/post/"+pid+"/collectpost";
            formdata.append("pid",pid)
            axios({
                cache: false,
                contentType: multipart/form-data,
                processData: false,
                methods: 'post',
                url: url,
                data:formdata,
            })
                .then(function (response) {
                    console.log(response);

                })
                .catch(function (error) {
                    console.log(error);
                })
        }
    }

}



// Vue.createApp(Vueapp).mount("#Vue")
var vuecommon = Vue.createApp(VueCommon).mount("#commonVue")
//html调用方法是creatAPp里面的，内容调用是第一个

// 鼠标悬浮上去后显示用户浮动小界面
function showDynamicUserDataFrame(){
    document.getElementById("dynamic_user_data").className="mini_userdata_contatiner_transparent";
}
function hideDynamicUserDataFrame(){
    document.getElementById("dynamic_user_data").className="mini_userdata_contatiner_transparent display_none";
}
function showDynamicMessageFrame(){
    document.getElementById("dynamic_message_data").className="mini_message_contatiner_transparent";
}
function hideDynamicMessageFrame(){
    document.getElementById("dynamic_message_data").className="mini_message_contatiner_transparent display_none";
}
function refresh(){
    location.reload();
}
function toTop(){
    document.body.scrollTop = 0;
    document.documentElement.scrollTop = 0;
}
function collectpost(){
    var rawurl = window.location.href;
    var pid = Number(rawurl.slice(27,rawurl.length));
    var formdata = new FormData();
    var url = "/post/"+pid+"/collectpost";
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

function floowButtonToggle(obj){
    switch ($(obj).attr("class")){
        case "margin_left_20px like_button":
            $(obj).attr("class","margin_left_20px like_button_checked");
            $(obj).text("已关注");
            break;

        case "margin_left_20px like_button_checked":
            $(obj).attr("class","margin_left_20px like_button");
            $(obj).text("+关注");
            break;

        case "like_button":
            $(obj).attr("class","like_button_checked");
            $(obj).text("已关注");
            break;

        case "like_button_checked":
            $(obj).attr("class","like_button");
            $(obj).text("+关注");
            break;
    }
}

function postCollectButtonToggle(obj){
    switch ($(obj).text()){
        case "收藏":
            $(obj).text("已收藏");
            break;

        case "已收藏":
            $(obj).text("收藏");
            break
    }


}

function postLikeButtonToggle(obj){
    switch ($(obj).text()){
        case "点赞":
            $(obj).text("已赞");
            break;

        case "已赞":
            $(obj).text("点赞");
            break
    }


}

function postdisLikeButtonToggle(obj){
    switch ($(obj).text()){
        case "点踩":
            $(obj).text("已踩");
            break;

        case "已踩":
            $(obj).text("点踩");
            break
    }


}

function thumbup(){

}

function thumbdown(){

}

function ajaxvisit(url){
    $.ajax({
        contentType:"application/json;charset=utf-8",
        type:"get",
        async:true,
        url:url,
        success:function(res) {
            console.log(res)
            if(res.code!=401 && res.code!=403){
                document.location.href=url;
            }
        },
        error:function(res){
            console.log("error");
        }
    });
}

Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,                   //月份
        "d+": this.getDate(),                        //日
        "h+": this.getHours(),                       //小时
        "m+": this.getMinutes(),                     //分
        "s+": this.getSeconds(),                     //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()                  //毫秒
    };

    //  获取年份
    // ①
    if (/(y+)/i.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }

    for (var k in o) {
        // ②
        if (new RegExp("(" + k + ")", "i").test(fmt)) {
            fmt = fmt.replace(
                RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}

var now = new Date();
var nowStr = now.format("YYYY-MM-DD"); // 2021-01-11