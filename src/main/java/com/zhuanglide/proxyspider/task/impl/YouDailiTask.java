package com.zhuanglide.proxyspider.task.impl;

import com.zhuanglide.proxyspider.db.mapper.Proxy;
import com.zhuanglide.proxyspider.task.DBPipeline;
import com.zhuanglide.proxyspider.task.ProxyTask;
import com.zhuanglide.proxyspider.utils.RegexUtils;
import org.apache.commons.lang3.math.NumberUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wwj on 16.1.13.
 * http://http://www.youdaili.net//代理
 */
public class YouDailiTask implements PageProcessor,ProxyTask {
    //抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site;
    private static Set<String> urls;

    private static synchronized List<String> duplicatedRemove(List<String> requestURLs) {
        if (urls.size() > 100) {
            return null;
        }
        List<String> noRetryList = new ArrayList<String>();
        for (String url : requestURLs) {
            if (urls.contains(url)) {
                continue;
            }
            urls.add(url);
            noRetryList.add(url);
        }
        return noRetryList;
    }


    public YouDailiTask() {
        urls = new HashSet<String>();
        site = Site.me().setDomain("www.youdaili.net/")
                .setRetryTimes(3)
                .setCharset("utf-8")
                .setSleepTime(5000)
                .addHeader("Refer", "http://www.youdaili.net")
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36");
    }

    public void process(Page page) {
        //定义如何抽取页面信息，并保存下来
        List<Proxy> proxies = new ArrayList<Proxy>();
        List<Selectable> rows = page.getHtml().xpath("//div[@class=cont_font]//p/text()").nodes();
        for (Selectable row : rows) {
            String[] lines = row.toString().split(" ");
            if(null != lines && lines.length>0){
                for (String line : lines) {
                    Proxy proxy = new Proxy();
                    String[] s1 = line.split(":");
                    if (s1.length< 2 || !RegexUtils.isIpv4(s1[0])) {
                        continue;
                    }
                    String[] s2 = s1[1].split("@");
                    if(s2.length<2 || NumberUtils.toInt(s2[0],0)<1){
                        continue;
                    }
                    proxy.setHost(s1[0]);
                    proxy.setPort(NumberUtils.toInt(s2[0]));
                    proxy.setRemark(s2[1]);
                    proxies.add(proxy);
                }
                if(proxies.size()>0) {
                    page.putField(IPS, proxies);
                }
            }



        }
        if (page.getResultItems().get(IPS) == null) {
            page.setSkip(true);
        }
        // 部分三：从页面发现后续的url地址来抓取
        List<String> requestLists = duplicatedRemove(page.getHtml().links().regex("(http://www.youdaili.net/Daili/http/\\w*\\.html)").all());
        if(requestLists.size()>0)
            page.addTargetRequests(requestLists);

    }

    public Site getSite() {
        return site;
    }


    public void run() {
        Spider.create(this).addUrl("http://www.youdaili.net/Daili/http/").addPipeline(new DBPipeline()).thread(1).run();

    }

    public static void main(String[] args) {
        new YouDailiTask().run();
    }
}
