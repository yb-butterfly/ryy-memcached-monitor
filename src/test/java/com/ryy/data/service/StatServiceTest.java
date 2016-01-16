package com.ryy.data.service;

import org.junit.Test;

/**
 * Created by xujb on 2016/1/14.
 */
public class StatServiceTest {

    @Test
    public void testConnectTest() {
        StatService statService = new StatService();
        System.out.println(statService.testConnect("183.61.86.182", 19527));
    }

    @Test
    public void test() {
        StatService statService = new StatService();
        statService.connect("183.61.86.182", 19527);
        statService.test();
        statService.close();
    }
}
