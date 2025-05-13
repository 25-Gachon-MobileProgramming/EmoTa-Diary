package kr.co.gachon.emotion_diary.ui.emotion;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.ui.taro.TaroActivity;

public class EmotionSelectActivity extends AppCompatActivity {

    ImageButton pressButton = null;

    String selectedEmotion = null;

    private ImageButton previousButton = null;
    private Integer originalTint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_emotion);

        Intent intent = getIntent();

        String CurrentDate = intent.getStringExtra("date");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

        Log.wtf("getTest", CurrentDate);
        Log.wtf("getTest", title);
        Log.wtf("getTest", content);

        ImageButton btn1 = findViewById(R.id.btn1);
        ImageButton btn2 = findViewById(R.id.btn2);
        ImageButton btn3 = findViewById(R.id.btn3);
        ImageButton btn4 = findViewById(R.id.btn4);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.custom_back_bar);

            ImageButton backButton = actionBar.getCustomView().findViewById(R.id.backButtonActionBar);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            // 액션 바 제목 바꾸기
            TextView titleTextView = actionBar.getCustomView().findViewById(R.id.titleTextViewActionBar);
            if (titleTextView != null) {
                titleTextView.setText("Emotion");
            }

            originalTint = getColor(R.color.white);

            // 감정 버튼 클릭 시, 이전 버튼 초기화 후 새로운 버튼 상태 변경 logic
            View.OnClickListener buttonClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ImageButton currentButton = (ImageButton) view;

                    pressButton = (ImageButton) view;
                    pressButton.setImageTintList(android.content.res.ColorStateList.valueOf(Color.GREEN));

                    if (view.getId() == R.id.btn1) {
                        selectedEmotion = "기쁨";
                    } else if (view.getId() == R.id.btn2) {
                        selectedEmotion = "분노";
                    } else if (view.getId() == R.id.btn3) {
                        selectedEmotion = "슬픔";
                    } else if (view.getId() == R.id.btn4) {
                        selectedEmotion = "피곤";
                    }
                    // 이전 버튼이 있고 현재 버튼과 다르다면 원래 Tint 색상으로 되돌림
                    if (previousButton != null && previousButton != currentButton && originalTint != null) {
                        previousButton.setImageTintList(android.content.res.ColorStateList.valueOf(originalTint));
                    }

                    previousButton = currentButton;
                    pressButton = currentButton;
                }
            };

            btn1.setOnClickListener(buttonClickListener);
            btn2.setOnClickListener(buttonClickListener);
            btn3.setOnClickListener(buttonClickListener);
            btn4.setOnClickListener(buttonClickListener);

            Button nextPage = findViewById(R.id.nextPageButton);
            nextPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pressButton == null) {
                        Toast.makeText(EmotionSelectActivity.this, "감정 선택하세요", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(EmotionSelectActivity.this, TaroActivity.class);
                        intent.putExtra("date", CurrentDate);
                        intent.putExtra("title", title);
                        intent.putExtra("content", content);
                        intent.putExtra("emotion", selectedEmotion);

                        startActivity(intent);
                    }
                }
            });
        }
    }
}


