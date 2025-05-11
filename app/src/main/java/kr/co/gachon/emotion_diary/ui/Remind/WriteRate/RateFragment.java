package kr.co.gachon.emotion_diary.ui.Remind.WriteRate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import kr.co.gachon.emotion_diary.data.AppDatabase;
import kr.co.gachon.emotion_diary.data.DiaryDao;
import kr.co.gachon.emotion_diary.databinding.FragmentCircleGraphBinding;
import kr.co.gachon.emotion_diary.ui.Remind.emotionStatistics.EmotionStatisticsActivity;

public class RateFragment extends Fragment {

    public interface RateTextListener {
        void onRateTextUpdated(String text);
    }

    private FragmentCircleGraphBinding binding;
    private CircleGraphView circleGraphView;
    private boolean isMonthly;
    private RateTextListener rateTextListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCircleGraphBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            isMonthly = getArguments().getBoolean("isMonthly", false);
        }

        // 리스너 연결 (Activity가 implements 했는지 확인)
        if (getActivity() instanceof RateTextListener) {
            rateTextListener = (RateTextListener) getActivity();
        }

        setupCircleGraphView();
        return binding.getRoot();
    }

    private void rateOfWrite(boolean isMonthly, Consumer<Float> callback) {
        float days = getLastDayOfCurrentMonth();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(requireContext());
            DiaryDao diaryDao = db.diaryDao();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String today = sdf.format(new Date());

            // 한 달 전
            Calendar calMonth = Calendar.getInstance();
            calMonth.add(Calendar.MONTH, -1);
            Date oneMonthAgo = calMonth.getTime();
            String oneMonthAgoStr = sdf.format(oneMonthAgo);

            // 일 년 전
            Calendar calYear = Calendar.getInstance();
            calYear.add(Calendar.YEAR, -1);
            Date oneYearAgo = calYear.getTime();
            String oneYearAgoStr = sdf.format(oneYearAgo);

            try {
                Date startDate, endDate;
                int count;

                if (isMonthly) {
                    startDate = sdf.parse(oneMonthAgoStr);
                    endDate = sdf.parse(today);
                    count = diaryDao.getDiaryCountPerDay(startDate, endDate);
                    float rate = (count / days) * 100f;

                    String result = (int) days + "일 중 총 " + count + "일 작성했어요";
                    runOnUiThread(rate, result, callback);

                } else {
                    startDate = sdf.parse(oneYearAgoStr);
                    endDate = sdf.parse(today);
                    count = diaryDao.getDiaryCountPerDay(startDate, endDate);
                    float rate = (count / 365f) * 100f;

                    String result = "365일 중 총 " + count + "일 작성했어요";
                    runOnUiThread(rate, result, callback);
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(0f, "날짜 변환 실패", callback);
            }
        });
    }

    private void runOnUiThread(float rate, String text, Consumer<Float> callback) {
        requireActivity().runOnUiThread(() -> {
            if (rateTextListener != null) {
                rateTextListener.onRateTextUpdated(text);  // Activity에 전달
            }
            callback.accept(rate);
        });
    }

    private void setupCircleGraphView() {
        circleGraphView = binding.circle;
        rateOfWrite(isMonthly, rate -> {
            float roundedRate = Math.round(rate);
            if (roundedRate < 1) {
                circleGraphView.animateSection(0, 0f, rate);
                circleGraphView.animateSection(1, 0f, 100f - rate);
            } else {
                circleGraphView.animateSection(0, 0f, roundedRate);
                circleGraphView.animateSection(1, 0f, 100f - roundedRate);
            }
        });
    }

    public int getLastDayOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
