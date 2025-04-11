package kr.co.gachon.emotion_diary.ui.emotion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import kr.co.gachon.emotion_diary.MainActivity;
import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.ui.writePage.DiaryWriteActivity;

public class EmotionSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_emotion);

        Intent intent = getIntent();

        //String 형태로 데이터 저장
        String CurrentDate = intent.getStringExtra("date");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

        Log.wtf("getTest",CurrentDate);
        Log.wtf("getTest",title);
        Log.wtf("getTest",content);

        TextView dateView = findViewById(R.id.emotionDate);      // 날짜 표시용
        TextView titleView = findViewById(R.id.emotionTitle);    // 제목 표시용
        TextView contentView = findViewById(R.id.emotionContent); // 내용 표시용
        TextView dateView = findViewById(R.id.emotionDate);
        TextView titleView = findViewById(R.id.emotionTitle);
        TextView contentView = findViewById(R.id.emotionContent);

        if (dateView != null) dateView.setText(CurrentDate);
        if (titleView != null) titleView.setText(title);
        if (contentView != null) contentView.setText(content);

        Log.wtf("newTest", title);
        Log.wtf("newTest", content);
        Log.wtf("newTest", CurrentDate);
        Button backbutton2 = findViewById(R.id.backButton2);
        backbutton2.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DiaryWriteActivity.class);
                startActivity(intent);
            }
        }));


    }
}
