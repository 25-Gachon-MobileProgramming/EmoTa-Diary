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

        Bundle args = new Bundle();
        args.putBoolean("isMonthly", isMonthly);

        // RateFragment
        RateFragment rateFragment = new RateFragment();
        rateFragment.setArguments(args);

        // EmotionStatisticsFragment
        EmotionStatisticsFragment emotionFragment = new EmotionStatisticsFragment();
        emotionFragment.setArguments(args);

        // TimeZoneFragment
        TimeZoneFragment timeFragment = new TimeZoneFragment();
        timeFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.summaryFragmentCircle, rateFragment)
                .replace(R.id.summaryFragmentEmotion,  emotionFragment)
                .replace(R.id.summaryFragmentTime, timeFragment)
                .commit();
    }
}
