package kr.co.gachon.emotion_diary.ui.Remind.remindSummary;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.ui.Remind.WriteRate.RateFragment;
import kr.co.gachon.emotion_diary.ui.Remind.emotionStatistics.EmotionStatisticsFragment;
import kr.co.gachon.emotion_diary.ui.Remind.timeGraph.TimeZoneFragment;

public class RemindSummary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.remind_summary);

        boolean isMonthly = getIntent().getBooleanExtra("isMonthly", true);

        RateFragment fragment = new RateFragment();
        Bundle args = new Bundle();
        args.putBoolean("isMonthly", isMonthly);
        fragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.summaryFragmentCircle, fragment)
            .replace(R.id.summaryFragmentEmotion, new EmotionStatisticsFragment())
            .replace(R.id.summaryFragmentTime, new TimeZoneFragment())
            .commit();
}
}
