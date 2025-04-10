package kr.co.gachon.emotion_diary.ui.OnBoarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import kr.co.gachon.emotion_diary.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logoImageView = findViewById(R.id.logoImageView);
        TextView titleTextView = findViewById(R.id.titleTextView);

        // 애니메이션 로드
        Animation alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha);

        // 애니메이션 시작
        logoImageView.startAnimation(alphaAnimation);
        titleTextView.startAnimation(alphaAnimation);


        // 애니메이션 끝난 후 AvataActivity로 이동
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // 애니메이션 시작 시 동작
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 애니메이션 종료 시 View를 숨김 처리(이걸 안하면 마지막에 한번 다시 등장함)
                logoImageView.setVisibility(ImageView.INVISIBLE);
                titleTextView.setVisibility(TextView.INVISIBLE);

                Intent intent = new Intent(SplashActivity.this, AvataActivity.class);
                startActivity(intent);
                finish(); // SplashActivity 종료
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // 반복 애니메이션 처리
            }
        });
    }
}


