package com.ryy.ui;

import javax.swing.table.DefaultTableModel;

/**
 * Created by xujb on 2016/1/15.
 */
public class ParamTableModel extends DefaultTableModel {

    public ParamTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }


}
