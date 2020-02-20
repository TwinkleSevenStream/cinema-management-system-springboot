package     com.example.cinema.controller.management;

import  com.example.cinema.bl.management.HallService;
import  com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.apache.ibatis.annotations.Param;



/**影厅管理
 * @author fjj
 * @date 2019/4/12 1:59 PM
 */
@RestController
public class HallController {
    @Autowired
    private HallService hallService;

    @RequestMapping(value = "hall/all", method = RequestMethod.GET)
    public ResponseVO searchAllHall(){
        return hallService.searchAllHall();
    }

    //J
    @RequestMapping(value="hall/add/{hallname}/{col}/{rown}",method = RequestMethod.GET)
    public ResponseVO addNewHall(@PathVariable String hallname, @PathVariable int col, @PathVariable int rown){
        return hallService.addHall(hallname,col,rown);
    }
    @RequestMapping(value="hall/update/{ind}/{hallname}/{col}/{rown}",method=RequestMethod.GET)
    public ResponseVO updateOldHall(@PathVariable int ind,@PathVariable String hallname,@PathVariable int col,@PathVariable int rown){
        return hallService.updateHall(ind,hallname,col,rown);
    }
}
