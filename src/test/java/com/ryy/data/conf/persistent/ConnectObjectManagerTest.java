package com.ryy.data.conf.persistent;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by xujb on 2016/1/14.
 */
public class ConnectObjectManagerTest {

    public static void main(String[] args) {
        ConnectObjectManager manager = ConnectObjectManager.newInstance();

        List<ConnectObject> connectObjectList = manager.getList();
        System.out.println(connectObjectList);
    }

    @Test
    public void saveTest() {
        ConnectObjectManager manager = ConnectObjectManager.newInstance();
        ConnectObject connectObject = new ConnectObject("abc", 12);
        ConnectObject connectObject1 = new ConnectObject("abc1", 121);
        manager.addConnectObject(connectObject);
        manager.addConnectObject(connectObject1);
        try {
            manager.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getListTest() {
        ConnectObjectManager manager = ConnectObjectManager.newInstance();
        List<ConnectObject> connectObjectList = manager.getList();
        System.out.println(connectObjectList);
    }
}
