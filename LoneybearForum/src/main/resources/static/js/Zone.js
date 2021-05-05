///WangEditor part



////Vue part
const VueappZone={
    data(){
        return {
            zurl:null,
            username: null,
            isShow: true,
            zone_data: {},
            user_data: {},
            posts_data: [],
            isZoneFollowed:false,
            isShow_post:true,
            isShow_good:false,
            good_data:[],
            logined:false,
            notlogined:true,
            show_refleshhit:false,
            uid:null,
            nick:null,
            iconUrl:null,
        }
    },
    methods: {
        initurl:function (){
            var rawurl = window.location.href;
            var param = rawurl.split("/")[4];
            this.zurl = param;
            console.log(param);
        },
        getzonesdata:function (){
            // var rawurl = window.location.href;
            // var param = rawurl.slice(27,rawurl.length)
            return axios({
                methods: 'get',
                url: '/zone/'+this.zurl+'/getzonesdata',
            })
        },
        getuserdata:function (){
            return axios({
                methods: 'get',
                url: '/GetUserFullDataFromSession',
            })
        },
        getzonepost:function (){
            var rawurl = window.location.href;
            var param = rawurl.slice(27,rawurl.length)
            return axios({
                methods: 'get',
                url: '/zone/'+this.zurl+'/getZonePostData/1',
            });

        },
        getFollowinfo:function (){
            var rawurl = window.location.href;
            var param = rawurl.slice(27,rawurl.length)
            return axios({
                methods: 'get',
                url: '/zone/'+this.zurl+"/getButtonsinfo",
            })
        },

    },
    mounted:function (){
        this.initurl();
        // axios.all([
        //     axios({
        //         methods: 'get',
        //         url: '/zone/'+param+'/getzonesdata',
        //     })
        //         .then(function (response) {
        //             console.log("zondata",response);
        //             vueAppZone.zone_data = response.data.ZoneData;
        //             vueAppZone.zurl=response.data.ZoneData.zurl;
        //             this.isShow = true;
        //         })
        //         .catch(function (error) {
        //             console.log(error);
        //         }),
        //     axios({
        //         methods: 'get',
        //         url: '/GetUserFullDataFromSession',
        //     })
        //         .then(function (response) {
        //             console.log("userdata",response);
        //             if(response.data.code==200){
        //                 vueAppZone.user_data = response.data.data;
        //                 vueAppZone.logined=true;
        //                 vueAppZone.notlogined=false;
        //
        //             }
        //             this.isShow = true;
        //         })
        //         .catch(function (error) {
        //             console.log(error);
        //         }),
        //     axios({
        //         methods: 'get',
        //         url: '/zone/'+param+'/getZonePostData/1',
        //     })
        //         .then(function (response) {
        //             console.log("postdata",response);
        //             vueAppZone.posts_data = response.data;
        //             this.isShow = true;
        //         })
        //         .catch(function (error) {
        //             console.log(error);
        //         }),
        //     axios({
        //         methods: 'get',
        //         url: '/zone/'+param+"/getButtonsinfo",
        //     })
        //         .then(function (response) {
        //             console.log("Buttonsinfo",response);
        //             if(response.data.code==200){
        //                 vueAppZone.isZoneFollowed=response.data.ZoneFollowedInfo.isZoneFollowed;
        //                 if(vueAppZone.isZoneFollowed){
        //                     $("#like_button").attr("class","margin_left_20px like_button_checked");
        //                     $("#like_button").text("已关注");
        //                 }
        //
        //             }
        //
        //
        //
        //             // vuePost.isPostLiked=response.data.PostCollectInfo.
        //             // vuePost.isPostdisliked=response.data.PostCollectInfo.
        //
        //         })
        //         .catch(function (error) {
        //             console.log(error);
        //         }),
        //
        // ])
        axios.all([
            this.getuserdata(),this.getzonesdata(),this.getzonepost(),this.getFollowinfo()
        ])
            .then(axios.spread(function (res1,res2,res3,res4){
                console.log("zonedata",res2);
                vueAppZone.zone_data = res2.data.ZoneData;
                console.log("userdata",res1);
                if(res1.data.code==200){
                    vueAppZone.user_data = res1.data.data;
                    vueAppZone.logined=true;
                    vueAppZone.notlogined=false;

                }

                console.log("postdata",res3);
                vueAppZone.posts_data = res3.data;

                console.log("followInfo",res4);
                if(res4.data.code==200){
                vueAppZone.isZoneFollowed=res4.data.ZoneFollowedInfo.isZoneFollowed;
                if(vueAppZone.isZoneFollowed){
                    $("#like_button").attr("class","margin_left_20px like_button_checked");
                    $("#like_button").text("已关注");
                }

            }

            // vuePost.isPostLiked=response.data.PostCollectInfo.
            // vuePost.isPostdisliked=response.data.PostCollectInfo.

            }))
    }
}

var vueAppZone = Vue.createApp(VueappZone).mount("#Vue");

