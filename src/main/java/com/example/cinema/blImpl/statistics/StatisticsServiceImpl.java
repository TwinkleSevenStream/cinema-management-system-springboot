package     com.example.cinema.blImpl.statistics;

import com.example.cinema.bl.sales.TicketService;
import  com.example.cinema.bl.statistics.StatisticsService;
import com.example.cinema.data.management.HallMapper;
import com.example.cinema.data.management.MovieMapper;
import com.example.cinema.data.management.ScheduleMapper;
import com.example.cinema.data.sales.TicketMapper;
import  com.example.cinema.data.statistics.StatisticsMapper;
import com.example.cinema.po.*;

import com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private HallMapper hallMapper;

    @Override
    public ResponseVO getScheduleRateByDate(Date date) {
        try{
            Date requireDate = date;
            if(requireDate == null){
                requireDate = new Date();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            requireDate = simpleDateFormat.parse(simpleDateFormat.format(requireDate));

            Date nextDate = getNumDayAfterDate(requireDate, 1);
            return ResponseVO.buildSuccess(movieScheduleTimeList2MovieScheduleTimeVOList(statisticsMapper.selectMovieScheduleTimes(requireDate, nextDate)));

        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getTotalBoxOffice() {
        try {
            return ResponseVO.buildSuccess(movieTotalBoxOfficeList2MovieTotalBoxOfficeVOList(statisticsMapper.selectMovieTotalBoxOffice()));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getAudiencePriceSevenDays() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Date startDate = getNumDayAfterDate(today, -6);
            List<AudiencePriceVO> audiencePriceVOList = new ArrayList<>();
            for(int i = 0; i < 7; i++){
                AudiencePriceVO audiencePriceVO = new AudiencePriceVO();
                Date date = getNumDayAfterDate(startDate, i);
                audiencePriceVO.setDate(date);
                List<AudiencePrice> audiencePriceList = statisticsMapper.selectAudiencePrice(date, getNumDayAfterDate(date, 1));
                double totalPrice = audiencePriceList.stream().mapToDouble(item -> item.getTotalPrice()).sum();
                audiencePriceVO.setPrice(Double.parseDouble(String.format("%.2f", audiencePriceList.size() == 0 ? 0 : totalPrice / audiencePriceList.size())));
                audiencePriceVOList.add(audiencePriceVO);
            }
            return ResponseVO.buildSuccess(audiencePriceVOList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }




//TODO
    @Override
//    获取所有电影某天的上座率
    public ResponseVO getMoviePlacingRateByDate(String d) {
        try{
//            String string = "2016-10-24";
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(d);
//            System.out.println(date);

            //返回到前端的VO
            List<MoviePlacingRateVO> moviePlacingRateVOList = new LinkedList<>();
            //先加入所有的电影（仅含电影名）
            List<Movie> movieList = movieMapper.selectAllMovie();
            for(int i=0;i<movieList.size();i++){
                MoviePlacingRateVO moviePlacingRateVO = new MoviePlacingRateVO();
                moviePlacingRateVO.setMovie(movieList.get(i).getName());
                moviePlacingRateVO.setMovieId(movieList.get(i).getId());
                moviePlacingRateVOList.add(moviePlacingRateVO);
            }

//          把票全部拿出来处理吧。
            List<Ticket> list = ticketMapper.selectAll();
//            按时间筛选
            List<Ticket> tickets = new ArrayList<>();
            for (Ticket ticket: list){
//                先要使用时间筛选
                if (isSameDay(ticket.getTime(),date)){
                    tickets.add(ticket);
                }
            }

//            System.out.println(tickets.size());
            //总的票数是没错的。

//           对每个电影进行处理: 电影票数量。
            for (int i=0;i<moviePlacingRateVOList.size();i++){
                MoviePlacingRateVO temp = moviePlacingRateVOList.get(i);
                for (Ticket t: tickets){
                    ScheduleItem scheduleItem = scheduleMapper.selectScheduleById(t.getScheduleId());
                    if (scheduleItem!=null){
                        if (scheduleItem.getMovieName().equals(temp.getMovie())){
//                            但是每部电影的票数没加上去。
                            moviePlacingRateVOList.get(i).ticketNumPlus();
                        }
                    }
                }

                //查询该电影的所有排片
                int seatNum = 0;
                //遍历排片，获取座位数。
                List<ScheduleItem> scheduleItemList = scheduleMapper.selectScheduleByMovieId(temp.getMovieId());
                for (ScheduleItem scheduleItem: scheduleItemList){
                    //根据日期筛选
                    if (isSameDay(scheduleItem.getStartTime(),date) && scheduleItem.getMovieName().equals(temp.getMovie())){
                        //日期相同而且该排片的电影相同。
                        //获取座位数
                        Hall hall = hallMapper.selectHallById(scheduleItem.getHallId());
                        seatNum += (hall.getCol()*hall.getRown());
                    }
                }
                moviePlacingRateVOList.get(i).setSeatNum(seatNum);
                float rate = (seatNum==0 ? 0:moviePlacingRateVOList.get(i).getTicketNum()/seatNum);
                moviePlacingRateVOList.get(i).setRate(rate);
            }

//            //按上座率排个序吧。朴素排序法。
//            List<MoviePlacingRateVO> resList = new LinkedList<>();
//            for (int i=0;i<moviePlacingRateVOList.size();i++){
//                MoviePlacingRateVO temp = moviePlacingRateVOList.get(0);
//                float maxRate = temp.getRate();
//                int index = 0;
//                for (int j=0;j<moviePlacingRateVOList.size();j++){
//                    if (maxRate<moviePlacingRateVOList.get(j).getRate()){
//                        maxRate = moviePlacingRateVOList.get(j).getRate();
//                        index = j;
//                    }
//                }
//                resList.add(moviePlacingRateVOList.get(index));
//                moviePlacingRateVOList.get(index).setRate(-1);
//            }


            return ResponseVO.buildSuccess(moviePlacingRateVOList);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("Failed!");
        }
    }


    @Override
    public ResponseVO getPopularMovies(int days, int movieNum) {
        //要求见接口说明
        try{
            Date today = new Date();
            Date beginDay = getNumDayAfterDate(today,-days);//负数也是可以的
            List<MovieTotalBoxOffice> list = statisticsMapper.selectMovieBoxOfficeWithTime(beginDay,today);
            movieNum = movieNum<=list.size()?movieNum:list.size();
            List<MovieTotalBoxOffice> topList = list.subList(0,movieNum);
            return ResponseVO.buildSuccess(topList);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败！");
        }
    }


    /**
     * 获得num天后的日期
     */
    Date getNumDayAfterDate(Date oldDate, int num){
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(oldDate);
        calendarTime.add(Calendar.DAY_OF_YEAR, num);
        return calendarTime.getTime();
    }


    private List<MovieScheduleTimeVO> movieScheduleTimeList2MovieScheduleTimeVOList(List<MovieScheduleTime> movieScheduleTimeList){
        List<MovieScheduleTimeVO> movieScheduleTimeVOList = new ArrayList<>();
        for(MovieScheduleTime movieScheduleTime : movieScheduleTimeList){
            movieScheduleTimeVOList.add(new MovieScheduleTimeVO(movieScheduleTime));
        }
        return movieScheduleTimeVOList;
    }


    private List<MovieTotalBoxOfficeVO> movieTotalBoxOfficeList2MovieTotalBoxOfficeVOList(List<MovieTotalBoxOffice> movieTotalBoxOfficeList){
        List<MovieTotalBoxOfficeVO> movieTotalBoxOfficeVOList = new ArrayList<>();
        for(MovieTotalBoxOffice movieTotalBoxOffice : movieTotalBoxOfficeList){
            movieTotalBoxOfficeVOList.add(new MovieTotalBoxOfficeVO(movieTotalBoxOffice));
        }
        return movieTotalBoxOfficeVOList;
    }

    private boolean isSameDay(Date a,Date b) {
        return a.getYear() == b.getYear() && a.getMonth() == b.getMonth() && a.getDay() == b.getDay();
    }


}
