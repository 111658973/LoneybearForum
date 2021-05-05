const Vueapp={
    data(){
        return {
            email:null,
            tip1:"用户已存在，请尝试其他用户名",
            tip2:"手机已注册",
            tip3:"验证码错误，请查看后重试",
            username:"xxx@qq.com",
            isShowname:true,
        }
    },
    method:{
    },
    mounted:function (){

        this.isShow=true;
    }
}

// Vue.createApp(Vueapp).mount("#Vue")
var vueApp = Vue.createApp(Vueapp).mount("#Vue")

function timeClock(){
    document.getElementById("tip").className ="";
    document.getElementById("resend").className ="get_email_verifycode_submit disabled";
    document.getElementById("reget2").className ="get_verifi_code_button disabled";

    var left_time = 10;
    var timeCLock = setInterval(function (){
        left_time--;
        $("#clock").html(left_time);
        if(left_time==0){
            clearInterval(timeCLock);
            $("#clock").html(10);
            document.getElementById("resend").className ="get_email_verifycode_submit"
            document.getElementById("reget2").className ="get_verifi_code_button";
            document.getElementById("tip").className ="display_none"

        }
    },1000)
}

function getnewRandomVerifycode(){
    var username = document.forms["form1"]["username"].value;
    var mail = document.forms["form1"]["email"].value;

    var data = new FormData;
    data.append("username",username);
    data.append("email",mail);
    console.log(data)
    $.ajax({
        // contentType:"application/json;charset=utf-8",
        type:"post",
        url:"/getEmailVerifycode",
        cache: false,
        contentType: false,
        processData: false,
        data: data,
        async:true,
        success:function(res) {
            console.log(res)
        },
        error:function(res){
            console.log("error");
        }
    });

}

function signUpAjax(){
    var formdata=new FormData(document.forms["form1"]);
    $.ajax({
        // contentType:"application/json;charset=utf-8",
        type:"post",
        url:"/signuping",
        async:true,
        data:formdata,
        cache: false,
        contentType: false,
        processData: false,
        success:function(res) {
            console.log(res)
            switch(res.status){
                case -1:
                    vueApp.tip1 = "用户已存在，请尝试其他用户名";
                    document.getElementById("tip1").className = "user_used_tip visibility_visible";
                    break;
                //手机存在
                case -2:
                    vueApp.tip2 = "手机已注册";
                    document.getElementById("tip2").className = "user_used_tip visibility_visible";
                    break;

                //验证码错误
                case -3:
                    vueApp.tip3 = "验证码错误，请查看后重试";
                    var tip1 = document.getElementById("tip3").className = "user_used_tip visibility_visible";
                    break;

                //成功
                case 200:
                    alert("注册成功");
                    document.getElementById("form1").submit();
                    break;
            }
        },
        error:function(res){
            console.log("error");
        }
    });

}

function CheckFormIfEmpty(){
    var isTrue = true;
    var form = document.forms["form1"];
    for(var i =0;i<form.length;i++) {
        if(form[i].value == ""){
            console.log(i+":"+form[i].name)
            isTrue = false;
        }
    }
    EnableSubmit(isTrue);
}

function EnableSubmit(isTrue){
    if(isTrue){
        document.getElementById("submit").className="sign_up_submit_button"
    }
    else
        document.getElementById("submit").className="sign_up_submit_button disabled"
}

function checkEmailIsEmpty(){
    var email =document.forms["form1"]["email"];
    if(email.value!=""){
        document.getElementById("reget2").className="get_verifi_code_button";
    }
    if(email.value==""){
        document.getElementById("reget2").className="get_verifi_code_button disabled";

    }

}

function clearTip(index){
    document.getElementById("tip"+index).className = "user_used_tip visibility_hidden";
}
