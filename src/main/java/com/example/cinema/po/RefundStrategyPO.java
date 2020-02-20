package com.example.cinema.po;

/**
 * 退票策略PO
 */
public class RefundStrategyPO {
    /**
     * 退款额度
     */
    int discount;

    /**
     * 可以退款的距离开场时间最短的分钟数
     */
    int beforeMinutes;

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getBeforeMinutes() {
        return beforeMinutes;
    }

    public void setBeforeMinutes(int beforeMinutes) {
        this.beforeMinutes = beforeMinutes;
    }
}
