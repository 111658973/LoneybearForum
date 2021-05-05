const VueUserData={
    data(){
        return {
            uid:null,
            user_data:{},
            user_posts:[],
            user_zone:[],
            sex:null,
            icon_url:null,
            display_url:null,
            phone:null,
            isShow:false,
            isEdit:true,
            sex_icon_url:"../static/images/page_icons/male.png",
            switchData:[],
            logined:false,
            notlogined:true,
            show_switch1:true,
            show_switch2:false,
            show_switch3:false,
            show_switch4:false,
        }
    },
    method:{

    },
    mounted:function (){
        var rawurl = window.location.href;
        var param = rawurl.slice(31, rawurl.length);
        axios({
            methods: 'get',
            url: '/GetUserdataResource/'+param,
        })
            .then(function (response) {
                console.log("userdata",response);
                VueUser.user_data = response.data.data;
                VueUser.user_posts = response.data.user_posts;
                VueUser.user_zones = response.data.user_favourite_zones;
                VueUser.uid = rawurl.slice(31, rawurl.length);
                if(VueUser.user_data.sex=='男'){
                    VueUser.sex="男";
                    VueUser.sex_icon_url = "../static/images/page_icons/male.png";
                }
                else if(VueUser.user_data.sex=='女'){
                    VueUser.sex="女";
                    VueUser.sex_icon_url = "../static/images/page_icons/female.png";
                }
                else {
                    VueUser.sex=null;
                    VueUser.sex_icon_url = "../static/images/page_icons/sex_none.png";

                }
                VueUser.isShow = true;

            })
            .catch(function (error) {
                console.log(error);
            }),
            axios({
                methods: 'get',
                url: '/GetUserFullDataFromSession',
            })
                .then(function (response) {
                    console.log("userdata",response);
                    vuePost.userdata = response.data.data;
                    if(response.data.code=-1){
                        VueUser.logined =true;
                        VueUser.notlogined =false;
                    }
                })
                .catch(function (error) {
                    console.log(error);
                })

    }
}

// Vue.createApp(Vueapp).mount("#Vue")
var VueUser = Vue.createApp(VueUserData).mount("#Vue")

function showEditContainer(){
    $.ajax({
        type:"get",
        url:"/GetUserFullDataFromSession",
        async:true,
        success:function(res) {
            console.log(res)
            document.getElementById("img").src= res.data.iconUrl
            document.getElementById("upload_display_img").value = '';
            document.getElementById("nick").value=res.data.nick
            document.getElementById("phone").value= res.data.phoneNumber
            document.getElementById("mail").value= res.data.email;
            document.getElementById("introduction").value=res.data.uintroduction;
            VueUser.sex = res.data.sex;
            VueUser.isShow=false;
            VueUser.isShow=true;
        },
        error:function(res){
            console.log("error");
        }
    })
    document.getElementById("edit_container").className="edit_userdata_container";
}

function hideEditContainer(){
    document.getElementById("edit_container").className="edit_userdata_container display_none";
}

function upload_icon(){
    document.getElementById("f").click();
}

function show_switch_button(index){
    var list= [1,2,3,4];
    list.splice(index-1,1);
    document.getElementById("s"+index).className="userdata_stage_switche_button userdata_stage_switche_button_selected";
    for(var i = 0;i<list.length;i++){
        document.getElementById("s"+list[i]).className="userdata_stage_switche_button";
    }
    switch (index) {
        case 1:
            VueUser.show_switch1=true;
            VueUser.show_switch2=false;
            VueUser.show_switch3=false;
            VueUser.show_switch4=false;
            break;
        case 2:
            VueUser.show_switch1=false;
            VueUser.show_switch2=true;
            VueUser.show_switch3=false;
            VueUser.show_switch4=false;
            break;
        case 3:
            VueUser.show_switch1=false;
            VueUser.show_switch2=false;
            VueUser.show_switch3=true;
            VueUser.show_switch4=false;
            break;
        case 4:
            VueUser.show_switch1=false;
            VueUser.show_switch2=false;
            VueUser.show_switch3=false;
            VueUser.show_switch4=true;
            break;
    }
}

function switchRadio(sex){
    if(sex == 1){
        document.getElementById("rm").checked=true;
        document.getElementById("rf").checked=false;
    }
    else {
        document.getElementById("rm").checked=false;
        document.getElementById("rf").checked=true;
    }
}


function getUpdateData(){
    var data={}
    data.new_iconUrl=document.getElementById("");
    data.new_displayUrl=document.getElementById("");
    var RadioList = document.getElementsByName("radio");
    for(var i=0;i<RadioList.length;i++){
        if(RadioList[i].checked == true){
            data.new_sex = RadioList[i].value;
        }
    }
    // VueUser.update_data.new_sex=document.getElementById("");
    data.new_nick=document.getElementById("nick").value;
    data.new_phoneNumber=document.getElementById("phone").value;
    data.new_email=document.getElementById("mail").value;
    data.new_introduction=document.getElementById("introduction").value;
    console.log(data.update_data)
    console.log("_____________")
    return data;
}

function getImgList(){
    let img1 = document.getElementById("f");
    let img2 = document.getElementById("upload_display_img");
    var imgList= new FormData();
    if(img.value!=''){
        imgList.append("img1",img1);
    }
    if(img.value!=''){
        imgList.append("img2",img2);
    }
    return imgList;
}

function UpdateAjax(){
    // var dataObj = getUpdateData();
    // let imgJson = getImgList();
    var form1= document.forms["form1"];
    var formData = new FormData(form1);
    // formData.append("data",dataObj);
    // if(imgJson != null){
    //     formData.append("imgList",imgJson);
    //
    // }



    $.ajax({
        cache: false,
        processData: false,
        type:"post",
        url:"/userdata/UpdateUserData",
        data: formData,
        // contentType:"application/json;charset=utf-8",

        contentType: false, //不设置内容类型
        // dataType:"json",
        async:true,
        success:function(res) {
            console.log(res)
            VueUser.isShow=false;
            VueUser.isShow=true;
            // alert(res.msg)
            window.location.reload();
        },
        error:function(res){
            console.log("error");
        }
    })

    // $.ajax({
    //         type:"post",
    //         url:"/userdata/UpdateUserData",
    //         data: imgJson,
    //         // contentType:"application/json;charset=utf-8",
    //
    //         contentType: false, //不设置内容类型
    //         cache:"false",
    //         dataType:"json",
    //         async:true,
    //         success:function(res) {
    //             console.log(res)
    //             VueUser.isShow=false;
    //             VueUser.isShow=true;
    //             alert(res.msg)
    //         },
    //         error:function(res){
    //             console.log("error");
    //         }
    //     })


};


function PreViewImg(){
    var file = document.getElementById("f").files; //获取input file的文件对象
    var url = URL.createObjectURL(file[0]); //获取所选文件的临时地址
    document.getElementById("img").src = url;
}

function switchUserda(type,page){
    var url="/userdata/switchdata/"+VueUser.uid+"/"+ type+"/"+page;
    $.ajax({
        type:"get",
        url:url,
        async:true,
        success:function(res) {
            console.log(res.msg,res)
            VueUser.switchData =res.data;
        },
        error:function(res){
            console.log("error");
        }
    })



}