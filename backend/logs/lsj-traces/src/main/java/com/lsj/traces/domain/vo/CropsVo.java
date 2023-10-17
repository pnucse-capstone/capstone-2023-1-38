package com.lsj.traces.domain.vo;

import java.util.Date;

/**
 * 농산물 정보
 */
public class CropsVo {
    private Long id;

    /**
     * 농산물 id
     */
    private String cropsId;

    /**
     * 농가 닉네임
     */
    private String farmerNickName;

    /**
     * 농가 전환번호
     */
    private String farmerTel;

    /**
     * 농가 부서
     */
    private String farmerDept;

    /**
     * 농산물 이름
     */
    private String cropsName;

    /**
     * 시간
     */
    private Date time;

    /**
     * 매칭 상태
     */
    private Integer machingStatus;

    /**
     * 출고 상태
     */
    private Integer outFactoryStatus;

    /**
     * 제품 기록 상태 产品写入状态
     */
    private Integer productWriteStatus;

    /**
     * 판매자 수령 상태
     */
    private Integer retailerReceiveStatus;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getRetailerReceiveStatus() {
        return retailerReceiveStatus;
    }

    public void setRetailerReceiveStatus(Integer retailerReceiveStatus) {
        this.retailerReceiveStatus = retailerReceiveStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProductWriteStatus() {
        return productWriteStatus;
    }

    public void setProductWriteStatus(Integer productWriteStatus) {
        this.productWriteStatus = productWriteStatus;
    }

    public Integer getMachingStatus() {
        return machingStatus;
    }

    public void setMachingStatus(Integer machingStatus) {
        this.machingStatus = machingStatus;
    }

    public Integer getOutFactoryStatus() {
        return outFactoryStatus;
    }

    public void setOutFactoryStatus(Integer outFactoryStatus) {
        this.outFactoryStatus = outFactoryStatus;
    }

    public String getCropsId() {
        return cropsId;
    }

    public void setCropsId(String cropsId) {
        this.cropsId = cropsId;
    }

    public String getFarmerNickName() {
        return farmerNickName;
    }

    public void setFarmerNickName(String farmerNickName) {
        this.farmerNickName = farmerNickName;
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

    public String getCropsName() {
        return cropsName;
    }

    public void setCropsName(String cropsName) {
        this.cropsName = cropsName;
    }
}
