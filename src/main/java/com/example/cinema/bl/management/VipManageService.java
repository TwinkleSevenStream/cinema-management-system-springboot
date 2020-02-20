package com.example.cinema.bl.management;

import com.example.cinema.po.VipTypePO;
import com.example.cinema.vo.ResponseVO;

import java.util.List;

public interface VipManageService {
    ResponseVO releaseVip(VipTypePO vipTypePO);
    ResponseVO getVipTypes();
    ResponseVO del(VipTypePO vipTypePO);
    boolean getOne(VipTypePO vipTypePO);
    ResponseVO update(List<VipTypePO> vipTypePOs);
    VipTypePO getOneByIndex(int index);
}

