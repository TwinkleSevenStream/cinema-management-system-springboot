package com.example.cinema.data.management;

import com.example.cinema.po.RefundStrategyPO;
import com.example.cinema.po.VipTypePO;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 退票策略
 * 该表中只存有一项数据
 */
@Mapper
public interface RefundStrategyMapper {

    /**
     * 更新退票策略
     * @param discount
     * @param beforeMinutes
     */
    @Update("update refund_strategy SET discount=#{discount} , beforeMinutes=#{beforeMinutes} ")
    void update(@Param("discount") int discount, @Param("beforeMinutes") int beforeMinutes);

    @Select("select * from refund_strategy limit 1")
    RefundStrategyPO getRefundStrategy();



}





