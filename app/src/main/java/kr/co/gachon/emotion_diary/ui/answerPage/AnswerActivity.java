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

        // Toast로 wifi 연결 상태 보여줌 - 작동은 맨 아래 코드
        if (wifiConnected(AnswerActivity.this)) {
            Toast.makeText(AnswerActivity.this, "Wi-Fi가 연결되었습니다", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AnswerActivity.this, "Wi-Fi가 연결 않되었습니다", Toast.LENGTH_SHORT).show();
        }

        // 백버튼 잠시 보류
        // EmotionSelectActivity로 돌아가는 버튼
//        Button backButton3 = findViewById(R.id.backButton3);
//        backButton3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), EmotionSelectActivity.class);
//
//                // EmotionSelectActivity로 정보 전송
//                intent.putExtra("date",CurrentDate);
//                intent.putExtra("title",title);
//                intent.putExtra("content",content);
//                startActivity(intent);
//            }});
    }

    // wifi 유무 판단 코드
    public static boolean wifiConnected(Context context) {
        // getSystemService가 시스템 서비스 가져옴
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // wifi에 대한 정보가 없을 때 false값 반환
        if (connectivityManager == null) {
            return false;
        }

        // 버전확인 코드 [NetworkCapabilities라는 코드가 버전 6.0이상 에서만 사용 가능하기 때문]
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Network network = connectivityManager.getActiveNetwork();

            if (network == null) {
                return false;
            }
            // NetworkCapabilities 코드로 네트워크 확인
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            if (capabilities == null) {
                return false;
            }

            // 만족하면 true 반환해서 wifi연결상태 코드로 전송
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        }

        return false;
    }
}
