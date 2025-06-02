package kr.co.gachon.emotion_diary.ui.OnBoarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import kr.co.gachon.emotion_diary.MainActivity;
import kr.co.gachon.emotion_diary.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logoGifView = findViewById(R.id.logoGifView);

        Glide.with(this)
                .asGif()
                .load(R.raw.logo)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model,
                                                   Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        // 애니메이션 멈추고 첫 프레임만 보여줌
                        resource.setLoopCount(1);
                        resource.stop();

                        new Handler().postDelayed(resource::start, 2000);

                        // 전체 종료 후 다음 화면 이동
                        new Handler().postDelayed(() -> {
                            proceedToNextScreen();
                        }, 5000);

                        return false;
                    }

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<GifDrawable> target, boolean isFirstResource) {
                        proceedToNextScreen(); // 로드 실패 시 바로 이동
                        return false;
                    }
                })
                .into(logoGifView);

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



