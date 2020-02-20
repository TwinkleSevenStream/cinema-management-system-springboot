package     com.example.cinema.vo;

import  com.example.cinema.po.Hall;

/**
 * @author fjj
 * @date 2019/4/11 3:46 PM
 */
public class HallVO {
    private Integer id;
    private String hallname;
    private Integer rown;
    private Integer col;


    public HallVO(Hall hall){
        this.id = hall.getId();
//        this.name = hall.getName();
//        this.row = hall.getRow();
//        this.column = hall.getColumn();
        this.hallname = hall.getHallname();
        this.rown = hall.getRown();
        this.col = hall.getCol();

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHallname() {
        return hallname;
    }
    public void setHallname(String hallname) {
        this.hallname = hallname;
    }
    public Integer getRown() {
        return rown;
    }
    public void setRown(Integer rown) {
        this.rown = rown;
    }
    public Integer getCol() {
        return col;
    }
    public void setCol(Integer col) {
        this.col = col;
    }


//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Integer getRow() {
//        return row;
//    }
//
//    public void setRow(Integer row) {
//        this.row = row;
//    }
//
//    public Integer getColumn() {
//        return column;
//    }
//
//    public void setColumn(Integer column) {
//        this.column = column;
//    }
}
