package kr.co.gachon.emotion_diary.ui.Remind.timeGraph;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.ui.Remind.WriteRate.RateActivity;
import kr.co.gachon.emotion_diary.ui.Remind.emotionStatistics.EmotionStatisticsActivity;
import kr.co.gachon.emotion_diary.ui.Remind.emotionStatistics.EmotionStatisticsFragment;
import kr.co.gachon.emotion_diary.ui.Remind.remindSummary.RemindSummary;

public class TimeZoneActivity extends AppCompatActivity {
    private TextView timeTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_zone);

        timeTextView = findViewById(R.id.time);

        boolean isMonthly = getIntent().getBooleanExtra("isMonthly", true);

        TimeZoneFragment fragment = new TimeZoneFragment();
        Bundle args = new Bundle();
        args.putBoolean("isMonthly", isMonthly);
        fragment.setArguments(args);

        // Fragment를 container에 삽입
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.timegraph_fragment_container, fragment);
        transaction.commit();

        Button button = findViewById(R.id.nextButton);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(TimeZoneActivity.this, RemindSummary.class);
            intent.putExtra("isMonthly", isMonthly);
            startActivity(intent);
        });
    }

    public void setTimeText(String time) {
        timeTextView.setText(time);
    }
}
