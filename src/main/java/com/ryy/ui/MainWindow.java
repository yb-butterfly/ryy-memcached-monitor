package com.ryy.ui;

import com.ryy.data.conf.persistent.ConnectObject;
import com.ryy.data.conf.persistent.ConnectObjectManager;
import com.ryy.data.service.StatConfService;
import com.ryy.data.service.StatService;
import com.ryy.data.service.bean.StatBean;
import com.ryy.util.NumberUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Created by xujb on 2016/1/14.
 */
public class MainWindow extends JFrame {

    private StatService statService = new StatService();

    private ConnectDialog connectDialog = new ConnectDialog(this, statService);

    private JPanel contentPane = new JPanel();

    private JPanel selectPanel = new JPanel();
    private JButton basicBtn = new JButton("基本信息");
    private JButton statBtn = new JButton("统计信息");
    private JButton slabBtn = new JButton("SLAB信息");
    private JButton reFreshBtn = new JButton("刷新");

    private final JTable paramTable = new JTable();
    private final JTable statTable = new JTable();
    private final JTable slabTable = new JTable();

    public void start() {
        // 设置标题
        String title = "Memcached Monitor";
        setTitle(title);

        // 设置长宽
        int width = 1366;
        int height = 700;
        setSize(width, height);

//        contentPane.setLayout(new GridLayout(2, 1));
        getContentPane().setLayout(null);


        selectPanel.add(basicBtn);
        setBasicBtnEvent();
        selectPanel.add(statBtn);
        setStatBtnEvent();
        selectPanel.add(slabBtn);
        setSlabBtnEvent();
        selectPanel.add(reFreshBtn);
        setReFreshBtnEvent();

        selectPanel.setLayout(new FlowLayout(0));
        getContentPane().add(selectPanel);
        selectPanel.setBounds(0, 0, width, 30);


        contentPane.setLayout(new CardLayout());
        getContentPane().add(contentPane);
        contentPane.setBounds(0, 40, width, height - 50);

        JScrollPane paramTablePanel = new JScrollPane(paramTable);
        contentPane.add("basic", paramTablePanel);

        JScrollPane statTablePanel = new JScrollPane(statTable);
        contentPane.add("stat", statTablePanel);

        JScrollPane slabTablePanel = new JScrollPane(slabTable);
        contentPane.add("slab", slabTablePanel);


        // 默认关闭方式
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocation(300, 300);

        // 设置菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu jMenu = new JMenu("文件");
        menuBar.add(jMenu);
        JMenuItem newConnectMenu = new JMenuItem("新的连接");
        jMenu.add(newConnectMenu);
        List<ConnectObject> connectObjectList = ConnectObjectManager.newInstance().getList();
        for (final ConnectObject connectObject : connectObjectList) {
            JMenuItem jMenuItem = new JMenuItem(connectObject.getHost() + ":" + connectObject.getPort());
            jMenu.add(jMenuItem);

            jMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    connectDialog.setModal(true);
                    connectDialog.start(connectObject.getHost(), String.valueOf(connectObject.getPort()));
                }
            });
        }

        jMenu = new JMenu("帮助");
        menuBar.add(jMenu);

        Toolkit kit = Toolkit.getDefaultToolkit(); //定义工具包
        Dimension screenSize = kit.getScreenSize(); //获取屏幕的尺寸
        int screenWidth = screenSize.width; //获取屏幕的宽
        int screenHeight = screenSize.height; //获取屏幕的高
