package com.flea.modules.report.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @Function
 * @Author DaiBo
 * @DATE 2017/5/22 14:48
 */
@Entity
@Table(name = "lib_circulation")
public class LightCirculation {

    @Id
    @GenericGenerator(name="generator",strategy="identity")
    @GeneratedValue(generator="generator")
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    // 经度
    @Column(name="longitude")
    private String longitude;

    @Column(name="latitude")
    private String latitude;

    @Column(name="libraryCode")
    private String libraryCode;

    @Column(name="totalBook")
    private Integer totalBook;

    @Column(name="totalBookBelong")
    private Integer totalBookBelong;

    @Column(name="updateDate")
    private Date updateDate;

    private String lightOption;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getLibraryCode() {
        return libraryCode;
    }

    public void setLibraryCode(String libraryCode) {
        this.libraryCode = libraryCode;
    }

    public Integer getTotalBook() {
        return totalBook;
    }

    public void setTotalBook(Integer totalBook) {
        this.totalBook = totalBook;
    }

    public Integer getTotalBookBelong() {
        return totalBookBelong;
    }

    public void setTotalBookBelong(Integer totalBookBelong) {
        this.totalBookBelong = totalBookBelong;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getLightOption() {
        return lightOption;
    }

    public void setLightOption(String lightOption) {
        this.lightOption = lightOption;
    }
}
