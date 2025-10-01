package com.sky.controller.admin;


import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

<<<<<<< HEAD
import jakarta.servlet.http.HttpServletResponse;
=======
import javax.servlet.http.HttpServletResponse;
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
import java.time.LocalDate;

/**
 * 报表
 */
@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "统计报表相关接口")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额数据统计")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end) {
        return Result.success(reportService.getTurnover(begin, end));
    }

    /**
<<<<<<< HEAD
     * 用户统计
=======
     * 用户数据统计
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/userStatistics")
<<<<<<< HEAD
    @ApiOperation("用户统计")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end) {
        return Result.success(reportService.getUserStatistics(begin, end));
    }

    /**
     * 订单统计
=======
    @ApiOperation("用户数据统计")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){

        return Result.success(reportService.getUserStatistics(begin,end));
    }

    /**
     * 订单数据统计
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/ordersStatistics")
<<<<<<< HEAD
    @ApiOperation("订单统计")
    public Result<OrderReportVO> ordersStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end) {
        return Result.success(reportService.getOrderStatistics(begin, end));
    }

    /**
     * 销量排名top10
=======
    @ApiOperation("用户数据统计")
    public Result<OrderReportVO> orderStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end){

        return Result.success(reportService.getOrderStatistics(begin,end));
    }

    /**
     * 销量排名统计
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/top10")
<<<<<<< HEAD
    @ApiOperation("销量排名top10")
    public Result<SalesTop10ReportVO> top10(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end) {
        return Result.success(reportService.getSalesTop10(begin, end));
=======
    @ApiOperation("销量排名统计")
    public Result<SalesTop10ReportVO> top10(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        return Result.success(reportService.getSalesTop10(begin,end));
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
    }

    /**
     * 导出运营数据报表
     * @param response
<<<<<<< HEAD
     * @return
     */
    @GetMapping("/export")
    @ApiOperation("导出运营数据报表")
    public void export(HttpServletResponse response) {
        reportService.exportBusinessData(response);
    }
}
=======
     */
    @GetMapping("/export")
    @ApiOperation("导出运营数据报表")
    public void export(HttpServletResponse response){
        reportService.exportBusinessData(response);
    }
}
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
