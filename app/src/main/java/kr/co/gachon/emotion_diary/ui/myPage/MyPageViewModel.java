package kr.co.gachon.emotion_diary.ui.myPage;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import kr.co.gachon.emotion_diary.data.Diary;
import kr.co.gachon.emotion_diary.data.DiaryRepository;

public class MyPageViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mText;

    private final LiveData<Integer> consecutiveWritingDays;

    public MyPageViewModel(Application application) {
        super(application);

        mText = new MutableLiveData<>();
        mText.setValue("This is MyPage fragment");

        DiaryRepository repository = new DiaryRepository(getApplication());
        consecutiveWritingDays = repository.getConsecutiveWritingDays();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Integer> getConsecutiveWritingDays() {
        return consecutiveWritingDays;
    }
}