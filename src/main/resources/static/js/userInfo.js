
$(function () {
    $("#username").html(sessionStorage.getItem("username"));
    $("#un").html(sessionStorage.getItem("username"));

});


function nameUpdate() {
    var name = $("#newName").val();
    if (name.length<4){
        alert("输入长度至少为4，请重试");
    }
    else {
        getSyncRequest(
            "update/name/"+name+"/"+sessionStorage.getItem("username"),
            function (res) {
                if (res.success){
                    $("#usernameModal").modal('hide');
                    alert("修改成功！");
                    sessionStorage.setItem("username",name);
                    $("#un").html(sessionStorage.getItem("username"));
                    $("#username").html(sessionStorage.getItem("username"));
                }
                else {
                    if (res.message=="existed"){
                        alert("该用户名已经被使用，请换一个!")
                    }
                }
            },
            function (e) {
                alert("出错了"+JSON.stringify(e));
            }
        )
    }
}

function pwUpdate() {
    var oldPw = $("#oldPw").val();
    var newPw = $("#newPw").val();
    if (oldPw.length<6 || newPw.length<6) {
        alert("密码长度至少为6，请重试");
    }
    else {
        getSyncRequest(
            "update/pw/"+oldPw+"/"+newPw+"/"+sessionStorage.getItem("username"),
            function (res) {
                // console.log(res);

                if (res.success){
                    $("#pwModal").modal('hide');
                    alert("修改成功啦！");
                }
                else {
                    if (res.message=="oldPw_err"){
                        alert("原密码输入错误！")
                    }
                }
            },
            function (e) {
                alert("出错了"+JSON.stringify(e));
            }
        )
    }


}