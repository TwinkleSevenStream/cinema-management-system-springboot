package com.example.cinema.bl.management;

import com.example.cinema.po.RefundStrategyPO;
import com.example.cinema.vo.ResponseVO;

public interface RefundStrategyService {
    ResponseVO getRefundStrategy();
    ResponseVO update(RefundStrategyPO refundStrategyPO);
}
