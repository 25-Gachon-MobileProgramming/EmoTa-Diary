package kr.co.gachon.emotion_diary.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface DiaryDao {
    @Query("SELECT COUNT(*) FROM diaries")
    int getDiaryCount();

    @Query("SELECT * FROM diaries ORDER BY date DESC")
    LiveData<List<Diary>> getAllDiaries();

    @Insert
    void insertDiary(Diary diary);

    @Update
    void updateDiary(Diary diary);

    @Delete
    void deleteDiary(Diary diary);

    @Query("SELECT COUNT(*) FROM diaries WHERE DATE(date) BETWEEN :startDate AND :endDate") //하루에 하나씩만 카운트되게하고 시작 날짜와 끝 날짜 정함
    int getDiaryCountPerDay(String startDate, String endDate);

}