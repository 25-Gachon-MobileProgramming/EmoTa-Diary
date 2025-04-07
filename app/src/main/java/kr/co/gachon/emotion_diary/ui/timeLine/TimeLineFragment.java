package kr.co.gachon.emotion_diary.ui.timeLine;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kr.co.gachon.emotion_diary.data.DiaryRepository;
import kr.co.gachon.emotion_diary.databinding.FragmentTimelineBinding;

public class TimeLineFragment extends Fragment {

    private FragmentTimelineBinding binding;
    private RecyclerView monthlyDiaryRecyclerView;
    private MonthlyDiaryAdapter adapter;

    private DiaryRepository diaryRepository;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        TimeLineViewModel timeLineViewModel =
                new ViewModelProvider(this).get(TimeLineViewModel.class);

        binding = FragmentTimelineBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        monthlyDiaryRecyclerView = binding.monthlyDiaryRecyclerView;
        monthlyDiaryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 임시 데이터 (실제 데이터는 데이터베이스나 파일 등에서 가져와야 합니다.)
        List<MonthlyDiaryEntry> diaryData = new ArrayList<>();
        diaryData.add(new MonthlyDiaryEntry("2023-10-05", "10월 5일 일기"));
        diaryData.add(new MonthlyDiaryEntry("2023-10-15", "10월 15일 일기"));
        diaryData.add(new MonthlyDiaryEntry("2023-10-26", "10월 26일 일기"));
        diaryData.add(new MonthlyDiaryEntry("2023-11-01", "11월 1일 일기"));
        diaryData.add(new MonthlyDiaryEntry("2023-11-20", "11월 20일 일기"));

        // 월별로 그룹화하는 로직 (Java 8 이상)
        Map<String, List<MonthlyDiaryEntry>> groupedDiaryDataMap = diaryData.stream()
                .collect(Collectors.groupingBy(entry -> entry.getDate().substring(0, 7)));


        // Map을 어댑터에 넘길 수 있는 List<Pair<String, List<DiaryEntry>>> 형태로 변환 및 정렬
        List<Pair<String, List<MonthlyDiaryEntry>>> groupedDiaryData = new ArrayList<>();

        for (Map.Entry<String, List<MonthlyDiaryEntry>> entry : groupedDiaryDataMap.entrySet()) {
            groupedDiaryData.add(new Pair<>(entry.getKey(), entry.getValue()));
        }

        // 최신 월부터 표시하기 위해 정렬
        groupedDiaryData.sort((pair1, pair2) -> pair2.first.compareTo(pair1.first));

        Log.d("TimeLineFragment", "groupedDiaryData: " + groupedDiaryData);

        adapter = new MonthlyDiaryAdapter(groupedDiaryData);
        monthlyDiaryRecyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}