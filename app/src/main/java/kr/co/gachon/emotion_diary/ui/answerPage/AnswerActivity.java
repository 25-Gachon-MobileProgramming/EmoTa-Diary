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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.ui.emotion.EmotionSelectActivity;

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

        TextView textView = findViewById(R.id.answer);

        // Toast로 wifi 연결 상태 보여줌 - 작동은 맨 아래 코드
        if (wifiConnected(AnswerActivity.this)) {
            Toast.makeText(AnswerActivity.this, "Wi-Fi가 연결되었습니다", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AnswerActivity.this, "Wi-Fi가 연결 않되었습니다", Toast.LENGTH_SHORT).show();
        }

        // EmotionSelectActivity로 돌아가는 버튼
        Button backButton3 = findViewById(R.id.backButton3);
        backButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }});
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
