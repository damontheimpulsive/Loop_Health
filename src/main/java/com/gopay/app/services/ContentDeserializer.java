package com.gopay.app.services;

import com.google.gson.*;
import com.gopay.app.contracts.ReplyContract;

import java.lang.reflect.Type;

public class ContentDeserializer implements JsonDeserializer<ReplyContract.Content> {
    @Override
    public ReplyContract.Content deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx)
            throws JsonParseException {

        // json is the string: "{\"text\":\"test message\"}"
        String raw = json.getAsString();
        return new Gson().fromJson(raw, ReplyContract.Content.class);
    }
}

