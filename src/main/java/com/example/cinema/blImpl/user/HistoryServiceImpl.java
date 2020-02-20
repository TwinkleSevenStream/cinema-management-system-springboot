package com.example.cinema.blImpl.user;

import com.example.cinema.bl.user.HistoryService;
import com.example.cinema.bl.management.ScheduleService;
import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.data.user.HistoryMapper;
import com.example.cinema.po.ScheduleItem;
import com.example.cinema.po.SpendHistoryPO;
import com.example.cinema.po.Ticket;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    HistoryMapper historyMapper;
    @Autowired
    TicketService ticketService;
    @Autowired
    ScheduleService scheduleService;

    @Override
    public ResponseVO getChargeHistory(){
        try {
            return ResponseVO.buildSuccess(historyMapper.selectAllChargeHistory());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("failed");
        }
    }

    @Override
    public ResponseVO getSpendHistory(int userId){
        try{
            List<Ticket> tickets = (List<Ticket>) ticketService.getTicketByUser(userId).getContent();
            List<SpendHistoryPO> spendHistoryPOs = new LinkedList<>();
            for (int i =0;i<tickets.size();i++){
                Ticket ticket = tickets.get(i);
                if (ticket.getUserId()==userId && ticket.getState()==1){
                    SpendHistoryPO spendHistoryPO = new SpendHistoryPO();
                    spendHistoryPO.setSpendTime(ticket.getTime());
                    ScheduleItem scheduleItem = (ScheduleItem) scheduleService.getScheduleById(ticket.getScheduleId()).getContent();
                    spendHistoryPO.setMovie(scheduleItem.getMovieName());
                    spendHistoryPO.setAmount((int)(scheduleItem.getFare())); //取整了。
                    spendHistoryPOs.add(spendHistoryPO);
                }
            }
            return ResponseVO.buildSuccess(spendHistoryPOs);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败了");
        }
    }
}



