package com.example.cinema.bl.management;

import org.junit.Test;

import static org.junit.Assert.*;

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

public class RefundStrategyServiceTest {

    @Autowired
    RefundStrategyService refundStrategyService;

    @Test
    public void getRefundStrategy() {
        assertEquals(true,refundStrategyService.getRefundStrategy().getSuccess());
    }
}