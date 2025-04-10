package kr.co.gachon.emotion_diary.ui.timeLine;

public class MonthlyDiaryEntry {
    private final String date;
    private final String content;

    public MonthlyDiaryEntry(String date, String content) {
        this.date = date;
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
}