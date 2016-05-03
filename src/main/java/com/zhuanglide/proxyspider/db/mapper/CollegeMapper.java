package com.zhuanglide.proxyspider.db.mapper;

import org.apache.ibatis.annotations.Insert;

/**
 * Created by wwj on 16.5.3.
 */
public interface CollegeMapper {
    @Insert("insert into college (page,name,href,logo,is211,is985,num) values (#{page},#{name},#{href},#{logo},#{is211},#{is985},#{num})")
    int insert(College college);

    /**
     * CREATE TABLE `college` (
     `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
     `page` varchar(200) NOT NULL DEFAULT '' COMMENT 'pageNum',
     `name` varchar(200) NOT NULL DEFAULT '' COMMENT '邮件',
     `href` varchar(200) NOT NULL DEFAULT '' COMMENT '邮件',
     `logo` varchar(200) NOT NULL DEFAULT '' COMMENT '邮件',
     `is211` tinyint(4) NOT NULL DEFAULT '0' COMMENT '邮件',
     `is985` tinyint(4) NOT NULL DEFAULT '0' COMMENT '邮件',
     `num` int(5) NOT NULL DEFAULT '0',
     PRIMARY KEY (`id`)
     ) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='college';
     */
}
