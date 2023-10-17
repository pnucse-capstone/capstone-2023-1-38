package com.lsj.traces.domain;


public class TraceLngLat {
    //트레이스 트랜스포트 Id
    private Long id;//trace_lng_lat.id

    //경도
    private Float lng;

    //위도
    private Float lat;

    //농산물 id
    private String cropsId;

    // 상태
    private int status;

    // 출고 상태
    private int outFactoryStatus;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public String getCropsId() {
        return cropsId;
    }

    public void setCropsId(String cropsId) {
        this.cropsId = cropsId == null ? null : cropsId.trim();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOutFactoryStatus() {
        return outFactoryStatus;
    }

    public void setOutFactoryStatus(int outFactoryStatus) {
        this.outFactoryStatus = outFactoryStatus;
    }

    @Override
    public String toString() {
        return "TraceLngLat{" +
                "id=" + id +
                ", lng=" + lng +
                ", lat=" + lat +
                ", cropsId='" + cropsId + '\'' +
                ", status=" + status +
                ", outFactoryStatus=" + outFactoryStatus +
                '}';
    }
}