////Vue part
const VueappZoneManage = {
    data() {
        return {
            zonename: null,
            zone_data: {},
            is_Show: false,
            show_table: [
                {"show_": true},
                {"show_": false},
                {"show_": false},
                {"show_": false},
                {"show_": false},
                {"show_": false},
                {"show_": false},
            ],
            managers: [],
            good_posts: [],
            black_list: [],
            add_admin_table: [],
            showSubTable: true,
            is_senior: false,
        }
    },
    method: {},
    mounted: function () {
        var rawurl = window.location.href;
        var param = rawurl.slice(27, rawurl.length - 7)

        axios.all([
            axios({
                methods: 'get',
                url: '/zone/' + param + '/getinfo',
            })
                .then(function (response) {
                    console.log("zondata", response);
                    vueAppZoneManage.zone_data = response.data;
                    vueAppZoneManage.zonename = response.data.zurl;
                    // vueAppZoneManage.show_table[1]=false;
                    // vueAppZoneManage.show_table[2]=false;
                    // vueAppZoneManage.show_table[3]=false;

                    vueAppZoneManage.is_Show = true;

                })
                .catch(function (error) {
                    console.log(error);
                }),
            axios({
                methods: 'get',
                url: '/authenticaion',
            })
                .then(function (response) {
                    console.log("auth", response);
                    for (var i=0;i< response.data.authorities.length;i++) {

                        if (response.data.authorities[i].authority == "3") {
                            vueAppZoneManage.is_senior = true;
                            break;
                        }

                    }

                }).catch(function (error) {
                console.log(error);
            })
        ])
        ;

    }

}

var vueAppZoneManage = Vue.createApp(VueappZoneManage).mount("#Vue");

function AddFunctionAtly(id) {
    return [
        '<button id="' + id + '" class="btn btn-danger">删除</button>',
    ].join("")
}

function AddFunctionAtly2() {
    return [
        '<button id="btn-editBlacklist" class="btn btn-default margin_right_10px" data-toggle="modal" data-target="#editBlacklist">编辑</button>',
        '<button id="btn-recoverBlacklist" class="btn btn-danger">删除</button>'

    ].join("")
}

function AddFunctionAtly3(index, row) {
    return [
        '<button id="btn-recoverPost" class="btn btn-success margin_right_10px">恢复</button>',
    ].join("")
}

function AddFunctionAtly4(index, row) {
    return [
        '<img alt="image" class="mini_data_icon" src="' + row['iconUrl'] + '">',
    ].join("")
}

function AddFunctionAtly5(id) {
    return [
        '<button id="' + id + '" v-show="is_senior" class="btn btn-danger">删除</button>',
    ].join("")
}
//remove addmin
window.operateEvents = {
    "click #btn-deleteAdmin": function (e, value, row, index) {
        removeAdmin($("#table_manager").bootstrapTable("getData")[index])
    },
    "click #btn-deltegood": function (e, value, row, index) {
        removeGood($("#table_good").bootstrapTable("getData")[index])
    },
    "click #btn-editBlacklist": function (e, value, row, index) {
        var data = $("#table_blacklist").bootstrapTable("getData")[index];
        $("#editblacklist_query").val(row.username);
        $('#subtable_blacklist').bootstrapTable({
            classes: "table table-striped table-hover text-center",
            theadClasses: "thead thead-light text-center",
            detailViewAlign: 'center',
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pageNumber: 1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            minimumCountColumns: 2,             //最少允许的列数
            columns: [
                {
                    field: 'username',
                    title: '账号',
                }, {
                    field: 'deal',
                    title: '账号状态',
                    formatter: function (index, row) {
                        if (row['deal'] == "封禁")
                            return [
                                '<li style="color: red">封禁中</li>'
                            ].join("")
                        else if (row['deal'] == "禁言")
                            return [
                                '<li style="color: orange">禁言中</li>'
                            ].join("")
                        else {
                            return [
                                '<li style="color: green">正常</li>'
                            ].join("")
                        }
                    }
                }, {
                    field: 'reason',
                    title: '处理原理'
                }, {
                    field: 'operator',
                    title: '处理人'
                }, {
                    field: 'time',
                    title: '处理时间'
                }, {
                    field: 'expire_days',
                    title: '持续时间'
                }, {
                    field: 'until',
                    title: '到期时间'
                },],
            data: JSON.parse(data),
        });


    },
    "click #btn-recoverBlacklist": function (e, value, row, index) {
        deleteBlacklist($("#table_blacklist").bootstrapTable("getData")[index])
    },
    "click #btn-recoverPost": function (e, value, row, index) {
        recoverPost($("#table_postcycle").bootstrapTable("getData")[index])
    },
}


