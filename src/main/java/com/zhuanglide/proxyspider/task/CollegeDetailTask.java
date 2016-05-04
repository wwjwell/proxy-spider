package com.zhuanglide.proxyspider.task;

import com.zhuanglide.proxyspider.db.DBUtils;
import com.zhuanglide.proxyspider.db.mapper.College;
import com.zhuanglide.proxyspider.db.mapper.CollegeMapper;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wwj on 16.5.3.
 */
public class CollegeDetailTask implements PageProcessor,ProxyTask {
    //抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site;
    private static Map<String,College> collegeMap = new HashMap<String, College>();
    private static LinkedList<String> hrefs = new LinkedList<String>();
    private DBUtils dbUtils = DBUtils.instance();
    SqlSession session = dbUtils.getSqlSession();
    CollegeMapper mapper = dbUtils.getMapper(CollegeMapper.class, session);

    public CollegeDetailTask(){
        site = Site.me().setDomain("http://www.gaokaopai.com")
                .setRetryTimes(3)
                .setCharset("utf-8")
                .setSleepTime(100)
                .addHeader("Refer", "http://www.gaokaopai.com")
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36");
        List<College> colleges = mapper.query();
        for (College college : colleges) {
            collegeMap.put(college.getHref(), college);
            hrefs.add(college.getHref());
        }



    }
    public void process(Page page) {
        //定义如何抽取页面信息，并保存下来
//        int pageNum = Integer.valueOf(page.getUrl().get().replace("http://www.gaokaopai.com/daxue-0-0-0-0-0-0-0--p-", "").replace(".html",""));
        List<College> cList = new ArrayList<College>();


        String city = page.getHtml().xpath("//div[@class=infos]//ul//li/text()").get().replaceAll("<label>*<label>","").trim();
        College college = collegeMap.get(page.getUrl().get());
        mapper.update(college.id, city);
        session.commit();
        System.out.println("update college="+ college.name +",college.city=" + city);
        // 部分三：从页面发现后续的url地址来抓取
//        pageNum++;
//        final int finalPageNum = pageNum;
//        System.out.println("current page = "+pageNum);
        String href = hrefs.pollFirst();
        if(href!=null && href.length()>0)
            page.addTargetRequest(href);
    }

    public Site getSite() {
        return site;
    }

    public void run() {
        Spider.create(this).addUrl(hrefs.pollFirst()).addPipeline(new CollegeDBPipeline()).thread(1).run();
    }
}
