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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import kr.co.gachon.emotion_diary.BuildConfig;
import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.ui.Gpt.GptApiService;
import kr.co.gachon.emotion_diary.ui.Gpt.GptRequest;
import kr.co.gachon.emotion_diary.ui.Gpt.GptResponse;
import kr.co.gachon.emotion_diary.ui.answerPage.AnswerActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                        String prompt = "내가 보낸 감정과 내용을 기반으로 사람이 진심으로 위로해주는 3문장 글좀 적어줘 .\n내용: " + content + "\n감정: " + selectedEmotion;

                        List<GptRequest.Message> messages = new ArrayList<>();
                        messages.add(new GptRequest.Message("user", prompt));

                        // temperature이 0은 보수적이고 정확한거, 1에 가까울 수록 이상한 답변해줌 0.7이 표준
                        GptRequest request = new GptRequest("gpt-3.5-turbo", messages, 0.7);

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://api.openai.com/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        // retrofit : java를 json형태로 바꾸기
                        GptApiService apiService = retrofit.create(GptApiService.class);

                        String apiKey = "Bearer " + BuildConfig.API_KEY;

                        Call<GptResponse> call = apiService.getGptMessage(apiKey, request);

                        call.enqueue(new Callback<GptResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<GptResponse> call, @NonNull Response<GptResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {

                                    // 첫 번째 본문 텍스트를 추출하여 변수에 저장하는 logic
                                    String gptResult = response.body().choices.get(0).message.content;

                                    Intent intent = new Intent(EmotionSelectActivity.this, AnswerActivity.class);
                                    intent.putExtra("gptReply", gptResult);
                                    intent.putExtra("date", CurrentDate);
                                    intent.putExtra("title", title);
                                    intent.putExtra("content", content);
                                    intent.putExtra("emotion", selectedEmotion);

                                    startActivity(intent);
                                } else {
                                    Toast.makeText(EmotionSelectActivity.this, "GPT 응답 실패", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<GptResponse> call, @NonNull Throwable t) {
                                Toast.makeText(EmotionSelectActivity.this, "API 호출 실패: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });

        }

    }
}