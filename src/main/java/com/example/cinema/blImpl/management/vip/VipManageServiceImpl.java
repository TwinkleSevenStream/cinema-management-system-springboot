package com.example.cinema.blImpl.management.vip;
import com.example.cinema.bl.management.VipManageService;
import com.example.cinema.data.management.VipManageMapper;
import com.example.cinema.po.VipTypePO;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class VipManageServiceImpl implements VipManageService {
    @Autowired
    private  VipManageMapper vipManageMapper;

    @Override
    public ResponseVO releaseVip(VipTypePO vipTypePO){
        try {
            vipManageMapper.insertVipType(vipTypePO);
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("failed");
        }
    }


    @Override
    public ResponseVO getVipTypes(){
        try{
            return ResponseVO.buildSuccess(vipManageMapper.getVipTypes());
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("Failed");
        }
    }
    @Override
    public  ResponseVO del(VipTypePO vipTypePO){
        try{
            return ResponseVO.buildSuccess(vipManageMapper.del(vipTypePO.getPrice(),vipTypePO.getFull(),vipTypePO.getReduce(),vipTypePO.getVaildTime()));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("Failed");
        }
    }
    @Override
    public  boolean getOne(VipTypePO v){
        try{
            return vipManageMapper.getOne(v.getPrice(), v.getPrice(), v.getReduce(), v.getVaildTime()).size() == 0;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ResponseVO update(List<VipTypePO> vipTypePOs){
        try{
            VipTypePO v1 = vipTypePOs.get(1);
            VipTypePO v2 = vipTypePOs.get(0);
            vipManageMapper.update(v1.getPrice(),v1.getFull(),v1.getReduce(),v1.getVaildTime(),v2.getPrice(),v2.getFull(),v2.getReduce(),v2.getVaildTime());
            return ResponseVO.buildSuccess();
        }
       catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("");
       }
    }


    @Override
    public  VipTypePO getOneByIndex(int index){
        return vipManageMapper.getOneBuyIndex(index);
    }
}



