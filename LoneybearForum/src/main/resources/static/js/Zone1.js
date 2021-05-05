///WangEditor part



////Vue part
const VueappZone={
    data(){
        return {

            username:null,
            isShow:false,
            zone_data:{}
        }
    },
    method:{
        getUserdataFromSession:function(){
            return axios({
                methods: 'get',
                url: '/GetUserFullDataFromSession',
            })
        },
        getZoneData:function(){
            var rawurl = window.location.href;
            var param = rawurl.slice(27,rawurl.length)
            return axios({
                methods: 'get',
                url: '/zone/'+param+'/getzonesdata',
            })
        },
        getPostdata:function(){
            var rawurl = window.location.href;
            url = rawurl+"/getZonePostData";
            return axios({
                methods: 'get',
                url: '',
            })
        }
    },
    mounted:function (){
        let r1 = this.getUserdataFromSession;
        let r2 = this.getZoneData;
        let r3 = this.getPostdata;
        axios.all([
            r1,r2,r3
        ]).then(
            results =>{ console.log(results) })}
        // .then(axios.spread((r1, r2, r3) => {
        //         console.log(r1);
        //         console.log(r2);
        //         console.log(r3);
        //     })
        // )}


}
var vueAppZone = Vue.createApp(VueappZone).mount("#Vue")



function create_new_post(){
    var tittle = $("#new_post_tittle").val();
    var content = editor.txt.text();
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
        async:true,
        success:function(res) {
            console.log(res)
            window.refresh();
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
    if(index==1) {
        document.getElementById("switch1").className="zone_switch_button_box_checked";
        document.getElementById("switch2").className="zone_switch_button_box";
    }
    else {
        document.getElementById("switch2").className="zone_switch_button_box_checked";
        document.getElementById("switch1").className="zone_switch_button_box";
    }

}
function checkEditorIsEmpty() {
    var tittle = $("#new_post_tittle").val();
    var text = editor.txt.text();
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