function show_tn(n) {
    for (var i = 0; i < vueAppZoneManage.show_table.length; i++) {
        if (i != n - 1) {
            vueAppZoneManage.show_table[i].show_ = false;
        } else {
            vueAppZoneManage.show_table[i].show_ = true;
        }
    }

}

function changeImg() {
    $("#file").click();
}

function PreViewImg() {
    var file = document.getElementById("file").files; //获取input file的文件对象
    var url = URL.createObjectURL(file[0]); //获取所选文件的临时地址
    document.getElementById("icon").src = url;
}

function changeIntroduction() {
    var intro = $("#introduction");
    intro.removeAttr("readonly");
    var text = intro.text();
    intro.focus();
}

function saveintroduction() {
    var form = document.forms["form"];
    var formData = new FormData(form);
    var text = $("#introduction").val();
    formData.append("intro", text);
    var rawurl = window.location.href;
    var param = vueAppZoneManage.zonename;
    var url = "/zoneadmin/saveZoneInfo/" + param;
    $.ajax({
        cache: false,
        processData: false,
        type: "post",
        url: url,
        data: formData,
        // contentType:"application/json;charset=utf-8",

        contentType: false, //不设置内容类型
        // dataType:"json",
        async: true,
        success: function (res) {
            console.log(res)
            window.location.reload();
        },
        error: function (res) {
            console.log("error");
        }
    })

}

function getAdminList() {
    var url = "/zone/" + vueAppZoneManage.zonename + "/manage/getManagerList";
    $('#table_manager').bootstrapTable({
        url: url,         //请求后台的URL（*）
        method: 'get',
        // ajax:
        classes: "table table-striped table-hover text-center",
        theadClasses: "thead thead-light",
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: true,                     //是否启用排序

        sortOrder: "asc",
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        // // searchOnEnterKey:true,
        // // strictSearch: true,
        showColumns: true,                  //是否显示所有的列
        showRefresh: true,               //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        showToggle: true,
        columns: [
            {
                field: 'index',
                title: '序号',
                formatter: function (value, row, index) {
                    return index + 1;
                }
            }, {
                field: 'uid',
                title: '用户id'
            }, {
                field: 'nick',
                title: '用户名'
            }, {
                field: 'authority',
                title: '职位',
                formatter: function (index, row) {
                    if (row['authority'] == 2)
                        return [
                            '<p style="color: #027de7;">吧主</p>'
                        ].join("")
                    else if (row['authority'] == 3)
                        return [
                            '<p style="color: #6f42c1">总管理</p>'
                        ].join("")

                }
            }, {
                field: 'time',
                title: '加入时间'

            }, {
                field: 'operator',
                title: '处理人'

            }, {
                field: 'operation',
                title: '操作',
                events: operateEvents,
                formatter: AddFunctionAtly5("btn-deleteAdmin"),
            }],
        // searchText:true,

    });
    var opt = {
        url: url
    };
    $("#table_manager").bootstrapTable('refresh', opt);
    // $.ajax({
    //     // contentType:"application/json;charset=utf-8",
    //     type: "get",
    //     url: url,
    //     async: true,
    //     success: function (res) {
    //         console.log(res)
    //         vueAppZoneManage.managers = res
    //
    //     },
    //     error: function (res) {
    //         console.log("error");
    //     }
    // });

}

