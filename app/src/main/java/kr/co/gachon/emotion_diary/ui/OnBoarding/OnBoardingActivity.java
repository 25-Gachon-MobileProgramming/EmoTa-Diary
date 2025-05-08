package kr.co.gachon.emotion_diary.ui.OnBoarding;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import kr.co.gachon.emotion_diary.R;

public class OnBoardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private OnBoardingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        adapter = new OnBoardingAdapter(getSupportFragmentManager(), getLifecycle());

        // 페이지 추가
        adapter.addFragment(OnBoardingPageFragment.newInstance(
                "일기 작성",
                "당신의 하루를 기록해보세요",
                R.drawable.img_diary
        ));
        adapter.addFragment(OnBoardingPageFragment.newInstance(
                "감정 통계",
                "어떤 날들을 보냈는지 \n한 눈에 확인할 수 있어요",
                R.drawable.img_calender
        ));
        adapter.addFragment(OnBoardingPageFragment.newInstance(
                "타로",
                "당신의 하루를 타로로 마무리하세요",
                R.drawable.tarot
        ));

        viewPager.setAdapter(adapter);

        // DotsIndicator 연결 (온보딩 스크롤바에 필요)
        WormDotsIndicator dotsIndicator = findViewById(R.id.dotsIndicator);
        dotsIndicator.setViewPager2(viewPager);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
    }
}
