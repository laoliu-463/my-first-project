package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
=======
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 根据时间区间统计营业额
     *
     * @param begin
     * @param end
     * @return
     */
<<<<<<< HEAD
    public TurnoverReportVO getTurnover(LocalDate begin, LocalDate end) {
        log.info("统计营业额：{}，{}", begin, end);

        //存放每天的日期（x轴）
=======
    @Override
    public TurnoverReportVO getTurnover(LocalDate begin, LocalDate end) {
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);

        while (!begin.equals(end)) {
<<<<<<< HEAD
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //存放每天的营业额（y轴）
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            //查询date日期对应的营业额（状态为已完成的订单金额）
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);

=======
            begin = begin.plusDays(1);//日期计算，获得指定日期后1天的日期
            dateList.add(begin);
        }

        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("status", Orders.COMPLETED);
            map.put("begin", beginTime);
            map.put("end", endTime);

//            根据动态条件统计营业额
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
            Double turnover = orderMapper.sumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }

<<<<<<< HEAD
        //封装返回结果
=======
        //数据封装
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 根据时间区间统计用户数量
     *
     * @param begin
     * @param end
     * @return
     */
<<<<<<< HEAD
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        log.info("统计用户数量：{}，{}", begin, end);

        //存放每天的日期（x轴）
        List<LocalDate> dateList = new ArrayList<>();
=======
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        ArrayList<LocalDate> dateList = new ArrayList<>();
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
        dateList.add(begin);

        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

<<<<<<< HEAD
        //存放每天的新增用户数量（y轴）
        List<Integer> newUserList = new ArrayList<>();
        //存放每天的总用户数量（y轴）
        List<Integer> totalUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            //查询date日期对应的新增用户数量
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);

            Integer newUser = userMapper.countByMap(map);
            newUserList.add(newUser);

            //查询date日期对应的总用户数量
            map.put("end", endTime);
            Integer totalUser = userMapper.countByMap(map);
            totalUserList.add(totalUser);
        }

        //封装返回结果
=======
        ArrayList<Integer> newUserList = new ArrayList<>();
        ArrayList<Integer> totalUserList = new ArrayList<>();

        dateList.forEach(date -> {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Integer newUser = getUserCount(beginTime, endTime);
            Integer totalUser = getUserCount(null, endTime);

            newUserList.add(newUser);
            totalUserList.add(totalUser);
        });

>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .build();
    }

    /**
     * 根据时间区间统计订单数量
     *
     * @param begin
     * @param end
     * @return
     */
<<<<<<< HEAD
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        log.info("统计订单数量：{}，{}", begin, end);

        //存放每天的日期（x轴）
        List<LocalDate> dateList = new ArrayList<>();
=======
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        ArrayList<LocalDate> dateList = new ArrayList<>();
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
        dateList.add(begin);

        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

<<<<<<< HEAD
        //存放每天的订单总数
        List<Integer> orderCountList = new ArrayList<>();
        //存放每天的有效订单数
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date : dateList) {
            //查询date日期对应的订单总数
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);

            Integer orderCount = orderMapper.countByMap(map);
            orderCountList.add(orderCount);

            //查询date日期对应的有效订单数
            map.put("status", Orders.COMPLETED);
            Integer validOrderCount = orderMapper.countByMap(map);
            validOrderCountList.add(validOrderCount);
        }

        //计算时间区间内的总订单数
        Integer totalOrderCount = orderCountList.stream().reduce(0, Integer::sum);

        //计算时间区间内的总有效订单数
        Integer validOrderCount = validOrderCountList.stream().reduce(0, Integer::sum);

        //订单完成率
=======
//        每天订单总数集合
        ArrayList<Integer> orderCountList = new ArrayList<>();

//        媒体有效订单数集合
        ArrayList<Integer> validOrderCountList = new ArrayList<>();

        dateList.forEach(date->{
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

//            查询每天总数
            Integer orderCount = getOrderCount(beginTime, endTime, null);

//            查询每天有效订单数
            Integer validOrderCount = getOrderCount(beginTime, endTime, Orders.COMPLETED);

            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        });

//        时间区间内的总订单数
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();

//        区间时间内有效订单数
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

//        订单完成率
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }

<<<<<<< HEAD
=======

>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 查询指定时间区间内的销量排名top10
     *
     * @param begin
     * @param end
     * @return
     */
<<<<<<< HEAD
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        log.info("查询销量排名top10：{}，{}", begin, end);

        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);
        //将List<GoodsSalesDTO>转换为两个List集合，一个存放商品名称，一个存放销量
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(names, ","))
                .numberList(StringUtils.join(numbers, ","))
                .build();
