package com.lopez.julz.readandbill.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ArrearsLedgerDistributionDao {
    @Query("SELECT * FROM ArrearsLedgerDistribution WHERE AccountNumber=:accountNumber")
    List<ArrearsLedgerDistribution> getAllByAccount(String accountNumber);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ArrearsLedgerDistribution... arrearsLedgerDistributions);

    @Update
    void updateAll(ArrearsLedgerDistribution... arrearsLedgerDistributions);

    @Query("SELECT * FROM ArrearsLedgerDistribution WHERE UploadStatus='Yes'")
    List<ArrearsLedgerDistribution> getUploadable();
}
