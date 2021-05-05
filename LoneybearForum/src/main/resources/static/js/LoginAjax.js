const Vueapp={
    data(){
        return {
            username:null,
            password:null,
            verifycode:null,
            message:"app started!",
            isUandPWrong:false,
            isVerycodeWrong:false,
            tip_content1:"账号密码错误,请重试或",
            a1:"找回密码",
            tip_content2:"验证码错误",
            tip1_url:"/signup",
            formname:"content",
            url:"/verifying",
            dataObj:{
                acc:null,
                pwd:null,
                veri:null
            }
        }
    },
    method:{
        warp(){
            vueApp.dataObj.acc = Vueapp.accValue;
            vueApp.dataObj.pwd = Vueapp.pwdValue;
            vueApp.dataObj.veri = Vueapp.vcValue;
        },
        sendModel(){

        },

    },
    mounted:function (){
        // axios({
        //     methods: 'get',
        //     url: '/defaultKaptcha',
        // })
        //     .then(function (response) {
        //         console.log(response);
        //     })
        //     .catch(function (error) {
        //         console.log(error);
        //     })


    }

}

// Vue.createApp(Vueapp).mount("#Vue")
var vueApp = Vue.createApp(Vueapp).mount("#Vue")


function getform(formname){
    var uname = document.forms[formname]["username"].value;
    var pword = document.forms[formname]["password"].value;
    var vcode = document.forms[formname]["verifyCode"].value;
    var data = {};
    data.username = uname;
    data.password = pword;
    data.verifycode = vcode;
    return data;
}






function LoginajaxPost(){
    // $("#loginForm").submit();
    // var JSONObj =getform("content");
    // JSON.stringify(JSONObj);


    $.ajax({
        contentType:"application/json;charset=utf-8",
        type:"post",
        url:"/verify",
        data:$("#loginForm").serialize(),
        async:true,
        success:function(res) {
            console.log(res)
            switch (res.status) {
                //账号不存在
                case 0:
                    var tip1 = document.getElementById("tip1").className = "find_password visibility_visible";
                    vueApp.tip_content1 = "账号不存在,请重试或";
                    vueApp.a1 = "注册新账号";
                    vueApp.tip1_url= "/signup"
                    break;
                //登陆成功
                case 200:
                    clearTip1();
                    clearTip2();
                    document.location.href="/homepage"
                    break;

                //密码错误
                case 2:
                    var tip1 = document.getElementById("tip1").className = "find_password visibility_visible";
                    vueApp.tip_content1 = "账号或密码错误,请重试或";
                    vueApp.a1 = "修改密码";
                    vueApp.tip1_url= "/reset";
                    break;

                //验证码错误
                case 3:
                    var tip2 = document.getElementById("tip2").className = "tip2_div visibility_visible";
                    break;
                //账号被封禁


                default:
                    break;
            }
        },
        error:function(res){
            console.log("error");
        }
    });


}

/**
 * 表单一开始提交按钮是.dsiabled
 * 检查表单是否为空
 * 如果账号为空$("#tip1")提示内容请输入账号
 * 其他同
 * 最后全部输入后按钮class变为.enabled
 */


function clearTip1(){
    document.getElementById("tip1").className = "find_password visibility_hidden";
}

function clearTip2(){
    document.getElementById("tip2").className = "tip2_div visibility_hidden";
}


$(function(){
    // 1.基本参数设置
    var options = {
        type: 'POST',    // 设置表单提交方式
        url: "/verify",    // 设置表单提交URL,默认为表单Form上action的路径
        dataType: 'json',    // 返回数据类型
        beforeSubmit: function(formData, jqForm, option){    // 表单提交之前的回调函数，一般用户表单验证
            // formData: 数组对象,提交表单时,Form插件会以Ajax方式自动提交这些数据,格式Json数组,形如[{name:userName, value:admin},{name:passWord, value:123}]
            // jqForm: jQuery对象,，封装了表单的元素
            // options: options对象
            var str = $.param(formData);    // name=admin&passWord=123
            var dom = jqForm[0];    // 将jqForm转换为DOM对象
            var name = dom.name.value;    // 访问jqForm的DOM元素
            /* 表单提交前的操作 */
            return true; // 只要不返回false,表单都会提交 
        },
        success: function(responseText, statusText, xhr, $form){    // 成功后的回调函数(返回数据由responseText获得)
            console.log("res",responseText)
            switch (responseText.code) {
                //账号不存在
                case 0:
                    var tip1 = document.getElementById("tip1").className = "find_password visibility_visible";
                    vueApp.tip_content1 = "账号不存在,请重试或";
                    vueApp.a1 = "注册新账号";
                    vueApp.tip1_url= "/signup"
                    break;
                //登陆成功
                case 200:
                    clearTip1();
                    clearTip2();
                    document.location.href="/homepage"
                    break;
                //密码错误
                case 2:
                    var tip1 = document.getElementById("tip1").className = "find_password visibility_visible";
                    vueApp.tip_content1 = "账号或密码错误,请重试或";
                    vueApp.a1 = "修改密码";
                    vueApp.tip1_url= "/reset";
                    break;
                //验证码错误
                case 3:
                    var tip2 = document.getElementById("tip2").className = "tip2_div visibility_visible";
                    break;
                case 406:
                    $("#tip1").attr("class","find_password visibility_visible ");
                    vueApp.tip_content1 = "账号已被封禁";
                    vueApp.a1 = "";
                    break;
                default:
                    break;
            }

        },
        error: function(xhr, status, err) {
            console.log(err)  // 访问地址失败，或发生异常没有正常返回
        },
        clearForm: false,    // 成功提交后，清除表单填写内容
        resetForm: false    // 成功提交后，重置表单填写内容
    };

    // 2.绑定ajaxSubmit()
    $("#loginForm").submit(function(){     // 提交表单的id
        $(this).ajaxSubmit(options);
        return false; //防止表单自动提交
    });
});

