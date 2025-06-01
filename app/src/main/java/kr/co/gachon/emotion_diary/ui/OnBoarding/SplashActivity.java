package kr.co.gachon.emotion_diary.ui.OnBoarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import kr.co.gachon.emotion_diary.MainActivity;
import kr.co.gachon.emotion_diary.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logoGifView = findViewById(R.id.logoGifView);

        // GIF 로고 표시
        Glide.with(this)
                .asGif()
                .load(R.raw.logo)
                .skipMemoryCache(true)
                .into(logoGifView);

        new Handler().postDelayed(this::proceedToNextScreen, 7000);
    }

    private void proceedToNextScreen() {
        SharedPreferences prefs = getSharedPreferences("avatar_pref", MODE_PRIVATE);
        boolean isAvatarCompleted = prefs.getBoolean("isAvatarCompleted", false);

        Intent intent = isAvatarCompleted ?
                new Intent(this, MainActivity.class) :
                new Intent(this, OnBoardingActivity.class);

        startActivity(intent);
        finish();
    }
}



