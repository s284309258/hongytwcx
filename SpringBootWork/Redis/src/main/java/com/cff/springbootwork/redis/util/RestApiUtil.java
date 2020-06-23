package com.cff.springbootwork.redis.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class RestApiUtil {

    private static Logger logger = LogManager.getLogger(RestApiUtil.class);


    /**
     * 配置忽略SSL认证
     *
     * @param clientBuilder
     */
    public static void configureHttpClient(HttpClientBuilder clientBuilder) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {
                return true;
            }
        }).build();
        //NoopHostNameVerifer 接受任何有效的SSL会话来匹配目标主机
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        clientBuilder.setSSLSocketFactory(sslsf);
    }

    public String post(String url, JSONObject query, String token) throws Exception {

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        configureHttpClient(httpClientBuilder);

        StringEntity requestEntity = new StringEntity(query.toJSONString(), "utf-8");
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("content-type", "application/json;charset=UTF-8");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("X-Nideshop-Token",token);
        httpPost.setEntity(requestEntity);

        HttpClient httpClient = httpClientBuilder.build();
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream inputStream = httpEntity.getContent();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String s;
        StringBuilder stringBuilder = new StringBuilder();
        while ((s = bufferedReader.readLine()) != null) {
            stringBuilder.append(s);
        }
        logger.info("RestApiUtil post:"+url);
        logger.info("RestApiUtil post output:"+stringBuilder);
        return stringBuilder.toString();














//        URL restURL = new URL(url);
//        /*
//         * 此处的urlConnection对象实际上是根据URL的请求协议(此处是http)生成的URLConnection类 的子类HttpURLConnection
//         */
//        HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
//        //请求方式
//        conn.setRequestMethod("POST");
//        //设置是否从httpUrlConnection读入，默认情况下是true; httpUrlConnection.setDoInput(true);
//        conn.setDoInput(true);
//        conn.setDoOutput(true);
//        //allowUserInteraction 如果为 true，则在允许用户交互（例如弹出一个验证对话框）的上下文中对此 URL 进行检查。
//        conn.setAllowUserInteraction(false);
//        conn.setRequestProperty("X-Nideshop-Token",token);
//
//        PrintWriter out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"));
//        // 发送请求参数
//        out.print(query);
//        // flush输出流的缓冲
//        out.flush();
//        // ps.close();
//
//        BufferedReader bReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//        String line, resultStr = "";
//
//        while (null != (line = bReader.readLine())) {
//            resultStr += line;
//        }
//        logger.info("RestApiUtil post:"+url);
//        bReader.close();
//        conn.disconnect();
//        logger.info("RestApiUtil post output:"+resultStr);
//        return resultStr;
    }
}
