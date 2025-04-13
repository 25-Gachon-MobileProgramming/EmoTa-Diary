package kr.co.gachon.emotion_diary.ui.writePage;
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

        Intent intent = getIntent();

        // DiaryWriteActivity에서 보낸 정보 받기
        String CurrentDate = intent.getStringExtra("date");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

//        Log.wtf("Test", CurrentDate);
//        Log.wtf("Test", title);
//        Log.wtf("Test", content);

        TextView textView = findViewById(R.id.dateTextView);
        textView.setText(CurrentDate);

        EditText titleView = findViewById(R.id.titleTextView);
        titleView.setText(title);

        EditText contentView = findViewById(R.id.contentTextView);
        contentView.setText(content);

        // 시간 제목 내용을 Emotionactivity로 보냄
        Button button = findViewById(R.id.nextPage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String titleText = titleView.getText().toString();
                String contentText = contentView.getText().toString();

                // 비어 있는지 확인하는 코드
                if (TextUtils.isEmpty(titleText) || TextUtils.isEmpty(contentText)) {
                    Toast.makeText(getBaseContext(), "제목이랑 내용 중 1개가 비어있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(DiaryWriteActivity.this, EmotionSelectActivity.class);
                    intent.putExtra("date", selectedDate.toString());
                    intent.putExtra("title", titleText);
                    intent.putExtra("content", contentText);
                    startActivity(intent);
                }
            }
        });

        // 백버튼 잠시 보류
        // MainActivity로 화면 이동
//        Button backbutton = findViewById(R.id.backButton);
//        backbutton.setOnClickListener((new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//            }
//        }));
    }
}