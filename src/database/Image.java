package database;

import java.sql.Timestamp;

public class Image {

    private String image;
    private String chatname;
    private String sender;
    private String path;
    private Timestamp timestamp;

    public Image (String image, String chatname, String sender, String path, Timestamp timestamp){
        this.image = image;
        this.chatname = chatname;
        this.sender = sender;
        this.path = path;
        this.timestamp = timestamp;
    }

    public String getChatname() {
        return chatname;
    }

    public String getSender() {
        return sender;
    }

    public String getPath() {
        return path;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getImage() {
        return image;
    }
}
