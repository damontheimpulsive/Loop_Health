package com.gopay.app.contracts;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageReply {
    @SerializedName("msg_type")
    private String msgType;

    private String content;
}
