package com.example.webgistest;

import com.example.webgistest.dao.PgEditMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class WebGIStestApplicationTests {

    @Autowired
    PgEditMapper pgEditMapper;

    @Test
    public void mapper() {
    }

}
