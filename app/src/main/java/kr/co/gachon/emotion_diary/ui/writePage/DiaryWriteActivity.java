package kr.co.gachon.emotion_diary.ui.writePage;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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

        // 나짜 데이터 연도 월 일로 바꿔서 @stirng으로 받게 한 뒤 화면에 뜨게 만듬
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = formatter.format(selectedDate);
        Log.wtf("Test", formattedDate);

        TextView dateTextView = findViewById(R.id.dateTextView);
        dateTextView.setText(formattedDate);

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
                titleTextView.setText("Diary Write");
            }
        }

            Intent intent = getIntent();

            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");

            EditText titleView = findViewById(R.id.titleTextView);
            titleView.setText(title);

            EditText contentView = findViewById(R.id.contentTextView);
            contentView.setText(content);

            Button nextPageButton = findViewById(R.id.nextPage); // ImageButton 대신 MaterialButton으로 변경
            nextPageButton.setOnClickListener(new View.OnClickListener() {
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
        }
    }