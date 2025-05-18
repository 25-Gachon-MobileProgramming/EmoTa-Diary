package kr.co.gachon.emotion_diary;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Calendar;
import java.util.Date;

import kr.co.gachon.emotion_diary.data.Diary;
import kr.co.gachon.emotion_diary.data.DiaryRepository;
import kr.co.gachon.emotion_diary.databinding.ActivityMainBinding;
import kr.co.gachon.emotion_diary.ui.writePage.DiaryWriteActivity;

public class MainActivity extends AppCompatActivity {

    /**
     * @noinspection FieldCanBeLocal
     */
    private ActivityMainBinding binding;

    // --------- Assign FOR DB TEST START---------
    /**
     * @noinspection FieldCanBeLocal
     */
    private DiaryRepository diaryRepository;
    // --------- Assign FOR DB TEST END-----------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_calendar, R.id.navigation_timeLine, R.id.navigation_myPage)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        FloatingActionButton diaryWriteButton = findViewById(R.id.diary_write_button);

        diaryWriteButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            Date todayDate = calendar.getTime();

            diaryRepository.getDiaryByDate(todayDate, diary -> {
                Intent intent = new Intent(getApplicationContext(), DiaryWriteActivity.class);
                intent.putExtra("selectedDate", System.currentTimeMillis());

                if (diary != null) {
                    // 같은 연, 월, 일에 해당하는 값을 가져오는 logic
                    intent.putExtra("title", diary.getTitle());
                    intent.putExtra("content", diary.getContent());
                    intent.putExtra("isUpdate", true);
                } else {
                    // 그 외 상황이면 작성하는 logic
                    intent.putExtra("isUpdate", false);
                }

                startActivity(intent);
            });
        });


        // --------- DB TEST START ---------
        diaryRepository = new DiaryRepository(getApplication());

        diaryRepository.insertDummyData();

        diaryRepository.getAllDiaries().observe(this, diaries -> {
            Log.d("RoomExample", "모든 일기 (Repository - ExecutorService):");

            for (Diary diary : diaries) {
                Log.d("RoomExample", "ID: " + diary.getId() + ", 제목: " + diary.getTitle() + ", 내용: " + diary.getContent() + ", 날짜: " + diary.getDate()  + ", 감정: " + diary.getEmotionText());
            }
        });

//         Diary newDiary = new Diary("title", "content",  Calendar.getInstance().getTime(), 1);
//         diaryRepository.insert(newDiary);


        // --------- DB TEST END ----------

    }
}
