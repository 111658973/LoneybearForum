const Vueapp={
    data(){
        return {
            user:null,
        }
    },
    method:{
    }
}

// Vue.createApp(Vueapp).mount("#Vue")
var vueApp = Vue.createApp(Vueapp).mount("#Vue")

function ResetVerifyAjax(){
    var formdata=new FormData(document.forms["form"]);
    Vueapp.user = document.getElementById("username").value;
    $.ajax({
        // contentType:"application/json;charset=utf-8",
        type:"post",
        url:"/resetVerifying",
        async:true,
        data:formdata,
        cache: false,
        contentType: false,
        processData: false,
        success:function(res) {
            console.log(res)
            if(res.code!=200){
                document.getElementById("tip1").className="reset_user_used_tip visibility_visible";
                document.getElementById("tip1").innerText = res.msg;
            }
            else{
                document.getElementById("down").innerHTML="        <div class=\"new_reset_warpper_down_part\">\n" +
                    "            <div class=\"margin_top50px\">\n" +
                    "                <form id=\"ResetForm\">\n" +
                    "                    <p><span id=\"tip2\" class=\"reset_user_used_tip2 visibility_hidden\">用户已存在，请尝试其他用户名</span></p>\n" +
                    "                    <div class=\"lab_fix_div\"><label class=\"text_label\">新密码</label></div><input id=\"reset1\" type=\"password\" class=\"reset_input_text \" placeholder=\"新密码\" name=\"newpassword\" onblur=\"checkIsEmpty()\" onfocus=\"ClearTip2()\">\n" +
                    "                    <div class=\"lab_fix_div\"><label class=\"text_label\">再次输入</label></div><input type=\"password\" id=\"reset2\" class=\"reset_input_text margin_top10px\" placeholder=\"新密码\" name=\"newpassword2\" onblur=\"checkIsEmpty()\" onfocus=\"ClearTip2()\">\n" +
                    "                    <input id=\"submit2\" type=\"button\" class=\"reset_submit_button disabled\" value=\"修改密码\" onclick=\"ResetConfirmAjax()\">\n" +
                    "                </form>\n" +
                    "            </div>\n" +
                    "        </div>\n"
            }
        },
        error:function(res){
            console.log("error");
        }
    });

}

function ResetConfirmAjax(){
    var formdata=new FormData(document.forms["ResetForm"]);
    formdata.append("username",Vueapp.user);
    $.ajax({
        // contentType:"application/json;charset=utf-8",
        type:"post",
        url:"/resetVerified",
        async:true,
        data:formdata,
        cache: false,
        contentType: false,
        processData: false,
        success:function(res) {
            console.log(res)
            alert(res.msg);
            if(res.code==200){
                window.location.href="/login";
            }
        },
        error:function(res){
            console.log("error");
        }
    });

}

function getEmailVerifycodeAjax(){
    var formdata=new FormData(document.forms["form"]);
    $.ajax({
        // contentType:"application/json;charset=utf-8",
        type:"post",
        url:"/getEmailVerifycode",
        async:true,
        data:formdata,
        cache: false,
        contentType: false,
        processData: false,
        success:function(res) {
            console.log(res)
        },
        error:function(res){
            console.log("error");
        }
    });
}

function ChangeVerifyMethod(){
    var select = $("#select option:selected").val()
    if(select=="phone"){
        document.getElementById("phone_verify_warpper").className="tel_verify_box"
        document.getElementById("mail_verify_warpper").className="mail_verify_box display_none "

    }
    if(select=="email"){
        document.getElementById("phone_verify_warpper").className="tel_verify_box display_none"
        document.getElementById("mail_verify_warpper").className="mail_verify_box "

    }
}

function CheckFormIfEmpty(){
    var isTrue = true;
    var form = document.forms["form"];
    for(var i =0;i<form.length;i++) {
        if(form[i].value == ""){
            isTrue = false;
        }
    }
    EnableSubmit(isTrue);
}

function EnableSubmit(isTrue){
    if(isTrue){
        document.getElementById("submit1").className="reset_submit_button"
    }
    else
        document.getElementById("submit1").className="reset_submit_button disabled"

}

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

function hide_verify_container(){
    document.getElementById("verify_container").className="get_email_verifycode_container1 display_none";
}

function show_verify_container(){
    document.getElementById("verify_container").className="get_email_verifycode_container1";
    document.getElementById("user_email").innerText=document.getElementById("email").value;

    timeClock();
}

function ClearTip(){
    document.getElementById("tip1").className="reset_user_used_tip visibility_hidden"
}

function ClearTip2(){
    document.getElementById("tip2").className="reset_user_used_tip visibility_hidden"

}

function checkIsEmpty(){
    if(document.getElementById("reset1").value!=""&&document.getElementById("reset2").value!="" && checkIsEqual()){
        document.getElementById("submit2").className="reset_submit_button";
    }
    else
        document.getElementById("submit2").className="reset_submit_button disabled";
}

function checkIsEqual(){
    if(document.getElementById("reset1").value!=document.getElementById("reset2").value){
        document.getElementById("tip2").className="reset_user_used_tip2 visibility_visible";
        document.getElementById("tip2").innerText="两次密码不一致，请重试";
        return false
    }
    else return true;
}