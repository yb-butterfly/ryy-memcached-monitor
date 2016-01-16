package com.ryy.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by xujb on 2016/1/14.
 */
public class MainWindowTest {

    public static void main(String[] args) {
        try {
            Font commonFont = new Font("宋体",Font.PLAIN,12);
            Font titleFont = new Font("宋体",Font.ITALIC,12);
            UIManager.getDefaults().put( "TextField.inactiveForeground", Color.darkGray);
            UIManager.getDefaults().put( "Button.font",commonFont);
            UIManager.getDefaults().put( "ComboBox.font",commonFont);
            UIManager.getDefaults().put( "CheckBox.font",commonFont);
            UIManager.getDefaults().put( "Label.font", commonFont);
            UIManager.getDefaults().put( "Menu.font", commonFont);
            UIManager.getDefaults().put( "MenuBar.font", commonFont);
            UIManager.getDefaults().put( "MenuItem.font", commonFont);
            UIManager.getDefaults().put( "RadioButtonMenuItem.font", commonFont);
            UIManager.getDefaults().put( "TabbedPane.font",commonFont);
            UIManager.getDefaults().put( "ToggleButton.font",commonFont);
            UIManager.getDefaults().put( "TitledBorder.font",titleFont);
            UIManager.getDefaults().put("List.font",commonFont);
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        MainWindow mainWindow = new MainWindow();
        mainWindow.start();
    }

}
