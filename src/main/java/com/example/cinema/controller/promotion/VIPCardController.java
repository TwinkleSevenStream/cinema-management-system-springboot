package   com.example.cinema.controller.promotion;

import  com.example.cinema.bl.promotion.VIPService;
import com.example.cinema.data.promotion.VIPCardMapper;
import  com.example.cinema.vo.VIPCardForm;
import  com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by liying on 2019/4/14.
 */
@RestController()
@RequestMapping("/vip")
public class VIPCardController {
    @Autowired
    VIPService vipService;

    @Autowired
    VIPCardMapper cardMapper;


    @GetMapping("/add/{userId}/{vipTypeIndex}")
    public ResponseVO addVIP(@PathVariable int userId,@PathVariable int vipTypeIndex){ //@RequestParam和@PathVariable是有很大区别的。
//        System.out.println(userId);
        return vipService.addVIPCard(userId,vipTypeIndex);
    }
    @GetMapping("/{userId}/get")
    public ResponseVO getVIP(@PathVariable int userId){
        return vipService.getCardByUserId(userId);
    }

    @GetMapping("/getVIPInfo")
    public ResponseVO getVIPInfo(){
        return vipService.getVIPInfo();
    }

    @PostMapping("/charge")
    public ResponseVO recharge(@RequestBody VIPCardForm vipCardForm){
        return vipService.recharge(vipCardForm);
    }

//
//    @GetMapping("/shit")
//    public void shit(){
//       int a = cardMapper.insertOneCard(2,1,new Date(),new Date(),1,1,1);
//       System.out.println("shit +" +a);
//    }

    /*
    问题来了：
    我在这里是可以插入到数据库中的。
    但是
    我在VipServiceImpl中却插不进去，到底是为什么？？？
     */
}

