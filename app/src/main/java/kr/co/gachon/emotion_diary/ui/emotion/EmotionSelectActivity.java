package kr.co.gachon.emotion_diary.ui.emotion;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Date;
import java.util.List;

import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.data.Diary;
import kr.co.gachon.emotion_diary.data.DiaryRepository;
import kr.co.gachon.emotion_diary.data.Emotions;
import kr.co.gachon.emotion_diary.helper.Helper;
import kr.co.gachon.emotion_diary.ui.taro.TaroActivity;

public class EmotionSelectActivity extends AppCompatActivity {

    String selectedEmotion = null;

    private Button previousButton = null;
    private DiaryRepository diaryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_emotion);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.custom_back_bar);

            ImageButton backButton = actionBar.getCustomView().findViewById(R.id.backButtonActionBar);
            backButton.setOnClickListener(v -> finish());

            TextView titleTextView = actionBar.getCustomView().findViewById(R.id.titleTextViewActionBar);
            if (titleTextView != null) titleTextView.setText("Emotion");
        }

        diaryRepository = new DiaryRepository(getApplication());

        Intent intent = getIntent();
        long dateMillis = intent.getLongExtra("date", -1);
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

        if (dateMillis == -1) {
            Toast.makeText(this, "Invalid date selected. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        makeEmotionButtons();

        Button nextPage = findViewById(R.id.nextPageButton);
        nextPage.setOnClickListener(v -> {
            v.setClickable(false);

            if (selectedEmotion == null) {
                Toast.makeText(EmotionSelectActivity.this, "감정을 선택하세요", Toast.LENGTH_SHORT).show();
                return;
            }

            Date currentDate = new Date(dateMillis);
            diaryRepository.insert(new Diary(title, content, currentDate, Emotions.getEmotionIdByText(selectedEmotion), -1, null));

            Intent intent1 = new Intent(EmotionSelectActivity.this, TaroActivity.class);
            intent1.putExtra("date", currentDate.getTime());
            intent1.putExtra("title", title);
            intent1.putExtra("content", content);
            intent1.putExtra("emotion", selectedEmotion);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
            v.setClickable(true);
        });
    }

    private void makeEmotionButtons() {
        GridLayout emotionGrid = findViewById(R.id.emotionGrid);

        List<Emotions.EmotionData> emotionList = Emotions.getAllEmotionDataList();
        for (Emotions.EmotionData emotion : emotionList) {
            String text = emotion.getText();
            String emoji = emotion.getEmoji();

            Button emojiButton = new Button(this);

            emojiButton.setText(String.format("%s\n%s", emoji, text));
            emojiButton.setContentDescription(text);
            emojiButton.setTextSize(20);
            emojiButton.setPadding(16, 16, 16, 16);
            emojiButton.setAllCaps(false);
            emojiButton.setBackgroundColor(Color.TRANSPARENT);
            emojiButton.setTextColor(Color.WHITE);
            emojiButton.setGravity(Gravity.CENTER);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            emojiButton.setLayoutParams(params);

            emojiButton.setOnClickListener(v -> {
                if (previousButton != null && previousButton != emojiButton) {
                    previousButton.setBackgroundColor(Color.TRANSPARENT);
                    previousButton.setTextColor(Color.WHITE);
                }

                emojiButton.setBackgroundColor(ContextCompat.getColor(EmotionSelectActivity.this, R.color.green));

                selectedEmotion = text;
                previousButton = emojiButton;
            });

            emotionGrid.addView(emojiButton);
        }
    }
}


