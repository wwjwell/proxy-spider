package com.zhuanglide.proxyspider.task.impl;

import com.zhuanglide.proxyspider.db.mapper.Proxy;
import com.zhuanglide.proxyspider.task.DBPipeline;
import com.zhuanglide.proxyspider.task.Task;
import org.apache.commons.lang.StringUtils;
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
 * http://www.xicidaili.com/代理
 */
public class XiciTask implements PageProcessor,Task {
    //抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site;
    private static Set<String> urls;

    private static synchronized List<String> duplicatedRemove(List<String> requestURLs) {
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


    public XiciTask() {
        urls = new HashSet<String>();
        site = Site.me().setDomain("www.xicidaili.com")
                .setRetryTimes(3)
                .setCharset("utf-8")
                .setSleepTime(5000)
                .addHeader("Refer", "http://www.xicidaili.com")
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36");
    }

    public void process(Page page) {
        //定义如何抽取页面信息，并保存下来
        List<Proxy> ips = new ArrayList<Proxy>();
        List<Selectable> rows = page.getHtml().xpath("//table[@id=ip_list]//tr").nodes();
        for (Selectable row : rows) {
            String ip = row.xpath("//td[2]/text()").regex("\\d+\\.\\d+\\.\\d+\\.\\d+").toString();
            String port;
            if (StringUtils.isBlank(ip)) {
                ip = row.xpath("//td[3]/text()").regex("\\d+\\.\\d+\\.\\d+\\.\\d+").toString();
                port = row.xpath("//td[4]/text()").regex("\\d+").toString();

            } else {
                port = row.xpath("//td[3]/text()").regex("\\d+").toString();
            }
            if (StringUtils.isNotBlank(ip)) {
                Proxy proxy = new Proxy();
                proxy.setHost(ip);
                proxy.setPort(Integer.valueOf(port));
                ips.add(proxy);
            }

        }
        page.putField("ips", ips);
        if (page.getResultItems().get("ips") == null) {
            page.setSkip(true);
        }
        // 部分三：从页面发现后续的url地址来抓取
        List<String> requestLists = duplicatedRemove(page.getHtml().links().regex("(http://www.xicidaili.com/\\w{2}/?)").all());
        page.addTargetRequests(requestLists);

    }

    public Site getSite() {
        return site;
    }


    public void run() {
        Spider.create(this).addUrl("http://www.xicidaili.com/").addPipeline(new DBPipeline()).thread(1).run();

    }

    public static void main(String[] args) {
        new XiciTask().run();
    }
}
