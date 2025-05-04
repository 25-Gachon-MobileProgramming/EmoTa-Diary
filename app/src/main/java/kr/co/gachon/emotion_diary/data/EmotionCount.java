package kr.co.gachon.emotion_diary.data;

public class EmotionCount {
    public String emotion;  // 감정명
    public int count;       // 해당 감정의 출현 횟수

    public EmotionCount(String emotion, int count) {
        this.emotion = emotion;
        this.count = count;
    }
}