function getGoodList() {
    var url = "/zone/" + vueAppZoneManage.zonename + "/getGoodPostList";
    $('#table_good').bootstrapTable({
        url: url,         //请求后台的URL（*）
        method: 'get',
        classes: "table table-striped table-hover text-center",
        theadClasses: "thead thead-light",
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: true,                     //是否启用排序
        sortOrder: "asc",
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        // // searchOnEnterKey:true,
        // // strictSearch: true,
        showColumns: true,                  //是否显示所有的列
        showRefresh: true,               //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        showToggle: true,
        columns: [
            {
                field: 'id',
                title: '编号',
                formatter: function (value, row, index) {
                    return index + 1;
                }
            }, {
                field: 'pid',
                title: '帖子id'
            }, {
                field: 'authorData.nick',
                title: '发帖人'
            }, {
                field: 'ptittle',
                title: '标题'
            }, {
                field: 'ptittle',
                title: '分类'
            }, {
                field: 'pcreateTime',
                title: '发帖时间'
            }, {
                field: 'operation',
                title: "操作",
                events: operateEvents,
                formatter: AddFunctionAtly("btn-deltegood"),

            }],
        // searchText:true,
        data: vueAppZoneManage.good_posts
    })
    var opt = {
        url: url
    };
    $("#table_good").bootstrapTable('refresh', opt);
    // $.ajax({
    //     // contentType:"application/json;charset=utf-8",
    //     type: "get",
    //     url: url,
    //     async: true,
    //     success: function (res) {
    //         console.log(res)
    //         vueAppZoneManage.good_posts = res
    //     },
    //     error: function (res) {
    //         console.log("error");
    //     }
    // });
}

function getBlackList() {
    var rawurl = window.location.href;
    var param = vueAppZoneManage.zonename
    var url = "/zone/" + param + "/manage/getBlacklist/all";
    $('#table_blacklist').bootstrapTable({
        url: url,         //请求后台的URL（*）
        method: 'get',
        classes: "table table-striped table-hover text-center",
        theadClasses: "thead thead-light text-center",
        detailViewAlign: 'center',
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: true,                     //是否启用排序
        sortOrder: "asc",
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        // // searchOnEnterKey:true,
        // // strictSearch: true,
        showColumns: true,                  //是否显示所有的列
        showRefresh: true,               //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        showToggle: true,
        columns: [
            {
                field: 'id',
                title: '序号',
                formatter: function (value, row, index) {
                    return index + 1;
                }
            }, {
                field: 'username',
                title: '账号',

            }, {
                field: 'deal',
                title: '账号状态',
                formatter: function (index, row) {
                    if (row['deal'] == "封禁")
                        return [
                            '<li style="color: red">封禁中</li>'
                        ].join("")
                    else if (row['deal'] == "禁言")
                        return [
                            '<li style="color: orange">禁言中</li>'
                        ].join("")
                    else {
                        return [
                            '<li style="color: green">正常</li>'
                        ].join("")
                    }
                }
            }, {
                field: 'reason',
                title: '处理原理'
            }, {
                field: 'operator',
                title: '处理人'
            }, {
                field: 'time',
                title: '处理时间'
            }, {
                field: 'expire_days',
                title: '持续时间'
            }, {
                field: 'until',
                title: '到期时间'
            }, {
                field: 'operation',
                title: "操作",
                events: operateEvents,
                formatter: AddFunctionAtly2,
            }],
        // searchText:true,


    });
    var opt = {
        url: url
    };
    $("#table_blacklist").bootstrapTable('refresh', opt);
    // $.ajax({
    //     // contentType:"application/json;charset=utf-8",
    //     type: "get",
    //     url: url,
    //     async: true,
    //     success: function (res) {
    //         console.log(res)
    //         vueAppZoneManage.black_list = res;
    //     },
    //     error: function (res) {
    //         console.log("error");
    //     }
    // });
}

