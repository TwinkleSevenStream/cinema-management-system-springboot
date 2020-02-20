//已选座位，是被别人选了的还是自己已选？？ 嗯是自己选的，renderSchedule(schedule, seats) 才会渲染别人锁定的。
var selectedSeats = []
var scheduleId;
//order 这里有电影信息
var order = {ticketId: [], couponId: 0};
var coupons = [];
var isVIP = false;
var useVIP = true;

$(function () {
    $("#username").html(sessionStorage.getItem("username"));

    scheduleId = parseInt(window.location.href.split('?')[1].split('&')[1].split('=')[1]);

    getInfo();

    function getInfo() {
        getSyncRequest(
            '/ticket/get/occupiedSeats?scheduleId=' + scheduleId,
            //发送请求：响应：
//            @GetMapping("/get/occupiedSeats")
//            public ResponseVO getOccupiedSeats(@RequestParam int scheduleId){
//                return ticketService.getBySchedule(scheduleId);
//            注意：ResponseVO还有一个Object的Content，十分机智，用来传递数据
            
            function (res) {
                if (res.success) {
                    renderSchedule(res.content.scheduleItem, res.content.seats);
                    //上面这个是加载座位表：哪些是可选，哪些不可选，哪些是我已选的。
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }
});





function renderSchedule(schedule, seats) {
    $('#schedule-hall-name').text(schedule.hallName);
    $('#order-schedule-hall-name').text(schedule.hallName);
    $('#schedule-fare').text(schedule.fare.toFixed(2));
    $('#order-schedule-fare').text(schedule.fare.toFixed(2));
    $('#schedule-time').text(schedule.startTime.substring(5, 7) + "月" + schedule.startTime.substring(8, 10) + "日 " + schedule.startTime.substring(11, 16) + "场");
    $('#order-schedule-time').text(schedule.startTime.substring(5, 7) + "月" + schedule.startTime.substring(8, 10) + "日 " + schedule.startTime.substring(11, 16) + "场");

    var hallDomStr = "";
    var seat = "";
    for (var i = 0; i < seats.length; i++) {
        var temp = ""
        for (var j = 0; j < seats[i].length; j++) {
            var id = "seat" + i + j
            if (seats[i][j] == 0) {
                // 未选
                temp += "<button class='cinema-hall-seat-choose' id='" + id + "' onclick='seatClick(\"" + id + "\"," + i + "," + j + ")'></button>";
            } else {
                // 已选中
                temp += "<button class='cinema-hall-seat-lock'></button>";
            }
        }
        seat += "<div>" + temp + "</div>";
    }
    var hallDom =
        "<div class='cinema-hall'>" +
        "<div>" +
        "<span class='cinema-hall-name'>" + schedule.hallName + "</span>" +
        "<span class='cinema-hall-size'>" + seats.length + '*' + seats[0].length + "</span>" +
        "</div>" +
        "<div class='cinema-seat'>" + seat +
        "</div>" +
        "</div>";
    hallDomStr += hallDom;

    $('#hall-card').html(hallDomStr);
}

//选座时点击会改变样式。
function seatClick(id, i, j) {
    let seat = $('#' + id);
    if (seat.hasClass("cinema-hall-seat-choose")) { //被选了
        seat.removeClass("cinema-hall-seat-choose");
        seat.addClass("cinema-hall-seat");

        selectedSeats[selectedSeats.length] = [i, j]
    } 
    else {//移除：
        seat.removeClass("cinema-hall-seat");
        seat.addClass("cinema-hall-seat-choose");

        selectedSeats = selectedSeats.filter(function (value) {
            return value[0] != i || value[1] != j;
        })
    }

    selectedSeats.sort(function (x, y) {
        var res = x[0] - y[0];
        return res === 0 ? x[1] - y[1] : res;
    });

    let seatDetailStr = "";
    if (selectedSeats.length == 0) {
        seatDetailStr += "还未选择座位"
        $('#order-confirm-btn').attr("disabled", "disabled")
    } else {
        for (let seatLoc of selectedSeats) {
            seatDetailStr += "<span>" + (seatLoc[0] + 1) + "排" + (seatLoc[1] + 1) + "座</span>";
        }
        $('#order-confirm-btn').removeAttr("disabled");
    }
    $('#seat-detail').html(seatDetailStr);
}



//注意：本方法是选座时执行的
/*
 * 分析：
 * 		购票时出现三个页面：
 * 		1.确认下单：orderConfirmClick()：下单，获取选座等信息，这里生成了票，不过如果没有付款票的状态就是失效的
		2.确认订单，立即支付：这个页面是使用的是orderinfo的数据，这个数据都是来自于后台，但是有些事先从前端传到后台。
		3.确认支付：payConfirmClick()：支付
 */
function orderConfirmClick() {
    $('#seat-state').css("display", "none");
    $('#order-state').css("display", "");

    // TODO:这里是假数据，需要连接后端获取真数据，数据格式可以自行修改，但如果改了格式，别忘了修改renderOrder方法
    
    function lockSeat(){
	//    把座位表发送给controller，然后加入数据库
	//    public ResponseVO lockSeat(@RequestBody TicketForm ticketForm){}
	//    需要一个json，同TicketForm格式
	//        private int userId;
	//	      private int scheduleId;
	//	      private List<SeatForm> seats;
	//    	     其中SeatForm：
	//    		columnIndex
	//    		rowIndex
	    var seats = {
	    		"userId":parseInt(sessionStorage.getItem("id")), //TicketForm中userID是int类型
	    		"scheduleId":scheduleId,
	    		"seats":
	    			//这里无法静态加入
	    			[
	    				//参数3是一个list
	    			]
	    };
	    //故使用动态加入：
    	var len = selectedSeats.length;

//    	console.log(selectedSeats); 嗯 还好，它帮你实现了，你点击机会添加到selectedSeats之中，直接使用。
    	for(let i =0;i<len;i++){
    		seats.seats[i]={
    				"columnIndex":selectedSeats[i][1],  //注意：[行，列]
    				"rowIndex":selectedSeats[i][0]
    		}
    	}

	    postSyncRequest(
	            '/ticket/lockSeat',
	            seats,
	            function (res) {
	               if(!res.success){
	                    alert("选座下单失败！");
	               }
	            },
	            function (error) {
	                alert(error);
	     });
    };
    //好了，现在把选座时下单但是没有付款的票都加入到了数据库。
//    2019年5月7日01:27:20   好晚了，还得冲个冷水澡，想想就舒服！！！晚安，Edwin。
    lockSeat();

//Warnning: 前端“确认订单，立即支付”页面的订单就是根据这个orderinfo来生成的，它是固定的，所以需要访问后端
    		//这里两张票，但实际上要看到底选了几个座位，使用json添加更多的票
//    	orderinfo需要这些数据：没有电影票状态
//	          电影	   场次	 票数/座位	单价(元)  	总价(元)
//            优惠 总金额、优惠金额、实付款


//注意注意：这里的activity我还没有从数据库中读取，但是本JS文件丝毫没有使用活动，在管理员那里能发布活动。所以暂时不影响用例测试。
//有多个活动。
//一个活动：
//    1.有自身属性
//    2.对应多个Movie
//    3.对应一个优惠。
    var orderInfo = {
        "ticketVOList": [],
        "total": 0,
        "coupons":[],
        "activities": [
        //     {
        //     "id": 4,
        //     "name": "测试活动",
        //     "description": "测试活动",
        //     "startTime": "2019-04-21T00:00:00.000+0800",
        //     "endTime": "2019-04-27T00:00:00.000+0800",
        //     "movieList": [{
        //         "id": 10,
        //         "name": "夏目友人帐",
        //         "posterUrl": "http://n.sinaimg.cn/translate/640/w600h840/20190312/ampL-hufnxfm4278816.jpg",
        //         "director": "大森贵弘 /伊藤秀樹",
        //         "screenWriter": "",
        //         "starring": "神谷浩史 /井上和彦 /高良健吾 /小林沙苗 /泽城美雪",
        //         "type": "动画",
        //         "country": null,
        //         "language": null,
        //         "startDate": "2019-04-14T22:54:31.000+0800",
        //         "length": 120,
        //         "description": "在人与妖怪之间过着忙碌日子的夏目，偶然与以前的同学结城重逢，由此回忆起了被妖怪缠身的苦涩记忆。此时，夏目认识了在归还名字的妖怪记忆中出现的女性·津村容莉枝。和玲子相识的她，现在和独子椋雄一同过着平稳的生活。夏目通过与他们的交流，心境也变得平和。但这对母子居住的城镇，却似乎潜伏着神秘的妖怪。在调查此事归来后，寄生于猫咪老师身体的“妖之种”，在藤原家的庭院中，一夜之间就长成树结出果实。而吃掉了与自己形状相似果实的猫咪老师，竟然分裂成了3个",
        //         "status": 0,
        //         "islike": null,
        //         "likeCount": null
        //     }],
        //     "coupon": {
        //         "id": 8,
        //         "description": "测试优惠券",
        //         "name": "123",
        //         "targetAmount": 100.0,
        //         "discountAmount": 99.0,
        //         "startTime": "2019-04-21T00:00:00.000+0800",
        //         "endTime": "2019-04-27T00:00:00.000+0800"
        //     }
        // }
        ]
    };


    getSyncRequest(
        "/coupon/"+sessionStorage.getItem("id")+"/get",
        function (res){
            if (res.success){
                list = res.content;
                // console.log(list);

                for(let i=0;i<list.length;i++){
                   var temp = list[i]; //Coupon
//                   console.log(temp.startTime) //2019-05-01T00:00:00.000+0800 正常，是一个字符串，而不是一个对象
                   orderInfo.coupons[orderInfo.coupons.length]={"id": temp.id,"description":temp.description,"name": temp.name,"targetAmount": temp.targetAmount,"discountAmount":temp.discountAmount,"startTime":temp.startTime ,"endTime": temp.endTime};
                }
            }
        },
        function (err){ alert(err); }
    );

/**   一切的一切，都是因为忽略了Ajax异步传输导致的！！！
json数组有长度json.arrayname.length,如果单纯是json格式，那么不能直接使用json.length方式获取长度，而应该使用其他方法。
*/


//再读取ticketVOList，吧orderinfo完善：
//            "ticketVOList": [
//    //                {
//    //                    "id": 63,
//    //                    "userId": 15,
//    //                    "scheduleId": 67,
//    //                    "columnIndex": 5,
//    //                    "rowIndex": 1,
//    //                    "state": "未完成"
//    //                }
//    	        ]

    function ticketsToOrder(){
           for (let i=0;i<selectedSeats.length;i++){
               getSyncRequest(
                    "/ticket/"+scheduleId+"/"+selectedSeats[i][1]+"/"+selectedSeats[i][0],
                    function (res){
                        if(res.success){
                            let a = res.content;
//                            console.log(a);
                            orderInfo.ticketVOList[orderInfo.ticketVOList.length]={
                                  "id": a.id,
                                  "userId": a.userId,
                                  "scheduleId": a.scheduleId,
                                  "columnIndex": a.columnIndex,
                                  "rowIndex": a.rowIndex,
                                  "state": a.state
                            }
                        }
                    }
               );
           }
    }
    ticketsToOrder();
    //之所以收不到数据：还是因为前面下单时使用的是异步，哪里还没加入数据库，我这里就查找，当然查不到。

//电影价格：在schedule里面。
    var fare = 0
    getSyncRequest(
        "/schedule/"+scheduleId,
        function (res){
            if(res.success){
                 fare= selectedSeats.length * res.content.fare;
            }
        },
        function (e){alert(e)}
    );
    //这里也是：必须使用同步，不然数据来没查找传过来，你就在前端渲染了。
    orderInfo.total = fare;
    renderOrder(orderInfo);
    
  //这个request就是判断用户是不是VIP，不是VIP就只有银行卡支付只一种选项
    getSyncRequest(
        '/vip/' + sessionStorage.getItem('id') + '/get',
        //VIPCardController里面 getVIP
        function (res) {
//        这个res就是调用Controller中public ResponseVO getVIP(@PathVariable int userId){}返回的类型，即ResponseVO，
//        	有私有变量private Boolean success;
            isVIP = res.success;
            useVIP = res.success;
            if (isVIP) {
                $('#member-balance').html("<div><b>会员卡余额：</b>" + res.content.balance.toFixed(2) + "元</div>");
            } else {
                $("#member-pay").css("display", "none");
                $("#nonmember-pay").addClass("active");

                $("#modal-body-member").css("display", "none");
                $("#modal-body-nonmember").css("display", "");
            }
        },
        function (error) {
            alert(error);
        });
}


function switchPay(type) {
    useVIP = (type == 0);
    if (type == 0) {
        $("#member-pay").addClass("active");
        $("#nonmember-pay").removeClass("active");

        $("#modal-body-member").css("display", "");
        $("#modal-body-nonmember").css("display", "none");
    } else {
        $("#member-pay").removeClass("active");
        $("#nonmember-pay").addClass("active");

        $("#modal-body-member").css("display", "none");
        $("#modal-body-nonmember").css("display", "");
    }
}

//render: 使成为;使变得;使处于某状态;给予;提供;回报;递交;呈献;提交
//这个看来是根据orderinfo来渲染订单页面，看来orderinfo存储了所有关键信息。
function renderOrder(orderInfo) {
    var ticketStr = "<div>" + selectedSeats.length + "张</div>";
    for (let ticketInfo of orderInfo.ticketVOList) {
        ticketStr += "<div>" + (ticketInfo.rowIndex + 1) + "排" + (ticketInfo.columnIndex + 1) + "座</div>";
//        本页面有一个全局json：order，order的内容是根据orderinfo来的，所以orderinfo在order之前
        order.ticketId.push(ticketInfo.id);
    }
    $('#order-tickets').html(ticketStr);

    var total = orderInfo.total.toFixed(2);

    //我用一个sessionStorage把它存起来吧。------------------------------------amount-------------------------------------
    sessionStorage.setItem("amount",total);


    $('#order-total').text(total);
    $('#order-footer-total').text("总金额： ¥" + total);


    var couponTicketStr = "";
    if (orderInfo.coupons.length == 0) {
        $('#order-discount').text("优惠金额：无");
        $('#order-actual-total').text(" ¥" + total);
        $('#pay-amount').html("<div><b>金额：</b>" + total + "元</div>");
    } else {
        coupons = orderInfo.coupons;
        for (let coupon of coupons) {
            couponTicketStr += "<option>满" + coupon.targetAmount + "减" + coupon.discountAmount + "</option>"
        }
        $('#order-coupons').html(couponTicketStr);
        changeCoupon(0);
    }
}

function changeCoupon(couponIndex) {
    order.couponId = coupons[couponIndex].id;
    var orderTotal = parseFloat($('#order-total').text());
    var targetAmount = parseFloat(coupons[couponIndex].targetAmount).toFixed(2)
    var discountAmount = parseFloat(coupons[couponIndex].discountAmount).toFixed(2);
    var actualTotal;
    if( orderTotal>targetAmount){
        actualTotal = orderTotal-discountAmount;
        $('#order-discount').text("优惠金额： ¥" + discountAmount);
        console.log("okok")
    }
    else{
        actualTotal = orderTotal;
        $('#order-discount').text("优惠金额： ¥0.00");
          console.log("notok")
    }
//    var actualTotal = ?orderTotal-discountAmount:orderTotal;
//    $('#order-discount').text("优惠金额： ¥" + coupons[couponIndex].discountAmount.toFixed(2));

    $('#order-actual-total').text(" ¥" + actualTotal);
    $('#pay-amount').html("<div><b>金额：</b>" + actualTotal + "元</div>");
}


//发送总额到后台。（本想就使用conpleteByVip的那个直接传，算了，我自己的，独立出来。）
function sendAmount(){
    getSyncRequest(
        "/ticket/"+parseInt((sessionStorage.getItem("amount"))),
        function (res){
            if (!res){ alert("Error!")}
        },
        function (err){alert("error!");console.log(err);}
    );
}



//这里需要改编电影票的状态吧，不需要了，如果不支付，一段时间后0--》2自动失效。
function payConfirmClick() {       //为了避免错误，先试用同步：
    sendAmount();
	//使用VIP卡：需要做几件事：
//	    1.扣费（费够）
//      2.把票的状态更新为1.
    if (useVIP) {

         var ticketIdAndCouponIdVO ={
                                ticketId:[],
                                couponId:order.couponId
                        };
         for(let i =0;i<order.ticketId.length;i++){
            ticketIdAndCouponIdVO.ticketId.push(order.ticketId[i]);
         }
    	 postSyncRequest(
    			"/ticket/vip/buy",
                ticketIdAndCouponIdVO,   //这里直接使用order也是可以的。
                function (res) {
                    if(res.success){
                        postPayRequest()  //扣费成功后才执行此方法。
                    }
                    else{
                        //需要提醒余额不足吗？
                        alert("余额不足了！请充值！");
                        console.log(ticketIdAndCouponIdVO)
                        console.log(res);
                    }
                },
                function (error) { alert("出错了："+error);console.log(error)}
         );
    }
    //使用银行卡支付：
    else {
        if (validateForm()) {
            if ($('#userBuy-cardNum').val() === "123123123" && $('#userBuy-cardPwd').val() === "123123") {
                postSyncRequest(
                    "/ticket/buy",
                    order,
                    function (res){
                         if(res.success){
//                            alert("购票成功");  //先不要加上多余alert（）
                            postPayRequest();
                         }
                         else{
                            alert("出错了啊！！！")
                         }
                    },
                    function (err){
                        alert("失败了");
                        console.log(err);
                    }
                );

            } else {
                alert("银行卡号或密码错误");
            }
        }
    }
}


// TODO:填空------------------------------------------------------------------------------------------------------

function postPayRequest() {
    //发送请求后，这里样式发生改变，确认支付框消失
    $('#order-state').css("display", "none");
    $('#success-state').css("display", "");
    $('#buyModal').modal('hide')
}

function validateForm() {
    var isValidate = true;
    if (!$('#userBuy-cardNum').val()) {
        isValidate = false;
        $('#userBuy-cardNum').parent('.form-group').addClass('has-error');
        $('#userBuy-cardNum-error').css("visibility", "visible");
    }
    if (!$('#userBuy-cardPwd').val()) {
        isValidate = false;
        $('#userBuy-cardPwd').parent('.form-group').addClass('has-error');
        $('#userBuy-cardPwd-error').css("visibility", "visible");
    }
    return isValidate;
}



//我加的，取消锁座。 算了，HTML先不要加这个。
function cancelTicket(){
// public ResponseVO cancelTicket(@RequestParam List<Integer> ticketId){
     postSyncRequest(
            '/ticket/cancel',
            order.ticketId,
            function (res) {
               if(!res.success){
                    alert("选座取消失败！");
               }
            },
            function (error) {
                alert(error);
     });
}