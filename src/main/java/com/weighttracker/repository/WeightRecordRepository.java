package com.weighttracker.repository;

import com.weighttracker.entity.WeightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {
    
    // タイムスタンプでの降順ソートを追加
    List<WeightRecord> findByUserIdOrderByTimestampDesc(Integer userId);
    
    @Query("SELECT AVG(w.weight) FROM WeightRecord w WHERE w.userId = :userId")
    Double calculateAverageWeight(@Param("userId") Integer userId);
}