package kr.co.gachon.emotion_diary.ui.writePage;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

import kr.co.gachon.emotion_diary.R;

public class DiaryWriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        long dateMillis = getIntent().getLongExtra("selectedDate", -1);

        // check that dateMillis is valid
        assert (dateMillis != -1);

        Date selectedDate = new Date(dateMillis);

        Log.wtf("Test", selectedDate.toString());
    }
}
