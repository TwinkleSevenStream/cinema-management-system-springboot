package     com.example.cinema.blImpl.management.movie;

import  com.example.cinema.bl.management.MovieService;
import  com.example.cinema.blImpl.management.schedule.MovieServiceForBl;
import  com.example.cinema.blImpl.management.schedule.ScheduleServiceForBl;
import  com.example.cinema.data.management.MovieMapper;
import  com.example.cinema.po.Movie;
import  com.example.cinema.po.ScheduleItem;
import  com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author fjj
 * @date 2019/3/12 6:43 PM
 */
@Service
public class MovieServiceImpl implements MovieService, MovieServiceForBl {
    private static final String SCHEDULE_ERROR_MESSAGE = "有电影后续仍有排片或已有观众购票且未使用";

    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private ScheduleServiceForBl scheduleServiceForBl;

    @Override
    public ResponseVO addMovie(MovieForm addMovieForm) {
        try {
            movieMapper.insertOneMovie(addMovieForm);
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO  modifyMovie(MovieFormWithOldName m) {
        try {
            MovieForm movieForm= new MovieForm();
            Movie movie = movieMapper.selectMovieByName(m.getOldName());
            if (movie==null){
                return ResponseVO.buildFailure("failed");
            }
            int id = movie.getId();
            movieForm.setCountry(m.getCountry());
            movieForm.setDescription(m.getDescription());
            movieForm.setDirector(m.getDirector());
            movieForm.setId(id);
            movieForm.setLength(m.getLength());
            movieForm.setLanguage(m.getLanguage());
            movieForm.setName(m.getName());
            movieForm.setPosterUrl(m.getPosterUrl());
            movieForm.setScreenWriter(m.getScreenWriter());
            movieForm.setStarring(m.getStarring());
            movieForm.setStartDate(m.getStartDate());
            movieForm.setStatus(m.getStatus());
            movieForm.setType(m.getType());

            movieMapper.updateMovie(movieForm);

            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO searchOneMovieByIdAndUserId(int id, int userId) {
        try {
            Movie movie = movieMapper.selectMovieByIdAndUserId(id, userId);
            if(movie != null){
                return ResponseVO.buildSuccess(new MovieVO(movie));
            }else{
                return ResponseVO.buildSuccess(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

    }

    @Override
    public ResponseVO searchAllMovie() {
        try {
            return ResponseVO.buildSuccess(movieList2MovieVOList(movieMapper.selectAllMovie()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO searchOtherMoviesExcludeOff() {
        try {
            return ResponseVO.buildSuccess(movieList2MovieVOList(movieMapper.selectOtherMoviesExcludeOff()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getMovieByKeyword(String keyword) {
        if (keyword==null||keyword.equals("")){
            return ResponseVO.buildSuccess(movieList2MovieVOList(movieMapper.selectAllMovie()));
        }
        return ResponseVO.buildSuccess(movieList2MovieVOList(movieMapper.selectMovieByKeyword(keyword)));
    }



    @Override
    public ResponseVO pullOfBatchOfMovie(MovieBatchOffForm movieBatchOffForm) {
        try {
            List<Integer> movieIdList =  movieBatchOffForm.getMovieIdList();
            ResponseVO responseVO = preCheck(movieIdList);
            if(!responseVO.getSuccess()){
                return responseVO;
            }
            movieMapper.updateMovieStatusBatch(movieIdList);
            return ResponseVO.buildSuccess();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO updateMovie(MovieForm updateMovieForm) {
        try {
            ResponseVO responseVO = preCheck(Arrays.asList(updateMovieForm.getId()));
            if(!responseVO.getSuccess()){
                return responseVO;
            }
            movieMapper.updateMovie(updateMovieForm);
            return ResponseVO.buildSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public Movie getMovieById(int id) {
        try {
            return movieMapper.selectMovieById(id);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 下架和修改电影的前置检查
     * @param movieIdList
     * @return
     */
    public ResponseVO preCheck(List<Integer> movieIdList){
        Date thisTime = new Date();
        List<ScheduleItem> scheduleItems = scheduleServiceForBl.getScheduleByMovieIdList(movieIdList);

        // 检查是否有电影后续有排片及是否有观众购票未使用
        for(ScheduleItem s : scheduleItems){
            if(s.getEndTime().after(thisTime)){
                return ResponseVO.buildFailure(SCHEDULE_ERROR_MESSAGE);
            }
        }
        return ResponseVO.buildSuccess();
    }


    private List<MovieVO> movieList2MovieVOList(List<Movie> movieList){
        List<MovieVO> movieVOList = new ArrayList<>();
        for(Movie movie : movieList){
            movieVOList.add(new MovieVO(movie));
        }
        return movieVOList;
    }

    @Override
    public ResponseVO withdrawMovie(String name){
        try {
            Movie movie = movieMapper.selectMovieByName(name);
            if (movie==null){
                return ResponseVO.buildFailure("failed");
            }
            int id = movie.getId();
            List<Integer> list = new LinkedList<>();
            list.add(id);

            return ResponseVO.buildSuccess(movieMapper.updateMovieStatusBatch(list));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("出错啦！");
        }
    }


}
