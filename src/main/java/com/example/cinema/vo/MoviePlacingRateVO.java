package com.example.cinema.vo;

/**
 * @author huwen
 * @date 2019/3/23
 */
public class MoviePlacingRateVO {
   private String movie;
   private int ticketNum;
   private float rate;
   private int seatNum;
   private int movieId;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getTicketNum() {
        return ticketNum;
    }

    public void setTicketNum(int ticketNum) {
        this.ticketNum = ticketNum;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public void  ticketNumPlus(){
        this.ticketNum++;
    }
    public void  seatNumPlus(int a){
        this.seatNum+=a;
    }
}
