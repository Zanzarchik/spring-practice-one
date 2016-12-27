package ua.epam.spring.hometask.service.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.service.api.AuditoriumService;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:test-spring-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestAuditoriumService {

    @Autowired
    private AuditoriumService auditoriumService;

    @Test
    public void testGetAll(){

        int initialSize = 1;
        int actualSize = auditoriumService.getAll().size();

        assertEquals("Try to get all auditoriums from the AuditoriumService", initialSize, actualSize);
    }

    @Test
    public void testGetByName(){

        Auditorium auditorium = auditoriumService.getByName("test_auditorium");

        Assert.assertNotNull(auditorium);
    }
}
