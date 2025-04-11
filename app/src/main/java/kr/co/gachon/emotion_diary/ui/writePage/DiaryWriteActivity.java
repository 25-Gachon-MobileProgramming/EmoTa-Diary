package kr.co.gachon.emotion_diary.ui.writePage;

import static kr.co.gachon.emotion_diary.data.Converters.CurrentDate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

import kr.co.gachon.emotion_diary.MainActivity;
import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.ui.emotion.EmotionSelectActivity;

public class DiaryWriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        long dateMillis = getIntent().getLongExtra("selectedDate", -1);

        // check that dateMillis is valid
        assert (dateMillis != -1);

        Date selectedDate = new Date(dateMillis);

        Log.wtf("Test", selectedDate.toString());

        // 가공후 포멧으로 값 가져옴
        //가공후 포멧으로 값 가져옴
        String CurrentDate = CurrentDate(selectedDate);

        TextView textView = findViewById(R.id.dateTextView);
        textView.setText(CurrentDate);

        Log.wtf("Test", CurrentDate.toString());

        EditText title = findViewById(R.id.titleTextView);
        EditText content = findViewById(R.id.contentTextView);

        // 현재 시간 제목 내용을 다음 Emotionactivity로 보냄
        //현재 시간 제목 내용을 다음 Emotionactivity로 보냄
        Button button = findViewById(R.id.nextPage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String titleText = title.getText().toString();
                String contentText = content.getText().toString();

                // 비어 있는지 확인하는 코드
                if (TextUtils.isEmpty(titleText) || TextUtils.isEmpty(contentText)) {
                    Toast.makeText(getBaseContext(), "제목이랑 내용 중 1개가 비어있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(DiaryWriteActivity.this, EmotionSelectActivity.class);
                    intent.putExtra("date", CurrentDate);
                    intent.putExtra("title", titleText);
                    intent.putExtra("content", contentText);
                    startActivity(intent);
                }
            }
        });
        Button backbutton = findViewById(R.id.backButton);
        backbutton.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }));
    }
}