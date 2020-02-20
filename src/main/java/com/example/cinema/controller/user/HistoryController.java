package com.example.cinema.controller.user;


import com.example.cinema.bl.user.HistoryService;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    @RequestMapping(value = "/user/history/charge",method = RequestMethod.GET)
    public ResponseVO getChargeHistory(){
        return historyService.getChargeHistory();
    }

    @RequestMapping(value = "/user/history/spend/{userId}",method = RequestMethod.GET)
    public ResponseVO getSpendHistory(@PathVariable int userId){
        return historyService.getSpendHistory(userId);
    }
}
