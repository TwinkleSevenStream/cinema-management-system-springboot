package     com.example.cinema.vo;

import java.util.List;

/**
 * 电影票表格
 * 	用户id
 * 	排片id
 * 	座位表：行列   ？为什么是多个座位，而不是一张票一个座位？
 * 
 */
public class TicketForm {

    /**
     * 用户id
     */
    private int userId;
    /**
     * 排片id
     */
    private int scheduleId;

    private List<SeatForm> seats;


    public List<SeatForm> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatForm> seats) {
        this.seats = seats;
    }

    public TicketForm() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }



}
