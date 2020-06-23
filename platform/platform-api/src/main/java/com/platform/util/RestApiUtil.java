package com.platform.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestApiUtil {
    public String post(String url,String query) throws Exception {
        URL restURL = new URL(url);
        /*
         * 此处的urlConnection对象实际上是根据URL的请求协议(此处是http)生成的URLConnection类 的子类HttpURLConnection
         */
        HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
        //请求方式
        conn.setRequestMethod("POST");
        //设置是否从httpUrlConnection读入，默认情况下是true; httpUrlConnection.setDoInput(true);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        //allowUserInteraction 如果为 true，则在允许用户交互（例如弹出一个验证对话框）的上下文中对此 URL 进行检查。
        conn.setAllowUserInteraction(false);
        //conn.setRequestProperty("X-Nideshop-Token","cz12kl7az80hyjrg10h097dix65dsdx8");

        PrintWriter out = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        out.print(query);
        // flush输出流的缓冲
        out.flush();
        // ps.close();

        BufferedReader bReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line, resultStr = "";

        while (null != (line = bReader.readLine())) {
            resultStr += line;
        }
        System.out.println("restApiUtil:" + resultStr);
        bReader.close();
        conn.disconnect();
        return resultStr;
    }
}
