package com.flea.modules.report.pojo.vo;

/**
 * @Function 全景分布图查询条件.
 * @Author DaiBo
 * @DATE 2017/5/17 11:55
 */
public class ScattergramQueryCriteria extends QueryCriteria {

    //起始经度
    private Double fromLng;

    //结束经度
    private Double toLng;

    //起始纬度
    private Double fromLat;

    //结束纬度
    private Double toLat;

    public Double getFromLng() {
        return fromLng;
    }

    public void setFromLng(Double fromLng) {
        this.fromLng = fromLng;
    }

    public Double getToLng() {
        return toLng;
    }

    public void setToLng(Double toLng) {
        this.toLng = toLng;
    }

    public Double getFromLat() {
        return fromLat;
    }

    public void setFromLat(Double fromLat) {
        this.fromLat = fromLat;
    }

    public Double getToLat() {
        return toLat;
    }

    public void setToLat(Double toLat) {
        this.toLat = toLat;
    }
}
