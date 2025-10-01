package com.sky.controller.notify;

<<<<<<< HEAD
=======
import com.alibaba.druid.support.json.JSONUtils;
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.properties.WeChatProperties;
import com.sky.service.OrderService;
<<<<<<< HEAD
import com.sky.utils.WeChatPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
=======
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

<<<<<<< HEAD
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
=======
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291

/**
 * 支付回调相关接口
 */
@RestController
@RequestMapping("/notify")
<<<<<<< HEAD
@Api(tags = "支付回调相关接口")
=======
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
@Slf4j
public class PayNotifyController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WeChatProperties weChatProperties;

<<<<<<< HEAD
    @Autowired
    private WeChatPayUtil weChatPayUtil;
=======
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291

    /**
     * 支付成功回调
     *
     * @param request
<<<<<<< HEAD
     * @param response
     */
    @RequestMapping("/paySuccess")
    @ApiOperation("支付成功回调")
    public void paySuccessNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
=======
     */
    @RequestMapping("/paySuccess")
    public void paySuccessNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
        //读取数据
        String body = readData(request);
        log.info("支付成功回调：{}", body);

<<<<<<< HEAD
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
=======
        //数据解密
        String plainText = decryptData(body);
        log.info("解密后的文本：{}", plainText);

        JSONObject jsonObject = JSON.parseObject(plainText);
        String outTradeNo = jsonObject.getString("out_trade_no");//商户平台订单号
        String transactionId = jsonObject.getString("transaction_id");//微信支付交易号

        log.info("商户平台订单号：{}", outTradeNo);
        log.info("微信支付交易号：{}", transactionId);

        //业务处理，修改订单状态、来单提醒
        orderService.paySuccess(outTradeNo);
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291

        //给微信响应
        responseToWeixin(response);
    }

    /**
     * 读取数据
     *
     * @param request
     * @return
<<<<<<< HEAD
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
=======
     * @throws Exception
     */
    private String readData(HttpServletRequest request) throws Exception {
        BufferedReader reader = request.getReader();
        StringBuilder result = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (result.length() > 0) {
                result.append("\n");
            }
            result.append(line);
        }
        return result.toString();
    }

    /**
     * 数据解密
     *
     * @param body
     * @return
     * @throws Exception
     */
    private String decryptData(String body) throws Exception {
        JSONObject resultObject = JSON.parseObject(body);
        JSONObject resource = resultObject.getJSONObject("resource");
        String ciphertext = resource.getString("ciphertext");
        String nonce = resource.getString("nonce");
        String associatedData = resource.getString("associated_data");

        AesUtil aesUtil = new AesUtil(weChatProperties.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        //密文解密
        String plainText = aesUtil.decryptToString(associatedData.getBytes(StandardCharsets.UTF_8),
                nonce.getBytes(StandardCharsets.UTF_8),
                ciphertext);

        return plainText;
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
    }

    /**
     * 给微信响应
     * @param response
     */
<<<<<<< HEAD
    private void responseToWeixin(HttpServletResponse response) throws IOException {
=======
    private void responseToWeixin(HttpServletResponse response) throws Exception{
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
        response.setStatus(200);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("code", "SUCCESS");
        map.put("message", "SUCCESS");
<<<<<<< HEAD
        response.setHeader("Content-type", "application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }
}
=======
        response.setHeader("Content-type", ContentType.APPLICATION_JSON.toString());
        response.getOutputStream().write(JSONUtils.toJSONString(map).getBytes(StandardCharsets.UTF_8));
        response.flushBuffer();
    }
}
>>>>>>> fa207713eb5075be3659e1f3e3bf93b9d9dc8291
