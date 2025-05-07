package kr.co.gachon.emotion_diary.ui.Remind.emotionStatistics;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.ui.Remind.timeGraph.TimeZoneActivity;

public class EmotionStatisticsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emotion_statistics);
        boolean isMonthly = getIntent().getBooleanExtra("isMonthly", true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.emotiond_statistics_fragment_container, new EmotionStatisticsFragment())
                .commit();
        // 버튼 클릭 시 이동
        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, TimeZoneActivity.class);
            intent.putExtra("isMonthly", isMonthly);
            startActivity(intent);
        });
    }
}