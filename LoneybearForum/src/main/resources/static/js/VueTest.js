const Vueapp={
    data(){
        return {
            uu:"Longyea",
            username:"null",
            password:null,
            verifycode:null,
            message:"app started!",
            tip_content1:"账号密码错误",
            a1:"找回密码",
            tip_content2:"验证码错误",
            dataObj:{
                acc:null,
                pwd:null,
                veri:null
            }

        }
    },
    method: {
        warp() {
            vueApp.dataObj.acc = vueApp.username;
            vueApp.dataObj.pwd = vueApp.password;
            vueApp.dataObj.veri = vueApp.verifycode;
            alert(vueApp.dataObj.acc + "|" + vueApp.dataObj.pwd + "|" + vueApp.dataObj.veri);

        },
        show1() {
            alert(vueApp.dataObj.acc + "|" + vueApp.dataObj.pwd + "|" + vueApp.dataObj.veri);
        },
        showAlert() {
            alert("test");
        }
    },
    // created:function (){
    //     axios({
    //         methods: 'get',
    //         url: '/Getreply',
    //     })
    //         .then(function (response) {
    //             console.log(response);
    //             vueApp.username = response.data;
    //         })
    //         .catch(function (error) {
    //             console.log(error);
    //         })
    // }
}

function ajax1(){
$.ajax({
    type:"get",
    url:"/Getreply",
    async:true,
    success:function(res) {
        console.log(res)
        alert(res);
        vueApp.username = res;
        document.getElementById("form1").submit();
    },
    error:function(res){
        console.log("error");
    }
});
}


function axios1() {
    axios({
              methods: 'get',
              url: '/Getreply',
          })
    .then(function (response) {
        console.log(response);
        this.username = response.data;
        document.getElementById("form1").submit();
    })
        .catch(function (error) {
            console.log(error);
        })

}

// Vue.createApp(Vueapp).mount("#Vue")
var vueApp = Vue.createApp(Vueapp).mount("#Vuehome");


function change(){
    vueApp.dataObj.acc = null;
    vueApp.dataObj.pwd = "chai";
}

function upload_icon(){
    document.getElementById("f").click();
}


function Mutiimg() {
    // var file = document.getElementById("file").files; //获取input file的文件对象
    // for (var i = 0; i < file.length; i++) { //多图,单图不用for
    //     var url = URL.createObjectURL(file[i]); //获取所选文件的临时地址
    //     $("#img").append('<img src="' + url + '" alt="" width="100px" height="auto">'); //单图用html,多图用append
    // } // 图片预览就是这么简单!
    //
    // var formData = new FormData();
    // for (var i = 0; i < file.length; i++){
    //     formData.append("pic",file[i]);
    // }
    let form= document.getElementById("files");
    let formData = new FormData(form);
    $.ajax( {
        url: '/up',//你的保存文件脚本的路径
        type: 'POST',
        data:formData,
        contentType: false, //不设置内容类型
        processData: false, //不处理数据
        cache:"false",
        async:"true",
        dataType:"json",
        success: function (data) {
            alert('上传成功');
            console.log(data)
        },
        error: function () {
            alert('上传失败');
        }
    })

}

function PreViewImg(){
    var file = document.getElementById("f").files; //获取input file的文件对象
        var url = URL.createObjectURL(file[0]); //获取所选文件的临时地址
    alert(url);
    document.getElementById("iii").src = url;
}

function clear1(){
    document.getElementById("file").value ='';
    // if(document.getElementById("file").value ==''){
    //     alert("1")
    // }
    if(document.getElementById("file").value ==null){
        alert("12")
    }

}

