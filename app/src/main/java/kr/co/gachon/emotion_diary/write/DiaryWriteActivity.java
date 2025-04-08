package kr.co.gachon.emotion_diary.write;

import android.os.Bundle;
import android.util.Log; // 로그 추가
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;

import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.data.DiaryRepository;

public class DiaryWriteActivity extends AppCompatActivity {

    private DiaryRepository diaryRepository;

    //activity_main.xml에 만든 activity_diary_write라는 버튼이 눌렸을 때 이벤트 발생
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        // 그리고 여기서 4번쨰 줄에서 fragment_container라고 따로 정의한 이유가
        //  무거워졌다는 내용의 글을봐 activit_diary_write에 따로 동적느낌으로 만들라고 해서 넘겨봤습니다

        DiaryWriteFirstFragment fragment = new DiaryWriteFirstFragment(); //fragmnet에 대한 객체 생성
        FragmentManager fragmentManager = getSupportFragmentManager(); //fragment 다룰 수 있는 코드
        FragmentTransaction transaction = fragmentManager.beginTransaction(); //화면 변경작업 추가
        transaction.replace(R.id.fragment_container, fragment); //frame laayout 안에 띄움
        transaction.commit();


        //날짜가 왜 계속 안넘어 왔나 했는데 제가 스펠링 하나 틀린걸 썼네여
        //selectedDate 라는 문장이어야 하는데 1개가 selectDate라고 적혀 있드라고여
        //주의!!!!!!!!!!
        long dateMillis = getIntent().getLongExtra("selectedDate", -1);
        if (dateMillis != -1) {
            Date selectedDate = new Date(dateMillis);

        //데이터 흐름 확인용
            Log.d("DiaryWriteActivity", "받은 날짜 : " + selectedDate.toString());


            DiaryWriteFirstViewModel viewModel = new ViewModelProvider(this).get(DiaryWriteFirstViewModel.class);
            viewModel.setSelectedDate(selectedDate);

        //데이터 흐름 확잉용
            Log.d("DiaryWriteActivity", "ViewModel 날짜: " + viewModel.getSelectedDate().getValue());


            diaryRepository = new DiaryRepository(getApplication());

        } else {
            Log.e("DiaryWriteActivity", "selectedDate 인텐트 값이 존재하지 않음");  //데이터 흐름 확인용
        }
    }
}
