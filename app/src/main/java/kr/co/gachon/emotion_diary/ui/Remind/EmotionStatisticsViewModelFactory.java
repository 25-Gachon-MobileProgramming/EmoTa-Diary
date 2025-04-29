package kr.co.gachon.emotion_diary.ui.Remind;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import kr.co.gachon.emotion_diary.data.DiaryDao;

public class EmotionStatisticsViewModelFactory implements ViewModelProvider.Factory {
    private final DiaryDao diaryDao;

    public EmotionStatisticsViewModelFactory(DiaryDao diaryDao) {
        this.diaryDao = diaryDao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EmotionStatisticsViewModel.class)) {
            return (T) new EmotionStatisticsViewModel(diaryDao);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
