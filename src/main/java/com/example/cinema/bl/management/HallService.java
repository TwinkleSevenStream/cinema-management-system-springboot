package    com.example.cinema.bl.management;

import  com.example.cinema.vo.ResponseVO;

/**
 * @author fjj
 * @date 2019/4/12 2:01 PM
 */
public interface HallService {
    /**
     * 搜索所有影厅
     * @return
     */
    ResponseVO searchAllHall();

    //J
    ResponseVO addHall(String hallname,int col,int rown);
    ResponseVO updateHall(int ind,String hallname,int col,int rown);

}
