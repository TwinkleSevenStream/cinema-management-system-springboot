package     com.example.cinema.blImpl.promotion;

import  com.example.cinema.bl.promotion.CouponService;
import  com.example.cinema.data.promotion.CouponMapper;
import com.example.cinema.data.user.AccountMapper;
import  com.example.cinema.po.Coupon;
import com.example.cinema.po.User;
import  com.example.cinema.vo.CouponForm;
import  com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    CouponMapper couponMapper;
    @Autowired
    AccountMapper accountMapper;

    @Override
    //这里为什么会查询失败。
    public ResponseVO getCouponsByUser(int userId) {
        try {
            List<Coupon> list = couponMapper.selectCouponByUser(userId);
            return ResponseVO.buildSuccess(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO addCoupon(CouponForm couponForm) {
        try {
            Coupon coupon=new Coupon();
            coupon.setName(couponForm.getName());
            coupon.setDescription(couponForm.getDescription());
            coupon.setTargetAmount(couponForm.getTargetAmount());
            coupon.setDiscountAmount(couponForm.getDiscountAmount());
            coupon.setStartTime(couponForm.getStartTime());
            coupon.setEndTime(couponForm.getEndTime());
            couponMapper.insertCoupon(coupon);
            return ResponseVO.buildSuccess(coupon);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO issueCoupon(int couponId, int userId) {
        try {
            couponMapper.insertCouponUser(couponId,userId);
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

    }

    @Override
    public ResponseVO present(String nameOrID,int index){
        try {
            int id;
            if (nameOrID.length()>=4){
                User user =accountMapper.getAccountByName(nameOrID);
                id = user.getId();
            }else {
                id = Integer.parseInt(nameOrID.trim());
            }
            Coupon coupon = couponMapper.selectCouponByIndex(index-1);
            int couponId = coupon.getId();
            int a = couponMapper.insertCouponUser(couponId,id);
            return ResponseVO.buildSuccess(a);

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }
}
