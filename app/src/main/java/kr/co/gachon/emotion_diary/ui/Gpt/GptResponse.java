package kr.co.gachon.emotion_diary.ui.Gpt;

import java.util.List;

public class GptResponse {
    public List<Choice> choices;

    public static class Choice {
        public Message message;
    }

    public static class Message {
        public String role;
        public String content;
    }
}