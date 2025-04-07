package kr.co.gachon.emotion_diary.ui.timeLine;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Pair;
import java.util.List;
import java.util.Locale;

import kr.co.gachon.emotion_diary.R;

public class MonthlyDiaryAdapter extends RecyclerView.Adapter<MonthlyDiaryAdapter.MonthlyDiaryViewHolder> {
    private final List<Pair<String, List<MonthlyDiaryEntry>>> monthlyDiaryData;
    private final OnMonthlyDiaryClickListener listener;

    public static class MonthlyDiaryViewHolder extends RecyclerView.ViewHolder {
        public final TextView monthTextView;
        public final TextView diaryCountTextView;

        public MonthlyDiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            monthTextView = itemView.findViewById(R.id.monthTextView);
            diaryCountTextView = itemView.findViewById(R.id.diaryCountTextView);
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

        holder.itemView.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION && listener != null) {
                Pair<String, List<MonthlyDiaryEntry>> clickedItem = monthlyDiaryData.get(position);
                listener.onMonthlyDiaryClick(clickedItem.first, clickedItem.second);
            }
        });
    }

    @Override
    public int getItemCount() {
        return monthlyDiaryData.size();
    }

    public interface OnMonthlyDiaryClickListener {
        void onMonthlyDiaryClick(String month, List<MonthlyDiaryEntry> diaryList);
    }
}