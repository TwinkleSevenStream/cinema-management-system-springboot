$(document).ready(function() {

    var canSeeDate = 0;

    getCanSeeDayNum();
    getCinemaHalls();

    function getCinemaHalls() {
        var halls = [];
        getRequest(
            '/hall/all',
            function (res) {
                halls = res.content;
                renderHall(halls);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }

    function renderHall(halls){
        $('#hall-card').empty();
        var hallDomStr = "";
        // halls.forEach(function (hall) {
        var ind =1;
        for (var k = 0;k<halls.length;k++){
            var hall = halls[k];

            var seat = "";
            // for(var i =0;i<hall.row;i++){
            //     var temp = ""
            //     for(var j =0;j<hall.column;j++){
            for(var i =0;i<hall.rown;i++){
                var temp = "";
                for(var j =0;j<hall.col;j++){

                    temp+="<div class='cinema-hall-seat'></div>";
                }
                seat+= "<div>"+temp+"</div>";
            }
            var hallDom =
                "<div class='cinema-hall'>" +
                "<div>" +
                // "<span class='cinema-hall-name'>"+ hall.name +"</span>" +
                // "<span class='cinema-hall-size'>"+ hall.column +'*'+ hall.row +"</span>" +
                "<span class='cinema-hall-name'>"+"影厅"+" "+ ind +" "+":"+" "+hall.hallname +"</span>" +
                "<span class='cinema-hall-size'>"+ hall.col +'*'+ hall.rown +"</span>" +
                "</div>" +
                "<div class='cinema-seat'>" + seat +
                "</div>" +
                "</div>";
            hallDomStr+=hallDom;
        // });
            ind+=1;
        }

        $('#hall-card').append(hallDomStr);
    }

    function getCanSeeDayNum() {
        getRequest(
            '/schedule/view',
            function (res) {
                canSeeDate = res.content;
                $("#can-see-num").text(canSeeDate);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }

    $('#canview-modify-btn').click(function () {
       $("#canview-modify-btn").hide();
       $("#canview-set-input").val(canSeeDate);
       $("#canview-set-input").show();
       $("#canview-confirm-btn").show();
    });

    $('#canview-confirm-btn').click(function () {
        var dayNum = $("#canview-set-input").val();
        // 验证一下是否为数字
        postRequest(
            '/schedule/view/set',
            {day:dayNum},
            function (res) {
                if(res.success){
                    getCanSeeDayNum();
                    canSeeDate = dayNum;
                    $("#canview-modify-btn").show();
                    $("#canview-set-input").hide();
                    $("#canview-confirm-btn").hide();
                } else{
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    })
});