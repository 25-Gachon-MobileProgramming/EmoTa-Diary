package kr.co.gachon.emotion_diary.ui.Remind;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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

public class EmotionStatisticsActivity extends AppCompatActivity {

    private BarChart barChart;
//    private String[] emotion = {"sad", "happy"};

    private int emotionSize = 0;
    private static final String SET_LABEL = "감정별 통계";

    EmotionStatisticsViewModel viewModel;
    List<EmotionCount> emotion;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emotion_statistics);



        barChart = findViewById(R.id.chart);
        barChart.setDrawBarShadow(false);

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        DiaryDao diaryDao = db.diaryDao();
        EmotionStatisticsViewModelFactory factory = new EmotionStatisticsViewModelFactory(diaryDao);

        viewModel = new ViewModelProvider(this, factory).get(EmotionStatisticsViewModel.class);

        Log.d("TAG", "observe 등록 전");

        viewModel.getEmotions().observe(this, emotions -> {
            Log.d("TAG", "LiveData 변경 감지됨!");
            this.emotion = emotions;
            this.emotionSize = emotions.size();

            configureChartAppearance();
            BarData data = createChartData();
            prepareChartData(data);
        });
    }


    private void configureChartAppearance() {

        barChart.getDescription().setEnabled(false); // chart 밑에 description 표시 유무
        barChart.setTouchEnabled(false); // 터치 유무
        barChart.getLegend().setEnabled(false); // Legend는 차트의 범례
        barChart.setExtraOffsets(10f, 10f, 10f, 30f);
        barChart.setHighlightFullBarEnabled(false);
        barChart.setBackgroundColor(Color.TRANSPARENT);

        // XAxis (수평 막대 기준 왼쪽) - 선 유무, 사이즈, 색상, 축 위치 설정
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(15f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
//        xAxis.setGridLineWidth(25f);
//        xAxis.setGridColor(Color.parseColor("#FF0000"));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X 축 데이터 표시 위치


        // YAxis(Left) (수평 막대 기준 아래쪽) - 선 유무, 데이터 최솟값/최댓값, label 유무
        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawAxisLine(false);
        axisLeft.setAxisMinimum(0f); // 최솟값
        axisLeft.setAxisMaximum(50f); // 최댓값
        axisLeft.setGranularity(1f); // 값만큼 라인선 설정
        axisLeft.setDrawLabels(false); // label 삭제

        // YAxis(Right) (수평 막대 기준 위쪽) - 사이즈, 선 유무
        YAxis axisRight = barChart.getAxisRight();
        axisRight.setTextSize(15f);
        axisRight.setDrawLabels(false); // label 삭제
        axisRight.setDrawGridLines(false);
        axisRight.setDrawAxisLine(false);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value; // 막대의 x 인덱스 (0, 1, 2...)
                if (index >= 0 && index < emotion.size()) {
                    return emotion.get(index).emotion; // 감정 이름 반환 (예: "happy")
                } else {
                    return ""; // 범위를 벗어나면 빈 문자열 반환
                }
            }
        });
    }
    private BarData createChartData() {

        // 1. [BarEntry] BarChart에 표시될 데이터 값 생성
        ArrayList<BarEntry> values = new ArrayList<>();


        for (int i = 0; i < emotionSize; i++) {
            values.add(new BarEntry(i, emotion.get(i).count));
        }

        // 2. [BarDataSet] 단순 데이터를 막대 모양으로 표시, BarChart의 막대 커스텀
        BarDataSet set = new BarDataSet(values, SET_LABEL);
        set.setDrawIcons(false);
        set.setDrawValues(true);
        set.setColor(Color.parseColor("#FF0000")); // 코드를 색상으로 변경
        set.setValueTextColor(Color.WHITE);
        // 데이터 값 원하는 String 포맷으로 설정하기 (ex. ~회)
        set.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (String.valueOf((int) value)) + "회";
            }
        });

        // 3. [BarData] 보여질 데이터 구성
        BarData data = new BarData(set);
        data.setBarWidth(0.5f);
        data.setValueTextSize(15);

        return data;
    }
    private void prepareChartData(BarData data) {
        barChart.setData(data); // BarData 전달
        barChart.invalidate(); // BarChart 갱신해 데이터 표시

    }
}