=======
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.getSalesTop10(beginTime, endTime);

        String nameList = StringUtils.join(goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList()), ",");
        String numberList = StringUtils.join(goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList()), ",");
        return SalesTop10ReportVO.builder().nameList(nameList).numberList(numberList).build();
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
    }

    /**
     * 导出近30天的运营数据报表
<<<<<<< HEAD
     *
     * @param response
     **/
    @Override
    public void exportBusinessData(HttpServletResponse response) {
        //1.查询概览数据
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);

        //营业额
        TurnoverReportVO turnoverReportVO = getTurnover(begin, end);
        Double turnover = 0.0;
        String turnoverStr = turnoverReportVO.getTurnoverList();
        if (StringUtils.isNotEmpty(turnoverStr)) {
            String[] split = turnoverStr.split(",");
            for (String s : split) {
                turnover += Double.parseDouble(s);
            }
        }

        //用户总数
        Map map = new HashMap();
        map.put("end", LocalDateTime.of(end, LocalTime.MAX));
        Integer totalUserCount = userMapper.countByMap(map);

        //新增用户数
        map.put("begin", LocalDateTime.of(begin, LocalTime.MIN));
        Integer newUserCount = userMapper.countByMap(map);

        //订单总数
        map = new HashMap();
        map.put("begin", LocalDateTime.of(begin, LocalTime.MIN));
        map.put("end", LocalDateTime.of(end, LocalTime.MAX));
        Integer totalOrderCount = orderMapper.countByMap(map);

        //有效订单数
        map.put("status", Orders.COMPLETED);
        Integer validOrderCount = orderMapper.countByMap(map);

        //订单完成率
        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }

        //平均客单价
        Double unitPrice = 0.0;
        if (validOrderCount != 0) {
            unitPrice = turnover / validOrderCount;
        }

        //30天数据
        BusinessDataVO businessDataVO = BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUserCount)
                .build();

        //2.通过POI将数据写入Excel文件
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = excel.getSheet("Sheet1");

            //填充时间数据
            sheet.getRow(2).getCell(1).setCellValue("时间：" + begin + "至" + end);

            //填充概览数据
            sheet.getRow(4).getCell(1).setCellValue(businessDataVO.getTurnover());
            sheet.getRow(4).getCell(3).setCellValue(businessDataVO.getOrderCompletionRate());
            sheet.getRow(4).getCell(5).setCellValue(businessDataVO.getNewUsers());
            sheet.getRow(4).getCell(7).setCellValue(businessDataVO.getValidOrderCount());
            sheet.getRow(4).getCell(9).setCellValue(businessDataVO.getUnitPrice());

            //填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i);
                //查询某一天的营业数据
                TurnoverReportVO turnoverReport = getTurnover(date, date);
                UserReportVO userReport = getUserStatistics(date, date);
                OrderReportVO orderReport = getOrderStatistics(date, date);

                XSSFRow row = sheet.getRow(7 + i);
                row.getCell(0).setCellValue(date.toString());
                row.getCell(1).setCellValue(turnoverReport.getTurnoverList());
                row.getCell(2).setCellValue(userReport.getNewUserList());
                row.getCell(3).setCellValue(userReport.getTotalUserList());
                row.getCell(4).setCellValue(orderReport.getOrderCountList());
                row.getCell(5).setCellValue(orderReport.getValidOrderCountList());
                row.getCell(6).setCellValue(orderReport.getOrderCompletionRate());
            }

            //3.通过输出流将Excel文件下载到客户端浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=运营数据报表.xlsx");
            excel.write(outputStream);
            outputStream.close();
            excel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
=======
     * @param response
     */
    @Override
    public void exportBusinessData(HttpServletResponse response) {
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        //查询概览运营数据，提供给Excel模板文件
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(begin,LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            //基于提供好的模板文件创建一个新的Excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            //获得Excel文件中的一个Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            sheet.getRow(1).getCell(1).setCellValue(begin + "至" + end);
            //获得第4行
            XSSFRow row = sheet.getRow(3);
            //获取单元格
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());
            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i);
                //准备明细数据
                businessData = workspaceService.getBusinessData(LocalDateTime.of(date,LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }
            //通过输出流将文件下载到客户端浏览器中
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);
            //关闭资源
            out.flush();
            out.close();
            excel.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 根据时间区间统计指定状态的订单数量
     *
     * @param beginTime
     * @param endTime
     * @param status
     * @return
     */
    private Integer getOrderCount(LocalDateTime beginTime, LocalDateTime endTime, Integer status) {
        Map map = new HashMap();
        map.put("status", status);
        map.put("begin", beginTime);
        map.put("end", endTime);
        return orderMapper.countByMap(map);
    }

    /**
     * 根据时间区间统计用户数量
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    private Integer getUserCount(LocalDateTime beginTime, LocalDateTime endTime) {
        Map map = new HashMap();
        map.put("begin", beginTime);
        map.put("end", endTime);
        return userMapper.countByMap(map);
    }
}
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
