package com.ryy.data.service;

import com.ryy.data.service.bean.StatBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xujb on 2016/1/15.
 */
public class StatConfService {

    private static StatConfService statConfService = new StatConfService();

    private Map<String, StatBean> dataMap = new HashMap<>();

    private StatConfService() {
        load();
    }

    private void load() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("stats.config")));
            int idx = 1000;
            String line;
            while ((line = br.readLine()) != null) {
                String[] tmpArr = line.split("=");
                StatBean statBean = new StatBean(tmpArr[0], null, tmpArr[1], tmpArr[2], idx--);
                dataMap.put(statBean.getKey(), statBean);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public StatBean get(String key) {
        return dataMap.get(key);
    }

    public void put(String key, String value) {
        StatBean statBean = get(key);
        if (statBean != null) {
            String type = statBean.getType();
            if (type.equals("String")) {
                statBean.setValue(value);
            } else if (type.equals("longTime")) {
                statBean.setValue(value);
            } else {
                statBean.setValue(value);
            }
        }
    }

    public static StatConfService newInstance() {
        return statConfService;
    }

}
