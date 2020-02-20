$(document).ready(function () {

    $("#login-btn").click(function () {
        var formData = getLoginForm();
        if (!validateLoginForm(formData)) {
            return;
        }

        var isUser = $("#userRadio").prop("checked");  //这里的user表示狭义的用户，不包含管理员。
        // console.log(isUser);

        if (isUser){
            //普通用户：
            postRequest(
                '/login',
                formData,
                function (res) {
                    console.log(res);
                    if (res.success) {
                        sessionStorage.setItem('username', formData.username);
                        sessionStorage.setItem('id', res.content.id);
                        sessionStorage.setItem('role', 'user');
                        window.location.href = "/user/home"
                    }
                    else {
                        alert("登录失败"+res.message);
                    }
                },
                function (error) {
                    alert(JSON.stringify(error));
                });
        }
        else {
            //管理员
            postRequest(
                '/login2',
                formData,
                function (res) {
                    console.log(res.success);
                    console.log(res);

                    if (res.success) {
                        sessionStorage.setItem('username', formData.username);

                        if (formData.username=="root") {
                            sessionStorage.setItem("isRoot",true);
                        }
                        else {
                            sessionStorage.setItem("isRoot",false);
                        }
                        sessionStorage.setItem('id', res.content.id);
                        sessionStorage.setItem('role', 'admin');
                        window.location.href = "/admin/movie/manage"

                    } else {
                        alert("登录失败:"+res.message);
                    }
                },
                function (error) {
                    alert(JSON.stringify(error));
                });
    }

    });

    function getLoginForm() {
        return {
            username: $('#index-name').val(),
            password: $('#index-password').val()
        };
    }

    function validateLoginForm(data) {
        var isValidate = true;
        if (!data.username) {
            isValidate = false;
            $('#index-name').parent('.input-group').addClass('has-error');
            $('#index-name-error').css("visibility", "visible");
        }
        if (!data.password) {
            isValidate = false;
            $('#index-password').parent('.input-group').addClass('has-error');
            $('#index-password-error').css("visibility", "visible");
        }
        return isValidate;
    }
});