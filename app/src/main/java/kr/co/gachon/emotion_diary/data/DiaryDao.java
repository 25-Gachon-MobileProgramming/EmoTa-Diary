package kr.co.gachon.emotion_diary.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.Date;
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

    @Query("SELECT COUNT(DISTINCT strftime('%Y-%m-%d', date / 1000, 'unixepoch')) FROM diaries WHERE date BETWEEN :startDate AND :endDate")
    int getDiaryCountPerDay(Date startDate, Date endDate);


    @Query("SELECT emotion_id, COUNT(*) as count FROM diaries GROUP BY emotion_id")
    List<EmotionCount> getEmotionCounts();


    @Query("SELECT date FROM diaries")
    List<Date> getAllDiaryDates();

}