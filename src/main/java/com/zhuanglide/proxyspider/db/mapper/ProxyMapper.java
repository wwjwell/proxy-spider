package com.zhuanglide.proxyspider.db.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by wwj on 16.1.20.
 */
public interface ProxyMapper {
    @Insert("insert into proxy (host,port,status) values (#{host},#{port},1)")
    int insert(Proxy proxy);

    @Select("select * from proxy")
    List<Proxy> selectAll();
}
