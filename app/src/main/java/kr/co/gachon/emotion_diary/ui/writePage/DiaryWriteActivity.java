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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.custom_back_bar);

            ImageButton backButton = actionBar.getCustomView().findViewById(R.id.backButtonActionBar);
            backButton.setOnClickListener(v -> finish());

            TextView titleTextView = actionBar.getCustomView().findViewById(R.id.titleTextViewActionBar);
            if (titleTextView != null) titleTextView.setText("Emotion");
        }

        long dateMillis = getIntent().getLongExtra("selectedDate", -1);

        // check that dateMillis is valid
        if (dateMillis == -1) {
            Toast.makeText(this, "Invalid date selected. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Date selectedDate = new Date(dateMillis);

        // 날짜 데이터 연도 월 일로 바꿔서 @stirng으로 받게 한 뒤 화면에 뜨게 만듬
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = formatter.format(selectedDate);

        TextView dateTextView = findViewById(R.id.dateTextView);
        dateTextView.setText(formattedDate);

        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

        EditText titleView = findViewById(R.id.titleTextView);
        titleView.setText(title);

        EditText contentView = findViewById(R.id.contentTextView);
        contentView.setText(content);

        Button nextPageButton = findViewById(R.id.nextPage);
        nextPageButton.setOnClickListener(view -> {
            String titleText = titleView.getText().toString();
            String contentText = contentView.getText().toString();

            if (TextUtils.isEmpty(titleText)) {
                Toast.makeText(getBaseContext(), "제목이 비어있습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(contentText)) {
                Toast.makeText(getBaseContext(), "내용이 비어있습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent writePageIntent = new Intent(DiaryWriteActivity.this, EmotionSelectActivity.class);
            writePageIntent.putExtra("date", selectedDate.getTime());
            writePageIntent.putExtra("title", titleText);
            writePageIntent.putExtra("content", contentText);
            startActivity(writePageIntent);
        });
    }
}