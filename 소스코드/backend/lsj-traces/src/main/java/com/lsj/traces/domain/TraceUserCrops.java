package com.lsj.traces.domain;

public class TraceUserCrops {
    private Long id;

    //사용자 이름
    private String username;

    //농산물 Id
    private String cropsId;

    //농산물 이름
    private String cropsName;

    //상태
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getCropsId() {
        return cropsId;
    }

    public void setCropsId(String cropsId) {
        this.cropsId = cropsId == null ? null : cropsId.trim();
    }

    public String getCropsName() {
        return cropsName;
    }

    public void setCropsName(String cropsName) {
        this.cropsName = cropsName == null ? null : cropsName.trim();
    }
}