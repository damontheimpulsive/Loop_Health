package com.gopay.app.services;

import com.gopay.app.clients.httpclients.LarkClient;
import com.gopay.app.contracts.LarkEventRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class LarkService {
    private final LarkClient larkClient;

    public void sendReply(final LarkEventRequest request, final String content) {
        try {
            larkClient.sendReply(
                    request.getEvent().getMessage().getMessageId(),
                    request.getHeader().getToken(),
                    content
            );
        } catch (Exception e) {
            log.error("Failed to send reply message to Lark: {}", e.getMessage());
        }
    }
}
