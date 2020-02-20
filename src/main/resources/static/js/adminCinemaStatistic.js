$(document).ready(function() {


    getScheduleRate();
    
    getBoxOffice();

    getAudiencePrice();

    //初始化时使用“今天”
    var d = new Date();
    var date = d.getFullYear() + '/' + (d.getMonth() + 1) + '/' + d.getDate();
    getPlacingRate(date);



    //重设时间
    $("#resure-btn").click(function () {
        var d = getDate();
        if (d!=false){
            getPlacingRate(d)
        }
        // $("#movieModal").css("display","none");
        // $("body").removeClass("modal-open");
        //------------------------------------注意：必使用.modal('hide');來隱藏，其他都不行--------------
        $("#movieModal").modal('hide');
    });


    //默认是最近一周，五部电影。
    getPolularMovie(7,5);
    //修改后：
    $("#resure-btn2").click(function () {
        var days = $("#popularMovieNumDay").val();
        var movieNum = $("#popularMovieNum").val();
        if (days<=0 || movieNum<=0){
            alert("输入格式不对，请重试！");
        }
        else {
            getPolularMovie(days,movieNum);
            $("#popularMovieModal").modal('hide');
        }
    });




    function getScheduleRate() {

        getRequest(
            '/statistics/scheduleRate',
            function (res) {
                var data = res.content||[];
                var tableData = data.map(function (item) {
                   return {
                       value: item.time,
                       name: item.name
                   };
                });
                var nameList = data.map(function (item) {
                    return item.name;
                });
                var option = {
                    title : {
                        text: '今日排片率',
                        subtext: new Date().toLocaleDateString(),
                        x:'center'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        x : 'center',
                        y : 'bottom',
                        data:nameList
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true},
                            dataView : {show: true, readOnly: false},
                            magicType : {
                                show: true,
                                type: ['pie', 'funnel']
                            },
                            restore : {show: true},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    series : [
                        {
                            name:'面积模式',
                            type:'pie',
                            radius : [30, 110],
                            center : ['50%', '50%'],
                            roseType : 'area',
                            data:tableData
                        }
                    ]
                };
                var scheduleRateChart = echarts.init($("#schedule-rate-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }

    function getBoxOffice() {

        getRequest(
            '/statistics/boxOffice/total',
            function (res) {
                var data = res.content || [];
                var tableData = data.map(function (item) {
                    return item.boxOffice;
                });
                var nameList = data.map(function (item) {
                    return item.name;
                });
                var option = {
                    title : {
                        text: '所有电影票房',
                        subtext: '截止至'+new Date().toLocaleDateString(),
                        x:'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: nameList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: tableData,
                        type: 'bar'
                    }]
                };
                var scheduleRateChart = echarts.init($("#box-office-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
                console.log(error);
            });
    }

//每日客单价
    function getAudiencePrice() {
        getRequest(
            '/statistics/audience/price',
            function (res) {
                var data = res.content || [];
                var tableData = data.map(function (item) {
                    return item.price;
                });
                var nameList = data.map(function (item) {
                    return formatDate(new Date(item.date));
                });
                var option = {
                    title : {
                        text: '每日客单价',
                        x:'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: nameList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: tableData,
                        type: 'line'
                    }]
                };
                var scheduleRateChart = echarts.init($("#audience-price-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            });
    }




// OK，let me finish it!
//Todo : 获取所有电影某天的上座率
// * 上座率参考公式：假设某影城设有n 个电影厅、m 个座位数，相对上座率=观众人次÷放映场次÷m÷n×100%
// * 此方法中运用到的相应的操作数据库的接口和实现请自行定义和实现，相应的结果需要自己定义一个VO类返回给前端
    function getPlacingRate(date) {
        // console.log("date: "+date);

        getRequest(
            'statistics/PlacingRate/'+date,  //返回字符串如2019/02/03

            function (res) {
                // console.log(res.content);

                var data = res.content || [];  // ||[]是何意？ 应该是排除为null的情况。
                //就一个map：电影名，对应的上座率

                //map函数，对data中的数据，返回它的值boxOffice组成的列表。
                var tableData = data.map(function (item) {
                    return item.seatNum===0?0:item.ticketNum/item.seatNum;
                    // return item.rate;
                });
                var nameList = data.map(function (item) {
                    return item.movie;
                });
                var option = {
                    title : {
                        text: '每日上座率',
                        subtext: date,
                        x:'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: nameList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: tableData,
                        type: 'bar'
                    }]
                };
                var scheduleRateChart = echarts.init($("#place-rate-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
                console.log(error);
            });
    }

    function getDate() {
        if ($("#movie-date-input").val().toString().length!==10){
           alert("输入格式不对！")
            return false;
        }
        else {
            var  d = $("#movie-date-input").val();
            return d.substring(0,4)+"/"+d.substring(5,7)+"/"+d.substring(9);
        }
    }


    function getPolularMovie(days, movieNum) {
        getRequest(
            'statistics/popular/movie/'+days+"/"+movieNum,

            function (res) {
                console.log(res.content);

                var data = res.content || [];
                var tableData = data.map(function (item) {
                    return item.boxOffice;
                });
                var nameList = data.map(function (item) {
                    return item.name;
                });
                movieNum = movieNum<=data.length?movieNum:data.length;
                var option = {
                    title : {
                        text: '最受欢迎的电影',
                        subtext: "最近 "+days+" 天最受欢迎的 "+movieNum+" 部电影",
                        x:'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: nameList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: tableData,
                        type: 'bar'
                    }]
                };
                var scheduleRateChart = echarts.init($("#popular-movie-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
                console.log(error);
            });
    }
});