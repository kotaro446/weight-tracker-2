package com.weighttracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.weighttracker.entity.WeightRecord;
import com.weighttracker.service.WeightService;

@Controller
@RequestMapping("/")  // ベースURLを明示的に指定
public class WeightController {
    
    @Autowired
    private WeightService weightService;
    
    // ホームページを表示
    @GetMapping
    public String home(Model model) {
        // 仮のユーザーID
        Integer userId = 1;
        
        // ユーザーの体重記録を取得
        List<WeightRecord> records = weightService.getWeightRecordsByUserId(userId);
        model.addAttribute("weightRecords", records);
        
        // 平均体重を計算
        Double averageWeight = weightService.calculateAverageWeight(userId);
        model.addAttribute("averageWeight", averageWeight);
        
        // 最新の体重記録がある場合、ランクを評価
        if (!records.isEmpty()) {
            Double latestWeight = records.get(0).getWeight();
            String rank = weightService.evaluateWeight(latestWeight, averageWeight);
            model.addAttribute("latestWeight", latestWeight);
            model.addAttribute("rank", rank);
        }
        
        return "index";
    }
    
    // グラフページを表示
    @GetMapping("/chart")
    public String showChart(Model model) {
        // 仮のユーザーID
        Integer userId = 1;
        
        // ユーザーの体重記録を取得
        List<WeightRecord> records = weightService.getWeightRecordsByUserId(userId);
        model.addAttribute("weightRecords", records);
        
        // 平均体重を計算
        Double averageWeight = weightService.calculateAverageWeight(userId);
        model.addAttribute("averageWeight", averageWeight);
        
        return "chart";
    }
    
    // 履歴ページを表示
    @GetMapping("/history")
    public String showHistory(Model model) {
        // 仮のユーザーID
        Integer userId = 1;
        
        // ユーザーの体重記録を取得
        List<WeightRecord> records = weightService.getWeightRecordsByUserId(userId);
        model.addAttribute("weightRecords", records);
        
        // 平均体重を計算
        Double averageWeight = weightService.calculateAverageWeight(userId);
        model.addAttribute("averageWeight", averageWeight);
        
        return "history";
    }
    
    // 設定ページを表示
    @GetMapping("/settings")
    public String showSettings(Model model) {
        return "settings";
    }
    
    // 詳細ページを表示
    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable("id") Long id, Model model) {
        // 体重記録の詳細を取得
        WeightRecord record = weightService.getWeightRecordById(id);
        model.addAttribute("weightRecord", record);
        
        // 仮のユーザーID
        Integer userId = 1;
        
        // 平均体重を計算
        Double averageWeight = weightService.calculateAverageWeight(userId);
        model.addAttribute("averageWeight", averageWeight);
        
        // ランクを評価
        String rank = weightService.evaluateWeight(record.getWeight(), averageWeight);
        model.addAttribute("rank", rank);
        
        return "details";
    }
    
    // 新しい体重記録を追加
    @PostMapping("/add")
    public String addWeight(@RequestParam("weight") Double weight) {
        // デバッグ出力を追加
        System.out.println("体重が送信されました: " + weight);
        
        try {
            // 仮のユーザーID
            Integer userId = 1;
            weightService.saveWeightRecord(userId, weight);
            return "redirect:/";
        } catch (Exception e) {
            System.err.println("エラーが発生しました: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/?error=true";
        }
    }
    
    // 体重記録を削除（CRUD機能の一部）
    @GetMapping("/delete/{id}")
    public String deleteWeight(@PathVariable("id") Long id) {
        try {
            weightService.deleteWeightRecord(id);
        } catch (Exception e) {
            System.err.println("削除エラー: " + e.getMessage());
        }
        return "redirect:/";
    }
}