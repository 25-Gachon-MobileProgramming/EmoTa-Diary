package kr.co.gachon.emotion_diary.ui.Remind;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kr.co.gachon.emotion_diary.data.DiaryDao;
import kr.co.gachon.emotion_diary.data.EmotionCount;

public class EmotionStatisticsViewModel extends ViewModel {
    private final MutableLiveData<List<EmotionCount>> emotions = new MutableLiveData<>();

    public EmotionStatisticsViewModel(DiaryDao diaryDao) {
        loadEmotions(diaryDao);
    }

    public LiveData<List<EmotionCount>> getEmotions() {
        return emotions;
    }

    private void loadEmotions(DiaryDao diaryDao) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<EmotionCount> result = diaryDao.getEmotionCounts();
            emotions.postValue(result);
        });
    }
}

