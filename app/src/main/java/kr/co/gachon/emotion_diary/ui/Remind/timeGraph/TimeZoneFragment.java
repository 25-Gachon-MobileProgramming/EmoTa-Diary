package kr.co.gachon.emotion_diary.ui.Remind.timeGraph;

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kr.co.gachon.emotion_diary.R;
import kr.co.gachon.emotion_diary.data.AppDatabase;
import kr.co.gachon.emotion_diary.data.DiaryDao;

public class TimeZoneFragment extends Fragment {

    private TimeGraph graphView;

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

        AppDatabase db = AppDatabase.getDatabase(requireContext());
        DiaryDao diaryDao = db.diaryDao();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Date> dates = diaryDao.getAllDiaryDates();

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Map<Integer, Integer> countMap = new HashMap<>();
            for (Date date : dates) {
                String timeStr = sdf.format(date);
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

            points.add(new Point(1350, 1)); // 더미

            int maxMinute = 0, maxCount = 0;
            for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxMinute = entry.getKey();
                    maxCount = entry.getValue();
                }
            }

            String mostFrequentTime = String.format("%02d:%02d", maxMinute / 60, maxMinute % 60);

            requireActivity().runOnUiThread(() -> {
                graphView.post(() -> {
                    graphView.setTimePoints(points);
                });

                // Activity에 있는 TextView 업데이트
                if (getActivity() instanceof TimeZoneActivity) {
                    ((TimeZoneActivity) getActivity()).setTimeText(mostFrequentTime);
                }
            });
        });
        executor.shutdown();
    }
}
