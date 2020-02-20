package     com.example.cinema.controller.statistics;

import  com.example.cinema.bl.statistics.StatisticsService;
import  com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author fjj
 * @date 2019/4/16 1:34 PM
 */
@RestController
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;


    @RequestMapping(value = "statistics/scheduleRate", method = RequestMethod.GET)
    public ResponseVO getScheduleRateByDate(@RequestParam(required = false) Date date){
        //此处date为非必填参数，若不填则默认为当天
        return statisticsService.getScheduleRateByDate(date);
    }


//    所有电影票房，后面上座率参考这个
    @RequestMapping(value = "statistics/boxOffice/total", method = RequestMethod.GET)
    public ResponseVO getTotalBoxOffice(){
        return statisticsService.getTotalBoxOffice();
    }



    @RequestMapping(value = "statistics/audience/price", method = RequestMethod.GET)
    public ResponseVO getAudiencePrice(){
        return statisticsService.getAudiencePriceSevenDays();
    }


//    上座率
    @RequestMapping(value = "/admin/cinema/statistics/PlacingRate/{year}/{month}/{day}", method = RequestMethod.GET)
//   ---------------------------搞不懂？？为什么这里的请求URL要加前面那个。
    public ResponseVO getMoviePlacingRateByDate(@PathVariable String year,@PathVariable String month,@PathVariable String day){
        return statisticsService.getMoviePlacingRateByDate(year+"/"+month+"/"+day);
    }

    @RequestMapping(value = "/admin/cinema/statistics/popular/movie/{days}/{movieNum}", method = RequestMethod.GET)
    public ResponseVO getPopularMovies(@PathVariable int days, @PathVariable int movieNum){
        return statisticsService.getPopularMovies(days, movieNum);
    }

}