function writeTempHtml(Tittle,Html,pid,user_data){
    var time=new Date().format("yyyy-MM-dd hh:mm:ss");
    let temple ="<div class=\"zone_post_warpper\" >\n" +
        "                        <div class=\"zone_post_up_warpper\">\n" +
        "                            <div class=\"zone_post_user_icon_box\"><a href=\"/userdata/"+ user_data.pid+"\" class=\"cursor_pointer\"><img class=\"zone_post_user_icon_img\" src=\""+user_data.iconUrl+"\"></a></div>\n" +
        "                            <div class=\"zone_post_user_data_box\">\n" +
        "                                <a href=\"/userdata/"+user_data.pid+"\" class=\"zone_post_user_data_name none_underline_link\">"+user_data.nick+"</a>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                        <div class=\"zone_post_text_warpper margin_botoom_10px\">\n" +
        "                            <a class=\"zone_post_content_tittle none_underline_link \" href=\"/post/"+pid+"\">"+Tittle+"</a>\n" +
        "                            <div class=\"margin_top10px\">"+Html+"</div>\n" +
        "                        </div>\n" +
        "                        <div class=\"zone_post_bottom_warpper margin_top30px\">\n" +
        "                            <span class=\"last_visited_time \">最后修改时间:"+time+"</span>\n" +
        "                            <span style=\"margin-left: 48%\">\n" +
        "                                <img class=\"thumb_up_link_icon\" src=\"../static/images/page_icons/appreciate.png\"\n" +
        "                                     style=\"height: 100%\"><a class=\"icon_link\">0</a>\n" +
        "                                <img class=\"comment_link_icon\" src=\"../static/images/page_icons/comment.png\"\n" +
        "                                     style=\"height: 100%\"><a class=\"icon_link\">1</a>\n" +
        "                                <img class=\"thumb_down_link_icon\" src=\"../static/images/page_icons/oppose_light.png\"\n" +
        "                                     style=\"height: 100%\"><a class=\"icon_link\">0</a>\n" +
        "                            </span>\n" +
        "                        </div>\n" +
        "                    </div>"
    $("#zone_post_wrapper").append(temple);
}

function create_new_post(){
    var tittle = $("#new_post_tittle").val();
    var content = editor.txt.html();
    var formdata = new FormData;
    var rawurl = window.location.href;

    var url = rawurl+"/createpost"
    formdata.append("tittle",tittle);
    formdata.append("content",content);

    $.ajax({
        cache: false,
        contentType: false,
        processData: false,
        // contentType:"application/json;charset=utf-8",
        type:"post",
        url:url,
        data: formdata,
        async:false,

        success:function(res) {
            console.log(res)
            if(res.code!= -1){
                // window.location.reload()
                writeTempHtml(tittle,content,res.pid,vueAppZone.user_data);
                editor.txt.clear();
                $("#new_post_tittle").val("");
                $("#Editor").blur();
            }
            else {
                alert(res.msg)
            }

        },
        error:function(res){
            console.log("error");
        }
    });

}

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
function switchButton(index){
    switch (index)
    {
        case 1:
            document.getElementById("switch1").className="zone_switch_button_box_checked";
            document.getElementById("switch2").className="zone_switch_button_box";
            document.getElementById("switch3").className="zone_switch_button_box";
            break;
        case 2:
            document.getElementById("switch1").className="zone_switch_button_box";
            document.getElementById("switch2").className="zone_switch_button_box_checked";
            document.getElementById("switch3").className="zone_switch_button_box";
            break;
        case 3:
            document.getElementById("switch1").className="zone_switch_button_box";
            document.getElementById("switch2").className="zone_switch_button_box";
            document.getElementById("switch3").className="zone_switch_button_box_checked";
            break;
    }


}
function checkEditorIsEmpty() {
    var tittle = $("#new_post_tittle").val();
    var text = editor.txt.html();
    if (text == "" || tittle == "") {
        $("#b_create_post").addClass("disabled");
    } else {
        $("#b_create_post").removeClass("disabled");
    }
}

function click_input_file(){
    document.getElementById("file").click();
}
function MutiImg() {
    var file = document.getElementById("file").files; //获取input file的文件对象
    for (var i = 0; i < file.length; i++) { //多图,单图不用for
        var url = URL.createObjectURL(file[i]); //获取所选文件的临时地址
        // alert(url);
        // document.getElementById("content").value=('<img class="zone_post_content_img" src="' + url + '" alt="" widht="100px" height="200px">'); //单图用html,多图用append
        document.getElementById("content").appendChild('<img class="zone_post_content_img" src="' + url + '" alt="">') //单图用html,多图用append

    } }


function follow(){
    var rawurl = window.location.href;
    var param = rawurl.slice(27,rawurl.length)
    var url = "/zone/"+param+"/followzone";
    $.ajax({
        // contentType:"application/json;charset=utf-8",
        type:"post",
        url:url,
        async:true,
        success:function(res) {
            console.log(res)
            if(res.code!=-1){
                window.refresh();
            }


        },
        error:function(res){
            console.log("error");
        }
    });

}

function getGoodPost(){
    var param = vueAppZone.zurl;
    var url= "/zone/"+param+"/getGoodPostList";
    $.ajax({
        type:"get",
        url:url,
        async:true,
        success:function(res) {
            console.log(res);
            vueAppZone.good_data = res;
            vueAppZone.isShow_post = false;
            vueAppZone.isShow_good = true;

        },
        error:function(res){
            console.log("error");
        }
    });
}


function showsortpost(type,page){
    var rawurl = window.location.href;
    var param = rawurl.slice(27,rawurl.length)
    var url= "/zone/"+param+"/getpost/"+type+"/"+page;
    $.ajax({
        type:"get",
        url:url,
        async:true,
        success:function(res) {
            console.log(res);
            vueAppZone.posts_data = res.sortedposts;
            vueAppZone.isShow_post = false;
            vueAppZone.isShow_post = true;

        },
        error:function(res){
            console.log("error");
        }
    });
}

function ToSearch(){
    var text = $("#zone_search").val();
    sessionStorage.setItem('Search',text);

    if(text!=""){
        document.location.href="/search";
    }

}


$(document).keydown(function(event){
    if (event.keyCode == 13) {
        $('form').each(function() {
            event.preventDefault();
        });
    }
});

function ajaxvisit_manage(){
    param = vueAppZone.zurl;
    url = "/zone/"+param+"/manage"
    ajaxvisit(url);
}