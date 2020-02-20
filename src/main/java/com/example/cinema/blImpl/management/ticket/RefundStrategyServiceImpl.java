package com.example.cinema.blImpl.management.ticket;

import com.example.cinema.bl.management.RefundStrategyService;
import com.example.cinema.data.management.RefundStrategyMapper;
import com.example.cinema.po.RefundStrategyPO;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefundStrategyServiceImpl implements RefundStrategyService {

    @Autowired
    RefundStrategyMapper refundStrategyMapper;

    @Override
    public ResponseVO getRefundStrategy(){
        try {
            RefundStrategyPO strategy = refundStrategyMapper.getRefundStrategy();
            return ResponseVO.buildSuccess(strategy);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO update(RefundStrategyPO refundStrategyPO){
        int discount = refundStrategyPO.getDiscount();
        int beforeMinutes = refundStrategyPO.getBeforeMinutes();
        try {
            refundStrategyMapper.update(discount, beforeMinutes);
            return ResponseVO.buildSuccess(true);
        }catch (Exception e){
            return ResponseVO.buildFailure("失败");
        }

    }

}
