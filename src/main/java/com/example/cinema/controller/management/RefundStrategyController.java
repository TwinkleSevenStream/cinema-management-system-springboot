package com.example.cinema.controller.management;

import com.example.cinema.bl.management.RefundStrategyService;
import com.example.cinema.po.RefundStrategyPO;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/refund")
public class RefundStrategyController {

    @Autowired
    RefundStrategyService refundStrategyService;


    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public ResponseVO getRefundStrategy (){
        return refundStrategyService.getRefundStrategy();
    }


    @RequestMapping(value = "/strategy",method = RequestMethod.POST)
    public ResponseVO update (@RequestBody RefundStrategyPO refundStrategyPO){
        return refundStrategyService.update(refundStrategyPO);
    }



}
