package com.lopez.julz.readandbill.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DownloadedPreviousReadingsDao {
    @Query("SELECT * FROM DownloadedPreviousReadings")
    List<DownloadedPreviousReadings> getAll();

    @Query("SELECT * FROM DownloadedPreviousReadings WHERE id = :id")
    DownloadedPreviousReadings getOne(String id);

    @Insert
    void insertAll(DownloadedPreviousReadings... downloadedPreviousReadings);

    @Update
    void updateAll(DownloadedPreviousReadings... downloadedPreviousReadings);

    @Query("DELETE FROM DownloadedPreviousReadings WHERE ServicePeriod = :servicePeriod")
    void deleteAllByServicePeriod(String servicePeriod);

    @Query("SELECT * FROM DownloadedPreviousReadings WHERE ServicePeriod = :servicePeriod AND Town = :areaCode AND GroupCode = :groupCode ORDER BY HouseNumber, ServiceAccountName")
    List<DownloadedPreviousReadings> getAllFromSchedule(String servicePeriod, String areaCode, String groupCode);

    @Query("SELECT * FROM DownloadedPreviousReadings WHERE (Status IS NULL OR Status NOT IN('READ')) AND CAST(Town AS VARCHAR) = :areaCode AND CAST(GroupCode AS VARCHAR) = :groupCode AND (HouseNumber >= :houseNumber ) AND OldAccountNo != :oldAccountNo ORDER BY HouseNumber, ServiceAccountName LIMIT 1")
    DownloadedPreviousReadings getNext(String houseNumber, String areaCode, String groupCode, String oldAccountNo);

    @Query("SELECT * FROM DownloadedPreviousReadings WHERE (Status IS NULL OR Status NOT IN('READ')) AND CAST(Town AS VARCHAR) = :areaCode AND CAST(GroupCode AS VARCHAR) = :groupCode AND HouseNumber <= :houseNumber AND OldAccountNo != :oldAccountNo ORDER BY HouseNumber DESC, ServiceAccountName DESC LIMIT 1")
    DownloadedPreviousReadings getPrevious(String houseNumber, String areaCode, String groupCode, String oldAccountNo);

    @Query("SELECT * FROM DownloadedPreviousReadings WHERE Town = :areaCode AND GroupCode = :groupCode ORDER BY HouseNumber, ServiceAccountName LIMIT 1")
    DownloadedPreviousReadings getFirst(String areaCode, String groupCode);

    @Query("SELECT * FROM DownloadedPreviousReadings WHERE Town = :areaCode AND GroupCode = :groupCode ORDER BY HouseNumber, ServiceAccountName DESC LIMIT 1")
    DownloadedPreviousReadings getLast(String areaCode, String groupCode);

    @Query("SELECT * FROM DownloadedPreviousReadings WHERE ServicePeriod = :servicePeriod AND Town = :areaCode AND GroupCode = :groupCode AND Status IS NULL ORDER BY HouseNumber, ServiceAccountName")
    List<DownloadedPreviousReadings> getAllUnread(String servicePeriod, String areaCode, String groupCode);

    @Query("SELECT * FROM DownloadedPreviousReadings WHERE ServicePeriod = :servicePeriod AND Town = :areaCode AND GroupCode = :groupCode AND Status='READ' ORDER BY HouseNumber, ServiceAccountName")
    List<DownloadedPreviousReadings> getAllRead(String servicePeriod, String areaCode, String groupCode);

    @Query("SELECT * FROM DownloadedPreviousReadings WHERE (ServiceAccountName LIKE :regex OR MeterSerial LIKE :regex OR OldAccountNo LIKE :regex) AND ServicePeriod = :servicePeriod AND Town = :areaCode AND GroupCode = :groupCode AND Status IS NULL ORDER BY HouseNumber, ServiceAccountName")
    List<DownloadedPreviousReadings> getSearch(String servicePeriod, String areaCode, String groupCode, String regex);
}
