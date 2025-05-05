package kr.co.gachon.emotion_diary.ui.Gpt;


import java.util.List;

public class GptRequest {
    private String model;
    private List<Message> messages;
    private double temperature;


    public GptRequest(String model, List<Message> messages, double temperature) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
    }

    public static class Message {
        private final String role;
        private final String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }
}