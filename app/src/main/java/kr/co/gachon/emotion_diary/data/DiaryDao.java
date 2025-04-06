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
}