$(function () {
    $("#username").html(sessionStorage.getItem("username"));

    $("#chargeHistoryNav").click(function () {
        // $("#chargeContent").css("display","block");
        $("#chargeContent").show();
        // $("#spendContent").css("display","none");
        $("#spendContent").hide();
        renderChargeHistory();
    });
    $("#spendHistoryNav").click(function () {
        // $("#chargeContent").css("display","none");
        $("#chargeContent").hide();
        // $("#spendContent").css("display","block");
        $("#spendContent").show();
        renderSpendHistory();
    });

    renderChargeHistory();


});


function renderChargeHistory() {
    getSyncRequest(
        "/user/history/charge",
        function (res) {
            if (res.success){
                var list = res.content;
                var tableStr ="";
                for (let i=0;i<list.length;i++){
                    tableStr+=
                        "<tr><td>"+list[i].amount+"</td><td>"+list[i].chargeTime.substring(0,10)+"</td></tr>";
                }
                $("#tb_charge").html(tableStr);
            }
        },
        function (e) {
            alert(JSON.stringify(e))
        }
    )
}


function renderSpendHistory() {
    getSyncRequest(
        "/user/history/spend/"+sessionStorage.getItem("id"),
        // /user/history/spend/{userId}
        function (res) {
            console.log(res.content);

            if (res.success){
                var list = res.content;
                var tableStr ="";
                for (let i=0;i<list.length;i++){
                    tableStr+=
                        "<tr><td>"+list[i].movie+"</td><td>"+list[i].amount+"</td><td>"+list[i].spendTime.substring(0,10)+"</td></tr>";
                }
                $("#tb_spend").html(tableStr);
            }
        },
        function (e) {
            alert(JSON.stringify(e))
        }
    )
}

