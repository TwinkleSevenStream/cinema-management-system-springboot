package     com.example.cinema.blImpl.sales;

import com.example.cinema.bl.promotion.VIPService;
import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.blImpl.management.hall.HallServiceForBl;
import com.example.cinema.blImpl.management.schedule.ScheduleServiceForBl;
import com.example.cinema.data.management.ScheduleMapper;
import com.example.cinema.data.promotion.CouponMapper;
import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.data.management.RefundStrategyMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.data.user.AccountMapper;
import com.example.cinema.po.*;
import com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;

import java.util.LinkedList;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    private int amount;

    @Autowired
    TicketMapper ticketMapper;
    
    @Autowired
    //
    ScheduleServiceForBl scheduleService;

    @Autowired
    HallServiceForBl hallService;
    
    //@author:Edwin Xu
    @Autowired
    VIPService vipService;

    @Autowired
    CouponMapper couponMapper;

    @Autowired
    VIPCardMapper vipCardMapper;

    @Autowired
    ScheduleMapper scheduleMapper;

    @Autowired
    RefundStrategyMapper refundStrategyMapper;

    @Autowired
    AccountMapper accountMapper;

    
    @Override
    @Transactional
    //锁座【增加票但状态为未付款】
    public ResponseVO addTicket(TicketForm ticketForm) {
    	try {
	//    	my TODO
	    	int userId = ticketForm.getUserId();
	    	int scheduleId = ticketForm.getScheduleId();
	    	List<SeatForm> seats = ticketForm.getSeats();
	    	//所以需要一个ticket表，不不不，这个ticketForm表示一次购买的可以是多张票。
	    	//所以需要多次插入。
	    	//一个用户id对应多张票
	    	List<Ticket> tickets = new LinkedList<Ticket>();
	    	for(int i =0;i<seats.size();i++){
	    		Ticket ticket = new Ticket();
		    	ticket.setUserId(userId);
		    	ticket.setScheduleId(scheduleId);
	    		SeatForm seatForm= seats.get(i);
	    		ticket.setColumnIndex(seatForm.getColumnIndex());
		    	ticket.setRowIndex(seatForm.getRowIndex());
		    	tickets.add(ticket);
	    	}
	    	ticketMapper.insertTickets(tickets);
	        return ResponseVO.buildSuccess();
    	}catch (Exception e) {
			e.printStackTrace();
			return ResponseVO.buildFailure("失败");
		}
    }
    @Override
    @Transactional
    /*完成购票【不使用会员卡】流程包括校验优惠券和根据优惠活动赠送优惠券
     */
    public ResponseVO completeTicket(List<Integer> ticketid, int couponId) {
        //需要更新状态。
        try {
            for (int i : ticketid) {
                ticketMapper.updateTicketState(i , 1);
            }
            //把优惠券用掉，即用数据表中移除。
            int userId = ticketMapper.selectTicketById(ticketid.get(0)).getUserId();
            couponMapper.deleteCouponUser(couponId,userId);

            accountMapper.updateConsumptionById(userId,amount);

            return ResponseVO.buildSuccess(true);
        }catch (Exception e){
            return ResponseVO.buildFailure("失败了！");
        }
//        不用扣费
    }

    @Override
    public ResponseVO getBySchedule(int scheduleId) {
        try {
            List<Ticket> tickets = ticketMapper.selectTicketsBySchedule(scheduleId);
            ScheduleItem schedule=scheduleService.getScheduleItemById(scheduleId);
            Hall hall=hallService.getHallById(schedule.getHallId());
            int[][] seats=new int[hall.getRown()][hall.getCol()];
            tickets.stream().forEach(ticket -> {
                seats[ticket.getRowIndex()][ticket.getColumnIndex()]=1;
            });
            ScheduleWithSeatVO scheduleWithSeatVO=new ScheduleWithSeatVO();
            scheduleWithSeatVO.setScheduleItem(schedule);
            scheduleWithSeatVO.setSeats(seats);
            return ResponseVO.buildSuccess(scheduleWithSeatVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    
    @Override
	//Edwin's TODO:
    //方法职责：使用一个用户id来过去该用户的所有电影票，包括付了钱的 以及 添加了但是没付钱：失效  （这里是简化了的）
    public ResponseVO getTicketByUser(int userId) {
    	// userBy.js中renderTicketList(list)需要调用本方法。
    	try {
    		List<Ticket> tickets = ticketMapper.selectTicketByUser(userId);
        	return ResponseVO.buildSuccess(tickets);
		} catch (Exception e) {
			return ResponseVO.buildFailure("失败");
		}
    }

    @Override
    @Transactional
    //完成购票【使用会员卡】流程包括会员卡扣费、校验优惠券和根据优惠活动赠送优惠券
    public ResponseVO completeByVIPCard(List<Integer> ticketid, int couponId) {
    	VIPCardForm vipCardForm = new VIPCardForm();

    	int userId = ticketMapper.selectTicketById(ticketid.get(0)).getUserId();
        VIPCard myCard = (VIPCard)vipService.getCardByUserId(userId).getContent();
    	vipCardForm.setVipId(myCard.getId());
    	  //付款金额；
    	vipCardForm.setAmount(amount);
    	//添加到消费额。
    	accountMapper.updateConsumptionById(userId,amount);
    	//把优惠券用掉，即用数据表中移除。
        couponMapper.deleteCouponUser(couponId,userId);

//    	还需要更新票的状态：
        boolean isOk =vipService.charge(vipCardForm).getSuccess();

        if (isOk){
            try {
                for (int i : ticketid) {
                    ticketMapper.updateTicketState(i , 1);
                }
                return ResponseVO.buildSuccess(true);
            }catch (Exception e){
                return ResponseVO.buildFailure("失败了！");
            }
        }
        else {
            return ResponseVO.buildFailure("失败了！");
        }

    }

    
    @Override
    //取消锁座。
    //在最后付费那里如果点击取消，是不是要把票从数据库中删除？？？ 不会吧，点击取消还能再付费，你把数据库中信息删除就出错了，那这个方法有什么用？？？
    public ResponseVO cancelTicket(List<Integer> id) {
        try {
            for (int i: id){
                ticketMapper.deleteTicket(i);
            }
            return ResponseVO.buildSuccess("成功");
        }catch (Exception e){
            return ResponseVO.buildFailure("失败");
        }
    }


    //Edwin:
    @Override
    public ResponseVO selectTicketByScheduleIdAndSeat(int scheduleId, int columnIndex, int rowIndex){
        try {
            TicketVO ticketvo = ticketMapper.selectTicketByScheduleIdAndSeat(scheduleId, columnIndex, rowIndex).getVO();
            //把tickets列表传回了Controller，在传回前端！
            return ResponseVO.buildSuccess(ticketvo);
        } catch (Exception e) {
            return ResponseVO.buildFailure("失败");
        }
    }

    //Edwin:
    @Override
    public void setAmount(int amount){
            this.amount =amount;
    }


    /**
     * 退票
     * @param ticketId
     * @return
     */
    @Override
    public ResponseVO refund(int ticketId){
        //  从退票策略表中读取退款额度和最晚退款时间
        int beforeMinutes = refundStrategyMapper.getRefundStrategy().getBeforeMinutes() ;
        float discount = refundStrategyMapper.getRefundStrategy().getDiscount();

        Ticket ticket = ticketMapper.selectTicketById(ticketId);
        int userId; //  购买该电影票的用户id
        int scheduleId = ticket.getScheduleId();     //  该场电影的排片id
        float fare; //  该场电影的票价
        float balance;  //  会员卡里本来的余额


        Date now = new Date();  // 获取当前的系统时间
        Date scheduleTime = scheduleMapper.selectScheduleById(scheduleId).getStartTime();   //  电影开场时间

        try {
            //  退票条件：是否在规定时间之前退票，票的状态是否为已生效
            // getTime()相减得出的是以毫秒为单位，除以60*1000得到分钟
            if((scheduleTime.getTime() - now.getTime()) / (60 * 1000) > beforeMinutes && ticket.getState() == 1){
                ticketMapper.updateTicketState(ticketId,2);     // 更改数据库里票的状态,2代表“已失效”

                //  退钱给VIP，非VIP退钱到银行卡里（无限余额）
                userId = ticket.getUserId();

                //  通过排片ID获得电影票价，然后更新余额
                fare = scheduleMapper.selectScheduleById(scheduleId).getFare();
                accountMapper.updateConsumptionById(userId,-(int)(fare*discount/100));

                if(vipCardMapper.selectCardByUserId(userId)!=null){   //  检查是否是VIP

                    //  找到余额
                    balance = vipCardMapper.selectCardByUserId(userId).getBalance();
                    //  更新余额
                    vipCardMapper.updateCardBalance(userId, balance + fare * discount);
                }
                return ResponseVO.buildSuccess("成功");
            }else{
                return ResponseVO.buildFailure("已经超过可退票的时间");
            }

        }catch (Exception e){
            return ResponseVO.buildFailure("失败");
        }

    }

}
