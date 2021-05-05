// $(document).ready(getResourceAxios())
const VueappHomepage = {
    data() {
        return {
            nick: "",
            img_url: "",
            user: {},
            username: null,
            show_left: false,
            isShow: true,
            hot_posts: [],
            hot_zones: [],
            user_data: {},
            isShow_post: true,
            Logined: true,
            NotLogined: false
        }
    },
    methods: {
        getResourse:function (){
            return axios({
                methods: 'get',
                url: '/GetHomepageResource',
            })
        },
        getAuthencation:function (){
            return axios({
                methods: 'get',
                url: '/authenticaion',
            })
        },

    },
    mounted: function () {
        var _this = this;
        axios.all([
            _this.getResourse(),_this.getAuthencation()
        ])
            .then(axios.spread(function (res1,res2){
                console.log("res1",res1);
                vueAppHome.hot_posts = res1.data.hot_posts;
                vueAppHome.hot_zones = res1.data.hot_zones;
                if (res1.data.user_data.code != -1) {
                    vueAppHome.user_data = res1.data.user_data;
                    vueAppHome.user = res1.data.user_data.data;
                    vueAppHome.nick = res1.data.user_data.data.nick;
                    vueAppHome.img_url = res1.data.user_data.data.iconUrl;
                    vueAppHome.show_left = true;
                }
                console.log("res2",res2);
                if (res2.data.principal == "anonymousUser") {
                    vueAppHome.Logined = false;
                    vueAppHome.NotLogined = true;
                }
            }))
        // axios({
        //     methods: 'get',
        //     url: '/GetHomepageResource',
        // })
        //     .then(function (response) {
        //
        //         console.log(response);
        //         vueAppHome.hot_posts = response.data.hot_posts;
        //         vueAppHome.hot_zones = response.data.hot_zones;
        //
        //         if (response.data.user_data.code != -1) {
        //             vueAppHome.user_data = response.data.user_data;
        //             vueAppHome.user = response.data.user_data.data;
        //             vueAppHome.nick = response.data.user_data.data.nick;
        //             vueAppHome.img_url = response.data.user_data.data.iconUrl;
        //             vueAppHome.show_left = true;
        //         }
        //         vueAppHome.isShow = true;
        //     })
        //     .catch(function (error) {
        //         console.log(error);
        //     }),
        // axios({
        //     methods: 'get',
        //     url: '/authenticaion',
        // })
        //     .then(function (response) {
        //         if (response.data.principal == "anonymousUser") {
        //             vueAppHome.Logined = false;
        //             vueAppHome.NotLogined = true;
        //         }
        //     })
        //     .catch(function (error) {
        //         console.log(error);
        //     }),
        axios({
            methods: 'get',
            url: '/search/init',
        })
            .then(function (response) {
                console.log(response);
            })
            .catch(function (error) {
                console.log(error);
            })
    }
}
var vueAppHome = Vue.createApp(VueappHomepage).mount("#Vue1")
//     function getResource(){
//         $.ajax({
//             contentType:"application/json;charset=utf-8",
//             type:"get",
//             url:"/GetHomepageResource",
//             data: null,
//             async:true,
//             cash:false,
//             success:function(res) {
//                 console.log(res)
//                 if(res != null){
//                     Vueapp.hot_posts=res.hot_posts;
//                     Vueapp.left_nav=res.left_nav;
//                     Vueapp.recommand_zones = res.recommand_zones;
//                     Vueapp.user_data = res.user_data;
//                     console.log(Vueapp.hot_posts)
//                     console.log(Vueapp.left_nav)
//                     console.log(Vueapp.recommand_zones)
//                     console.log(Vueapp.user_data)
//                 }
//                 else{
//                     alert("false");
//                 }
//             },
//             error:function(res){
//                 console.log("error");
//             }
//         });
//
//
//     }
//
//     function getResourceAxios(){
//         axios({
//             methods: 'get',
//             url: '/GetHomepageResource',
//             })
//             .then(function (response) {
//                 console.log(response);
//                 Vueapp.hot_posts=res.hot_posts;
//                 Vueapp.left_nav=res.left_nav;
//                 Vueapp.recommand_zones = res.recommand_zones;
//                 Vueapp.user_data = res.user_data;
//                 console.log(Vueapp.hot_posts)
//                 console.log(Vueapp.left_nav)
//                 console.log(Vueapp.recommand_zones)
//                 console.log(Vueapp.user_data)
//             })
//             .catch(function (error) {
//                 console.log(error);
//             })
//
//     }


// 鼠标悬浮上去后显示用户浮动小界面
function showDynamicUserDataFrame() {
    document.getElementById("dynamic_user_data").className = "mini_userdata_contatiner_transparent";
}

function hideDynamicUserDataFrame() {
    document.getElementById("dynamic_user_data").className = "mini_userdata_contatiner_transparent display_none";
}

function showDynamicMessageFrame() {
    document.getElementById("dynamic_message_data").className = "mini_message_contatiner_transparent";
}

function hideDynamicMessageFrame() {
    document.getElementById("dynamic_message_data").className = "mini_message_contatiner_transparent display_none";
}

function ButtonCheck(obj) {
    id = $(obj).attr("id")
    switch (id) {
        case "button_hot":
            $(obj).addClass("switch_button_style_checked")
            $(obj).next().removeClass("switch_button_style_checked")
            break;
        case "button_new":
            $(obj).addClass("switch_button_style_checked")
            $(obj).prev().removeClass("switch_button_style_checked")
            break;

    }
}

//点击按钮后获取最新的帖子消息或者是最热门的
//1是按热度2是按时间
function showSort(type) {

    getpostresources(type, 0);
}

function sleep(ms, a) {
    setTimeout(a, ms);
}

//获取帖子数据，类型以及分页
function getpostresources(type, page) {
    var url = "/homepage/getpost/" + type + "/" + page;
    $.ajax({
        type: "get",
        url: url,
        async: true,
        success: function (res) {
            console.log(res);
            vueAppHome.hot_posts = getfirstelem(res);
            vueAppHome.isShow_post = false;
            vueAppHome.isShow_post = true;


        },
        error: function (res) {
            console.log("error");
        }
    });

}

function getfirstelem(obj) {
    for (var key in obj)
        return obj[key];
}

function ToSearch() {
    var text = $("#homepage_search").val();
    sessionStorage.setItem('Search', text);

    if (text != "") {
        document.location.href = "/search";
    }

}

$(document).keydown(function (event) {
    if (event.keyCode == 13) {
        $('form').each(function () {
            event.preventDefault();
        });
    }
});