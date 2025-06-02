package kr.co.gachon.emotion_diary.ui.timeLine;


import java.time.LocalDate;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.data.Emotions;

public class MonthlyDiaryAdapter extends RecyclerView.Adapter<MonthlyDiaryAdapter.MonthlyDiaryViewHolder> {
    private final List<Pair<String, List<MonthlyDiaryEntry>>> monthlyDiaryData;
    private final OnMonthlyDiaryClickListener listener;

    public static class MonthlyDiaryViewHolder extends RecyclerView.ViewHolder {
        public final TextView monthTextView;
        public final TextView diaryCountTextView;
        public final TextView emotionTextView;
        public final TextView tarotTextView;
        public final TextView emotionFlowTextView;
        public final TextView badgeTextView;
        public final TextView percentTextView;

        public MonthlyDiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            monthTextView = itemView.findViewById(R.id.monthTextView);
            diaryCountTextView = itemView.findViewById(R.id.diaryCountTextView);
            percentTextView = itemView.findViewById(R.id.diaryPercent);
            emotionTextView = itemView.findViewById(R.id.emotionSummaryTextView);
            tarotTextView = itemView.findViewById(R.id.tarotSummaryTextView);
            emotionFlowTextView = itemView.findViewById(R.id.emotionFlowTextView);
            badgeTextView = itemView.findViewById(R.id.badgeTextView);
        }
    }

    public MonthlyDiaryAdapter(List<Pair<String, List<MonthlyDiaryEntry>>> monthlyDiaryData, OnMonthlyDiaryClickListener listener) {
        this.monthlyDiaryData = monthlyDiaryData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MonthlyDiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_monthly_diary, parent, false);

        return new MonthlyDiaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthlyDiaryViewHolder holder, int position) {
        Pair<String, List<MonthlyDiaryEntry>> monthData = monthlyDiaryData.get(position);
        String dateString = monthData.first; // 2025-02
        List<MonthlyDiaryEntry> diaryList = monthData.second;

        String[] parts = dateString.split("-");
        String strYear = parts[0];
        String strMonth = parts[1];

        holder.monthTextView.setText(String.format("%s년 %s월", strYear, strMonth));
        holder.diaryCountTextView.setText(String.format(Locale.KOREA, "작성된 일기: %d개", diaryList.size()));

        int writingPercent = getMonthlyWritingPercent(diaryList);
        String percentText = String.format(Locale.KOREA, "📊 개근률: %d%%", writingPercent);
        holder.percentTextView.setText(percentText);


        holder.itemView.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION && listener != null) {
                Pair<String, List<MonthlyDiaryEntry>> clickedItem = monthlyDiaryData.get(position);
                listener.onMonthlyDiaryClick(clickedItem.first, clickedItem.second);
            }
        });

        // inside onBindViewHolder

        String topEmotion = getMostFrequent(diaryList, "emotion");
        String topTarot = getMostFrequent(diaryList, "tarot");

        if (Objects.equals(topEmotion, "정보없음"))
            holder.emotionTextView.setText("가장 많이 느낀 감정: " + topEmotion);
        else
            holder.emotionTextView.setText("가장 많이 느낀 감정: " + topEmotion + Emotions.getEmotionDataByText(topEmotion).getEmoji());

        if (!Objects.equals(topTarot, "데이터 없음"))
            holder.tarotTextView.setText("가장 많이 뽑은 타로: " + topTarot);
        else
            holder.tarotTextView.setVisibility(View.GONE);

        String emotionFlow = getEmotionFlow(diaryList);

        if (!Objects.equals(emotionFlow, "감정 변화: 데이터 부족"))
            holder.emotionFlowTextView.setText(emotionFlow);
        else
            holder.emotionFlowTextView.setVisibility(View.GONE);

        List<String> badges = new ArrayList<>();
        if (has7DayStreak(diaryList)) badges.add("🎖 7일 연속 일기 작성!");
        if (hasEmotionDiversity(diaryList)) badges.add("🎭 이번 달 5가지 감정 경험");
        if (isMonthlyFullyWritten(diaryList)) badges.add("📓 월간 개근왕");
        if (hasEmotionExplorerBadge(diaryList)) badges.add("🎨 감정 탐험가");
        if (hasTarotLoverBadge(diaryList)) badges.add("🔮 타로 애호가");

        holder.badgeTextView.setText(TextUtils.join("\n", badges));
    }

    @Override
    public int getItemCount() {
        return monthlyDiaryData.size();
    }

    public interface OnMonthlyDiaryClickListener {
        void onMonthlyDiaryClick(String month, List<MonthlyDiaryEntry> diaryList);
    }

    private String getMostFrequent(List<MonthlyDiaryEntry> list, String type) {
        Map<String, Integer> counter = new HashMap<>();
        for (MonthlyDiaryEntry entry : list) {
            String value = type.equals("emotion") ? entry.getEmotion() : entry.getTarotCard();
            if (value != null) {
                counter.put(value, counter.getOrDefault(value, 0) + 1);
            }
        }
        return counter.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("정보 없음");
    }

    private String getEmotionFlow(List<MonthlyDiaryEntry> diaryEntries) {
        List<String> emotionSequence = new ArrayList<>();

        for (MonthlyDiaryEntry entry : diaryEntries) {
            String emotion = entry.getEmotion();
            if (emotion != null && !emotion.trim().isEmpty()) {
                emotionSequence.add(emotion);
            }
        }

        // 너무 적으면 감정 변화가 의미 없음
        if (emotionSequence.size() < 3) return "감정 변화: 데이터 부족";

        // 중복 제거 (순서 유지)
        List<String> emotionFlow = new ArrayList<>();
        for (String emotion : emotionSequence) {
            if (emotionFlow.isEmpty() || !emotionFlow.get(emotionFlow.size() - 1).equals(emotion)) {
                emotionFlow.add(emotion);
            }
        }

        // 감정 흐름 텍스트로 변환
        return "감정 변화: " + String.join(" → ", emotionFlow);
    }

    private boolean has7DayStreak(List<MonthlyDiaryEntry> entries) {
        Set<LocalDate> dates = entries.stream()
                .map(e -> LocalDate.parse(e.getDate()))
                .collect(Collectors.toSet());

        for (LocalDate date : dates) {
            boolean streak = true;
            for (int i = 0; i < 7; i++) {
                if (!dates.contains(date.plusDays(i))) {
                    streak = false;
                    break;
                }
            }
            if (streak) return true;
        }
        return false;
    }

    private boolean hasEmotionDiversity(List<MonthlyDiaryEntry> entries) {
        Set<String> emotionSet = entries.stream()
                .map(MonthlyDiaryEntry::getEmotion)
                .filter(e -> e != null && !e.isBlank())
                .collect(Collectors.toSet());

        return emotionSet.size() >= 5;
    }

    private boolean isMonthlyFullyWritten(List<MonthlyDiaryEntry> entries) {
        if (entries.isEmpty()) return false;

        LocalDate firstDate = LocalDate.parse(entries.get(0).getDate());
        YearMonth yearMonth = YearMonth.of(firstDate.getYear(), firstDate.getMonth());

        int totalDaysInMonth = yearMonth.lengthOfMonth();
        Set<LocalDate> uniqueDays = entries.stream()
                .map(e -> LocalDate.parse(e.getDate()))
                .collect(Collectors.toSet());

        return uniqueDays.size() == totalDaysInMonth;
    }

    private boolean hasEmotionExplorerBadge(List<MonthlyDiaryEntry> entries) {
        Set<String> emotionSet = entries.stream()
                .map(MonthlyDiaryEntry::getEmotion)
                .filter(e -> e != null && !e.isBlank())
                .collect(Collectors.toSet());

        return emotionSet.size() >= 10;
    }

    private boolean hasTarotLoverBadge(List<MonthlyDiaryEntry> entries) {
        Set<String> tarotSet = entries.stream()
                .map(MonthlyDiaryEntry::getTarotCard)
                .filter(t -> t != null && !t.isBlank())
                .collect(Collectors.toSet());

        return tarotSet.size() >= 5;
    }

    private int getMonthlyWritingPercent(List<MonthlyDiaryEntry> entries) {
        if (entries.isEmpty()) return 0;

        LocalDate firstDate = LocalDate.parse(entries.get(0).getDate());
        YearMonth yearMonth = YearMonth.of(firstDate.getYear(), firstDate.getMonth());

        int totalDaysInMonth = yearMonth.lengthOfMonth();
        Set<LocalDate> uniqueDays = entries.stream()
                .map(e -> LocalDate.parse(e.getDate()))
                .collect(Collectors.toSet());

        int writtenDays = uniqueDays.size();
        return (writtenDays * 100) / totalDaysInMonth;
    }
}