function getPostCycle() {
    var rawurl = window.location.href;
    var param = vueAppZoneManage.zonename
    var url = "/zone/" + param + "/getPostRecycle/all";
    $('#table_postcycle').bootstrapTable({
        url: url,         //请求后台的URL（*）
        method: 'get',
        classes: "table table-striped table-hover text-center",
        theadClasses: "thead thead-light text-center",
        detailViewAlign: 'center',
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: true,                     //是否启用排序
        sortOrder: "asc",
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        // // searchOnEnterKey:true,
        // // strictSearch: true,
        showColumns: true,                  //是否显示所有的列
        showRefresh: true,               //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        showToggle: true,
        columns: [
            {
                field: 'id',
                title: '编号',
                formatter: function (value, row, index) {
                    return index + 1;
                }
            }, {
                field: 'pid',
                title: '帖子id'
            }, {
                field: 'user.nick',
                title: '发帖人'
            }, {
                field: 'post.ptittle',
                title: '标题'
            }, {
                field: 'time',
                title: '删除时间'
            }, {
                field: 'operation',
                title: "操作",
                events: operateEvents,
                formatter: AddFunctionAtly3,

            }],

        // searchText:true,


    });
    var opt = {
        url: url
    };
    $("#table_postcycle").bootstrapTable('refresh', opt);

    // $.ajax({
    //     // contentType:"application/json;charset=utf-8",
    //     type: "get",
    //     url: url,
    //     async: true,
    //     success: function (res) {
    //         console.log(res)
    //         vueAppZoneManage.black_list = res;
    //     },
    //     error: function (res) {
    //         console.log("error");
    //     }
    // });
}

function getPostList() {
    var rawurl = window.location.href;
    var param = vueAppZoneManage.zonename
    var url = "/zone/" + param + "/getPostList";
    $('#table_post').bootstrapTable({

        url: url,         //请求后台的URL（*）
        method: 'get',
        classes: "table table-striped table-hover text-center",
        theadClasses: "thead thead-light text-center",
        detailViewAlign: 'center',
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: true,                     //是否启用排序
        sortOrder: "asc",
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        // // searchOnEnterKey:true,
        // // strictSearch: true,
        showColumns: true,                  //是否显示所有的列
        showRefresh: true,               //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        showToggle: true,
        columns: [
            {
                field: 'checked',
                checkbox: true,
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {//设置满足条件的行可以使用复选框

                }
            },
            {
                field: 'id',
                title: '编号',
                formatter: function (value, row, index) {
                    return index + 1;
                }
            }, {
                field: 'pid',
                title: '帖子id'
            }, {
                field: 'authorData.nick',
                title: '发帖人'
            }, {
                field: 'ptittle',
                title: '标题'
            }, {
                field: 'class',
                title: '分类',
                formatter: function (index, row) {
                    if (row['isGood'] == true)
                        return [
                            '<img src="../../static/images/page_icons/hot-3.png" class="icon_sm">精品贴'
                        ].join("")
                    else {
                        return [
                            '<a class="none_underline_link">普通帖子</a>'
                        ].join("")
                    }
                }

            }, {
                field: 'floorCount',
                title: '楼层数'
            }, {
                field: 'plikeCount',
                title: '热度'
            }, {
                field: 'pcreateTime',
                title: '发帖时间',
                sortable: true
            }],

        // searchText:true,


    });
    var opt = {
        url: url
    };
    $("#table_post").bootstrapTable('refresh', opt);
    // $.ajax({
    //     // contentType:"application/json;charset=utf-8",
    //     type: "get",
    //     url: url,
    //     async: true,
    //     success: function (res) {
    //         console.log(res)
    //     },
    //     error: function (res) {
    //         console.log("error");
    //     }
    // });
}


function getselected(tableId) {
    return $('#' + tableId).bootstrapTable('getSelections');
}

