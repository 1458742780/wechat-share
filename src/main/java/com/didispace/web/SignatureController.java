package com.didispace.web;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by YangDi on 2017/8/9.
 */
@RestController
public class SignatureController {

    private static final Logger logger = LoggerFactory.getLogger(SignatureController.class);
    private static Map dbTokenMap = new HashMap<>();//在分布式系统中不能保存在java运行内存中
    private static Map dbTicketMap = new HashMap<>();

    public static String getAccessToken( String AppId, String secret) {


        //String access_token = "";
        String access_token = (String) dbTokenMap.get("access_token");
        String create_time = (String)dbTokenMap.get("create_time") ;

        if(!StringUtils.isEmpty(create_time)){
            long _time = Long.parseLong(create_time);
            if((System.currentTimeMillis()-_time)/1000 < 7000){//有效
                logger.error("不是一次获取token！！！！！！！！！！");
                return  access_token;
            }
        }



        String grant_type = "client_credential";//获取access_token填写client_credential


        //这个url链接地址和参数皆不能变
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type="+grant_type+"&appid="+AppId+"&secret="+secret;
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.parseObject(message);
            System.out.println("获取JSON字符串："+demoJson);
            access_token = demoJson.getString("access_token");
            is.close();
            dbTokenMap.put("create_time",System.currentTimeMillis()+"");
            dbTokenMap.put("access_token",access_token);
        } catch (Exception e) {
            // e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return access_token;
    }
    public static String getTicket(String access_token) {

        String ticket = (String) dbTicketMap.get("ticket");
        String create_time = (String)dbTicketMap.get("create_time") ;
        if(!StringUtils.isEmpty(create_time)){
            long _time = Long.parseLong(create_time);
            if((System.currentTimeMillis()-_time)/1000 < 7000){//有效
                logger.error("不是一次获取ticket！！！！！！！！！！");
                return  ticket;
            }
        }
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ access_token +"&type=jsapi";//这个url链接和参数不能变
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.parseObject(message);
            System.out.println("获取TicketJSON字符串："+demoJson);
            ticket = demoJson.getString("ticket");
            is.close();
            dbTicketMap.put("create_time",System.currentTimeMillis()+"");
            dbTicketMap.put("ticket",ticket);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return ticket;
    }

    public static String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }



    @RequestMapping("/queueGame/getSignature/csjdw")
    public Object getSignature(HttpServletRequest request) {
        Map map = new HashMap<>();

         String appId="appId*******";//第三方用户唯一凭证,YangDi微信测试号
         String secret="d32a0d2396d36682****";//第三方用户唯一凭证密钥，即appsecret

       

        //1、获取AccessToken
        String accessToken = getAccessToken(appId,secret);

        //2、获取Ticket
        String jsapi_ticket = getTicket(accessToken);

        //3、时间戳和随机字符串
        String noncestr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);//随机字符串
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳
        logger.error("appId:"+appId+"\nsecret:"+secret+"\naccessToken:"+accessToken+"\njsapi_ticket:"+jsapi_ticket+"\n时间戳："+timestamp+"\n随机字符串："+noncestr);


        //4、获取url
        //String url="http://172.25.2.101:8086";
        String url=request.getParameter("targetUrl");


        //5、将参数排序并拼接字符串
        String str = "jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;

        //6、将字符串进行sha1加密
        String signature =SHA1(str);
        logger.error("参数："+str+"\n签名："+signature);

        map.put("timestamp",timestamp);
        map.put("noncestr",noncestr);
        map.put("signature",signature);

        return map;
    }
}
