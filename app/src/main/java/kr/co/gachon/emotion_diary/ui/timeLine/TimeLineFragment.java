package kr.co.gachon.emotion_diary.ui.timeLine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import kr.co.gachon.emotion_diary.data.Diary;
import kr.co.gachon.emotion_diary.data.DiaryRepository;
import kr.co.gachon.emotion_diary.databinding.FragmentTimelineBinding;
import kr.co.gachon.emotion_diary.ui.Remind.WriteRate.RateActivity;

public class TimeLineFragment extends Fragment implements MonthlyDiaryAdapter.OnMonthlyDiaryClickListener {

    private FragmentTimelineBinding binding;
    private final String logTitle = "TimeLineFragment";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        TimeLineViewModel timeLineViewModel =
//                new ViewModelProvider(this).get(TimeLineViewModel.class);

        binding = FragmentTimelineBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView monthlyDiaryRecyclerView = binding.monthlyDiaryRecyclerView;
        monthlyDiaryRecyclerView.addItemDecoration(new TimelineItemDecoration(requireContext()));
        monthlyDiaryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // DB 연결
        DiaryRepository diaryRepository = new DiaryRepository(requireActivity().getApplication());

        diaryRepository.getAllDiaries().observe(getViewLifecycleOwner(), diaries -> {
            Log.d(logTitle, "recycler 모든 일기 (Repository - ExecutorService):");
            for (Diary diary : diaries) {
                Log.d(logTitle, "ID: " + diary.getId() + ", 제목: " + diary.getTitle() + ", 내용: " + diary.getContent() + ", 날짜: " + diary.getDate());
            }
            Log.d(logTitle, "===========================================================");

            // 월별로 그룹화하는 로직 (Date 객체 직접 사용)
            Map<String, List<Diary>> groupedDiaryDataMap = diaries.stream()
                    .collect(Collectors.groupingBy(diary -> {
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM");
                        return outputFormat.format(diary.getDate());
                    }));

            // Map을 어댑터에 넘길 수 있는 List<Pair<String, List<MonthlyDiaryEntry>>> 형태로 변환
            List<Pair<String, List<MonthlyDiaryEntry>>> groupedDiaryData = new ArrayList<>();
            for (Map.Entry<String, List<Diary>> entry : groupedDiaryDataMap.entrySet()) {
                String month = entry.getKey();
                List<Diary> diaryList = entry.getValue();
                List<MonthlyDiaryEntry> entriesForMonth = new ArrayList<>();

                Map<String, Integer> emotionCount = new HashMap<>();
                Map<String, Integer> tarotCount = new HashMap<>();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                for (Diary diary : diaryList) {
                    String formattedDate = dateFormat.format(diary.getDate());

                    String emotion = diary.getEmotionText();
                    String tarot = diary.getTaroName();

                    // 카운팅
                    if (emotion != null) {
                        emotionCount.put(emotion, emotionCount.getOrDefault(emotion, 0) + 1);
                    }
                    if (tarot != null) {
                        tarotCount.put(tarot, tarotCount.getOrDefault(tarot, 0) + 1);
                    }

                    entriesForMonth.add(new MonthlyDiaryEntry(formattedDate, diary.getContent(), emotion, tarot));
                }

                // 가장 많은 감정/타로
                String topEmotion = emotionCount.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse("정보 없음");

                String topTarot = tarotCount.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse("정보 없음");

                // MonthlyDiaryAdapter에 전달할 때 확장
                groupedDiaryData.add(new Pair<>(month, entriesForMonth));

                Log.d(logTitle, "📊 " + month + " 감정 통계: " + topEmotion);
                Log.d(logTitle, "🔮 " + month + " 타로 통계: " + topTarot);
            }


            // 최신 월부터 표시하기 위해 정렬
            groupedDiaryData.sort((pair1, pair2) -> pair2.first.compareTo(pair1.first));

            Log.d("TimeLineFragment", "groupedDiaryData from DB: " + groupedDiaryData);

            // 어댑터에 새로운 데이터 설정
            MonthlyDiaryAdapter adapter = new MonthlyDiaryAdapter(groupedDiaryData, this);
            monthlyDiaryRecyclerView.setAdapter(adapter);
        });

        return root;
    }

    @Override
    public void onMonthlyDiaryClick(String month, List<MonthlyDiaryEntry> diaryList) {
        moveToStatActivity(true, Integer.parseInt(month.split("-")[0]), Integer.parseInt(month.split("-")[1]));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void moveToStatActivity(boolean isMonthly, int year, int month) {
        String term = year + "-" + month;

        Intent intent = new Intent(getActivity(), RateActivity.class);
        intent.putExtra("isMonthly", isMonthly);
        intent.putExtra("term", term);

        startActivity(intent);
    }
}