var input1 = $("#admin_query")
input1.keydown(function (e) {
    if (e.keyCode === 13) {
        if (input1.val() != "") {
            var param = input1.val();
            url = "/getUserByUidOrNick/" + param;

            $('#table_search').bootstrapTable({
                url: url,         //请求后台的URL（*）
                method: 'get',
                classes: "table table-striped table-hover text-center",
                theadClasses: "thead thead-light text-center",
                detailViewAlign: 'center',
                striped: true,                      //是否显示行间隔色
                cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                pageNumber: 1,                       //初始化加载第一页，默认第一页
                pageSize: 10,                       //每页的记录行数（*）
                minimumCountColumns: 2,             //最少允许的列数
                columns: [
                    {
                        field: 'checked',
                        checkbox: true,
                        align: 'center',
                        valign: 'middle',
                        formatter: function (value, row, index) {//设置满足条件的行可以使用复选框

                        }
                    },
                    {
                        field: 'index',
                        title: '序号',
                        formatter: function (value, row, index) {
                            return index + 1;
                        }
                    }, {
                        field: 'uid',
                        title: '用户id'
                    }, {
                        field: 'iconUrl',
                        title: '头像',
                        formatter: function (index, row) {
                            return [
                                '<img alt="image" class="mini_data_icon" src="../' + row['iconUrl'] + '">',

                            ].join("")
                        }
                    }, {
                        field: 'nick',
                        title: '用户名'
                    }, {
                        field: 'status',
                        title: '用户状态',
                        formatter: function (index, row) {
                            if (row['authority'] == "-2")
                                return [
                                    '<li style="color: red">封禁中</li>'
                                ].join("")
                            else {
                                return [
                                    '<li style="color: green">正常</li>'
                                ].join("")
                            }
                        }
                    }
                ],
                data: vueAppZoneManage.add_admin_table,
            });
            var opt = {
                url: url
            };
            $("#table_search").bootstrapTable('refresh', opt);
            // $.ajax({
            //     contentType:"application/json;charset=utf-8",
            //     type:"get",
            //     url:url,
            //     async:true,
            //     success:function(res) {
            //         console.log(res);
            //
            //     },
            //     error:function(res){
            //         console.log("error");
            //     }
            // });
        }


    }
});

var input2 = $("#good_query")
input2.keydown(function (e) {
    if (e.keyCode === 13) {
        if (input2.val() != "") {
            var param = input2.val();
            url = "/getPostByPidOrTittle/" + vueAppZoneManage.zonename + "/" + param;
            var opt = {
                url: url
            };
            $('#subtable_post').bootstrapTable({
                url: url,         //请求后台的URL（*）
                method: 'get',
                classes: "table table-striped table-hover text-center",
                theadClasses: "thead thead-light text-center",
                detailViewAlign: 'center',
                striped: true,                      //是否显示行间隔色
                cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                pageNumber: 1,                       //初始化加载第一页，默认第一页
                pageSize: 10,                       //每页的记录行数（*）
                minimumCountColumns: 2,             //最少允许的列数
                columns: [
                    {
                        field: 'checked',
                        checkbox: true,
                        align: 'center',
                        valign: 'middle',
                        formatter: function (value, row, index) {//设置满足条件的行可以使用复选框

                        }
                    },
                    {
                        field: 'index',
                        title: '序号',
                        formatter: function (value, row, index) {
                            return index + 1;
                        }
                    }, {
                        field: 'pid',
                        title: '帖子id'
                    }, {
                        field: 'authorData.nick',
                        title: '发帖人'
                    }, {
                        field: 'ptittle',
                        title: '标题'
                    }, {
                        field: 'class',
                        title: '分类',
                        formatter: function (e, value, index, row) {
                            if (row['isGood'] == true)
                                return [
                                    '<img src="../../static/images/page_icons/hot-3.png" class="icon_sm">精品贴'
                                ].join("")
                            else {
                                return [
                                    '普通帖子'
                                ].join("")
                            }
                        }
                    }, {
                        field: 'floorCount',
                        title: '楼层数'
                    }
                ],
            });
            $("#subtable_post").bootstrapTable('refresh', opt);
            // $.ajax({
            //     contentType:"application/json;charset=utf-8",
            //     type:"get",
            //     url:url,
            //     async:true,
            //     success:function(res) {
            //         console.log(res);
            //
            //     },
            //     error:function(res){
            //         console.log("error");
            //     }
            // });
        }


    }
});

