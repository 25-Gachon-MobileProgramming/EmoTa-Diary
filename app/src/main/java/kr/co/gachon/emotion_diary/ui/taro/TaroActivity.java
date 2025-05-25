package kr.co.gachon.emotion_diary.ui.taro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import kr.co.gachon.emotion_diary.BuildConfig;
import kr.co.gachon.emotion_diary.MainActivity;
import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.data.Gpt.GptApiService;
import kr.co.gachon.emotion_diary.data.Gpt.GptRequest;
import kr.co.gachon.emotion_diary.data.Gpt.GptResponse;
import kr.co.gachon.emotion_diary.helper.Helper;
import kr.co.gachon.emotion_diary.ui.answerPage.AnswerActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaroActivity extends AppCompatActivity {
    private int selectedCardIndex = -1;
    private String selectedCardTitle = "";

    List<String> cardTitles = new ArrayList<>(Arrays.asList(
            "fool", "magician", "highpriestess", "empress", "hierophant", "lovers", "chariot", "strength", "hermit",
            "wheeloffortune", "justice", "hangedman", "death", "temperance", "devil", "tower", "star", "moon",
            "sun", "judgment", "world", "top"
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_taro);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.custom_back_bar);

            ImageButton backButton = actionBar.getCustomView().findViewById(R.id.backButtonActionBar);
            backButton.setOnClickListener(v -> finish());

            TextView titleTextView = actionBar.getCustomView().findViewById(R.id.titleTextViewActionBar);
            if (titleTextView != null) titleTextView.setText("Taro Card");
        }

        this.getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishToMainActivity();
            }
        });

        Helper helper = new Helper(this);

        if (!helper.checkNetworkState())
            finishToMainActivity();

        Intent intent = getIntent();
        long dateMillis = intent.getLongExtra("date", -1);
        String content = intent.getStringExtra("content");
        String selectedEmotion = intent.getStringExtra("emotion");
        String title = intent.getStringExtra("title");

        if (dateMillis == -1) {
            Toast.makeText(this, "Invalid date selected. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Date currentDate = new Date(dateMillis);

        ImageButton cardTopLeft = findViewById(R.id.card_top_left);
        ImageButton cardTopRight = findViewById(R.id.card_top_right);
        ImageButton cardBottomLeft = findViewById(R.id.card_bottom_left);
        ImageButton cardBottomRight = findViewById(R.id.card_bottom_right);
        Button nextButton = findViewById(R.id.next_button);

        View.OnClickListener cardClickListener = view -> {
            if (view != cardTopLeft) cardTopLeft.setAlpha(0.3f);
            if (view != cardTopRight) cardTopRight.setAlpha(0.3f);
            if (view != cardBottomLeft) cardBottomLeft.setAlpha(0.3f);
            if (view != cardBottomRight) cardBottomRight.setAlpha(0.3f);

            view.setAlpha(1f);

            Random random = new Random();
            selectedCardIndex = random.nextInt(cardTitles.size());
            selectedCardTitle = cardTitles.get(selectedCardIndex);

            nextButton.setEnabled(true);
        };

        cardTopLeft.setOnClickListener(cardClickListener);
        cardTopRight.setOnClickListener(cardClickListener);
        cardBottomLeft.setOnClickListener(cardClickListener);
        cardBottomRight.setOnClickListener(cardClickListener);

        nextButton.setOnClickListener(v -> {
            if (selectedCardIndex != -1) {

                ImageButton selectedCard = null;

                if (cardTopLeft.getAlpha() == 1f) selectedCard = cardTopLeft;
                else if (cardTopRight.getAlpha() == 1f) selectedCard = cardTopRight;
                else if (cardBottomLeft.getAlpha() == 1f) selectedCard = cardBottomLeft;
                else if (cardBottomRight.getAlpha() == 1f) selectedCard = cardBottomRight;

                if (selectedCard != null) flipCard(selectedCard);

                String prompt = "타로 카드 제목에 해당하는 내용과 내가 일기장에 쓴 내용을 종합하여 사람이 해주는 느낌으로 세 문장 정도 위로 글을 적어줘.\n내용: " + content + "\n타로 카드 제목: " + selectedCardTitle;

                List<GptRequest.Message> messages = new ArrayList<>();
                messages.add(new GptRequest.Message("user", prompt));

                GptRequest request = new GptRequest("gpt-3.5-turbo", messages, 0.7);

                // retrofit : java코드를 json형태로 변환
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.openai.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                GptApiService apiService = retrofit.create(GptApiService.class);

                String apiKey = "Bearer " + BuildConfig.API_KEY;

                Call<GptResponse> call = apiService.getGptMessage(apiKey, request);

                call.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<GptResponse> call, @NonNull Response<GptResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String gptResult = response.body().choices.get(0).message.content;
                            Intent sendintent = new Intent(TaroActivity.this, AnswerActivity.class);
                            sendintent.putExtra("gptReply", gptResult);
                            sendintent.putExtra("date", currentDate);
                            sendintent.putExtra("title", title);
                            sendintent.putExtra("content", content);
                            sendintent.putExtra("emotion", selectedEmotion);
                            sendintent.putExtra("taroCard", selectedCardTitle);
                            startActivity(sendintent);
                        } else {
                            Toast.makeText(TaroActivity.this, "GPT 응답 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GptResponse> call, @NonNull Throwable t) {
                        Toast.makeText(TaroActivity.this, "API 호출 실패: ", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(TaroActivity.this, "카드를 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        nextButton.setEnabled(false);
    }

    private void finishToMainActivity() {
        Toast.makeText(this, "네트워크가 없습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    // 카드 회전 애니메이션 후 drawable에 있는 카드 이미지 설정
    private void flipCard(final ImageButton card) {
        card.animate()
                .rotationY(90f)
                .setDuration(150)
                .withEndAction(() -> {
                    int imageResId = getResources().getIdentifier(
                            selectedCardTitle,
                            "drawable",
                            getPackageName()
                    );

                    if (imageResId != 0) {
                        card.setImageResource(imageResId);
                    } else {
                        card.setImageResource(R.drawable.card_back);
                        Toast.makeText(TaroActivity.this, "이미지 리소스를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }

                    card.animate().rotationY(0f).setDuration(700).start();
                })
                .start();
    }
}