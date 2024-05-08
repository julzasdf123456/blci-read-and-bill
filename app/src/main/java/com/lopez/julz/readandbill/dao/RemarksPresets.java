package com.lopez.julz.readandbill.dao;

public class RemarksPresets {
    int code;
    String remarks;

    public RemarksPresets() {
    }

    public RemarksPresets(int code, String remarks) {
        this.code = code;
        this.remarks = remarks;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