var input3 = $("#blacklist_query")
input3.keydown(function (e) {
    if (e.keyCode === 13) {
        if (input3.val() != "") {
            var param = input3.val();
            url = "/getUserByUidOrNick/" + param;
            var opt = {
                url: url
            };
            $('#subtable_blacklist').bootstrapTable({
                url: url,         //请求后台的URL（*）
                method: 'get',
                classes: "table table-striped table-hover text-center",
                theadClasses: "thead thead-light text-center",
                detailViewAlign: 'center',
                striped: true,                      //是否显示行间隔色
                cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                pageNumber: 1,                       //初始化加载第一页，默认第一页
                pageSize: 10,                       //每页的记录行数（*）
                minimumCountColumns: 2,             //最少允许的列数
                columns: [
                    {
                        field: 'checked',
                        checkbox: true,
                        align: 'center',
                        valign: 'middle',
                        formatter: function (value, row, index) {//设置满足条件的行可以使用复选框

                        }
                    },
                    {
                        field: 'index',
                        title: '序号',
                        formatter: function (value, row, index) {
                            return index + 1;
                        }
                    }, {
                        field: 'uid',
                        title: '用户id'
                    }, {
                        field: 'iconUrl',
                        title: '头像',
                        formatter: function (index, row) {
                            return [
                                '<img alt="image" class="mini_data_icon" src="../' + row['iconUrl'] + '">',

                            ].join("")
                        }
                    }, {
                        field: 'nick',
                        title: '用户名'
                    }
                ],
                data: vueAppZoneManage.add_admin_table,
            });
            $("#subtable_blacklist").bootstrapTable('refresh', opt);
            // $.ajax({
            //     contentType:"application/json;charset=utf-8",
            //     type:"get",
            //     url:url,
            //     async:true,
            //     success:function(res) {
            //         console.log(res);
            //
            //     },
            //     error:function(res){
            //         console.log("error");
            //     }
            // });
        }


    }
});


function addMutiAdmin() {
    var check = getselected("table_search")
    var formdata = new FormData;
    formdata.append("checked", JSON.stringify(check));
    formdata.append("zname", vueAppZoneManage.zonename);
    var url = "/senior/addzonemananger";

    $.ajax({
        // contentType:"application/json;charset=utf-8",
        type: "post",
        url: url,
        data: formdata,
        async: true,
        contentType: false,
        processData: false,
        success: function (res) {
            console.log(res);
            $("#close_addadmin").click();
            $("#nav_admin").click();
        },
        error: function (res) {
            console.log("error");
        }
    });

}

function addGood() {
    var check = getselected("subtable_post")
    var formdata = new FormData;
    formdata.append("checked", JSON.stringify(check));
    formdata.append("zname", vueAppZoneManage.zonename);
    formdata.append("op", 1);
    var url = "/zoneadmin/addgood";
    $.ajax({
        // contentType:"application/json;charset=utf-8",
        type: "post",
        url: url,
        data: formdata,
        async: true,
        contentType: false,
        processData: false,
        success: function (res) {
            console.log(res)
            $("#close_good").click();
            $("#nav_good").click();
        },
        error: function (res) {
            console.log("error");
        }
    });
}

function addBlackList() {
    var check = getselected("subtable_blacklist")
    var formdata = new FormData(document.forms["formBlackList"]);
    formdata.append("check", JSON.stringify(check));
    formdata.append("zname", vueAppZoneManage.zonename);
    var url = "/zoneadmin/addblacklist";
    $.ajax({
        // contentType:"application/json;charset=utf-8",
        type: "post",
        url: url,
        data: formdata,
        async: true,
        contentType: false,
        processData: false,
        success: function (res) {
            console.log(res);
            $("#close_blacklist").click();
            $("#nav_blacklist").click();
            },
        error: function (res) {
            console.log("error");
        }
    });

}

