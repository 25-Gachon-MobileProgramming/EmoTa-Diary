package kr.co.gachon.emotion_diary.ui.Remind.timeGraph;

import android.graphics.Point;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Collections;


import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.data.AppDatabase;
import kr.co.gachon.emotion_diary.data.DiaryDao;

public class TimeZoneActivity extends AppCompatActivity {
    List<String> times = new ArrayList<>();
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_zone);

        getTimeZone();

    }
    public void getTimeZone() {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        DiaryDao diaryDao = db.diaryDao();
        TimeGraph graphView = findViewById(R.id.timeGraph);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Date> dates = diaryDao.getAllDiaryDates();

            // 정확한 분 단위로 시간 변환
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Map<Integer, Integer> countMap = new HashMap<>();
            for (Date date : dates) {
                String timeStr = sdf.format(date);
                String[] parts = timeStr.split(":");
                int totalMinutes = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
                countMap.put(totalMinutes, countMap.getOrDefault(totalMinutes, 0) + 1);
            }

            // 분 단위 key를 정렬해서 Point 리스트 생성
            List<Point> points = new ArrayList<>();
            List<Integer> sortedMinutes = new ArrayList<>(countMap.keySet());
            Collections.sort(sortedMinutes);  // 시간순 정렬

            for (int minute : sortedMinutes) {
                int count = countMap.get(minute);
                points.add(new Point(minute, count));
            }
            points.add(new Point(1350, 1)); // 그래프 마지막까지 유지하기 위해 더미 넣음... 실제로 무의미한 값이라 넣어도 그래프상의 높낮이 변화는  없음

            // 가장 많이 나온 시간 계산
            int maxMinute = 0, maxCount = 0;
            for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxMinute = entry.getKey();
                    maxCount = entry.getValue();
                }
            }
            String mostFrequentTime = String.format("%02d:%02d", maxMinute / 60, maxMinute % 60);

            // UI에 적용
            runOnUiThread(() -> {
                graphView.setTimePoints(points);
                TextView textView = findViewById(R.id.time);
                textView.setText(mostFrequentTime);
            });
        });
    }


}
