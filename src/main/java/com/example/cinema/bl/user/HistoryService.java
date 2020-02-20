package com.example.cinema.bl.user;

import com.example.cinema.vo.ResponseVO;

import java.util.List;

/**
 * Created by XuTao on 2019/6/6 17:21
 */
public interface HistoryService {
    ResponseVO getChargeHistory();
    ResponseVO getSpendHistory(int userId);

}
