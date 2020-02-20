$(function () {
    sessionStorage.setItem("userType","user");  //要增删查改的对象：用户还是管理员。
    if (sessionStorage.getItem("isRoot").length!==4){
        $("#adminBox").css("display","none");
        $("#adminNav").hide();
        $("#adminLi").css("display","none");
        // $("#navUl").css("width","120px");
        $("#userLi").css("width","230px");
        // $("#navUl").hide();
        renderUser();
//        renderAdmin();

    }
    else {
//        $("#adminNav").hide();
        $("#adminNav").show();

        renderAdmin();
        renderUser();
        $("#userNav").click(function () {
            $("#adminBox").css("display","none");
            $("#userBox").css("display","block");
            $("#userNav").addClass("active");
            $("#adminNav").removeClass("active");
            renderUser();
            sessionStorage.setItem("userType","user");
        });
        $("#adminNav").click(function () {
            $("#adminBox").css("display","block");
            $("#userBox").css("display","none");
            $("#userNav").removeClass("active");
            $("#adminNav").addClass("active");
            renderAdmin();
            sessionStorage.setItem("userType","admin");
        })
    }






});

function renderUser() {
    getSyncRequest(
        "/admin/getAllUser",
        function (res) {
            if (res.success){
                var list = res.content;
                var htmlStr="";
                for (let i = 0;i<list.length;i++){
                    htmlStr+=
                        "<tr>" +
                        "  <td>"+(i+1)+"</td>" +
                        "  <td>"+list[i].id+"</td>" +
                        "  <td>"+list[i].username+"</td>" +
                        "  <td>******</td>"+
                        "  <td>"+list[i].consumption+"</td>"+
                        "</tr>";
                }
                $("#tb_user").html(htmlStr);
            }
        },
        function (e) {
            alert(JSON.stringify(e))
        }
    )
}

function renderAdmin() {
    getSyncRequest(
        "/admin/getAllAdmin",
        function (res) {
            if (res.success){
                var list = res.content;
                var htmlStr="";
                for (let i = 0;i<list.length;i++){
                    htmlStr+=
                        "<tr>" +
                        "  <td>"+(i+1)+"</td>" +
                        "  <td>"+list[i].id+"</td>" +
                        "  <td>"+list[i].username+"</td>" +
                        "  <td>******</td>"+
                        "</tr>";
                }
                $("#tb_admin").html(htmlStr);
            }
        },
        function (e) {
            alert(JSON.stringify(e))
        }
    )
}

function addOne() {
    var name = $("#name").val();
    var pw = $("#pw").val();
    var userType = sessionStorage.getItem("userType");
    if (name.length>=4 && pw.length>=6){
        getSyncRequest(
            "add/"+userType+"/"+name+"/"+pw,
            function (res) {
                if (res.success){
                    alert("添加成功！");
                    $("#addModal").modal("hide");
                    location.reload();
                }
                else {
                    if (res.message=="existed"){
                        alert("该用户已存在！")
                    }
                }
            },
            function (e) {
                alert(JSON.stringify(e));
            }
        )
    }
    else {
        alert("输入不合法，请重试！")
    }

}







function delOne() {
    var inp= $("#inp").val();
    var userType = sessionStorage.getItem("userType");
    var byWhat="";
    if (inp.length!=0){
        if (inp.length>=4) {
            byWhat = "name";
        }
        else {
            byWhat = "id";
        }

        getSyncRequest(
            "del/"+userType+"/"+byWhat+"/"+inp,
            function (res) {
                if (res.success){
                    alert("删除成功！");
                    $("#delModal").modal("hide");
                    location.reload();
                }
            },
            function (e) {
                alert(JSON.stringify(e));
            }
        )
    }
    else {
        alert("输入不合法，请重试！")
    }

}

