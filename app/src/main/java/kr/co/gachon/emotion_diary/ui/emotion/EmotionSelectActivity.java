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
import kr.co.gachon.emotion_diary.ui.answerPage.AnswerActivity;

public class EmotionSelectActivity extends AppCompatActivity {

    // 버튼 상태 확인 버튼
    ImageButton pressButton = null;

    // 감정 선택을 위한 변수 추가
    String selectedEmotion = null;

    // 이전 선택된 버튼 저장
    private ImageButton previousButton = null;
    private Integer originalTint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_emotion);

        Intent intent = getIntent();

        // DiaryWriteActivity에서 보낸 정보 받기
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

        // 바 왼쪽에 imageButton 사용해서 뒤로가기
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


            // 각 버튼의 원래 tint 색상이 하양이라고 저장
            originalTint = getColor(R.color.white);

            // 감정 버튼 클릭 시, 이전 버튼 초기화 후 새로운 버튼 상태 변경 logic
            View.OnClickListener buttonClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // tint 바꾸기 위한 변수
                    ImageButton currentButton = (ImageButton) view;

                    // 새로운 버튼 상태 변경
                    pressButton = (ImageButton) view;
                    pressButton.setImageTintList(android.content.res.ColorStateList.valueOf(Color.GREEN));

                    // 감정 값 설정
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
                    // 현재 상태 갱신
                    previousButton = currentButton;
                    pressButton = currentButton;
                }
            };

            // 감정 버튼
            btn1.setOnClickListener(buttonClickListener);
            btn2.setOnClickListener(buttonClickListener);
            btn3.setOnClickListener(buttonClickListener);
            btn4.setOnClickListener(buttonClickListener);

            // AnswerActivity로 넘어가는 버튼
            Button nextPage = findViewById(R.id.nextPageButton);
            nextPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pressButton == null) {
                        Toast.makeText(EmotionSelectActivity.this, "감정 선택하세요", Toast.LENGTH_SHORT).show();
                    } else {

                        // AnswerActivity로 데이터 전송
                        Intent intent = new Intent(EmotionSelectActivity.this, AnswerActivity.class);
                        intent.putExtra("date", CurrentDate);
                        intent.putExtra("title", title);
                        intent.putExtra("content", content);
                        // 선택된 감정 전송
                        intent.putExtra("emotion", selectedEmotion);
                        startActivity(intent);
                    }
                }
            });

        }

    }
}