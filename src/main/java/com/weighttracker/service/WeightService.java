package com.weighttracker.service;

import com.weighttracker.entity.WeightRecord;
import com.weighttracker.repository.WeightRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WeightService {
    
    @Autowired
    private WeightRecordRepository weightRecordRepository;
    
    // 新しい体重記録を保存
    public WeightRecord saveWeightRecord(Integer userId, Double weight) {
        WeightRecord record = new WeightRecord(userId, weight, LocalDate.now());
        return weightRecordRepository.save(record);
    }
    
    // ユーザーの体重記録を取得
    public List<WeightRecord> getWeightRecordsByUserId(Integer userId) {
        return weightRecordRepository.findByUserIdOrderByTimestampDesc(userId);
    }
    
    // ユーザーの平均体重を計算
    public Double calculateAverageWeight(Integer userId) {
        return weightRecordRepository.calculateAverageWeight(userId);
    }
    
    // 体重に基づいてランクを評価
    public String evaluateWeight(Double currentWeight, Double averageWeight) {
        if (averageWeight == null) {
            return "データ不足";
        }
        
        // 平均との差の絶対値
        double difference = Math.abs(currentWeight - averageWeight);
        
        // ランク付け（仕様通り）
        if (difference <= 0.5) {
            return "A";  // 平均から±0.5kg以内
        } else if (difference <= 1.0) {
            return "B";  // 平均から±0.5〜1.0kg
        } else {
            return "C";  // 平均から±1.0kg以上
        }
    }
    
    // 体重記録を削除
    public void deleteWeightRecord(Long id) {
        weightRecordRepository.deleteById(id);
    }
    // ID に基づいて体重記録を取得
public WeightRecord getWeightRecordById(Long id) {
    return weightRecordRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("指定されたIDの記録が見つかりません: " + id));
}
}