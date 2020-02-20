package com.example.cinema.controller.management;

import com.example.cinema.bl.management.VipManageService;
import com.example.cinema.po.VipTypePO;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class VipManageController {
//    private List<VipTypePO> vipTypePOs;


    @Autowired
    VipManageService vipManageService;
//注意这里的URL，从err读取的
    @RequestMapping(value = "/admin/vip/vipType/addVipType",method = RequestMethod.POST)
    public ResponseVO realseVip (@RequestBody VipTypePO vipTypePO){
        boolean isOk = vipManageService.getOne(vipTypePO);
        if (isOk){
            return vipManageService.releaseVip(vipTypePO);
        }
        else {
            return ResponseVO.buildFailure("existed");
        }
    }

    @RequestMapping(value = "/getVipTypes",method = RequestMethod.GET)
    public ResponseVO getVipTypes (){
        return vipManageService.getVipTypes();
    }


    @RequestMapping(value = "/delvipType",method = RequestMethod.POST)
    public ResponseVO del (@RequestBody VipTypePO vipTypePO){
        return vipManageService.del(vipTypePO);
    }


    @RequestMapping(value = "/updVipType",method = RequestMethod.POST)
    public ResponseVO beforeUpdate (@RequestBody List<VipTypePO> vipTypePOs){
        return vipManageService.update(vipTypePOs);
    }

}





