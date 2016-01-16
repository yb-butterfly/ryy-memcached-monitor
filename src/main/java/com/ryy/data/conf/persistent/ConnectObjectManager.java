package com.ryy.data.conf.persistent;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by xujb on 2016/1/14.
 */
public class ConnectObjectManager {

    private static final String SAVE_PATH = "D:\\.ryy-memcached-monitor\\connectObjectList.data";

    private List<ConnectObject> connectObjectList = new ArrayList<>();

    private static final ConnectObjectManager manager = new ConnectObjectManager();

    public static ConnectObjectManager newInstance() {
        return manager;
    }

    private ConnectObjectManager() {
        try {
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load() throws IOException, ClassNotFoundException {
        File file = new File(SAVE_PATH);
        if (file.exists()) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            connectObjectList = (List<ConnectObject>) ois.readObject();
            ois.close();
        }

    }

    public void save() throws IOException {
        // Ŀ¼�Ƿ����
        String dirPath = SAVE_PATH.substring(0, SAVE_PATH.lastIndexOf("\\"));
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(SAVE_PATH);
        if (!file.exists()) {
            file.createNewFile();
        }

        file.delete();

        Set<String> ipSet = new HashSet<>();
        List<ConnectObject> newList = new ArrayList<>();
        for (ConnectObject connectObject : connectObjectList) {
            if (ipSet.contains(connectObject.getHost())) {
                continue;
            }
            newList.add(connectObject);
            ipSet.add(connectObject.getHost());
        }

        ObjectOutputStream obs = new ObjectOutputStream(new FileOutputStream(SAVE_PATH));
        obs.writeObject(newList);
        obs.flush();
        obs.close();
    }

    public List<ConnectObject> getList() {
        return connectObjectList;
    }

    public void addConnectObject(ConnectObject connectObject) {
        connectObjectList.add(connectObject);
    }

    public void cleanAll() {
        File file = new File(SAVE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

}
