package    com.example.cinema.controller.sales;

import   com.example.cinema.bl.sales.TicketService;
import   com.example.cinema.vo.ResponseVO;
import   com.example.cinema.vo.TicketForm;
import com.example.cinema.vo.TicketIdAndCouponIdVO;
import   org.springframework.beans.factory.annotation.Autowired;
import   org.springframework.web.bind.annotation.*;
import   java.util.List;


@RestController
@RequestMapping("/ticket")
public class TicketController {
    //这里应该是使用了多态，只使用了父接口，没有使用具体实现，但是在具体调用方法的时候，会自动识别是哪个实现类的方法。
    @Autowired
    TicketService ticketService;

    @PostMapping("/vip/buy")
    //userMovieBy.js里面应该就发送这个request：
    // 完成购票【使用会员卡】流程包括会员卡扣费、校验优惠券和根据优惠活动赠送优惠券

    //@RequestBody
    public ResponseVO buyTicketByVIPCard(@RequestBody TicketIdAndCouponIdVO ticketIdAndCouponIdVO){
        //这里传了两个参数，我不知道怎么搞，于是我自己写一个VO：TicketIdAndCouponIdVO
        List<Integer> ticketId = ticketIdAndCouponIdVO.getTicketId();
        int couponId = ticketIdAndCouponIdVO.getCouponId();
    	return ticketService.completeByVIPCard(ticketId,couponId);
    }

    
//    这里在购票的第一阶段确定选座，下单时完成，就把票添加到数据库中。
    @PostMapping("/lockSeat")
    //在选座界面有一个“确认下单”，然后就lock seat了
//    GET请求是不可以用@RequestBody来接收参数的。
    public ResponseVO lockSeat(@RequestBody TicketForm ticketForm){
        return ticketService.addTicket(ticketForm);
    }
    
    
    @PostMapping("/buy")
    public ResponseVO buyTicket(@RequestBody TicketIdAndCouponIdVO ticketIdAndCouponIdVO){
//        System.out.println("我在 buyTicket！");
        return ticketService.completeTicket(ticketIdAndCouponIdVO.getTicketId(),ticketIdAndCouponIdVO.getCouponId());
    }
    
    @GetMapping("/get/{userId}")
    //根据用户获取电影票，userBuy.js传来的请求，用户在入口函数初始化“我的电影票”
    public ResponseVO getTicketByUserId(@PathVariable  int userId){
        return ticketService.getTicketByUser(userId);
    }

    
    @GetMapping("/get/occupiedSeats")
    public ResponseVO getOccupiedSeats(@RequestParam int scheduleId){
        return ticketService.getBySchedule(scheduleId);
    }
    
    
    @PostMapping("/cancel")
    public ResponseVO cancelTicket(@RequestBody List<Integer> ticketId){
        return ticketService.cancelTicket(ticketId);
    }



    //Edwin added:
    @GetMapping("/{scheduleId}/{columnIndex}/{rowIndex}")
    public ResponseVO getTicketByScheduleIdAndSeat(@PathVariable int scheduleId,@PathVariable int columnIndex,@PathVariable int rowIndex){
        return ticketService.selectTicketByScheduleIdAndSeat(scheduleId,columnIndex,rowIndex);
    }

    //Edwin added:
    @GetMapping("/{amount}")
    public Boolean getAmount(@PathVariable int amount){
        ticketService.setAmount(amount);
        return amount>0 ;
    }

    @GetMapping("/refund/{ticketId}")
    public ResponseVO refund(@PathVariable int ticketId){
        return ticketService.refund(ticketId);

    }
}
