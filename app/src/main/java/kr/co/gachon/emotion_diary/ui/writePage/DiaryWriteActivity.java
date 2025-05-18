package kr.co.gachon.emotion_diary.ui.writePage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.data.AppDatabase;
import kr.co.gachon.emotion_diary.data.DiaryRepository;
import kr.co.gachon.emotion_diary.ui.emotion.EmotionSelectActivity;

public class DiaryWriteActivity extends AppCompatActivity {

    private EditText titleView, contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);


        titleView = findViewById(R.id.titleTextView);
        contentView = findViewById(R.id.contentTextView);

        // 날짜 받아오기
        long dateMillis = getIntent().getLongExtra("selectedDate", -1);
        if (dateMillis == -1) {
            Toast.makeText(this, "Invalid date selected. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Date selectedDate = new Date(dateMillis);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = formatter.format(selectedDate);
        Log.wtf("Test", formattedDate);

        // 날짜 표시
        TextView dateTextView = findViewById(R.id.dateTextView);
        dateTextView.setText(formattedDate);

        // ActionBar 설정
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.custom_back_bar);

            ImageButton backButton = actionBar.getCustomView().findViewById(R.id.backButtonActionBar);
            backButton.setOnClickListener(v -> finish());

            TextView titleTextView = actionBar.getCustomView().findViewById(R.id.titleTextViewActionBar);
            if (titleTextView != null) {
                titleTextView.setText("Diary Write");
            }
        }

        // DB 연결 및 기존 데이터 불러오기
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "diary-database").allowMainThreadQueries().build(); // allowMainThread는 임시
        DiaryRepository repository = new DiaryRepository(getApplication());

        // 수정하기 위해 DB안에 현재 날짜에 해당하는 최근 title과 content를 불러옴
        repository.getLatestDiaryByDateAsync(formattedDate, latestDiary -> {
            if (latestDiary != null) {
                titleView.setText(latestDiary.getTitle());
                contentView.setText(latestDiary.getContent());
            }
        });


        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        if (title != null) titleView.setText(title);
        if (content != null) contentView.setText(content);

        
        Button nextPageButton = findViewById(R.id.nextPage);
        nextPageButton.setOnClickListener(view -> {
            String titleText = titleView.getText().toString();
            String contentText = contentView.getText().toString();

            if (TextUtils.isEmpty(titleText)) {
                Toast.makeText(getBaseContext(), "제목이 비어있습니다.", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(contentText)) {
                Toast.makeText(getBaseContext(), "내용이 비어있습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Intent nextIntent = new Intent(DiaryWriteActivity.this, EmotionSelectActivity.class);
                nextIntent.putExtra("date", selectedDate.toString());
                nextIntent.putExtra("title", titleText);
                nextIntent.putExtra("content", contentText);
                startActivity(nextIntent);
            }
        });
    }
}
