package kr.co.gachon.emotion_diary.ui.Remind.WriteRate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
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

    private String term;
    private RateTextListener rateTextListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCircleGraphBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            isMonthly = getArguments().getBoolean("isMonthly", false);
            term = getArguments().getString("term", "12");
        }

        // 리스너 연결 (Activity가 implements 했는지 확인)
        if (getActivity() instanceof RateTextListener) {
            rateTextListener = (RateTextListener) getActivity();
        }

        setupCircleGraphView();
        return binding.getRoot();
    }

    private void rateOfWrite(boolean isMonthly, Consumer<Float> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(requireContext());
            DiaryDao diaryDao = db.diaryDao();

            try {
                Pair<Date, Date> range = getDateRangeFromTerm(term);
                Date startDate = range.first;
                Date endDate = range.second;

                int count = diaryDao.getDiaryCountPerDay(startDate, endDate);

                float days = isMonthly
                        ? getDaysBetween(startDate, endDate) // 정확한 일수 계산
                        : 365f;

                float rate = (count / days) * 100f;

                String result;
                if (isMonthly) {
                    SimpleDateFormat monthFormat = new SimpleDateFormat("M", Locale.getDefault());
                    String monthText = monthFormat.format(startDate); // 예: "5"
                    result = monthText + "월에 " + (int) days + "일 중 총 " + count + "일 작성했어요";
                } else {
                    result = "365일 중 총 " + count + "일 작성했어요";
                }

                runOnUiThread(rate, result, callback);
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(0f, "날짜 계산 실패", callback);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public static Pair<Date, Date> getDateRangeFromTerm(String term) throws ParseException {
        Calendar calStart = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();

        if (term.matches("^\\d{4}-\\d{1,2}$")) {
            // term이 "2024-5" 또는 "2024-05" 형태
            String[] parts = term.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1; // Calendar는 0-based

            // 시작일: YYYY-MM-01 00:00:00
            calStart.set(year, month, 1, 0, 0, 0);
            calStart.set(Calendar.MILLISECOND, 0);

            // 종료일: 해당 월의 마지막 날 23:59:59
            calEnd.set(year, month, 1);
            int lastDay = calEnd.getActualMaximum(Calendar.DAY_OF_MONTH);
            calEnd.set(Calendar.DAY_OF_MONTH, lastDay);
            calEnd.set(Calendar.HOUR_OF_DAY, 23);
            calEnd.set(Calendar.MINUTE, 59);
            calEnd.set(Calendar.SECOND, 59);
            calEnd.set(Calendar.MILLISECOND, 999);
        } else if (term.matches("^\\d{4}$")) {
            // term이 "2024" 형태
            int year = Integer.parseInt(term);

            // 시작일: YYYY-01-01 00:00:00
            calStart.set(year, Calendar.JANUARY, 1, 0, 0, 0);
            calStart.set(Calendar.MILLISECOND, 0);

            // 종료일: YYYY-12-31 23:59:59
            calEnd.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
            calEnd.set(Calendar.MILLISECOND, 999);
        } else {
            throw new IllegalArgumentException("Invalid term format: " + term);
        }

        return new Pair<>(calStart.getTime(), calEnd.getTime());
    }
    private int getDaysBetween(Date start, Date end) {
        long diffInMillis = end.getTime() - start.getTime();
        return (int) Math.ceil(diffInMillis / (1000.0 * 60 * 60 * 24)); // 일 수 계산
    }

}
