package com.zhuanglide.proxyspider.task;

import com.zhuanglide.proxyspider.db.DBUtils;
import com.zhuanglide.proxyspider.db.mapper.Proxy;
import com.zhuanglide.proxyspider.db.mapper.ProxyMapper;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * Created by wwj on 16.1.13.
 */
public class DBPipeline implements Pipeline {

    public void process(ResultItems resultItems, Task task) {
        if (null != resultItems) {
            DBUtils dbUtils = DBUtils.instance();
            SqlSession session = dbUtils.getSqlSession();
            ProxyMapper mapper = dbUtils.getMapper(ProxyMapper.class, session);
            List<Proxy> proxyList = (List<Proxy>) resultItems.get("ips");
            if (null != proxyList) {
                for (Proxy proxy : proxyList) {
                    mapper.insert(proxy);
                    System.out.println(proxy.getHost() +":" + proxy.getPort() +" db");
                }
            }
            session.commit();
            dbUtils.closeSqlSession();
        }
    }
}
