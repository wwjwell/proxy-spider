package com.zhuanglide.proxyspider.db.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by wwj on 16.1.20.
 */
public interface ProxyMapper {
    @Insert("insert into proxy (host,port,status) values (#{host},#{port},1)")
    int insert(Proxy proxy);

    @Select("select id,host,port,status from proxy")
    @ResultType(Proxy.class)
    List<Proxy> selectAll();

    @Update("update proxy set host=#{host},port=#{port},status=#{status} where id=#{id}")
    int update(Proxy proxy);

    @Delete("delete from proxy where id=#{id}")
    int delete(int id);
}
