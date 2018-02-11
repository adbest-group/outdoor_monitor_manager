package com.bt.om.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caiting on 2017/9/4.
 */
public class HttpClientHelper {
    private static PoolingHttpClientConnectionManager poolConnManager = null;
    private int maxTotalPool = 200;
    private int maxConPerRoute = 20;
    private int socketTimeout = 10000;
    private int connectionRequestTimeout = 2000;
    private int connectTimeout = 10000;

    // 代理信息
//    public static String proxyIp = "10.180.162.66";
    public static int proxyPort = 1234;
    public static String proxyIp = null;

    private static HttpClientHelper httpClientHelper = null;

    private HttpClientHelper() {
        init();
    }

    private static synchronized void syncInit() {
        if (httpClientHelper == null) {
            httpClientHelper = new HttpClientHelper();
        }
    }

    public static HttpClientHelper getInstance() {
        if (httpClientHelper == null) {
            syncInit();
        }
        return httpClientHelper;
    }

    public PoolingHttpClientConnectionManager getConnManager() {
        PoolingHttpClientConnectionManager cm = null;
        try {
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
            cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // 将最大连接数增加到200
            cm.setMaxTotal(maxTotalPool);
            // 将每个路由基础的连接增加到20
            cm.setDefaultMaxPerRoute(maxConPerRoute);
        } catch (Exception e) {
            System.out.println("InterfacePhpUtilManager init Exception" + e.toString());
            e.printStackTrace();
        }
        return cm;
    }

    public void init() {
        try {
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
            poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // Increase max total connection to 200
            poolConnManager.setMaxTotal(maxTotalPool);
            // Increase default max connection per route to 20
            poolConnManager.setDefaultMaxPerRoute(maxConPerRoute);
            SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(socketTimeout).build();
            poolConnManager.setDefaultSocketConfig(socketConfig);
        } catch (Exception e) {
            System.out.println("InterfacePhpUtilManager init Exception" + e.toString());
            e.printStackTrace();
        }
    }

    public CloseableHttpClient getConnection() {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
                .setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(poolConnManager).setDefaultRequestConfig(requestConfig);// set proxy
        if (proxyIp != null) {
            org.apache.http.HttpHost proxy = new org.apache.http.HttpHost(proxyIp, Integer.valueOf(proxyPort));
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            httpClientBuilder.setRoutePlanner(routePlanner);
        }

        CloseableHttpClient httpClient = httpClientBuilder.build();
        if (poolConnManager != null && poolConnManager.getTotalStats() != null) {
            System.out.println("now client pool " + poolConnManager.getTotalStats().toString());
        }
        return httpClient;
    }

    /**
     * 发送 GET 请求（HTTP），不带输入数据
     *
     * @param url
     * @return
     */
    public String doGet(String url) {
        return doGet(url, new HashMap<String, String>());
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     *
     * @param url
     * @param params
     * @return
     */
    public String doGet(String url, Map<String, String> params) {
        String apiUrl = url;
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0) {
                param.append("?");
            } else {
                param.append("&");
            }
            param.append(key).append("=").append(params.get(key));
            i++;
        }
        apiUrl += param;
        System.out.println(apiUrl);
        String result = null;
        CloseableHttpClient httpClient = getConnection();
        CloseableHttpResponse response = null;
        HttpGet httpPost = null;
        try {
            httpPost = new HttpGet(apiUrl);
            response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            System.out.println("http request url : " + url + " status : " + status);

            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(response.getEntity(), "UTF-8");
                    Header[] headers = response.getAllHeaders();
                    for(Header header:headers){
                        System.out.println("header:["+header.getName()+"] value : " + response.getAllHeaders());
                    }
                    System.out.println("Request result : " + result);
                }
            }
            EntityUtils.consume(response.getEntity());
            response.close();
            return result;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public byte[] getStaticResourceInputStream(String url) {
        byte[] b = null;
        byte[] tmp = new byte[512];
        InputStream is = null;
        String apiUrl = url;
        StringBuffer param = new StringBuffer();
        int i = 0;

        apiUrl += param;
        System.out.println(apiUrl);
        String result = null;
        CloseableHttpClient httpClient = getConnection();
        CloseableHttpResponse response = null;
        HttpGet httpPost = null;
        try {
            httpPost = new HttpGet(apiUrl);
            response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            System.out.println("http request url : " + url + " status : " + status);

            if (status >= 200 && status < 300) {
                is = response.getEntity().getContent();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int read = 0;
                while((read = is.read(tmp))!=-1){
                    out.write(tmp,0,read);
                }
                b = out.toByteArray();
            }
            EntityUtils.consume(response.getEntity());
            response.close();
            return b;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return b;
    }

    public String doPost(String url) {
        return doPost(url, new HashMap<String, String>());
    }

    /**
     * 发送 POST 请求（HTTP），K-V形式
     *
     * @param url
     *            接口URL
     * @param params
     *            参数map
     * @return
     */
    public String doPost(String url, Map<String, String> params,Header...headers) {
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        if(headers.length>0){
            httpPost.setHeaders(headers);
        }
        CloseableHttpClient httpClient = getConnection();
        CloseableHttpResponse response = null;
        try {
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            System.out.println("http request url : " + url + " status : " + status);
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(response.getEntity(), "UTF-8");
                    System.out.println("Request result : " + result);
                }
            }
            EntityUtils.consume(response.getEntity());
            response.close();
            return result;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
