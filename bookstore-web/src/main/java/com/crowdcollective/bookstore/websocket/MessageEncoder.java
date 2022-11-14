package com.crowdcollective.bookstore.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageEncoder implements Encoder.Text<Message> {
    @Inject
    Logger log;

    @Override
    public String encode(Message message) throws EncodeException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(message);
            return json;
        } catch (JsonProcessingException e) {
            log.log(Level.SEVERE, e.toString());
        }
        return message.toString();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
