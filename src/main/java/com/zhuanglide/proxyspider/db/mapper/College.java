package com.zhuanglide.proxyspider.db.mapper;

/**
 * Created by wwj on 16.5.3.
 */
public class College {
    public int id;
    public int page;
    public int num;
    public String name;
    public String href;
    public String city;
    public String logo;
    public int is211;
    public int is985;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getIs211() {
        return is211;
    }

    public void setIs211(int is211) {
        this.is211 = is211;
    }

    public int getIs985() {
        return is985;
    }

    public void setIs985(int is985) {
        this.is985 = is985;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
