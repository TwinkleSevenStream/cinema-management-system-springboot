

$(function(){
    render();
});




function render(){
    //渲染页面：显示有哪些会员卡。
    getSyncRequest(
        "/refund/get/",
        function(res){

            if (res.success){
                var strategy = res.content;

                var content = $("#content");
                var a = "";

                a +=
                    "<div class='activity-container'>" +
                    "    <div class='activity-card card'>" +
                    "       <div class='activity-line'>" +
                    // "           <span class='title'>会员卡"+(strategy+1)+"<a class='update'>修改</a></span>" +
                    "           <span class='title'>退款策略"+"</span>" +
                    "       </div>" +
                    "    </div>" +
                    "    <div class='activity-coupon primary-bg'>" +
                    "        <span class='title'>退还比例：" + strategy.discount + "%</span><br>" +
                    "        <span class='title'>提前时间："+strategy.beforeMinutes+"分钟</span>" +
                    "    </div>" +
                    "</div>";

                content.html($(a));

                // del(list);
                // update(list)

            }
            else {
                alert("出错啦")
            }
        },
        function(e){
            alert(JSON.stringify(e));
        }
    )

}

function update() {
    var beforeMinutes = parseInt($("#beforeMinutes").val());
    var discount = parseInt($("#discount").val());

    var sendList = {
            "discount": discount,
            "beforeMinutes": beforeMinutes,
        };

    if (discount >= 0 && beforeMinutes >= 0) {  //  检查两个参数格式是否正确
        postSyncRequest(
            "/refund/strategy/",
            sendList
            ,
            function (res) {
               alert("修改成功！");

               // $("#refunudModal").modal("hide");

                location.reload();

            },
            function (e) {
                alert(JSON.stringify(e));
            }
        );
    }
    else {
        alert("输入不合法，请重试！")
    }
}
