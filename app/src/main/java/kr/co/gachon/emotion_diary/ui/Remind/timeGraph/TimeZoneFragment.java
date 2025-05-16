package kr.co.gachon.emotion_diary.ui.Remind.timeGraph;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.data.AppDatabase;
import kr.co.gachon.emotion_diary.data.DiaryDao;

public class TimeZoneFragment extends Fragment {

    private TimeGraph graphView;
    private boolean isMonthly;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_graph, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        graphView = view.findViewById(R.id.timeGraph);
        TextView placeholder = view.findViewById(R.id.time_zone_hint);

        if (getArguments() != null) {
            isMonthly = getArguments().getBoolean("isMonthly", true);
            Log.d("TimeGraph", "💡 전달받은 isMonthly: " + isMonthly);
        }

        AppDatabase db = AppDatabase.getDatabase(requireContext());
        DiaryDao diaryDao = db.diaryDao();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // 날짜 범위 계산
        Calendar cal = Calendar.getInstance();
        if (isMonthly) {
            cal.add(Calendar.MONTH, -1);
        } else {
            cal.add(Calendar.YEAR, -1);
        }
        String rangeStartStr = sdf.format(cal.getTime());
        String todayStr = sdf.format(new Date());

        try {
            Date rangeStart = sdf.parse(rangeStartStr);
            Date today = sdf.parse(todayStr);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                List<Date> dates = diaryDao.getAllDiaryDates(rangeStart, today);



                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                Map<Integer, Integer> countMap = new HashMap<>();

                for (Date date : dates) {
                    String timeStr = timeFormat.format(date);
                    String[] parts = timeStr.split(":");
                    int totalMinutes = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
                    countMap.put(totalMinutes, countMap.getOrDefault(totalMinutes, 0) + 1);
                }

                List<Point> points = new ArrayList<>();
                List<Integer> sortedMinutes = new ArrayList<>(countMap.keySet());
                Collections.sort(sortedMinutes);

                for (int minute : sortedMinutes) {
                    points.add(new Point(minute, countMap.get(minute)));
                }

                if (points.isEmpty()) {
                    points.add(new Point(0, 0)); // 더미
                }

                int maxMinute = 0, maxCount = 0;
                for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
                    if (entry.getValue() > maxCount) {
                        maxMinute = entry.getKey();
                        maxCount = entry.getValue();
                    }
                }

                String mostFrequentTime = String.format("%02d:%02d", maxMinute / 60, maxMinute % 60);
                Log.d("TimeGraph", "⏰ 최다 시간: " + mostFrequentTime);

                requireActivity().runOnUiThread(() -> {


                    if (countMap.isEmpty()) {

                        placeholder.setText("표시할 데이터가 없습니다.");
                        graphView.setVisibility(View.GONE);
                    } else {
                        placeholder.setVisibility(View.GONE);
                        graphView.setVisibility(View.VISIBLE);
                        graphView.setTimePoints(points);

                        if (getActivity() instanceof TimeZoneActivity) {
                            ((TimeZoneActivity) getActivity()).setTimeText(mostFrequentTime);
                        }
                    }
                });
            });

            executor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
