package codes.fepi.notificationlistener.dto;

import java.util.Objects;

public class NotificationDto {
    private int id;
    private long time;
    private String title;
    private String content;
    private String source;
    private String key;

    public NotificationDto() {
    }

    public NotificationDto(int id, String key, long time, String title, String content, String source) {
        this.id = id;
        this.key = key;
        this.time = time;
        this.title = title;
        this.content = content;
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSource() {
        return source;
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationDto that = (NotificationDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
