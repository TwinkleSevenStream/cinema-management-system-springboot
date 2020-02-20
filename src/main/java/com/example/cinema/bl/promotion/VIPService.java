package     com.example.cinema.bl.promotion;

import  com.example.cinema.vo.VIPCardForm;
import  com.example.cinema.vo.ResponseVO;



/**
 * Created by liying on 2019/4/14.
 */

public interface VIPService {

//    ResponseVO addVIPCard(int userId);

    ResponseVO getCardById(int id);

    ResponseVO getVIPInfo();

    ResponseVO recharge(VIPCardForm vipCardForm);

    ResponseVO getCardByUserId(int userId);

    //自己写一个扣费的：charge好像就是扣费，但是助教使用它作为充值，我只好使用"charge".
    ResponseVO charge(VIPCardForm vipCardForm);

    ResponseVO addVIPCard(int userId,int vipTypeIndex);

    ResponseVO delete(int userId);


}
