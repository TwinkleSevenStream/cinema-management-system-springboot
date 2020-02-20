package     com.example.cinema.controller.user;

import  com.example.cinema.blImpl.user.AccountServiceImpl;
import  com.example.cinema.config.InterceptorConfiguration;
import  com.example.cinema.vo.UserForm;
import  com.example.cinema.vo.ResponseVO;
import  com.example.cinema.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController()
public class AccountController {
    private final static String ACCOUNT_INFO_ERROR="用户名或密码错误";
    @Autowired
    private AccountServiceImpl accountService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseVO login(@RequestBody UserForm userForm, HttpSession session){
        UserVO user = accountService.login(userForm);
        if(user==null){
           return ResponseVO.buildFailure(ACCOUNT_INFO_ERROR);
        }
        //注册session
        session.setAttribute(InterceptorConfiguration.SESSION_KEY,userForm);
        return ResponseVO.buildSuccess(user);
    }
    @RequestMapping(value = "/login2",method = RequestMethod.POST)
    public ResponseVO adminLogin(@RequestBody UserForm userForm, HttpSession session){
        UserVO user = accountService.adminLogin(userForm);
        if(user==null){
            return ResponseVO.buildFailure(ACCOUNT_INFO_ERROR);
        }
        //注册session
        session.setAttribute(InterceptorConfiguration.SESSION_KEY,userForm);
        return ResponseVO.buildSuccess(user);
    }

    @PostMapping("/register")
    public ResponseVO registerAccount(@RequestBody UserForm userForm){
        return accountService.registerAccount(userForm);
    }

    @PostMapping("/logout")
    public String logOut(HttpSession session){
        session.removeAttribute(InterceptorConfiguration.SESSION_KEY);
        return "index";
    }

    @GetMapping("user/update/name/{name}/{oldName}")
    public ResponseVO updateName(@PathVariable String name,@PathVariable String oldName) {
        return accountService.updateName(name, oldName);
    }

    @GetMapping("user/update/pw/{oldPw}/{newPw}/{name}")
    public ResponseVO updatePw(@PathVariable String oldPw,@PathVariable String newPw,@PathVariable String name){
//        return ResponseVO.buildSuccess(accountService.updatePw(oldPw,newPw,name));//这里不要犯傻啊，搞个双重的做啥。
        return accountService.updatePw(oldPw,newPw,name);
    }

    @GetMapping("/admin/getAllUser")
    public ResponseVO getAllUser(){
        return accountService.getAllUser();
    }
    @GetMapping("/admin/getAllAdmin")
    public ResponseVO getAllAdmin(){
        return accountService.getAllAdmin();
    }

    @GetMapping("/admin/user/add/{userType}/{name}/{pw}")
    public ResponseVO addOne(@PathVariable String userType, @PathVariable String name,@PathVariable String pw){
        return accountService.addOne(userType,name,pw);
    }
    @GetMapping("/admin/user/del/{type}/{byWhat}/{nameOrID}")
    public ResponseVO delOne(@PathVariable String type, @PathVariable String byWhat,@PathVariable String nameOrID){
        return accountService.delOne(type,byWhat,nameOrID);
    }
}



