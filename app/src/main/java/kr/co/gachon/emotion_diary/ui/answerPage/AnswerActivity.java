package kr.co.gachon.emotion_diary.ui.answerPage;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import kr.co.gachon.emotion_diary.R;

public class AnswerActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_answer);

        Intent intent = getIntent();

        // 정보 받고 확인
        String CurrentDate = intent.getStringExtra("date");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String emotion = intent.getStringExtra("emotion");

        Log.wtf("get2Test",CurrentDate);
        Log.wtf("get2Test",title);
        Log.wtf("get2Test",content);
        Log.wtf("get2Test",emotion);

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
                titleTextView.setText("Answer");
            }

        }

        TextView textView = findViewById(R.id.answer);


        // Toast로 wifi 연결 상태 보여줌 - 작동은 맨 아래 코드
        if (wifiConnected(AnswerActivity.this)) {
            Toast.makeText(AnswerActivity.this, "Wi-Fi가 연결되었습니다", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(AnswerActivity.this, "Wi-Fi가 연결 않되었습니다", Toast.LENGTH_SHORT).show();
        }

    }

    // wifi 유무 판단 코드
    public static boolean wifiConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return false;
            }
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            if (capabilities == null) {
                return false;
            }
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        }
        return false;
    }
}
