package kr.co.gachon.emotion_diary.ui.timeLine;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Pair;
import java.util.List;
import kr.co.gachon.emotion_diary.R;

public class MonthlyDiaryAdapter extends RecyclerView.Adapter<MonthlyDiaryAdapter.MonthlyDiaryViewHolder> {
    private final List<Pair<String, List<MonthlyDiaryEntry>>> monthlyDiaryData;

    public static class MonthlyDiaryViewHolder extends RecyclerView.ViewHolder {
        public final TextView monthTextView;
        public final TextView diaryCountTextView;

        public MonthlyDiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            monthTextView = itemView.findViewById(R.id.monthTextView);
            diaryCountTextView = itemView.findViewById(R.id.diaryCountTextView);
        }
    }

    public MonthlyDiaryAdapter(List<Pair<String, List<MonthlyDiaryEntry>>> monthlyDiaryData) {
        this.monthlyDiaryData = monthlyDiaryData;
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
        String month = monthData.first;
        List<MonthlyDiaryEntry> diaryList = monthData.second;

        holder.monthTextView.setText(month);
        holder.diaryCountTextView.setText("작성된 일기: " + diaryList.size() + "개");
    }

    @Override
    public int getItemCount() {
        return monthlyDiaryData.size();
    }
}