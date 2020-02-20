package     com.example.cinema.data.promotion;

import  com.example.cinema.po.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liying on 2019/4/17.
 */
@Mapper
public interface CouponMapper {

    int insertCoupon(Coupon coupon);

    List<Coupon> selectCouponByUser(int userId);

    Coupon selectById(int id);

    int insertCouponUser(@Param("couponId") int couponId, @Param("userId") int userId);

    void deleteCouponUser(@Param("couponId") int couponId, @Param("userId") int userId);

    //没看懂这条语句有什么用。
    List<Coupon> selectCouponByUserAndAmount(@Param("userId") int userId, @Param("amount") double amount);

    Coupon selectCouponByIndex(int index);


}
