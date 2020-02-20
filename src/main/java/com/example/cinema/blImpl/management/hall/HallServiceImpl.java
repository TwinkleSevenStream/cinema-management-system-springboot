package     com.example.cinema.blImpl.management.hall;

import  com.example.cinema.bl.management.HallService;
import  com.example.cinema.data.management.HallMapper;
import  com.example.cinema.po.Hall;
import  com.example.cinema.vo.HallVO;
import  com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fjj
 * @date 2019/4/12 2:01 PM
 */
@Service
public class HallServiceImpl implements HallService, HallServiceForBl {
    @Autowired
    private HallMapper hallMapper;

    @Override
    public ResponseVO searchAllHall() {
        try {
            return ResponseVO.buildSuccess(hallList2HallVOList(hallMapper.selectAllHall()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public Hall getHallById(int id) {
        try {
            return hallMapper.selectHallById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private List<HallVO> hallList2HallVOList(List<Hall> hallList){
        List<HallVO> hallVOList = new ArrayList<>();
        for(Hall hall : hallList){
            hallVOList.add(new HallVO(hall));
        }
        return hallVOList;
    }
    //J
    @Override
    public ResponseVO addHall(String hallname,int col,int rown){
        try {
            List<Hall> list  = hallMapper.selectAllHall();
            boolean isRepeat = false;
            for (Hall h:list){
                if (h.getHallname().equals(hallname)) {
                    isRepeat = true;
                    break;
                }
            }
            if (isRepeat){
                return ResponseVO.buildFailure("e");
            }else {
                return ResponseVO.buildSuccess(hallMapper.insertHall(hallname,col,rown));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("");
        }
    }

    @Override
    public ResponseVO updateHall(int ind,String hallname,int col,int rown){
        try {
            List<Hall> list  = hallMapper.selectAllHall();
            boolean isRepeat = false;
            for (Hall h:list){
                if (h.getHallname().equals(hallname)) {
                    isRepeat = true;
                    break;
                }
            }
            if (isRepeat){
                return ResponseVO.buildFailure("e");
            }else {
                Hall indexHall = list.get(ind-1);
                String oldName = indexHall.getHallname();
                return ResponseVO.buildSuccess(hallMapper.updatHall(ind,hallname,col,rown,oldName));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("dd");
        }
    }

}
