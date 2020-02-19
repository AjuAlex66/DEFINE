package com.example.define;

public class Profile {
    private String pDesc;
    private String pid;
    private String pImage;
    private String pTime;
    private String pTitle;
    private String uName;
    private String uInfo;

    public Profile() {
    }

    public Profile(String pDesc, String pid, String pImage, String pTime, String pTitle, String uName, String uInfo) {
        this.pDesc = pDesc;
        this.pid = pid;
        this.pImage = pImage;
        this.pTime = pTime;
        this.pTitle = pTitle;
        this.uName = uName;
        this.uInfo = uInfo;
    }

    public String getpDesc() {
        return pDesc;
    }

    public void setpDesc(String pDesc) {
        this.pDesc = pDesc;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public void setuInfo(String uInfo) {
        this.uInfo = uInfo;
    }

    public String getuInfo() {
        return uInfo;
    }
}
