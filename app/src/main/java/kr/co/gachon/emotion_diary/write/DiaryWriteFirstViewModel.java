package kr.co.gachon.emotion_diary.write;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;

import kr.co.gachon.emotion_diary.data.Diary;
import kr.co.gachon.emotion_diary.data.DiaryRepository;

// ViewModel: UI에서 사용할 데이터를 처리하고 저장하는 역할 (Lifecycle-aware)
public class DiaryWriteFirstViewModel extends AndroidViewModel {

    private final DiaryRepository repository;           // DB 작업을 위한 Repository
    private final LiveData<Diary> firstDiary;           // DB에서 가져온 첫 번째 다이어리 데이터
    private final MutableLiveData<String> date = new MutableLiveData<>();
    private final MutableLiveData<Date> selectedDate = new MutableLiveData<>();

    // 생성자
    public DiaryWriteFirstViewModel( Application application) {
        super(application);
        repository = new DiaryRepository(application);
        firstDiary = repository.getFirstDiaryLive(); // 첫 번째 다이어리 LiveData 초기화
    }

    // 첫 번째 다이어리 LiveData 반환
//    public LiveData<Diary> getFirstDiary() {
//        return firstDiary;
//    }
//
//    // 날짜 설정
//
//    public LiveData<String> getDate() {
//        return date;
//    }


    // 전체 Diary 객체로 업데이트
    public void updateDiary(Diary diary) {repository.update(diary);}

    // 선택된 날짜 설정 및 가져오기
    public void setSelectedDate(Date date) {selectedDate.setValue(date);}

    public LiveData<Date> getSelectedDate() {return selectedDate;}

    // ViewModelFactory - 외부에서 ViewModel을 만들 때 사용
    public static class Factory implements ViewModelProvider.Factory {
        private final Application application;

        public Factory(Application application) {this.application = application;}


        // Db를 사용할 때 필요한 코
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(DiaryWriteFirstViewModel.class)) {
                return (T) new DiaryWriteFirstViewModel(application);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
