package com.gloiot.hygo.bean;

/**
 * 我的账单实体类
 * */
public class MyBillBean {

    /** ID*/
    private String id;

    /** 说明*/
    private String explain;

    /** 积分*/
    private String integral;

    /** 录入时间*/
    private String entryTime;

    /** 余额(已格式化)*/
    private String balance;

    /** 备注*/
    private String Remarks;

    /** 交易账户*/
    private String transactionAccount;

    /** 交易类别*/
    private String transactionType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getTransactionAccount() {
        return transactionAccount;
    }

    public void setTransactionAccount(String transactionAccount) {
        this.transactionAccount = transactionAccount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
