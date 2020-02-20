package com.example.cinema.bl.management;

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


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration

public class HallServiceTest {
    @Autowired
    private HallMapper hallmapper;

    @Test
    public void searchAllHall() {
        List<Hall> halls = hallmapper.selectAllHall();
        Hall hall1 = halls.get(0);
        Hall hall2 = halls.get(1);

        assertArrayEquals(
                new Object[]{
                        5,5,"A"
                },
                new Object[]{
                        hall1.getCol(),hall1.getRown(),hall1.getHallname()
                }
        );
        assertArrayEquals(
                new Object[]{
                        12,2,"B"
                },
                new Object[]{
                        hall2.getCol(),hall2.getRown(),hall2.getHallname()
                }
        );

    }

    @Test
    public void addHall() {

    }

    @Test
    public void updateHall() {
    }
}