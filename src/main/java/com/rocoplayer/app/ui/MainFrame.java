package com.rocoplayer.app.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import com.rocoplayer.app.App;
import com.rocoplayer.app.config.AppConfig;
import com.rocoplayer.app.week.WeekData;

/**
 * 主界面
 * 
 * @author ZJ
 *
 */
public class MainFrame {

    /**
     * 初始化界面
     */
    public void init() {

        JFrame window = new JFrame(AppConfig.title);
        window.setSize(600, 720);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(AppConfig.stageResizable);
        window.setLocationRelativeTo(null);

        // 设置应用图标
        Image image = new ImageIcon(App.class.getResource(AppConfig.icon)).getImage();
        window.setIconImage(image);

        // 使用 GridBagLayout 来布局整个窗口
        window.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 创建按钮 "显示开门表"
        JButton showScheduleButton = new JButton("显示开门表");
        gbc.gridx = 0; // 第0列
        gbc.gridy = 0; // 第0行
        gbc.insets = new Insets(10, 10, 10, 10); // 设置边距
        window.add(showScheduleButton, gbc);

        // 创建按钮 "显示所有课程"
        JButton showAllCoursesButton = new JButton("显示所有课程");
        gbc.gridx = 1; // 第1列
        gbc.gridy = 0; // 第0行
        window.add(showAllCoursesButton, gbc);

        // 创建按钮 "添加课程"
        JButton addCourseButton = new JButton("添加课程");
        gbc.gridx = 2; // 第2列
        gbc.gridy = 0; // 第0行
        window.add(addCourseButton, gbc);

        // 创建按钮 "修改始末日期"
        JButton modifyDatesButton = new JButton("修改始末日期");
        gbc.gridx = 3; // 第3列
        gbc.gridy = 0; // 第0行
        window.add(modifyDatesButton, gbc);

        // 为“添加课程”按钮设置动作监听
        addCourseButton.addActionListener(e -> {
            AddCourseDialog dialog = new AddCourseDialog(window);
            dialog.setVisible(true);
        });

        // 为“修改始末日期”按钮设置动作监听
        modifyDatesButton.addActionListener(e -> {
            ModifyDatesDialog dialog = new ModifyDatesDialog(window);
            dialog.setVisible(true);
        });

        // 创建用于显示课程内容的面板
        JPanel coursePanel = new JPanel();
        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(coursePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        gbc.gridx = 0; // 第0列
        gbc.gridy = 1; // 第1行
        gbc.gridwidth = 4; // 占四列
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; // 水平方向拉伸
        gbc.weighty = 1.0; // 垂直方向拉伸
        window.add(scrollPane, gbc);

        // 设置按钮点击事件 - 显示开门表
        showScheduleButton.addActionListener(e -> {
            // 清空面板的旧内容
            coursePanel.removeAll();

            // 创建 WeekData 的实例并调用 getScheduleOutput 方法，收集输出内容
            WeekData weekData = new WeekData();
            String scheduleOutput = weekData.getScheduleOutput();

            // 将输出内容逐行添加到面板
            String[] scheduleLines = scheduleOutput.split("\n");
            JPanel titlePanel = new JPanel();
            JLabel titleLabel = new JLabel("开门表安排:");
            titlePanel.add(titleLabel);
            coursePanel.add(titlePanel);

            for (String line : scheduleLines) {
                JLabel courseLabel = new JLabel(line);
                courseLabel.setBorder(new EmptyBorder(5, 10, 5, 10)); // 适度增加边距
                coursePanel.add(courseLabel);
            }

            coursePanel.revalidate(); // 重新布局
            coursePanel.repaint(); // 重绘界面
        });

        // 设置按钮点击事件 - 显示所有课程
        showAllCoursesButton.addActionListener(e -> {
            // 清空面板的旧内容
            coursePanel.removeAll();

            // 获取当前工作目录并读取 data.txt 文件
            String exePath = System.getProperty("user.dir") + "/data.txt";

            try (BufferedReader reader = new BufferedReader(new FileReader(exePath, StandardCharsets.UTF_8))) {
                String line;

                // 添加标题
                JPanel titlePanel = new JPanel();
                JLabel titleLabel = new JLabel("所有课程安排:");
                titlePanel.add(titleLabel);
                titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                coursePanel.add(titlePanel);

                // 逐行显示课程内容
                while ((line = reader.readLine()) != null) {
                    // 使用 GridBagLayout 对每行课程进行布局，使删除按钮靠右
                    JPanel courseRow = new JPanel(new GridBagLayout());
                    courseRow.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // 添加外边距以使行之间分隔更清晰
                    GridBagConstraints rowGbc = new GridBagConstraints();

                    // 课程信息
                    JLabel courseLabel = new JLabel(line);
                    rowGbc.gridx = 0; // 第0列
                    rowGbc.gridy = 0; // 第0行
                    rowGbc.weightx = 1.0; // 使得课程信息部分占据大部分空间
                    rowGbc.anchor = GridBagConstraints.WEST; // 靠左对齐
                    rowGbc.fill = GridBagConstraints.HORIZONTAL; // 水平方向填充
                    courseRow.add(courseLabel, rowGbc);

                    // 删除按钮
                    JButton deleteButton = new JButton("删除");
                    rowGbc.gridx = 1; // 第1列
                    rowGbc.gridy = 0; // 第0行
                    rowGbc.weightx = 0; // 删除按钮不占据多余空间
                    rowGbc.anchor = GridBagConstraints.EAST; // 靠右对齐
                    rowGbc.insets = new Insets(0, 10, 0, 0); // 给按钮一些左侧空隙
                    courseRow.add(deleteButton, rowGbc);

                    // 将line放入final变量中
                    final String courseLine = line;

                    // 删除按钮的功能
                    deleteButton.addActionListener(deleteEvent -> {
                        int confirm = JOptionPane.showConfirmDialog(window, "确认删除该课程吗?", "删除课程", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            // 删除该行课程
                            removeCourseFromFile(exePath, courseLine);
                            // 重新加载显示
                            showAllCoursesButton.doClick();
                        }
                    });

                    // 将整个行面板添加到课程面板
                    coursePanel.add(courseRow);
                }

                coursePanel.revalidate(); // 重新布局
                coursePanel.repaint(); // 重绘界面
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(window, "读取课程文件失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 创建菜单栏
        JMenuBar menuBar = new JMenuBar();
        window.setJMenuBar(menuBar);

        JMenu menu1 = new JMenu("阿哈！");
        menuBar.add(menu1);
        JMenuItem helloName = new JMenuItem("嘿嘿！");
        menu1.add(helloName);

        helloName.setToolTipText("菜单？");

        helloName.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "芜湖！");
        });

        JMenu menu = new JMenu("退出");
        menuBar.add(menu);
        JMenuItem mntmClose = new JMenuItem("快捷键");

        // 绑定快捷键，ctrl+q直接退出程序
        mntmClose.setMnemonic(KeyEvent.VK_Q);
        mntmClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        mntmClose.setToolTipText("退出软件");

        mntmClose.addActionListener(e -> {
            System.exit(0);
        });

        menu.add(mntmClose);

        // 最后显示window
        window.setVisible(true);
    }

    /**
     * 删除指定课程
     * 
     * @param exePath data.txt 文件路径
     * @param courseLine 要删除的课程内容
     */
    private void removeCourseFromFile(String exePath, String courseLine) {
        // 读取文件并删除指定课程
        try {
            // 读取所有行并过滤掉要删除的课程
            BufferedReader reader = new BufferedReader(new FileReader(exePath, StandardCharsets.UTF_8));
            StringBuilder updatedContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals(courseLine)) {
                    updatedContent.append(line).append("\n");
                }
            }
            reader.close();

            // 写回更新后的内容到文件
            FileWriter writer = new FileWriter(exePath, StandardCharsets.UTF_8);
            writer.write(updatedContent.toString());
            writer.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "删除课程失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
