package com.ryy.ui;

import com.ryy.data.conf.persistent.ConnectObject;
import com.ryy.data.conf.persistent.ConnectObjectManager;
import com.ryy.data.service.StatService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by xujb on 2016/1/14.
 */
public class ConnectDialog extends JDialog {

    private MainWindow parent;
    private StatService statService;

    private ConnectObjectManager connectManager = ConnectObjectManager.newInstance();

    private final JTextField ipField = new JTextField();
    private final JTextField portField = new JTextField();
    private final JButton okBtn = new JButton("确定");
    private final JButton testBtn = new JButton("测试");
    private final JLabel tipsLab = new JLabel("提示:");
    private final JLabel tipsResultLab = new JLabel("输入IP和端口");

    private boolean hasStart = false;

    public ConnectDialog(MainWindow parent, StatService statService) {
        this.parent = parent;
        this.statService = statService;
    }

    public void start(String host, String port) {

        if (!hasStart) {
            // 设置长宽
            int width = 300;
            int height = 200;
            setSize(width, height);

            Container container = getContentPane();
            container.setLayout(null);

//            java.util.List<ConnectObject> connectList = connectManager.getList();
//            if (connectList.size() > 0) {
//                ConnectObject connectObject = connectList.get(connectList.size() - 1);
//                ipField.setText(connectObject.getHost());
//                portField.setText(String.valueOf(connectObject.getPort()));
//            }

            JLabel ipLab = new JLabel("IP:");
            ipLab.setBounds(60, 10, 50, 30);
            ipField.setBounds(110, 10, 130, 30);
            container.add(ipLab);
            container.add(ipField);

            JLabel portLab = new JLabel("PORT");
            portLab.setBounds(60, 50, 100, 30);
            portField.setBounds(110, 50, 130, 30);
            container.add(portLab);
            container.add(portField);

            testBtn.setBounds(80, 90, 60, 30);
            okBtn.setBounds(160, 90, 60, 30);
            container.add(testBtn);
            container.add(okBtn);
            setTestBtnEvent();
            setOKBtnEvent();

            tipsLab.setBounds(60, 130, 50, 30);
            tipsResultLab.setBounds(90, 130, 100, 30);
            container.add(tipsLab);
            tipsResultLab.setForeground(Color.RED);
            container.add(tipsResultLab);

            hasStart = true;
            setResizable(false);
        }

        ipField.setText(host);
        portField.setText(port);

        setVisible(true);
    }

    void setTestBtnEvent() {

        testBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = "Memcached Monitor";
                parent.setTitle(title);
                tipsResultLab.setText("...");

                String ipStr = ipField.getText();
                String portStr = portField.getText();

                if (ipStr == null || ipStr.trim().isEmpty()) {
                    tipsResultLab.setText("IP不能为空");
                    return;
                }

                if (portStr == null || portStr.trim().isEmpty()) {
                    tipsResultLab.setText("端口不能为空");
                    return;
                }

                int port = -1;
                try {
                    port = Integer.parseInt(portStr);
                } finally {
                }
                if (port == -1) {
                    tipsResultLab.setText("端口不是整数");
                    return;
                }

                boolean isOK = statService.testConnect(ipStr, port);
                if (isOK) {
                    tipsResultLab.setText("连接测试成功");
                } else {
                    tipsResultLab.setText("连接测试失败");
                }
            }
        });
    }

    void setOKBtnEvent() {

        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tipsResultLab.setText("...");
                String title = "Memcached Monitor";
                parent.setTitle(title);
                String ipStr = ipField.getText();
                String portStr = portField.getText();

                if (ipStr == null || ipStr.trim().isEmpty()) {
                    tipsResultLab.setText("IP不能为空");
                    return;
                }

                if (portStr == null || portStr.trim().isEmpty()) {
                    tipsResultLab.setText("端口不能为空");
                    return;
                }

                int port = -1;
                try {
                    port = Integer.parseInt(portStr);
                } finally {
                }
                if (port == -1) {
                    tipsResultLab.setText("端口不是整数");
                    return;
                }

                boolean isOK = statService.connect(ipStr, port);
                if (isOK) {
                    // 把IP和端口保存好
                    ConnectObject connectObject = new ConnectObject(ipStr, port);
                    connectManager.addConnectObject(connectObject);
                    try {
                        connectManager.save();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    // 修改显示
                    parent.setTitle("当前已连接至【" + ipStr + ":" + portStr + "】");

                    parent.monitor();

                    dispose();
                } else {
                    tipsResultLab.setText("连接测试失败");
                }
            }
        });
    }
}
