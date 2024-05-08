package com.lopez.julz.readandbill.dao;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Readings {
    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "AccountNumber")
    private String AccountNumber;

    @ColumnInfo(name = "ServicePeriod")
    private String ServicePeriod;

    @ColumnInfo(name = "ReadingTimestamp")
    private String ReadingTimestamp;

    @ColumnInfo(name = "KwhUsed")
    private String KwhUsed;

    @ColumnInfo(name = "DemandKwhUsed")
    private String DemandKwhUsed;

    @ColumnInfo(name = "Notes")
    private String Notes;

    @ColumnInfo(name = "Latitude")
    private String Latitude;

    @ColumnInfo(name = "Longitude")
    private String Longitude;

    @ColumnInfo(name = "UploadStatus")
    private String UploadStatus;

    @ColumnInfo(name = "FieldStatus")
    private String FieldStatus;

    @ColumnInfo(name = "MeterReader")
    private String MeterReader;

    @ColumnInfo(name = "SolarKwhUsed")
    private String SolarKwhUsed;

    @ColumnInfo(name = "PreviousReadingDate")
    private String PreviousReadingDate;

    @ColumnInfo(name = "PreviousReading")
    private String PreviousReading;

    @ColumnInfo(name = "ReadingErrorCode")
    private String ReadingErrorCode;

    @ColumnInfo(name = "ReadingErrorRemarks")
    private String ReadingErrorRemarks;

    @ColumnInfo(name = "KwhConsumed")
    private String KwhConsumed;

    @ColumnInfo(name = "HouseNumber")
    private String HouseNumber;

    @ColumnInfo(name = "ConsumerName")
    private String ConsumerName;

    @ColumnInfo(name = "OldAccountNo")
    private String OldAccountNo;

    @ColumnInfo(name = "AreaCode")
    private String AreaCode;

    @ColumnInfo(name = "GroupCode")
    private String GroupCode;

    @ColumnInfo(name = "MeterNumber")
    private String MeterNumber;
    public Readings() {}

    public Readings(@NonNull String id, String accountNumber, String servicePeriod, String readingTimestamp, String kwhUsed, String demandKwhUsed, String notes, String latitude, String longitude, String uploadStatus, String fieldStatus, String meterReader, String solarKwhUsed, String previousReadingDate, String previousReading, String readingErrorCode, String readingErrorRemarks, String kwhConsumed, String houseNumber, String consumerName, String oldAccountNo, String areaCode, String groupCode, String meterNumber) {
        this.id = id;
        AccountNumber = accountNumber;
        ServicePeriod = servicePeriod;
        ReadingTimestamp = readingTimestamp;
        KwhUsed = kwhUsed;
        DemandKwhUsed = demandKwhUsed;
        Notes = notes;
        Latitude = latitude;
        Longitude = longitude;
        UploadStatus = uploadStatus;
        FieldStatus = fieldStatus;
        MeterReader = meterReader;
        SolarKwhUsed = solarKwhUsed;
        PreviousReadingDate = previousReadingDate;
        PreviousReading = previousReading;
        ReadingErrorCode = readingErrorCode;
        ReadingErrorRemarks = readingErrorRemarks;
        KwhConsumed = kwhConsumed;
        HouseNumber = houseNumber;
        ConsumerName = consumerName;
        OldAccountNo = oldAccountNo;
        AreaCode = areaCode;
        GroupCode = groupCode;
        MeterNumber = meterNumber;
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

    public String getReadingTimestamp() {
        return ReadingTimestamp;
    }

    public void setReadingTimestamp(String readingTimestamp) {
        ReadingTimestamp = readingTimestamp;
    }

    public String getKwhUsed() {
        return KwhUsed;
    }

    public void setKwhUsed(String kwhUsed) {
        KwhUsed = kwhUsed;
    }

    public String getDemandKwhUsed() {
        return DemandKwhUsed;
    }

    public void setDemandKwhUsed(String demandKwhUsed) {
        DemandKwhUsed = demandKwhUsed;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getUploadStatus() {
        return UploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        UploadStatus = uploadStatus;
    }

    public String getFieldStatus() {
        return FieldStatus;
    }

    public void setFieldStatus(String fieldStatus) {
        FieldStatus = fieldStatus;
    }

    public String getMeterReader() {
        return MeterReader;
    }

    public void setMeterReader(String meterReader) {
        MeterReader = meterReader;
    }

    public String getSolarKwhUsed() {
        return SolarKwhUsed;
    }

    public void setSolarKwhUsed(String solarKwhUsed) {
        SolarKwhUsed = solarKwhUsed;
    }

    public String getPreviousReadingDate() {
        return PreviousReadingDate;
    }

    public void setPreviousReadingDate(String previousReadingDate) {
        PreviousReadingDate = previousReadingDate;
    }

    public String getPreviousReading() {
        return PreviousReading;
    }

    public void setPreviousReading(String previousReading) {
        PreviousReading = previousReading;
    }

    public String getReadingErrorCode() {
        return ReadingErrorCode;
    }

    public void setReadingErrorCode(String readingErrorCode) {
        ReadingErrorCode = readingErrorCode;
    }

    public String getReadingErrorRemarks() {
        return ReadingErrorRemarks;
    }

    public void setReadingErrorRemarks(String readingErrorRemarks) {
        ReadingErrorRemarks = readingErrorRemarks;
    }

    public String getKwhConsumed() {
        return KwhConsumed;
    }

    public void setKwhConsumed(String kwhConsumed) {
        KwhConsumed = kwhConsumed;
    }

    public String getHouseNumber() {
        return HouseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        HouseNumber = houseNumber;
    }

    public String getConsumerName() {
        return ConsumerName;
    }

    public void setConsumerName(String consumerName) {
        ConsumerName = consumerName;
    }

    public String getOldAccountNo() {
        return OldAccountNo;
    }

    public void setOldAccountNo(String oldAccountNo) {
        OldAccountNo = oldAccountNo;
    }

    public String getAreaCode() {
        return AreaCode;
    }

    public void setAreaCode(String areaCode) {
        AreaCode = areaCode;
    }

    public String getGroupCode() {
        return GroupCode;
    }

    public void setGroupCode(String groupCode) {
        GroupCode = groupCode;
    }

    public String getMeterNumber() {
        return MeterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        MeterNumber = meterNumber;
    }
}