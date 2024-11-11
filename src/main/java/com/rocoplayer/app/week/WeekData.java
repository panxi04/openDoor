package com.rocoplayer.app.week;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WeekData {

    private Date semesterStartDate;
    private Date semesterEndDate;
    private String[][] schedule;  // 用来存储课表数据

    public WeekData() {
        loadSemesterDataFromFile();
    }

    private void loadSemesterDataFromFile() {
        // 获取当前程序所在的路径
        String exePath = getExePath();

        // 通过路径定位到 data.txt 文件
        String filePath = exePath + "/data.txt";
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            // 读取学期起止日期
            String dateLine = reader.readLine();
            if (dateLine != null) {
                String[] dates = dateLine.split(",");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                semesterStartDate = sdf.parse(dates[0]);
                semesterEndDate = sdf.parse(dates[1]);
                System.out.println("学期开始日期: " + semesterStartDate);
                System.out.println("学期结束日期: " + semesterEndDate);
            }

            // 读取课程安排
            List<String[]> scheduleList = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseData = line.split(",");
                if (courseData.length == 5) {
                    scheduleList.add(courseData);
                } else {
                    System.err.println("无效的课程数据行: " + line);
                }
            }
            // 转换为数组形式
            schedule = scheduleList.toArray(new String[0][0]);
            System.out.println("成功读取课程安排，共 " + schedule.length + " 行数据。");

        } catch (IOException | ParseException e) {
            System.err.println("读取学期数据文件时出错: " + e.getMessage());
        }
    }

    private String getExePath() {
        // 获取当前执行文件(.exe)的路径（即工作目录）
        return System.getProperty("user.dir");
    }

    public String getScheduleOutput() {
        StringBuilder result = new StringBuilder();

        // 获取当前日期并显示
        Date currentDate = new Date();
        result.append("当前日期: ").append(currentDate).append("\n");

        // 获取当天是星期几
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        String[] daysOfWeek = {"", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        String today = daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK)];

        result.append("今天是: ").append(today).append("\n");

        // 检查是否成功加载了学期的开始和结束日期
        if (semesterStartDate == null || semesterEndDate == null) {
            result.append("无法加载学期的开始或结束日期，请检查日期格式和代码中的常量设置。\n");
            return result.toString();
        }

        // 获取当前日期并计算当前是学期的第几周
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(semesterStartDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(semesterEndDate);

        // 计算当前周数
        long millisBetween = calendar.getTimeInMillis() - startCal.getTimeInMillis();
        long daysBetween = millisBetween / (1000 * 60 * 60 * 24);
        int weekNumber = (int) (daysBetween / 7) + 1;

        // 判断是否超出学期范围
        if (calendar.after(endCal)) {
            result.append("当前日期已超出学期范围。\n");
        } else {
            result.append("当前是第 ").append(weekNumber).append(" 周。\n");
        }

        // 获取当天和明天的星期几
        int tomorrow = (calendar.get(Calendar.DAY_OF_WEEK) % 7) + 1; // 明天的星期几

        // 星期映射表
        result.append(displaySchedule(schedule, weekNumber, daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK)], "上午", "今天上午的课程安排"));
        result.append(displaySchedule(schedule, weekNumber, daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK)], "下午", "今天下午的课程安排"));
        result.append(displaySchedule(schedule, weekNumber, daysOfWeek[tomorrow], "上午", "明天上午的课程安排"));
        result.append(displaySchedule(schedule, weekNumber, daysOfWeek[tomorrow], "下午", "明天下午的课程安排"));

        return result.toString();
    }

    private String displaySchedule(String[][] schedule, int weekNumber, String dayOfWeek, String period, String title) {
        StringBuilder result = new StringBuilder();

        result.append("\n").append(title).append(":\n");
        result.append(String.format("%-10s %-20s\n", "教室", "课程"));

        boolean hasCourses = false;
        for (String[] entry : schedule) {
            String room = entry[0];
            String day = entry[1];
            String coursePeriod = entry[2];
            String courseName = entry[3];
            String weekRange = entry[4];

            // 解析周次范围
            String[] weeks = weekRange.split("-");
            if (weeks.length != 2) {
                System.err.println("无效的周次范围: " + weekRange);
                continue;  // 跳过不合法的周次范围
            }
            int startWeek = Integer.parseInt(weeks[0]);
            int endWeek = Integer.parseInt(weeks[1]);

            // 判断当前周次是否在课程的周次范围内，并且是指定的日期和时间段
            if (weekNumber >= startWeek && weekNumber <= endWeek && day.equals(dayOfWeek) && coursePeriod.equals(period)) {
                result.append(String.format("%-10s %-20s\n", room, courseName));
                hasCourses = true;
            }
        }

        if (!hasCourses) {
            result.append("无课程安排\n");
        }

        return result.toString();
    }

    public static void main(String[] args) {
        WeekData weekData = new WeekData();
        System.out.println(weekData.getScheduleOutput());
    }
}
