package   com.example.cinema.data.promotion;

import  com.example.cinema.po.VIPCard;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.sql.Timestamp;
@Mapper
public interface VIPCardMapper {

    @Insert("insert into vip_card(user_id,balance,join_time,endTime,vipTypeIndex,full,reduce) values(#{user_id}, #{balance},#{join_time},#{endTime},#{vipTypeIndex},#{full},#{reduce})")
    int insertOneCard(@Param("user_id") int user_id, @Param("balance") float balance, @Param("join_time") java.util.Date join_time, @Param("endTime")java.util.Date time, @Param("vipTypeIndex") int vipTypeIndex, @Param("full" ) int full, @Param("reduce") int reduce);

    VIPCard selectCardById(int id);

    void updateCardBalance(@Param("id") int id, @Param("balance") float balance);

    VIPCard selectCardByUserId(int userId);

    int delete(int userId);

}



