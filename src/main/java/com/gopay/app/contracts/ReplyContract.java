package com.gopay.app.contracts;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReplyContract {
    private String msgType;
    private Content content;

    @AllArgsConstructor
    @Data
    public static class Content {
        private String text;
    }
}
