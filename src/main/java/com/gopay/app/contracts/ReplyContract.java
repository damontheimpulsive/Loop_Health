package com.gopay.app.contracts;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReplyContract {
    private String msgType;
    private String content;
}
