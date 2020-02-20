package     com.example.cinema.controller.promotion;

import  com.example.cinema.bl.promotion.CouponService;
import  com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liying on 2019/4/16.
 */
@RestController
//@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    CouponService couponService;

    @GetMapping("/coupon/{userId}/get")
    public ResponseVO getCoupons(@PathVariable int userId){
//        System.out.println(userId);
        return couponService.getCouponsByUser(userId);
    }
    @GetMapping("/admin/promotion/coupon/present/{nameOrID}/{index}")
    public ResponseVO present(@PathVariable String nameOrID,@PathVariable int index){
        return couponService.present(nameOrID,index);
    }





}
