package com.zhuanglide.proxyspider.utils;

import com.zhuanglide.proxyspider.db.DBUtils;
import com.zhuanglide.proxyspider.db.mapper.Proxy;
import com.zhuanglide.proxyspider.db.mapper.ProxyMapper;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by wwj on 16.1.20.
 */
public class ProxyCheck{
    private static String url = "http://www.baidu.com";

    public static boolean checkProxy(Proxy proxy) {
        boolean result = false;
        CloseableHttpClient  client = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(10000).build();//设置请求和传输超时时间

        HttpHost host = new HttpHost(proxy.getHost(), proxy.getPort());
        HttpGet get = new HttpGet();
        get.setConfig(requestConfig);

        get.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        get.addHeader("Connection", "Keep-Alive");
        get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
        get.addHeader("Cookie", "");
        get.addHeader("Referer", url);
        try {
            HttpResponse response = client.execute(host, get);
            if(null != response){
                if (200 == response.getStatusLine().getStatusCode()) {
                    return true;
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
        return result;
    }


    public static void run(Proxy proxy) {
        DBUtils db = DBUtils.instance();
        SqlSession session = db.getSqlSession();
        ProxyMapper mapper = db.getMapper(ProxyMapper.class, session);
        if(!ProxyCheck.checkProxy(proxy)){
            mapper.delete(proxy.getId());
            session.commit();
            System.err.println(proxy.getHost()+":"+proxy.getPort()+" fail!");

        }else {
            System.out.println(proxy.getHost()+":"+proxy.getPort()+" OK!");
        }
        db.closeSqlSession(session);
    }

    public static void check() {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5,20,2, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
        DBUtils db = DBUtils.instance();
        SqlSession session = db.getSqlSession();
        ProxyMapper mapper = db.getMapper(ProxyMapper.class, session);
        List<Proxy> proxies = mapper.selectAll();
        db.closeSqlSession(session);

        for (final Proxy proxy : proxies) {
            threadPool.execute(new Runnable() {
                public void run() {
                    ProxyCheck.run(proxy);
                }
            });
        }

        threadPool.shutdown();
    }
}
