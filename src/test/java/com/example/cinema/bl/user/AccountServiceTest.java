package com.example.cinema.bl.user;

import com.example.cinema.vo.UserVO;
import org.junit.Test;

import static org.junit.Assert.*;


import com.example.cinema.data.management.HallMapper;
import com.example.cinema.po.Hall;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import com.example.cinema.vo.UserForm;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration

public class AccountServiceTest {

    @Autowired
    private AccountService accountService;


    @Test
    public void adminLogin() {
        UserForm userForm  = new UserForm();
        userForm.setPassword("123456");
        userForm.setUsername("root");

        UserVO userVO = accountService.adminLogin(userForm);
        assertArrayEquals(
                new Object[]{
                        "123456","root",1
                },
                new Object[]{
                        userVO.getPassword(),userVO.getUsername(),userVO.getId()
                }
        );
    }

    @Test
    public void updateName() {
    }

    @Test
    public void updatePw() {
    }

    @Test
    public void getAllUser() {
    }

    @Test
    public void getAllAdmin() {
    }

    @Test
    public void addOne() {
    }

    @Test
    public void delOne() {
    }
}