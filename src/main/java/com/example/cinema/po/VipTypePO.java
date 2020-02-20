package com.example.cinema.po;

/**
 * @author huwen
 * @date 2019/3/23
 */
public class VipTypePO {
    private Integer price;
    private Integer full;
    private Integer reduce;
    private Integer vaildTime;

    public Integer getVaildTime() {
        return vaildTime;
    }

    public void setVaildTime(Integer vaildTime) {
        this.vaildTime = vaildTime;
    }


    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getFull() {
        return full;
    }

    public void setFull(Integer full) {
        this.full = full;
    }

    public Integer getReduce() {
        return reduce;
    }

    public void setReduce(Integer reduce) {
        this.reduce = reduce;
    }



}
