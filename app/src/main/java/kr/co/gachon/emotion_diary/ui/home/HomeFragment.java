package kr.co.gachon.emotion_diary.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.databinding.FragmentHomeBinding;
import kr.co.gachon.emotion_diary.notification.AlarmScheduler;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupDatePager();

        return root;
    }

    private void setupDatePager() {
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = -5; i <= 1; i++) {
            dateList.add(today.plusDays(i));
        }

        DatePagerAdapter adapter = new DatePagerAdapter(dateList);
        binding.dateViewPager.setAdapter(adapter);
        binding.dateViewPager.setCurrentItem(5, false);

        // 캐러셀 효과
        binding.dateViewPager.setClipToPadding(false);
        binding.dateViewPager.setClipChildren(false);
        binding.dateViewPager.setOffscreenPageLimit(3);
        binding.dateViewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.dateViewPager.setPadding(120, 0, 120, 0);

        binding.dateViewPager.setPageTransformer((page, position) -> {
            float scale = 1 - Math.abs(position) * 0.2f;
            page.setScaleY(scale);
            page.setAlpha(0.5f + (1 - Math.abs(position)) * 0.5f);
        });

        binding.btn.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 1); // 현재 시간 + 1분

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            AlarmScheduler.scheduleDiaryReminder(requireContext(), hour, minute);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
