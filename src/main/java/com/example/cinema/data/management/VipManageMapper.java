package com.example.cinema.data.management;
import com.example.cinema.po.VipTypePO;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface VipManageMapper {
    @Insert("insert into vip_type(id,price,full,reduce,vaildTime) values(0,#{price},#{full},#{reduce},#{vaildTime})")
    int insertVipType(VipTypePO vipTypePO);

    @Select("select * from vip_type")
    List<VipTypePO>  getVipTypes();

    @Select("select * from vip_type where price=#{price} and full = #{full} and reduce= #{reduce} and vaildTime=#{vaildTime}")
    List<VipTypePO> getOne(@Param("price") int price, @Param("full") int full, @Param("reduce") int reduce, @Param("vaildTime") int vaildTime);


    @Delete("delete from vip_type where price=#{price} and full = #{full} and reduce= #{reduce} and vaildTime=#{vaildTime} limit 1")
    int del(@Param("price") int price, @Param("full") int full, @Param("reduce") int reduce, @Param("vaildTime") int vaildTime);

    @Update("update vip_type SET price=#{price} , full = #{full} , reduce= #{reduce} , vaildTime=#{vaildTime}  where  price=#{price2} and full = #{full2} and reduce= #{reduce2} and vaildTime=#{vaildTime2}")
    int update(@Param("price") int price, @Param("full") int full, @Param("reduce") int reduce, @Param("vaildTime") int vaildTime, @Param("price2") int price2, @Param("full2") int full2, @Param("reduce2") int reduce2, @Param("vaildTime2") int vaildTime2);

    @Select("select * from vip_type limit #{index},1")
    VipTypePO getOneBuyIndex(@Param("index") int index);
}





