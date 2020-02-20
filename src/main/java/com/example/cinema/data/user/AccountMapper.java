package  com.example.cinema.data.user;
import  com.example.cinema.po.User;
import com.example.cinema.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface AccountMapper {
    int createNewAccount(@Param("username") String username, @Param("password") String password);

    User getAccountByName(@Param("username") String username);
    User getAdminAccountByName(@Param("username") String username);

    int updateName(@Param("name") String name, @Param("oldName") String oldName);
    int updatePw(@Param("name") String name, @Param("pw") String pw);

    List<UserVO> getAllUser();
    List<UserVO> getAllAdmin();

    int insertIntoUser(@Param("username") String name, @Param("password") String pw);
    int insertIntoAdmin(@Param("username") String name, @Param("password") String pw);

    int delUserByName(@Param("username") String username);
    int delUserByID(@Param("id") int id);

    int delAdminByName(@Param("username") String username);
    int delAdminByID(@Param("id") int id);

    int updateConsumptionById(@Param("id") int id, @Param("consumption") int consumption);



}
