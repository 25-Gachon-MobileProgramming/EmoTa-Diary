package kr.co.gachon.emotion_diary.ui.answerPage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import kr.co.gachon.emotion_diary.MainActivity;
import kr.co.gachon.emotion_diary.R;

public class AnswerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_answer);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.custom_back_bar);

            Toolbar parent = (Toolbar) actionBar.getCustomView().getParent();
            parent.setContentInsetsAbsolute(0, 0);

            ImageButton backButton = actionBar.getCustomView().findViewById(R.id.backButtonActionBar);
            backButton.setOnClickListener(v -> showExitConfirmationDialog());

            TextView titleTextView = actionBar.getCustomView().findViewById(R.id.titleTextViewActionBar);
            if (titleTextView != null) titleTextView.setText("Answer");
        }

        this.getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });

        Intent intent = getIntent();
        String gptReply = intent.getStringExtra("gptReply");
        String cardName = getIntent().getStringExtra("taroCard");

        ImageView taroImage = findViewById(R.id.taro);

        int imageResId = getResources().getIdentifier("taro_" + cardName, "drawable", getPackageName());
        taroImage.setImageResource(imageResId != 0 ? imageResId : R.drawable.card_back);
        if (imageResId == 0) {
            Toast.makeText(this, "타로카드를 불러오기 실패했습니다.", Toast.LENGTH_SHORT).show();
        }

        TextView textView = findViewById(R.id.answer);
        textView.setText(gptReply);

        findViewById(R.id.room_button).setOnClickListener(v -> finishToMainActivity());
    }

    private void finishToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void showExitConfirmationDialog() {
        AlertDialog dialogBuilder = new AlertDialog.Builder(this)
                .setTitle("종료 확인")
                .setMessage("정말 일기 결과 확인을 종료하시겠습니까?")
                .setPositiveButton("예", (dialog, which) -> finishToMainActivity())
                .setNegativeButton("아니오", (dialog, which) -> dialog.dismiss())
                .show();

        dialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.colorSecondary));
        dialogBuilder.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.colorSecondary));
    }
}
