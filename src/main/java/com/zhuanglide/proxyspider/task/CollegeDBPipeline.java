package com.zhuanglide.proxyspider.task;

import com.zhuanglide.proxyspider.db.DBUtils;
import com.zhuanglide.proxyspider.db.mapper.College;
import com.zhuanglide.proxyspider.db.mapper.CollegeMapper;
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
public class CollegeDBPipeline implements Pipeline {

    public void process(ResultItems resultItems, Task task) {
        if (null != resultItems) {
            DBUtils dbUtils = DBUtils.instance();
            SqlSession session = dbUtils.getSqlSession();
            CollegeMapper mapper = dbUtils.getMapper(CollegeMapper.class, session);
            List<College> proxyList = (List<College>) resultItems.get(ProxyTask.IPS);
            if (null != proxyList) {
                for (College college : proxyList) {
                    mapper.insert(college);
                    System.out.println(college.getName() + " add db");
                }
            }
            session.commit();
            dbUtils.closeSqlSession(session);
        }
    }
}