function deletePost() {
    var check = $('#table_post').bootstrapTable('getSelections');

    var formdata = new FormData;
    formdata.append("checked", JSON.stringify(check));
    var url = "/admin/deletePostList";
    $.ajax({
        // contentType:"application/json;charset=utf-8",
        type: "post",
        url: url,
        data: formdata,
        async: true,
        contentType: false,
        processData: false,
        success: function (res) {
            console.log(res);
            $("#nav_posts").click();
        },
        error: function (res) {
            console.log("error");
        }
    });
}

//通项
function removeAjaxs(row, url) {
    var formdata = new FormData;
    formdata.append("row", JSON.stringify(row));
    formdata.append("zname", vueAppZoneManage.zonename);
    $.ajax({
        // contentType:"application/json;charset=utf-8",
        type: "post",
        url: url,
        data: formdata,
        async: true,
        contentType: false,
        processData: false,
        success: function (res) {
            console.log(res);

        },
        error: function (res) {
            console.log("error");
        }
    });
}


function removeAdmin(row) {
    var formdata = new FormData;
    formdata.append("row", JSON.stringify(row));
    formdata.append("zname", vueAppZoneManage.zonename);
    var url = "/senior/removemanager";
    $.ajax({
        // contentType:"application/json;charset=utf-8",
        type: "post",
        url: url,
        data: formdata,
        async: true,
        contentType: false,
        processData: false,
        success: function (res) {
            console.log(res);
            $("#nav_admin").click();
        },
        error: function (res) {
            console.log("error");
        }
    });
}

function removeGood(row) {
    var formdata = new FormData;
    formdata.append("row", JSON.stringify(row));
    formdata.append("zname", vueAppZoneManage.zonename);
    var url = "/zoneadmin/removegood";
    $.ajax({
        // contentType:"application/json;charset=utf-8",
        type: "post",
        url: url,
        data: formdata,
        async: true,
        contentType: false,
        processData: false,
        success: function (res) {
            console.log(res);
            $("#nav_good").click();
        },
        error: function (res) {
            console.log("error");
        }
    });
}

function editBlacklist() {
    var formdata = new FormData(document.forms["formeditBlackList"]);
    formdata.append("zname", vueAppZoneManage.zonename);
    var url = "/zoneadmin/editblacklist";
    $.ajax({
        // contentType:"application/json;charset=utf-8",
        type: "post",
        url: url,
        data: formdata,
        async: true,
        contentType: false,
        processData: false,
        success: function (res) {
            console.log(res);
            $("#close_editblacklist").click();
            $("#nav_blacklist").click();
        },
        error: function (res) {
            console.log("error");
        }
    });
}

function deleteBlacklist(row) {
    var formdata = new FormData;
    formdata.append("row", JSON.stringify(row));
    formdata.append("zname", vueAppZoneManage.zonename);
    var url = "/zoneadmin/removeblacklist";
    $.ajax({
        // contentType:"application/json;charset=utf-8",
        type: "post",
        url: url,
        data: formdata,
        async: true,
        contentType: false,
        processData: false,
        success: function (res) {
            console.log(res);
            $("#nav_blacklist").click();
        },
        error: function (res) {
            console.log("error");
        }
    });
}

function recoverPost(row) {
    var formdata = new FormData;
    formdata.append("row", JSON.stringify(row));
    formdata.append("zname", vueAppZoneManage.zonename);
    var url = "/zoneadmin/recoverpost";
    $.ajax({
        // contentType:"application/json;charset=utf-8",
        type: "post",
        url: url,
        data: formdata,
        async: true,
        contentType: false,
        processData: false,
        success: function (res) {
            console.log(res);
            $("#nav_recycle").click();
        },
        error: function (res) {
            console.log("error");
        }
    });
}


$(document).keydown(function (event) {
    if (event.keyCode == 13) {
        $('form').each(function () {
            event.preventDefault();
        });
    }
});

function fillReason(obj) {
    $("#reason1").val($(obj).text());
    $("#reason2").val($(obj).text());
}