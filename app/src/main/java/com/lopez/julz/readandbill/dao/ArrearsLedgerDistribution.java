package com.lopez.julz.readandbill.dao;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ArrearsLedgerDistribution {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "AccountNumber")
    private String AccountNumber;

    @ColumnInfo(name = "ServicePeriod")
    private String ServicePeriod;

    @ColumnInfo(name = "Amount")
    private String Amount;

    @ColumnInfo(name = "IsBilled")
    private String IsBilled;

    @ColumnInfo(name = "IsPaid")
    private String IsPaid;

    @ColumnInfo(name = "LinkedBillNumber")
    private String LinkedBillNumber;

    @ColumnInfo(name = "Notes")
    private String Notes;

    @ColumnInfo(name = "created_at")
    private String created_at;

    @ColumnInfo(name = "updated_at")
    private String updated_at;

    @ColumnInfo(name = "CollectibleId")
    private String CollectibleId;

    @ColumnInfo(name = "UploadStatus")
    private String UploadStatus;

    public ArrearsLedgerDistribution() {
    }

    public ArrearsLedgerDistribution(@NonNull String id, String accountNumber, String servicePeriod, String amount, String isBilled, String isPaid, String linkedBillNumber, String notes, String created_at, String updated_at, String collectibleId, String uploadStatus) {
        this.id = id;
        AccountNumber = accountNumber;
        ServicePeriod = servicePeriod;
        Amount = amount;
        IsBilled = isBilled;
        IsPaid = isPaid;
        LinkedBillNumber = linkedBillNumber;
        Notes = notes;
        this.created_at = created_at;
        this.updated_at = updated_at;
        CollectibleId = collectibleId;
        UploadStatus = uploadStatus;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getServicePeriod() {
        return ServicePeriod;
    }

    public void setServicePeriod(String servicePeriod) {
        ServicePeriod = servicePeriod;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getIsBilled() {
        return IsBilled;
    }

    public void setIsBilled(String isBilled) {
        IsBilled = isBilled;
    }

    public String getIsPaid() {
        return IsPaid;
    }

    public void setIsPaid(String isPaid) {
        IsPaid = isPaid;
    }

    public String getLinkedBillNumber() {
        return LinkedBillNumber;
    }

    public void setLinkedBillNumber(String linkedBillNumber) {
        LinkedBillNumber = linkedBillNumber;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCollectibleId() {
        return CollectibleId;
    }

    public void setCollectibleId(String collectibleId) {
        CollectibleId = collectibleId;
    }

    public String getUploadStatus() {
        return UploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        UploadStatus = uploadStatus;
    }
}
