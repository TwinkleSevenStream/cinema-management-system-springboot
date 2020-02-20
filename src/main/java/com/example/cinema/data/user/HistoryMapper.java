package com.example.cinema.data.user;

import com.example.cinema.po.ChargeHistoryPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface HistoryMapper {

    @Insert("insert into chargeHistory(userId,amount,chargeTime) values(#{userId},#{amount},#{chargeTime})")
    int insertChargeHistory(@Param("userId") int userId, @Param("amount") int amount, @Param("chargeTime") java.util.Date chargeTime);

    @Select("select * from chargeHistory")
    List<ChargeHistoryPO> selectAllChargeHistory();

}