//        connectDialog.setLocation(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2);
        connectDialog.setLocation(getLocation());
        newConnectMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectDialog.setModal(true);
                connectDialog.start("", "");
            }
        });

        setJMenuBar(menuBar);
        setResizable(false);
        setVisible(true);
    }

    public void monitor() {
        Map<String, String> map = statService.getStatMap();

        StatConfService statConfService = StatConfService.newInstance();

        // 只使用配置需要显示的参数
        List<StatBean> showMapList = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            statConfService.put(entry.getKey(), entry.getValue());
            StatBean statBean = statConfService.get(entry.getKey());
            if (statBean != null) {
                showMapList.add(statBean);
            }
        }

        // 按权限排序
        Collections.sort(showMapList, new Comparator<StatBean>() {
            @Override
            public int compare(StatBean o1, StatBean o2) {
                return o2.getWeight() - o1.getWeight();
            }
        });

        int size = showMapList.size();
        String[][] data = new String[size][3];
        StatBean statBean;
        for (int i = 0; i < size; i++) {
            statBean = showMapList.get(i);
            data[i][0] = statBean.getDesc();
            data[i][1] = statBean.getValue();
            data[i][2] = statBean.getKey();
        }

        String[] columnNames = {"描述", "值", "参数"};
        ParamTableModel paramTableModel = new ParamTableModel(data, columnNames);
        paramTable.setModel(paramTableModel);
        // ------------------------------------------- 以上是基本信息 -----------------------------------------------------------

        // 统计类
        List<StatBean> calcList = new ArrayList<>();
        // 缓存命中率 = get_hits/cmd_get * 100%
        int getHits = Integer.parseInt(statConfService.get("get_hits").getValue());
        int cmdGet = Integer.parseInt(statConfService.get("cmd_get").getValue());
        double getHitPercent = (double) getHits / cmdGet * 100;
        StatBean calcBean = new StatBean("getHitPercent", NumberUtil.toFix(getHitPercent, 2) + "%", "缓存命中率", "calc", -1);
        calcList.add(calcBean);

        int calcListSize = calcList.size();
        String[][] calcData = new String[calcListSize][3];
        for (int i = 0; i < calcListSize; i++) {
            statBean = calcList.get(i);
            calcData[i][0] = statBean.getDesc();
            calcData[i][1] = statBean.getValue();
            calcData[i][2] = statBean.getKey();
        }
        ParamTableModel statModel = new ParamTableModel(calcData, new String[]{"描述", "值", "参数"});
        statTable.setModel(statModel);
        // -------------------------------------------- 以上是统计信息 ------------------------------------------------------------


        // ============================================= 以下是slab信息 ==========================================================
//                    String[] slabColArr = new String[]{
//                            "cas_badval", "cas_hits", "chunk_size", "chunks_per_page", "cmd_set", "decr_hits", "delete_hits", "free_chunks", "free_chunks_end", "get_hits", "incr_hits", "mem_requested", "total_chunks", "total_pages", "touch_hits", "used_chunks"
//                    };
        String[] slabColArr = new String[]{
                "chunk_size", "chunks_per_page", "total_pages", "free_chunks", "free_chunks_end", "cmd_set", "get_hits", "decr_hits", "cas_hits", "mem_requested", "total_chunks", "used_chunks"
        };
        Map<String, Map<String, String>> slabMap = new HashMap<>();
        String active_slabs = "";
        String total_malloced = "";
        for (Map.Entry<String, String> entry : statService.getStatSlabsMap().entrySet()) {
            String[] tmpArr = entry.getKey().split(":");
            if (tmpArr.length != 2) {
                if (entry.getKey().equals("active_slabs")) {
                    active_slabs = entry.getValue();
                } else if (entry.getKey().equals("total_malloced")) {
                    total_malloced = entry.getValue();
                }
                continue;
            }
            Map<String, String> tmpMap = slabMap.get(tmpArr[0]);
            if (tmpMap == null) {
                tmpMap = new HashMap<>();
                slabMap.put(tmpArr[0], tmpMap);
            }
            tmpMap.put(tmpArr[1], entry.getValue());
        }

        int slabListSize = slabMap.size();
        String[][] slabData = new String[slabListSize][slabColArr.length];
        int slabIdx = 0;
        for (Map<String, String> tmpMap : new TreeMap<>(slabMap).values()) {
            for (int i = 0; i < slabColArr.length; i++) {
                slabData[slabIdx][i] = tmpMap.get(slabColArr[i]);
            }
            slabIdx++;
        }
        ParamTableModel slabModel = new ParamTableModel(slabData, slabColArr);
        slabTable.setModel(slabModel);
    }

    public void setBasicBtnEvent() {
        basicBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) contentPane.getLayout()).show(contentPane, "basic");
            }
        });
    }

    public void setStatBtnEvent() {
        statBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) contentPane.getLayout()).show(contentPane, "stat");
            }
        });
    }

    public void setSlabBtnEvent() {
        slabBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) contentPane.getLayout()).show(contentPane, "slab");
            }
        });
    }

    public void setReFreshBtnEvent() {
        reFreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (statService.isConnected()) {
                    monitor();
                }
            }
        });
    }

}
