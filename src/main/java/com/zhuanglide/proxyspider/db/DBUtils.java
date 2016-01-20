package com.zhuanglide.proxyspider.db;

import com.zhuanglide.proxyspider.db.mapper.Proxy;
import com.zhuanglide.proxyspider.db.mapper.ProxyMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by wwj on 16.1.13.
 */
public class DBUtils {
    private static DBUtils utils;

    public SqlSessionFactory sessionFactory;
    private DBUtils(){
    }

    public static DBUtils instance() {
        if(null == utils){
            synchronized (DBUtils.class){
                if (null == utils) {
                    utils = new DBUtils();
                    String resource = "mybatis-config.xml";
                    Reader reader = null;
                    try {
                        reader = Resources.getResourceAsReader(resource);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    utils.sessionFactory = new SqlSessionFactoryBuilder().build(reader);
                    utils.init();
                }
            }
        }
        return utils;
    }

    private void init(){
        Configuration config = sessionFactory.getConfiguration();
        config.addMapper(ProxyMapper.class);
    }

    public SqlSession getSqlSession(){
        return sessionFactory.openSession();
    }

    public void closeSqlSession(SqlSession sqlSession){
        if (null != sqlSession) {
            sqlSession.close();
        }
    }


    public <T> T getMapper(Class<T> clazz,SqlSession sqlSession) {
        return sqlSession.getMapper(clazz);
    }


    public static void main(String[] args) {
        DBUtils dbUtils = DBUtils.instance();
        SqlSession session = dbUtils.getSqlSession();
        ProxyMapper proxyMapper = dbUtils.getMapper(ProxyMapper.class,session);
        Proxy proxy = new Proxy();
        proxy.setHost("127.0.0.1");
        proxy.setPort(80);
        proxyMapper.insert(proxy);
        session.commit();
        dbUtils.closeSqlSession(session);
    }
}
