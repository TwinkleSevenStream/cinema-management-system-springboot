package com.example.cinema.vo;

import java.util.List;

/**
 * Created by XuTao on 2019/5/8 13:54
 * 解决多参数 Ajax无法传递问题。
 *  public ResponseVO buyTicketByVIPCard(@RequestParam List<Integer> ticketId, @RequestParam int couponId){
 */
public class TicketIdAndCouponIdVO {
    private List<Integer> ticketId;
    private int couponId;

    public TicketIdAndCouponIdVO(){}

    public List<Integer> getTicketId() {
        return ticketId;
    }

    public void setTicketId(List<Integer> ticketId) {
        this.ticketId = ticketId;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }
}
