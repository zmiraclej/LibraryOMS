package com.flea.modules.report.pojo;

import javax.persistence.Column;
import java.math.BigInteger;


/**
 * 全景分布图
 * @Author daibo
 * @DATE 2017/5/17 9:48
 */
public class Scattergram {

    //馆号
    private String hallCode;

    //馆名
    private String libName;

    //经度
    private String longitude;

    //纬度
    private String latitude;

    //读者数
    private BigInteger readership;

    //藏书统计总册数-所在
    private BigInteger totalBook;

    //藏书统计总册数-所属
    private BigInteger totalBookBelong;

    public String getHallCode() {
        return hallCode;
    }

    public void setHallCode(String hallCode) {
        this.hallCode = hallCode;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public BigInteger getReadership() {
        return readership;
    }

    public void setReadership(BigInteger readership) {
        this.readership = readership;
    }

    public BigInteger getTotalBook() {
        return totalBook;
    }

    public void setTotalBook(BigInteger totalBook) {
        this.totalBook = totalBook;
    }

    public BigInteger getTotalBookBelong() {
        return totalBookBelong;
    }

    public void setTotalBookBelong(BigInteger totalBookBelong) {
        this.totalBookBelong = totalBookBelong;
    }
}

