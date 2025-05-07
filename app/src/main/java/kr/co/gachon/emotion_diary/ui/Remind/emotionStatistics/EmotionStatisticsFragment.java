package kr.co.gachon.emotion_diary.ui.Remind.emotionStatistics;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.data.AppDatabase;
import kr.co.gachon.emotion_diary.data.DiaryDao;
import kr.co.gachon.emotion_diary.data.EmotionCount;
import kr.co.gachon.emotion_diary.ui.Remind.timeGraph.TimeZoneActivity;

public class EmotionStatisticsFragment extends Fragment {

    private BarChart barChart;
    private static final String SET_LABEL = "감정별 통계";
    private List<EmotionCount> emotions = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emotion_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        barChart = view.findViewById(R.id.chart);
        AppDatabase db = AppDatabase.getDatabase(requireContext());
        DiaryDao diaryDao = db.diaryDao();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            emotions = diaryDao.getEmotionCounts();

            requireActivity().runOnUiThread(() -> {
                configureChartAppearance();
                BarData data = createChartData();
                prepareChartData(data);
            });
        });
    }


    private void configureChartAppearance() {
        barChart.getDescription().setEnabled(false);
        barChart.setTouchEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setExtraOffsets(10f, 10f, 10f, 30f);
        barChart.setHighlightFullBarEnabled(false);
        barChart.setBackgroundColor(Color.TRANSPARENT);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(15f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                return (index >= 0 && index < emotions.size()) ? emotions.get(index).emotion : "";
            }
        });

        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawAxisLine(false);
        axisLeft.setAxisMinimum(0f);
        axisLeft.setAxisMaximum(50f);
        axisLeft.setGranularity(1f);
        axisLeft.setDrawLabels(false);

        YAxis axisRight = barChart.getAxisRight();
        axisRight.setTextSize(15f);
        axisRight.setDrawLabels(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawAxisLine(false);
    }

    private BarData createChartData() {
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < emotions.size(); i++) {
            values.add(new BarEntry(i, emotions.get(i).count));
        }

        BarDataSet set = new BarDataSet(values, SET_LABEL);
        set.setDrawIcons(false);
        set.setDrawValues(true);
        set.setColor(Color.parseColor("#FF0000"));
        set.setValueTextColor(Color.WHITE);
        set.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int) value + "회";
            }
        });

        BarData data = new BarData(set);
        data.setBarWidth(0.5f);
        data.setValueTextSize(15);
        return data;
    }

    private void prepareChartData(BarData data) {
        barChart.setData(data);
        barChart.invalidate();
    }
}