$(document).ready(function () {
    $("#username").html(sessionStorage.getItem("username"));

    getVIP();
    getCoupon();

    $(".info .buyBtn").click(function () {
        buyClick(this);
        //click中的this不一样，谁调用谁就是。
    })
});

var isBuyState = true;
var vipCardId;


// function shit() {
//     console.log("shit");
//     getRequest(
//         "/vip/shit",
//         function () {
//             alert("succ")
//         },
//         function (e) {
//             console.log(e);
//         }
//     )
// }



function getVIP() {
    //获取会员号。如果是会员就渲染出来。
    getSyncRequest(
        '/vip/' + sessionStorage.getItem('id') + '/get',
        function (res) {

            console.log(res.success);


            if (res.success) {
                // 是会员
                $("#member-card").css("visibility", "visible");
                $("#member-card").css("display", "");
                $("#nonmember-card").css("display", "none");

                vipCardId = res.content.id;
                $("#member-id").text(res.content.id);
                $("#member-balance").text("¥" + res.content.balance.toFixed(2));
                $("#member-joinDate").text(res.content.joinDate.substring(0, 10));
                var a =res.content.endTime.substring(0, 10);
                var endYear = parseInt(a.substring(0,4));
                if (endYear>2100){
                    a = "永久有效"
                }
                $("#member-endDate").text(a);
                $("#member-description").text("满 "+res.content.full+" 减 "+res.content.reduce);
            }
            else {
                // 非会员
                $("#member-card").css("display", "none");
                $("#nonmember-card").css("display", "");

                renderAllCardTypes();
            }
        },
        function (error) {

            alert("getVIP");
            console.log(error);
        });

}
    function renderAllCardTypes(){
        //连接后端，获取会与卡价格和优惠，并渲染在页面上。
        //不是会员才进行的吧
        getSyncRequest(
            '/vip/getVIPInfo',
            function (res) {
                if (res.success) {
                    var list = res.content;
                    var a = "";

                    // console.log(list);

                    for (let i =0;i<list.length;i++){
                        var cou = "有效期 : ";
                        if ( list[i].vaildTime<=0){
                            cou += "永久有效";
                        }
                        else {
                            cou += list[i].vaildTime+"天"
                        }
                        a+="<div class='info'>"+
                            " <div class='price'><b id='member-buy-price'></b>"+list[i].price+"元/张</div>"+
                            " <div id='member-buy-description' class='description'>充值优惠 : 满"+list[i].full+"减"+list[i].reduce+"</div>\n" +
                            " <div id='member-buy-description' class='description'>"+cou+"</div>\n" +
                            " <button onclick='buyClick()' class='buyBtn'>立即购买"+(i+1)+"</button>"+
                            "</div>";
                        // <span class='cir'>"+(i+1)+"</span>
                    }
                    $("#nonmember-card").append($(a));

                }
                else {
                    alert(res.content);
                }

            },
            function (error) {
                alert("getVIPInfo");
                console.log(error);
            });

    }



// <button onclick="buyClick()">立即购买</button> 效果：弹出银行卡支付页面。
function buyClick(_this) {
    clearForm();
    $('#buyModal').modal('show');
    $("#userMember-amount-group").css("display", "none");
    isBuyState = true;

    //使用一个session吧：
    let indexStr =$(_this).text().substring(4);
    sessionStorage.setItem("index",parseInt(indexStr)-1);
    // console.log($(_this));
    // 这里的this 表示当前窗口，卧槽。那怎么拿到那个button呢？使用JQ添加click，然后在函数中把this传过来
}

// <button onclick="chargeClick()">立即充值</button>
function chargeClick() {
    clearForm();
    $('#buyModal').modal('show');
    $("#userMember-amount-group").css("display", "block");
    isBuyState = false;
}

function clearForm() {
    $('#userMember-form input').val("");
    $('#userMember-form .form-group').removeClass("has-error");
    $('#userMember-form p').css("visibility", "hidden");
}

//购买
function confirmCommit() {


    if (validateForm()) {
        if ($('#userMember-cardNum').val() === "123123123" && $('#userMember-cardPwd').val() === "123123") {
            if (isBuyState) {
                console.log('/vip/add/' + sessionStorage.getItem('id')+"/"+sessionStorage.getItem("index"));


                getSyncRequest(
                    '/vip/add/' + sessionStorage.getItem('id')+"/"+sessionStorage.getItem("index"),
                    //把vip类型传过去，类型是数据表的index条（0开始）
                    function (res) {
                        if(res.success){
                            $('#buyModal').modal('hide');
                            alert("购买会员卡成功"); //alert是助教加到，先不删掉。 先不提示，我不知道是不是这里的问题。
                            getVIP();
                        }
                        else{
                            alert("不好意思, 出错了！"+res.message+res.success)
                        }
                    },
                    function (error) {
                        alert("失败了");
                        console.log(error);
                    });
            }
            else {
                postSyncRequest(
                    '/vip/charge',
                    {
                        vipId: vipCardId,
                        amount: parseInt($('#userMember-amount').val())
                    },
                    function (res) {
                        $('#buyModal').modal('hide');
                        alert("充值成功");
                        getVIP();
                    },
                    function (error) {
                        alert(error);
                    });
            }
        } else {
            alert("银行卡号或密码错误");
        }
    }
}

function validateForm() {
    var isValidate = true;
    if (!$('#userMember-cardNum').val()) {
        isValidate = false;
        $('#userMember-cardNum').parent('.form-group').addClass('has-error');
        $('#userMember-cardNum-error').css("visibility", "visible");
    }
    if (!$('#userMember-cardPwd').val()) {
        isValidate = false;
        $('#userMember-cardPwd').parent('.form-group').addClass('has-error');
        $('#userMember-cardPwd-error').css("visibility", "visible");
    }
    if (!isBuyState && (!$('#userMember-amount').val() || parseInt($('#userMember-amount').val()) <= 0)) {
        isValidate = false;
        $('#userMember-amount').parent('.form-group').addClass('has-error');
        $('#userMember-amount-error').css("visibility", "visible");
    }
    return isValidate;
}






//获取优惠活动。
function getCoupon() {
    getSyncRequest(
        '/coupon/' + sessionStorage.getItem('id') + '/get',
        function (res) {
            if (res.success) {
                var couponList = res.content;
                console.log(couponList);

                var couponListContent = '';
                for (let coupon of couponList) {
                    couponListContent += '<div class="col-md-6 coupon-wrapper"><div class="coupon"><div class="content">' +
                        '<div class="col-md-8 left">' +
                        '<div class="name">' +
                        coupon.name +
                        '</div>' +
                        '<div class="description">' +
                        coupon.description +
                        '</div>' +
                        '<div class="price">' +
                        '满' + coupon.targetAmount + '减' + coupon.discountAmount +
                        '</div>' +
                        '</div>' +
                        '<div class="col-md-4 right">' +
                        '<div>有效日期：</div>' +
                        '<div>' + formatDate(coupon.startTime) + ' ~ ' + formatDate(coupon.endTime) + '</div>' +
                        '</div></div></div></div>'
                }
                $('#coupon-list').html(couponListContent);

            }
        },
        function (error) {
            alert(error);
        });
}

function formatDate(date) {
    return date.substring(5, 10).replace("-", ".");
}