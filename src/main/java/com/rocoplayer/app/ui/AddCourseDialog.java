package com.rocoplayer.app.ui;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;


public class AddCourseDialog extends JDialog {

    public AddCourseDialog(JFrame parent) {
        super(parent, "添加课程", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 教室名称输入
        JLabel roomLabel = new JLabel("教室名称:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 5, 10);
        add(roomLabel, gbc);

        JTextField roomField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(roomField, gbc);

        // 课程名称输入
        JLabel courseLabel = new JLabel("课程名称:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(courseLabel, gbc);

        JTextField courseField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(courseField, gbc);

        // 星期几选择
        JLabel dayLabel = new JLabel("星期几:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(dayLabel, gbc);

        String[] daysOfWeek = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        JComboBox<String> dayComboBox = new JComboBox<>(daysOfWeek);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(dayComboBox, gbc);

        // 上午/下午选择
        JLabel timeLabel = new JLabel("时间:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(timeLabel, gbc);

        String[] timesOfDay = {"上午", "下午"};
        JComboBox<String> timeComboBox = new JComboBox<>(timesOfDay);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(timeComboBox, gbc);

        // 起始和结束周数输入
        JLabel weekLabel = new JLabel("起始-结束周:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(weekLabel, gbc);

        JTextField weekStartField = new JTextField(5);
        JTextField weekEndField = new JTextField(5);
        JPanel weekPanel = new JPanel();
        weekPanel.add(weekStartField);
        weekPanel.add(new JLabel("-"));
        weekPanel.add(weekEndField);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(weekPanel, gbc);

        // 添加课程按钮
        JButton addButton = new JButton("添加课程");
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 0, 10, 10);
        add(addButton, gbc);

        addButton.addActionListener(e -> {
            String room = roomField.getText().trim();
            String course = courseField.getText().trim();
            String day = (String) dayComboBox.getSelectedItem();
            String time = (String) timeComboBox.getSelectedItem();
            String weekStart = weekStartField.getText().trim();
            String weekEnd = weekEndField.getText().trim();

            if (room.isEmpty() || course.isEmpty() || weekStart.isEmpty() || weekEnd.isEmpty()) {
                JOptionPane.showMessageDialog(AddCourseDialog.this, "所有字段都必须填写", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                String newCourse = room + "," + day + "," + time + "," + course + "," + weekStart + "-" + weekEnd;
                addCourseToFile(newCourse);
                dispose();  // 关闭窗口
            }
        });
    }

    private void addCourseToFile(String courseLine) {
        String exePath = System.getProperty("user.dir") + "/data.txt";
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(exePath, true), StandardCharsets.UTF_8)) {
            writer.write("\n" + courseLine); // 保证每个课程信息独立一行，并在前面加上换行符
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "添加课程失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
