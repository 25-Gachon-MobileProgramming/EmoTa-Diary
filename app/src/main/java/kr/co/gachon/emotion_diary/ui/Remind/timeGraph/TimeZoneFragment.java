package kr.co.gachon.emotion_diary.ui.Remind.timeGraph;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

    private Date startDate;
    private Date endDate;
    private FrameLayout graph;

    private TextView hint;



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
        hint = getActivity().findViewById(R.id.time_zone_activity_hint);

        graph = getActivity().findViewById(R.id.timegraph_fragment_container);
        if (getArguments() != null) {
            isMonthly = getArguments().getBoolean("isMonthly", true);
            startDate = (Date) getArguments().getSerializable("startDate");
            endDate = (Date) getArguments().getSerializable("endDate");
            Log.d("TimeGraph", "💡 전달받은 isMonthly: " + isMonthly);
        }

        AppDatabase db = AppDatabase.getDatabase(requireContext());
        DiaryDao diaryDao = db.diaryDao();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


        try {

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                List<Date> dates = diaryDao.getAllDiaryDates(startDate, endDate);



                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                Map<Integer, Integer> countMap = new HashMap<>();

                for (Date date : dates) {
                    String timeStr = timeFormat.format(date);
                    String[] parts = timeStr.split(":");
                    int totalMinutes = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);

                    // ⏱ 30분 단위로 묶기
                    int roundedMinutes = (totalMinutes / 30) * 30;

                    countMap.put(roundedMinutes, countMap.getOrDefault(roundedMinutes, 0) + 1);
                }

                List<Point> points = new ArrayList<>();
                List<Integer> sortedMinutes = new ArrayList<>(countMap.keySet());
                Collections.sort(sortedMinutes);

                for (int minute : sortedMinutes) {
                    points.add(new Point(minute, countMap.get(minute)));
                }

//                if (points.isEmpty()) {
//                    points.add(new Point(0, 0)); // 더미
//                }

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


                    boolean noFrequentData = countMap.values().stream().noneMatch(count -> count >= 2);

                    if (countMap.isEmpty() || noFrequentData) {

                        graph.setVisibility(View.GONE);
                        hint.setVisibility(View.VISIBLE);

                        graphView.setVisibility(View.GONE);
                    } else {
                        Log.d("point", "onViewCreated: " + points);
                        graph.setVisibility(View.VISIBLE);
                        graphView.setVisibility(View.VISIBLE);
                        graphView.setTimePoints(points);
                        hint.setVisibility(View.GONE);

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
