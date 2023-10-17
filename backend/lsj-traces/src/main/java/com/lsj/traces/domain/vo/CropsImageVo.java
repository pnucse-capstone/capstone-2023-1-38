package com.lsj.traces.domain.vo;


public class CropsImageVo {
    private String imageBase64;

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    @Override
    public String toString() {
        return "CropsImageVo{" +
                "imageBase64='" + imageBase64 + '\'' +
                '}';
    }
}
