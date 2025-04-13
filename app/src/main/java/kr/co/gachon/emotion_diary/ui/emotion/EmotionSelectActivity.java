package kr.co.gachon.emotion_diary.ui.emotion;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.ui.answerPage.AnswerActivity;
import kr.co.gachon.emotion_diary.ui.writePage.DiaryWriteActivity;

public class EmotionSelectActivity extends AppCompatActivity {

    // 버튼 상태 확인 버튼
    Button pressButton = null;

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

        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);

        // 다음 버튼 누를 때 전에 누른 버튼 초기화
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pressButton != null) {
                    pressButton.setBackgroundColor(Color.BLACK);
                }
                // button을 view를 버튼처럼 사용
                pressButton = (Button) view;
                pressButton.setBackgroundColor(Color.WHITE);
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
                    intent.putExtra("emotion", pressButton.getText().toString());
                    startActivity(intent);

                }}
        });

        // 백버튼 잠시 보류
        // DiaryWriteActivity로 돌아가는 버튼
//        Button backbutton2 = findViewById(R.id.backButton2);
//        backbutton2.setOnClickListener((new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), DiaryWriteActivity.class);
//                intent.putExtra("date", CurrentDate);
//                intent.putExtra("title", title);
//                intent.putExtra("content", content);
//            }}));
    }

}