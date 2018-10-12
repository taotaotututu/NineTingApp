package com.zzti.lsy.ninetingapp.entity;

/**
 * author：anxin on 2018/10/12 19:17
 * 合同实体类
 */
public class PactInfo {
    private String pactID;//合同编号
    private String pactType;//合同类型
    private String pactContent;//合同简介
    private String pactSchedule;//合同进度
    private String pactTime;//合同周期
    private String pactMoney;//合同总金额
    private String pactRealMoney;//应收金额
    private String pactOutMoney;//合同未收款
    private String pactInMoney;//合同已收款

    public String getPactID() {
        return pactID;
    }

    public void setPactID(String pactID) {
        this.pactID = pactID;
    }

    public String getPactType() {
        return pactType;
    }

    public void setPactType(String pactType) {
        this.pactType = pactType;
    }

    public String getPactContent() {
        return pactContent;
    }

    public void setPactContent(String pactContent) {
        this.pactContent = pactContent;
    }

    public String getPactSchedule() {
        return pactSchedule;
    }

    public void setPactSchedule(String pactSchedule) {
        this.pactSchedule = pactSchedule;
    }

    public String getPactTime() {
        return pactTime;
    }

    public void setPactTime(String pactTime) {
        this.pactTime = pactTime;
    }

    public String getPactMoney() {
        return pactMoney;
    }

    public void setPactMoney(String pactMoney) {
        this.pactMoney = pactMoney;
    }

    public String getPactRealMoney() {
        return pactRealMoney;
    }

    public void setPactRealMoney(String pactRealMoney) {
        this.pactRealMoney = pactRealMoney;
    }

    public String getPactOutMoney() {
        return pactOutMoney;
    }

    public void setPactOutMoney(String pactOutMoney) {
        this.pactOutMoney = pactOutMoney;
    }

    public String getPactInMoney() {
        return pactInMoney;
    }

    public void setPactInMoney(String pactInMoney) {
        this.pactInMoney = pactInMoney;
    }
}
