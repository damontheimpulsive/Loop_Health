package com.gopay.app.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class LarkEventRequest {
    private String schema;
    private Header header;
    private Event event;

    // Getters and setters

    @Data
    public static class Header {
        @JsonProperty("event_id")
        private String eventId;
        private String token;
        @JsonProperty("create_time")
        private String createTime;
        @JsonProperty("event_type")
        private String eventType;
        @JsonProperty("tenant_key")
        private String tenantKey;
        @JsonProperty("app_id")
        private String appId;

        // Getters and setters
    }

    @Data
    public static class Event {
        private Message message;
        private Sender sender;

        // Getters and setters
    }

    @Data
    public static class Message {
        @JsonProperty("chat_id")
        private String chatId;
        @JsonProperty("chat_type")
        private String chatType;
        private ReplyContract.Content content;
        @JsonProperty("create_time")
        private String createTime;
        private List<Mention> mentions;
        @JsonProperty("message_id")
        private String messageId;
        @JsonProperty("message_type")
        private String messageType;
        @JsonProperty("update_time")
        private String updateTime;

        // Getters and setters
    }

    public static class Mention {
        private MentionId id;
        private String key;
        private String name;
        @JsonProperty("tenant_key")
        private String tenantKey;

        // Getters and setters
    }

    public static class MentionId {
        @JsonProperty("open_id")
        private String openId;
        @JsonProperty("union_id")
        private String unionId;
        @JsonProperty("user_id")
        private String userId;

        // Getters and setters
    }

    public static class Sender {
        @JsonProperty("sender_id")
        private SenderId senderId;
        @JsonProperty("sender_type")
        private String senderType;
        @JsonProperty("tenant_key")
        private String tenantKey;

        // Getters and setters
    }

    public static class SenderId {
        @JsonProperty("open_id")
        private String openId;
        @JsonProperty("union_id")
        private String unionId;
        @JsonProperty("user_id")
        private String userId;

        // Getters and setters
    }

    // Getters and setters for all top-level fields
}
