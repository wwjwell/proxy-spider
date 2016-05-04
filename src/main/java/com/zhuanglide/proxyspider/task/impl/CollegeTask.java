package com.zhuanglide.proxyspider.task.impl;

import com.zhuanglide.proxyspider.db.mapper.College;
import com.zhuanglide.proxyspider.db.mapper.Proxy;
import com.zhuanglide.proxyspider.task.CollegeDBPipeline;
import com.zhuanglide.proxyspider.task.DBPipeline;
import com.zhuanglide.proxyspider.task.ProxyTask;
import org.apache.commons.lang.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wwj on 16.5.3.
 */
public class CollegeTask implements PageProcessor,ProxyTask {
    //抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site;
    private static AtomicInteger integer = new AtomicInteger(232);
    private static ArrayList<String> urls;
//    private static synchronized List<String> duplicatedRemove(List<String> requestURLs) {
//        if (urls.size() > 100) {
//            return null;
//        }
//        List<String> noRetryList = new ArrayList<String>();
//        for (String url : requestURLs) {
//            if (urls.contains(url)) {
//                continue;
//            }
//            urls.add(url);
//            noRetryList.add(url);
//        }
//        return noRetryList;
//    }

    public CollegeTask(){
        urls = new ArrayList<String>();
        site = Site.me().setDomain("http://www.gaokaopai.com")
                .setRetryTimes(3)
                .setCharset("utf-8")
                .setSleepTime(100)
                .addHeader("Refer", "http://www.gaokaopai.com")
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36");
//        for(int i=1;i<256;i++){
        for(int i=1;i<256;i++){
            urls.add("http://www.gaokaopai.com/daxue-0-0-0-0-0-0-0--p-"+i+".html");
        }
    }
    public void process(Page page) {
        final int pageNum = integer.incrementAndGet();
        //定义如何抽取页面信息，并保存下来
//        int pageNum = Integer.valueOf(page.getUrl().get().replace("http://www.gaokaopai.com/daxue-0-0-0-0-0-0-0--p-", "").replace(".html",""));
        List<College> cList = new ArrayList<College>();
        List<Selectable> rows = page.getHtml().xpath("//div[@class=schoolList]//ul/li").nodes();
        int i = 1;
        for (Selectable row : rows) {
            College c = new College();
            c.page = pageNum;

            c.logo = org.apache.commons.lang3.StringUtils.trimToEmpty(row.xpath("//div[@class=pic]//a").$("img", "src").get());
            c.name = row.xpath("//div[@class=con]//div[@class=tit]//h3//a/text()").get();
            c.href = row.xpath("//div[@class=con]//div[@class=tit]//h3//").$("a","href").get();
            List<String> allTag = row.xpath("//div[@class=tag]//").$("img", "alt").all();
            if(null != allTag){
                if (allTag.contains("211")) {
                    c.is211 = 1;
                }
                if(allTag.contains("985")){
                    c.is985 = 1;
                }
            }
            c.num = (c.page-1)*rows.size()+i;
            i++;
            cList.add(c);
        }
        page.putField(IPS, cList);
        if (page.getResultItems().get(IPS) == null) {
            page.setSkip(true);
        }
        // 部分三：从页面发现后续的url地址来抓取
//        pageNum++;
//        final int finalPageNum = pageNum;
//        System.out.println("current page = "+pageNum);
        page.addTargetRequest("http://www.gaokaopai.com/daxue-0-0-0-0-0-0-0--p-"+(pageNum+1)+".html");
    }

    public Site getSite() {
        return site;
    }

    public void run() {
        Spider.create(this).addUrl("http://www.gaokaopai.com/daxue-0-0-0-0-0-0-0--p-233.html").addPipeline(new CollegeDBPipeline()).thread(1).run();
    }
}
