package kr.co.gachon.emotion_diary.ui.OnBoarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import kr.co.gachon.emotion_diary.MainActivity;
import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.data.Diary;
import kr.co.gachon.emotion_diary.data.Gpt.GptGetDiaryResponse;
import kr.co.gachon.emotion_diary.helper.Helper;
import kr.co.gachon.emotion_diary.ui.answerPage.AnswerActivity;
import kr.co.gachon.emotion_diary.ui.taro.TaroActivity;

public class SplashActivity extends AppCompatActivity {
    List<String> cardTitles = new ArrayList<>(Arrays.asList(
            "chariot", "death", "devil", "emperor", "empress",
            "fool", "hangedman", "hermit", "hierophant", "highpriestess",
            "judgement", "justice", "lovers", "magician", "moon", "star",
            "strength", "sun", "temperance", "tower", "wheeloffortune", "world"
    ));

    private String selectedCardTitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Random random = new Random();
        int selectedCardIndex = random.nextInt(cardTitles.size());
        selectedCardTitle = cardTitles.get(selectedCardIndex);

        ImageView card_logo = findViewById(R.id.card_logo);

        new Handler(Looper.getMainLooper()).postDelayed(() -> flipCard(card_logo), 1000);
        new Handler(Looper.getMainLooper()).postDelayed(this::proceedToNextScreen, 2500);
    }

    private void proceedToNextScreen() {
        SharedPreferences prefs = getSharedPreferences("avatar_pref", MODE_PRIVATE);
        boolean isAvatarCompleted = prefs.getBoolean("isAvatarCompleted", false);

        Intent intent = isAvatarCompleted ?
                new Intent(this, OnBoardingActivity.class) :
                new Intent(this, OnBoardingActivity.class);

        startActivity(intent);
        finish();
    }

    private void flipCard(final ImageView card) {
        card.animate()
                .rotationY(90f)
                .setDuration(150)
                .withEndAction(() -> {
                    Log.d("TaroActivity", "selectedCardTitle: " + selectedCardTitle);

                    int imageResId = getResources().getIdentifier(
                            "taro_" + selectedCardTitle,
                            "drawable",
                            getPackageName()
                    );

                    if (imageResId != 0) {
                        card.setImageResource(imageResId);
                    } else {
                        card.setImageResource(R.drawable.card_back);
                        Toast.makeText(SplashActivity.this, "이미지 리소스를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }

                    card.animate().rotationY(0f).setDuration(700).start();
                })
                .start();
    }
}



