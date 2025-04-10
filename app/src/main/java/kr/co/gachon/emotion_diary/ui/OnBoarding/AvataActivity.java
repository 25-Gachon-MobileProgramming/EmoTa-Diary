package kr.co.gachon.emotion_diary.ui.OnBoarding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import kr.co.gachon.emotion_diary.MainActivity;
import kr.co.gachon.emotion_diary.R;
public class AvataActivity extends AppCompatActivity {
    Button button;
    EditText editText;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_avata);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        context = getApplicationContext();
        button = findViewById(R.id.button);
        editText = findViewById(R.id.nickname);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = editText.getText().toString();

                Toast.makeText(context,"Welcome,"+input, Toast.LENGTH_SHORT).show();

                // 다음 화면으로 넘어가는 코드
                Intent intent = new Intent(AvataActivity.this, MainActivity.class);
                intent.putExtra("nickname", input); // 닉네임 데이터를 다음 화면으로 전달
                startActivity(intent);
            }
        });
    }
}