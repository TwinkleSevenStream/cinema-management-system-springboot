package     com.example.cinema.bl.statistics;

import  com.example.cinema.vo.ResponseVO;

import java.util.Date;




public interface StatisticsService {
    /**
     * 获取某日各影片排片率统计数据
     * @param date
     * @return
     */
    ResponseVO getScheduleRateByDate(Date date);
    /**
     * 获取所有电影的累计票房(降序排序，且包含已下架的电影)
     * @return
     */
    ResponseVO getTotalBoxOffice();

    /**
     * 客单价：（某天的客单价=当天观众购票所花金额/购票人次数）
     * 返回值为过去7天内每天客单价
     * @return
     */
    ResponseVO getAudiencePriceSevenDays();

    /**
     * TODO:获取所有电影某天的上座率
     * 上座率参考公式：假设某影城设有n 个电影厅、m 个座位数，相对上座率=观众人次÷放映场次÷m÷n×100%
     * 此方法中运用到的相应的操作数据库的接口和实现请自行定义和实现，相应的结果需要自己定义一个VO类返回给前端
     *
     * 关于这个TODO:
     * 1.需要在持久成获取所有的票
     * 2.按该时间筛选出该天的票。
     * 3.统计票数。
     * 4.获取座位数：场次*影厅数*单影厅座位数
     * 5.计算上座率
     * 6.返回到前端，显示。
     */
    ResponseVO getMoviePlacingRateByDate(String date);

    /**
     * TODO:获取最近days天内，最受欢迎的movieNum个电影(可以简单理解为最近days内票房越高的电影越受欢迎)
     * 此方法中运用到的相应的操作数据库的接口和实现请自行定义和实现，相应的结果需要自己定义一个VO类返回给前端
     *
     * 关于这个TODO：
     * 1.首先需要自定义一个持久层：获取所有的票。
     * 2.按时间来筛选：选取该时间内的所有票。
     * 3.按电影分类，计算每个电影的票房。
     * 4.对电影票房排序。
     * 5.返回到前端，前端使用js动态渲染出结果。
     */
    ResponseVO getPopularMovies(int days, int movieNum);
}

//注意：mapper.XML中已经有筛选并且计算出数据了。
//mysql语句太强大了


