package kr.co.gachon.emotion_diary.ui.Remind;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kr.co.gachon.emotion_diary.R;



import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import kr.co.gachon.emotion_diary.data.AppDatabase;
import kr.co.gachon.emotion_diary.data.DiaryDao;
import kr.co.gachon.emotion_diary.data.DiaryRepository;
import kr.co.gachon.emotion_diary.databinding.RemindBinding;


public class RemindActivity extends AppCompatActivity {

    private RemindBinding binding;
    private CircleGraphView circleGraphView;

    private Boolean isMonthly;

    private DiaryRepository diaryRepository;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remind);

        binding = RemindBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        // boolean 값 추출 (key는 보낸 쪽에서 쓴 것과 똑같아야 함!)
        isMonthly = intent.getBooleanExtra("isMonthly", false);

        setupCircleGraphView();

    }
    private void rateOfWrite(boolean isMonthly, Consumer<Float> callback) {
        float days = getLastDayOfCurrentMonth();
        TextView day = findViewById(R.id.rateText);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplication());
            DiaryDao diaryDao = db.diaryDao();

            if (diaryDao == null) {
                Log.e("rateOfWrite", "DiaryDao is null");
                runOnUiThread(() -> callback.accept(0f));
                day.setText((int)days+ "일 중 총 n일 작성했어요");
                return;
            }

            if (isMonthly == true){ //월간

                int count = diaryDao.getDiaryCountPerDay("2025-03-01", "2025-04-07"); // diaryDao가 null이 아니므로  //데이터 추가


                day.setText((int)days+ "일 중 총 "+ (int)count + "일 작성했어요");
                float rate = (count / days) * 100f;

                runOnUiThread(() -> callback.accept(rate));
            }else{//연간
                int count = diaryDao.getDiaryCountPerDay("2024-04-07", "2025-04-07"); // diaryDao가 null이 아니므로  //데이터 추가


                day.setText("365일 중 총 "+ (int)count + "일 작성했어요");
                float rate = (count / 365) * 100f;

                runOnUiThread(() -> callback.accept(rate));
            }
        });
    }

    public int getLastDayOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance(); // 현재 날짜
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당 달의 마지막 일

        return lastDay;
    }
    private void setupCircleGraphView() {
        circleGraphView = binding.circle;
        rateOfWrite(isMonthly, rate -> {
            float roundedRate = Math.round(rate);
            if (roundedRate < 1){ // 너무 작으면 최소값 0.1 단위로 표시
                circleGraphView.animateSection(0, 0f, rate);         // 작성한 비율
                circleGraphView.animateSection(1, 0f, 100f - rate);    // 작성 안 한 비율
            }else {
                circleGraphView.animateSection(0, 0f, roundedRate);         // 작성한 비율
                circleGraphView.animateSection(1, 0f, 100f - roundedRate);    // 작성 안 한 비율
            }
        });
    }

    }
}
