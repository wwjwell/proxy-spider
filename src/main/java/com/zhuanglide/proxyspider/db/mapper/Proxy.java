package com.zhuanglide.proxyspider.db.mapper;

import java.io.Serializable;

/**
 * Created by wwj on 16.1.19.
 */

/**
 CREATE TABLE `proxy` (
 `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
 `host` VARCHAR(50) NOT NULL DEFAULT '' COMMENT 'host',
 `remark` VARCHAR(50) NOT NULL DEFAULT '' COMMENT 'remark',
 `port` INT(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT 'port',
 `status` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '状态，0 无效，1有效',
 PRIMARY KEY (`id`),
 UNIQUE INDEX `u_idx_proxy` (`host`, `port`)
 )
 COMMENT='proxy'
 default charset utf8
 ENGINE=InnoDB ;
 */
public class Proxy implements Serializable {
    private int id;
    private String host;
    private int port;
    private int status;
    private String remark="";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
