package     com.example.cinema.blImpl.promotion;

import  com.example.cinema.bl.promotion.VIPService;
import com.example.cinema.data.user.HistoryMapper;
import  com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.data.management.VipManageMapper;
import com.example.cinema.po.VipTypePO;
import  com.example.cinema.vo.VIPCardForm;
import  com.example.cinema.po.VIPCard;
import  com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;


@Service
public class VIPServiceImpl implements VIPService {
    @Autowired
    VIPCardMapper vipCardMapper;
    @Autowired
    VipManageMapper vipManageMapper;

    @Autowired
    HistoryMapper historyMapper;


    Date getNumDayAfterDate(Date oldDate, int num){
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(oldDate);
        calendarTime.add(Calendar.DAY_OF_YEAR, num);
        return calendarTime.getTime();
    }


    @Override
    public ResponseVO addVIPCard(int userId,int vipTypeIndex) {
        VIPCard vipCard = new VIPCard();
        vipCard.setUserId(userId);
        vipCard.setBalance(0);

        VipTypePO vipTypePO = vipManageMapper.getOneBuyIndex(vipTypeIndex);
//        System.out.println("VipTypePO: "+vipTypePO.getVaildTime());

        vipCard.setEndTime(getNumDayAfterDate(new Date(),vipTypePO.getVaildTime()>0?vipTypePO.getVaildTime():1000000));
        vipCard.setFull(vipTypePO.getFull());
        vipCard.setReduce(vipTypePO.getReduce());
        vipCard.setVipTypeIndex(vipTypeIndex);
//        Timestamp time = new Timestamp();
        vipCard.setJoinDate(new Date());


        int id = vipCardMapper.insertOneCard(vipCard.getUserId(),vipCard.getBalance(),new Date(),vipCard.getEndTime(),vipCard.getVipTypeIndex(),vipCard.getFull(),vipCard.getReduce());
        //        int id2 = vipCardMapper.insertOneCard(3,1,new Date(),new Date(),1,1,1);
//        System.out.println(id+" "+userId+"---讲道理我是插进去了的，为什么没出现在数据库中");
        return ResponseVO.buildSuccess(vipCardMapper.selectCardById(id));
    }


    @Override
    public ResponseVO getCardById(int id) {
        try {
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }
    @Override
    public ResponseVO getVIPInfo() {
        /**
         * edwin：
         *  需要读取VIP类型表，返回
         */
        try {
            return ResponseVO.buildSuccess(vipManageMapper.getVipTypes());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

    }

    @Override
    public ResponseVO recharge(VIPCardForm vipCardForm) {

        VIPCard vipCard = vipCardMapper.selectCardById(vipCardForm.getVipId());
        if (vipCard == null) {
            return ResponseVO.buildFailure("会员卡不存在");
        }
        float balance = vipCard.calculate(vipCardForm.getAmount());

        vipCard.setBalance(vipCard.getBalance() + balance); //这明明是充200送30嘛
        try {
            //插入历史记录：
            historyMapper.insertChargeHistory(vipCard.getUserId(),vipCardForm.getAmount(),new Date());


            vipCardMapper.updateCardBalance(vipCardForm.getVipId(), vipCard.getBalance());
            return ResponseVO.buildSuccess(vipCard);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getCardByUserId(int userId) {

        try {
            VIPCard vipCard = vipCardMapper.selectCardByUserId(userId);
            if(vipCard==null){
                System.out.println("用户不存在");
                return ResponseVO.buildFailure("用户卡不存在");
            }
            else {//会员存在
                //看会员是否过期

                Date bt = vipCard.getEndTime();
                Date et = new Date();
                if (bt.before(et)){
                    //过期了，删除掉。
                    int a = vipCardMapper.delete(userId);
                    return ResponseVO.buildFailure("过期了---"+ a);
                }
            }
            //存在，且没过期。
            vipCard.setUserId(vipCard.getUserId());
            return ResponseVO.buildSuccess(vipCard);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }
    @Override
    //扣费
    public ResponseVO charge(VIPCardForm vipCardForm){
        VIPCard vipCard = vipCardMapper.selectCardById(vipCardForm.getVipId());
        if (vipCard == null) {
            return ResponseVO.buildFailure("会员卡不存在");
        }
        else {
            try {
                if (vipCard.getBalance()>=vipCardForm.getAmount()){
                    vipCardMapper.updateCardBalance(vipCard.getId(),vipCard.getBalance()-vipCardForm.getAmount());
                    return ResponseVO.buildSuccess();
                }
                else {
                    return ResponseVO.buildFailure("false");//表示余额不足。
                }
            } catch (Exception e) {
                return ResponseVO.buildFailure("失败！");
            }
        }
    }


//    根本没必要
    @Override
    public ResponseVO delete(int userId){
        try{
            return ResponseVO.buildSuccess(vipCardMapper.delete(userId));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("failed");
        }
    }
}
