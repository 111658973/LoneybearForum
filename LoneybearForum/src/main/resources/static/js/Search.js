const VueSearch={
    data(){
        return {
            is_Show:false,
            show_floors:false,
            floorList:[],
            zoneList:[],
            userList:[],
            is_floor_empty:false,
            is_user_empty:false,
            is_zone_empty:false,
            show_res_floor:true,
            show_res_zone:true,
            show_res_user:true
        }
    },
    method:{

    },
    mounted:function (){
        var content = sessionStorage.getItem("Search");
        if(content!=""){
            $("#search").val(content);
            $("#wrapper").animate({top: "2%"}, 700);
            mainsearch();
            showcontent();
            sessionStorage.removeItem("Search");
        }

    }

}

// Vue.createApp(Vueapp).mount("#Vue")
var vueSearch = Vue.createApp(VueSearch).mount("#Vue")



function showcontent(){
    // $("#content").removeClass("display_none");
    $("#content").fadeIn(1500);

}

function hidecontent(){

    // $("#content").addClass("display_none");
    $("#content").fadeOut(300);

}

function showoutline(obj){
    $(obj).css({"border":"#2C8DFB solid 1px"})
}

function mainsearch(){
    var text = $("#search").val();
    var url = "/search/"+text;
    $.ajax({
        // cache: false,
        // contentType: false,
        // processData: false,
        contentType:"application/json;charset=utf-8",
        type:"get",
        url:url,
        async:true,
        success:function(res) {
            console.log(res)
            if(res.floors!=null){
                vueSearch.floorList=res.floors;
                vueSearch.is_floor_empty =true;
            }
            if(res.users!=null){
                vueSearch.userList=res.users;
                vueSearch.is_user_empty =true;
            }
            if(res.zones!=null){
                vueSearch.zoneList=res.zones;
                vueSearch.is_zone_empty =true;
            }
            console.log("user",vueSearch.userList);
            console.log("zones",vueSearch.zoneList);
            console.log("floor",vueSearch.floorList);
            vueSearch.show_floors=false;
            vueSearch.show_floors=true;

        },
        error:function(res){
            console.log("error");
        }
    });

}

var input = $("#search")
input.keydown(function (e) {
    if (e.keyCode == 13) {
        if (input.val() != '') {
            // $("#wrapper").attr("class","search_frame-top");
            $("#wrapper").animate({top: "2%"}, 700)
            mainsearch();
            showcontent();
            vueSearch.show_res_user =true;
            vueSearch.show_res_zone =true;
            vueSearch.show_res_floor=true;
        } else {
            // $("#wrapper").attr("class","search_frame");
            $("#wrapper").animate({top: "35%"}, 700)
            hidecontent();
        }

    }
});

function ButtonCheck(obj){
    id = $(obj).attr("id")
    switch (id){
        case "button_user":
            $(obj).addClass("switch_button_style_checked")
            $(obj).next().removeClass("switch_button_style_checked")
            $(obj).next().next().removeClass("switch_button_style_checked")
            break;
        case "button_zone":
            $(obj).addClass("switch_button_style_checked")
            $(obj).next().removeClass("switch_button_style_checked")
            $(obj).prev().removeClass("switch_button_style_checked")
            break;
        case "button_reply":
            $(obj).addClass("switch_button_style_checked")
            $(obj).prev().prev().removeClass("switch_button_style_checked")
            $(obj).prev().removeClass("switch_button_style_checked")

            break;
    }
}

function showResult(index){
    switch (index){
        case 1:
            vueSearch.show_res_user =true;
            vueSearch.show_res_zone =false;
            vueSearch.show_res_floor=false;
            break;
        case 2:
            vueSearch.show_res_user =false;
            vueSearch.show_res_zone =true;
            vueSearch.show_res_floor=false;
            break;
        case 3:
            vueSearch.show_res_user =false;
            vueSearch.show_res_zone =false;
            vueSearch.show_res_floor=true;
            break;
    }
}



