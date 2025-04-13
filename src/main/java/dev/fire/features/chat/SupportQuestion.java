package dev.fire.features.chat;

public class SupportQuestion {
    public String name, rank, message;
    public long timestamp;
    public SupportQuestion(String name, String rank, String message, long timestamp) {
        this.name = name;
        this.rank = rank;
        this.message = message;
        this.timestamp = timestamp;
    }
}
