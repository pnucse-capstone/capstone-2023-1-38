package com.lsj.traces.domain.vo;

import java.util.Date;

public class TransportVo {
    private Long id;

    /**
     * 농산물 id
     */
    private String cropsId;

    /**
     * 기사 id
     */
    private String driverId;

    /**
     * 부서 이름
     */
    private String deptName;

    /**
     * 전화번호
     */
    private String tel;

    /**
     * 시간
     */
    private Date time;

    /**
     * 농가 닉네임
     */
    private String farmerNickName;

    /**
     * 농가 사용자 이름 农夫用户名称
     */
    private String farmerUserName;

    /**
     * 상태
     */
    private Integer status;

    /**
     * 비고
     */
    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCropsId() {
        return cropsId;
    }

    public void setCropsId(String cropsId) {
        this.cropsId = cropsId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getFarmerNickName() {
        return farmerNickName;
    }

    public void setFarmerNickName(String farmerNickName) {
        this.farmerNickName = farmerNickName;
    }

    public String getFarmerUserName() {
        return farmerUserName;
    }

    public void setFarmerUserName(String farmerUserName) {
        this.farmerUserName = farmerUserName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
