package kr.co.gachon.emotion_diary.ui.Remind;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.data.AppDatabase;
import kr.co.gachon.emotion_diary.data.DiaryDao;
import kr.co.gachon.emotion_diary.data.EmotionCount;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmotionStatisticsActivity extends AppCompatActivity {

    private BarChart barChart;
    private int emotionSize = 0;
    private static final String SET_LABEL = "감정별 통계";

    private List<EmotionCount> emotion = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emotion_statistics);

        barChart = findViewById(R.id.chart);
        barChart.setDrawBarShadow(false);

        // DB 접근 준비
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        DiaryDao diaryDao = db.diaryDao();

        // 백그라운드에서 DB 조회 후 UI 업데이트
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<EmotionCount> emotions = diaryDao.getEmotionCounts();  // DAO 메서드 호출

            runOnUiThread(() -> {
                this.emotion = emotions;
                this.emotionSize = emotions.size();

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
                if (index >= 0 && index < emotion.size()) {
                    return emotion.get(index).emotion;
                } else {
                    return "";
                }
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
        for (int i = 0; i < emotionSize; i++) {
            values.add(new BarEntry(i, emotion.get(i).count));
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
