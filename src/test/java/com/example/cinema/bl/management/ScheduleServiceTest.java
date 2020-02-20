package com.example.cinema.bl.management;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration

public class ScheduleServiceTest {

    @Autowired
    ScheduleService scheduleService;

    @Test
    public void getScheduleById() {
        assertEquals(true,scheduleService.getScheduleById(20).getSuccess());

    }

    @Test
    public void getScheduleView() {
        assertEquals(true,scheduleService.getScheduleView().getSuccess());
    }
}