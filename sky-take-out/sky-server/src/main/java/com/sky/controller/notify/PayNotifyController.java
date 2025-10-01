package com.sky.controller.notify;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.properties.WeChatProperties;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付回调相关接口
 */
@RestController
@RequestMapping("/notify")
@Api(tags = "支付回调相关接口")
@Slf4j
public class PayNotifyController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private WeChatPayUtil weChatPayUtil;

    /**
     * 支付成功回调
     *
     * @param request
     * @param response
     */
    @RequestMapping("/paySuccess")
    @ApiOperation("支付成功回调")
    public void paySuccessNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //读取数据
        String body = readData(request);
        log.info("支付成功回调：{}", body);

        try {
            String plainText = weChatPayUtil.parseOrderNotifyData(body);
            log.info("解密后的文本：{}", plainText);

            JSONObject jsonObject = JSON.parseObject(plainText);
            String outTradeNo = jsonObject.getString("out_trade_no");//商户平台订单号
            String transactionId = jsonObject.getString("transaction_id");//微信支付交易号

            log.info("商户平台订单号：{}，微信支付交易号：{}", outTradeNo, transactionId);

            //业务处理，修改订单状态
            orderService.paySuccess(outTradeNo);
        } catch (Exception e) {
            log.error("处理微信支付回调异常", e);
        }

        //给微信响应
        responseToWeixin(response);
    }

    /**
     * 读取数据
     *
     * @param request
     * @return
     */
    private String readData(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            char[] buff = new char[1024];
            int len;
            while ((len = reader.read(buff)) != -1) {
                sb.append(buff, 0, len);
            }
        } catch (IOException e) {
            throw e;
        }
        return sb.toString();
    }

    /**
     * 给微信响应
     * @param response
     */
    private void responseToWeixin(HttpServletResponse response) throws IOException {
        response.setStatus(200);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("code", "SUCCESS");
        map.put("message", "SUCCESS");
        response.setHeader("Content-type", "application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }
}