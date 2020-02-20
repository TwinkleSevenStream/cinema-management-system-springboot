package     com.example.cinema.blImpl.user;

import  com.example.cinema.bl.user.AccountService;
import  com.example.cinema.data.user.AccountMapper;
import  com.example.cinema.po.User;
import  com.example.cinema.vo.UserForm;
import  com.example.cinema.vo.ResponseVO;
import  com.example.cinema.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author huwen
 * @date 2019/3/23
 */
@Service
public class AccountServiceImpl implements AccountService {
    private final static String ACCOUNT_EXIST = "账号已存在";
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public ResponseVO registerAccount(UserForm userForm) {
        try {
            accountMapper.createNewAccount(userForm.getUsername(), userForm.getPassword());
        } catch (Exception e) {
            return ResponseVO.buildFailure(ACCOUNT_EXIST);
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public UserVO login(UserForm userForm) {
        User user = accountMapper.getAccountByName(userForm.getUsername());
        if (null == user || !user.getPassword().equals(userForm.getPassword())) {
            return null;
        }
        return new UserVO(user);
    }

    @Override
    public UserVO adminLogin(UserForm userForm) {
        User user = accountMapper.getAdminAccountByName(userForm.getUsername());
        if (null == user || !user.getPassword().equals(userForm.getPassword())) {
            return null;
        }
        return new UserVO(user);
    }

    @Override
    public ResponseVO updateName(String  name,String oldName){
        try{
            User user = accountMapper.getAccountByName(name);
            if (user!=null){
                return ResponseVO.buildFailure("existed");
            }
            else {
                int a = accountMapper.updateName(name,oldName);
                return ResponseVO.buildSuccess(a);
            }
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseVO.buildFailure("error");
        }
    }

    @Override
    public ResponseVO updatePw(String oldPw,String newPw,String name){
        try{
            User user = accountMapper.getAccountByName(name);
            if (!user.getPassword().equals(oldPw)){
                return ResponseVO.buildFailure("oldPw_err");
            }
            return ResponseVO.buildSuccess(accountMapper.updatePw(name,newPw));
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseVO.buildFailure("error");
        }
    }

    @Override
    public ResponseVO getAllUser(){
        try{
            return ResponseVO.buildSuccess(accountMapper.getAllUser());
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseVO.buildFailure("error");
        }
    }
    @Override
    public ResponseVO getAllAdmin(){
        try{
            return ResponseVO.buildSuccess(accountMapper.getAllAdmin());
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseVO.buildFailure("error");
        }
    }

    @Override
    public ResponseVO addOne(String type,String name, String pw){
        try{
            if (type.equals("user")){
                User user = accountMapper.getAccountByName(name);
                if (user!=null){
                    return ResponseVO.buildFailure("existed");
                }
                return  ResponseVO.buildSuccess(accountMapper.insertIntoUser(name,pw));
            }
            else if (type.equals("admin")){
                User user = accountMapper.getAdminAccountByName(name);
                if (user!=null){
                    return ResponseVO.buildFailure("existed");
                }
                return  ResponseVO.buildSuccess(accountMapper.insertIntoAdmin(name,pw));
            }
            else {
                return ResponseVO.buildFailure("type错误！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseVO.buildFailure("error");
        }

    }

    @Override
    public ResponseVO delOne(String type,String byWhat, String nameOrID){
        try{
            if (type.equals("user")){
                if (byWhat.equals("name")){
                    return ResponseVO.buildSuccess(accountMapper.delUserByName(nameOrID));
                }
                else {
                    return  ResponseVO.buildSuccess(accountMapper.delUserByID(Integer.parseInt(nameOrID)));
                }
            }
            else if (type.equals("admin")){
                if (byWhat.equals("name")){
                    return ResponseVO.buildSuccess(accountMapper.delAdminByName(nameOrID));
                }
                else {
                    return  ResponseVO.buildSuccess(accountMapper.delAdminByID(Integer.parseInt(nameOrID)));
                }
            }
            else {
                return ResponseVO.buildFailure("type错误！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseVO.buildFailure("error");
        }

    }
}
