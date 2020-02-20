package     com.example.cinema.po;


import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by liying on 2019/4/14.
 */

public class VIPCard {

    private int full;
    private int reduce;
    private int userId;
    private int id;
    private float balance;
    private Date joinDate;
    private Date endTime;
    private int vipTypeIndex; //在数据库中的第几条


//    这里的vipTypeIndex居然写错了，setter写错了，是不是这里的影响导致插入不进数据库。居然不是。
    public VIPCard() {

    }

    public int getFull() {
        return full;
    }

    public void setFull(int full) {
        this.full = full;
    }

    public int getReduce() {
        return reduce;
    }

    public void setReduce(int reduce) {
        this.reduce = reduce;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getVipTypeIndex() {
        return vipTypeIndex;
    }

    public void setVipTypeIndex(int vipTypeIndex) {
        this.vipTypeIndex = vipTypeIndex;
    }

    public float calculate(float amount) {
        return (int)(amount/full)*reduce+amount;
    }
}
