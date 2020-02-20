package     com.example.cinema.bl.user;

import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.UserForm;
import com.example.cinema.vo.UserVO;
/**
 * @author huwen
 * @date 2019/3/23
 */
public interface AccountService {

    /**
     * 注册账号
     * @return
     */
    ResponseVO registerAccount(UserForm userForm);

    /**
     * 用户登录，登录成功会将用户信息保存再session中
     * @return
     */

    UserVO login(UserForm userForm);

    UserVO adminLogin(UserForm userForm);

    ResponseVO updateName(String name, String oldName);
    ResponseVO updatePw(String oldPw,String newPw,String name);

    ResponseVO getAllUser();
    ResponseVO getAllAdmin();
    ResponseVO addOne(String type,String name , String pw);
    ResponseVO delOne(String type,String byWhat , String nameOrID);

}
