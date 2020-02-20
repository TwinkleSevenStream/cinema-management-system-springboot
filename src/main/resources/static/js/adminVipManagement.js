var VIPLIST = [];

$(function(){
    render();
});


function addAVipType(){
    var price = $("#price").val();
    var full = $("#full").val();
    var reduce = $("#reduce").val();
    var vaildTime = $("#vaildTime").val();

    if (price && full && reduce){
        if (price>0 && full >reduce&& reduce>0 ) {
            var url = "vipType/addVipType";
            postRequest(
                url,
                {
                    "price":price,
                    "full":full,
                    "reduce":reduce,
                    "vaildTime":vaildTime
                },
                function(res){
                    if (res.success){
                        $("#activityModal").modal('hide'); //使用hide来隐藏---------------------
                        // alert("添加会员卡成功");
                        render();
                    }
                    else {
                        if(res.message=="existed"){
                            alert("该会员卡类型已存在，请勿重复添加！")
                        }
                        else {
                            alert("添加会员卡失败！")
                        }
                    }
                },
                function(e){
                    alert(JSON.stringify(e));
                }
            )
        }
        else {
            alert("输入不合法！")
        }
    }
    else {
        alert("输入不合法！")
    }
}


function render(){
    //渲染页面：显示有哪些会员卡。
//    对了，原来的那张
    getSyncRequest(
        "/getVipTypes",
        function(res){
            if (res.success){
                var list = res.content;
                VIPLIST = list;

                // console.log(list);

                var content = $("#content");
                var a = "";
                for (let i =0;i<list.length;i++){
                    var days = "";
                    if ( list[i].vaildTime<=0){
                        days = "永久有效";
                    }
                    else {
                        days = list[i].vaildTime+" 天"
                    }

                    a +=
                        "<div class='activity-container'>" +
                        "    <div class='activity-card card'>" +
                        "       <div class='activity-line'>" +
                        // "           <span class='title'>会员卡"+(i+1)+"<a class='update'>修改</a></span>" +
                        "           <span class='title'>会员卡"+(i+1)+"</span>" +
                        "       </div>" +
                        "       <div class='activity-line'>" +
                        "           <span>有效天数："+days+"</span>" +
                        "       </div>" +
                        "    </div>" +
                        "    <div class='activity-coupon primary-bg'>" +
                        "        <span class='title'>优惠</span><br>" +
                        "        <span class='title'>满"+list[i].full+"减<span class='error-text title'>"+list[i].reduce+"</span></span>" +
                        "    </div>" +
                        "</div>";
                }
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


// function del(data) {
//     var as = $(".del");
//     for (let i =0;i<as.length;i++) {
//         $(as[i]).click(function () {
//             postSyncRequest(
//                 "/delvipType",
//                 {
//                     "price":data[i].price,
//                     "full":data[i].full,
//                     "reduce":data[i].reduce,
//                     "vaildTime":data[i].vaildTime
//                 },
//                 function (res) {
//
//                 },
//                 function (e) {
//                     alert(JSON.stringify(e));
//                 }
//             );
//             location.reload();
//         })
//
//     }
// }

//


function update() {
    var data =VIPLIST;
        var i = $("#vipNum").val()-1;
        // console.log(data);

        var price = $("#price2").val();
        var full = $("#full2").val();
        var reduce = $("#reduce2").val();
        var vaildTime = $("#vaildTime2").val();
        var sendList = [
            //原来的数据
            {
                "price":data[i].price,
                "full":data[i].full,
                "reduce":data[i].reduce,
                "vaildTime":data[i].vaildTime
            },
            //修改后的数据
            {
                "price":price,
                "full":full,
                "reduce":reduce,
                "vaildTime":vaildTime
            }
        ];

        // console.log(sendList);

        if (price>0 && full >reduce&& reduce>0 && i>=0 && i<VIPLIST.length) {
            postSyncRequest(
                "/updVipType",
                sendList
                ,
                function (res) {
                   alert("修改成功！");
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
