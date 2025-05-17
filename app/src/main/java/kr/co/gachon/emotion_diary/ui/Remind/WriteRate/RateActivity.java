package kr.co.gachon.emotion_diary.ui.Remind.WriteRate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import kr.co.gachon.emotion_diary.R;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import kr.co.gachon.emotion_diary.data.AppDatabase;
import kr.co.gachon.emotion_diary.data.DiaryDao;
import kr.co.gachon.emotion_diary.databinding.RemindBinding;
import kr.co.gachon.emotion_diary.ui.Remind.emotionStatistics.EmotionStatisticsActivity;
import kr.co.gachon.emotion_diary.ui.Remind.timeGraph.TimeZoneFragment;


public class RateActivity extends AppCompatActivity implements RateFragment.RateTextListener {


    private TextView rateText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remind);

        rateText = findViewById(R.id.rateText);


        boolean isMonthly = getIntent().getBooleanExtra("isMonthly", true);
        String term = "2025";

        RateFragment fragment = new RateFragment();
        Bundle args = new Bundle();
        args.putBoolean("isMonthly", isMonthly);
        args.putString("term", term);
        fragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.circleFragmentContainer, fragment)
                .commit();

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(view -> {
            Intent intent = new Intent(RateActivity.this, EmotionStatisticsActivity.class);
            intent.putExtra("isMonthly", isMonthly);
            startActivity(intent);
        });
    }

    @Override
    public void onRateTextUpdated(String text) {
        rateText.setText(text);
    }
}