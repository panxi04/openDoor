package com.rocoplayer.app.ui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ModifyDatesDialog extends JDialog {

    public ModifyDatesDialog(JFrame parent) {
        super(parent, "修改始末日期", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
    
        // 起始日期输入
        JLabel startLabel = new JLabel("起始日期 (年-月-日):");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 5, 10);
        add(startLabel, gbc);
    
        JTextField startYearField = new JTextField(5);
        JTextField startMonthField = new JTextField(3);
        JTextField startDayField = new JTextField(3);
        JPanel startPanel = new JPanel();
        startPanel.add(startYearField);
        startPanel.add(new JLabel("-"));
        startPanel.add(startMonthField);
        startPanel.add(new JLabel("-"));
        startPanel.add(startDayField);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(startPanel, gbc);
    
        // 结束日期输入
        JLabel endLabel = new JLabel("结束日期 (年-月-日):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(endLabel, gbc);
    
        JTextField endYearField = new JTextField(5);
        JTextField endMonthField = new JTextField(3);
        JTextField endDayField = new JTextField(3);
        JPanel endPanel = new JPanel();
        endPanel.add(endYearField);
        endPanel.add(new JLabel("-"));
        endPanel.add(endMonthField);
        endPanel.add(new JLabel("-"));
        endPanel.add(endDayField);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(endPanel, gbc);
    
        // 确认按钮
        JButton confirmButton = new JButton("确定");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 0, 10, 10);
        add(confirmButton, gbc);
    
        confirmButton.addActionListener(e -> {
            String startYear = startYearField.getText().trim();
            String startMonth = startMonthField.getText().trim();
            String startDay = startDayField.getText().trim();
            String endYear = endYearField.getText().trim();
            String endMonth = endMonthField.getText().trim();
            String endDay = endDayField.getText().trim();
    
            if (startYear.isEmpty() || startMonth.isEmpty() || startDay.isEmpty() ||
                endYear.isEmpty() || endMonth.isEmpty() || endDay.isEmpty()) {
                JOptionPane.showMessageDialog(ModifyDatesDialog.this, "所有字段都必须填写", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                String newStartDate = startYear + "-" + startMonth + "-" + startDay;
                String newEndDate = endYear + "-" + endMonth + "-" + endDay;
                updateDatesInFile(newStartDate, newEndDate);
                dispose();  // 关闭窗口
            }
        });
    }
    
    private void updateDatesInFile(String newStartDate, String newEndDate) {
        String exePath = System.getProperty("user.dir") + "/data.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(exePath, StandardCharsets.UTF_8))) {
            StringBuilder updatedContent = new StringBuilder();
            String line;
            boolean isFirstLine = true;
    
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    // 替换第一行的日期
                    updatedContent.append(newStartDate).append(",").append(newEndDate).append("\n");
                    isFirstLine = false;
                } else {
                    updatedContent.append(line).append("\n");
                }
            }
    
            // 写回更新后的内容到文件
            try (FileWriter writer = new FileWriter(exePath, StandardCharsets.UTF_8)) {
                writer.write(updatedContent.toString());
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "修改日期失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}