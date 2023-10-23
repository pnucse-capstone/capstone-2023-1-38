package com.lsj.traces.domain;

import java.util.Date;

public class TraceTransport {

    //트레이스 트랜스포트 Id
    private Long id; //trace_transport.id

    //농산물 Id
    private String cropsId;

    //기사 Id
    private String driverId;

    //시간
    private Date time;//trace_transport.time

    //농가 닉네임
    private String farmerNickName;

    //농가 사용자 이름
    private String farmerUserName;

    //농가 연락처
    private String farmerTel;

    //농가 부서
    private String farmerDept;

    //상태
    private Integer status;

    //출고 상태
    private Integer outFactoryStatus;

    //공장 id
    private Integer factoryId;

    //비고
    private String remarks;

    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    public Integer getOutFactoryStatus() {
        return outFactoryStatus;
    }

    public void setOutFactoryStatus(Integer outFactoryStatus) {
        this.outFactoryStatus = outFactoryStatus;
    }

    public String getFarmerTel() {
        return farmerTel;
    }

    public void setFarmerTel(String farmerTel) {
        this.farmerTel = farmerTel;
    }

    public String getFarmerDept() {
        return farmerDept;
    }

    public void setFarmerDept(String farmerDept) {
        this.farmerDept = farmerDept;
    }

    public Long getId() {
        return id;
    }

    /**
     * sets trace_transport.id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * returns trace_transport.crops_id
     */
    public String getCropsId() {
        return cropsId;
    }

    /**
     * sets trace_transport.crops_id
     */
    public void setCropsId(String cropsId) {
        this.cropsId = cropsId == null ? null : cropsId.trim();
    }

    /**
     * returns trace_transport.driver_id
     */
    public String getDriverId() {
        return driverId;
    }

    /**
     * sets trace_transport.driver_id
     */
    public void setDriverId(String driverId) {
        this.driverId = driverId == null ? null : driverId.trim();
    }

    public Date getTime() {
        return time;
    }

    /**
     * sets trace_transport.time
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * returns trace_transport.farmer_nick_name
     */
    public String getFarmerNickName() {
        return farmerNickName;
    }

    /**
     * sets trace_transport.farmer_nick_name
     */
    public void setFarmerNickName(String farmerNickName) {
        this.farmerNickName = farmerNickName == null ? null : farmerNickName.trim();
    }

    /**
     * returns trace_transport.farmer_user_name
     */
    public String getFarmerUserName() {
        return farmerUserName;
    }

    /**
     * sets trace_transport.farmer_user_name
     */
    public void setFarmerUserName(String farmerUserName) {
        this.farmerUserName = farmerUserName == null ? null : farmerUserName.trim();
    }

    /**
     * returns trace_transport.status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * sets trace_transport.status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    @Override
    public String toString() {
        return "TraceTransport{" +
                "id=" + id +
                ", cropsId='" + cropsId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", time=" + time +
                ", farmerNickName='" + farmerNickName + '\'' +
                ", farmerUserName='" + farmerUserName + '\'' +
                ", farmerTel='" + farmerTel + '\'' +
                ", farmerDept='" + farmerDept + '\'' +
                ", status=" + status +
                ", outFactoryStatus=" + outFactoryStatus +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}