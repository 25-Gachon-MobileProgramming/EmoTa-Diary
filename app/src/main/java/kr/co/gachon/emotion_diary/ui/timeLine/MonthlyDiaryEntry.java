package kr.co.gachon.emotion_diary.ui.timeLine;

public class MonthlyDiaryEntry {
    private String date;
    private String content;
    private String emotion;     // 추가
    private String tarotCard;   // 추가

    public MonthlyDiaryEntry(String date, String content, String emotion, String tarotCard) {
        this.date = date;
        this.content = content;
        this.emotion = emotion;
        this.tarotCard = tarotCard;
    }

    // getter 추가
    public String getEmotion() { return emotion; }
    public String getTarotCard() { return tarotCard; }
    public String getDate() { return date; }
}
