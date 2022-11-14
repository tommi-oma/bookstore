package com.crowdcollective.bookstore.websocket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.crowdcollective.model.Book;

@ServerEndpoint(value = "/bookws", encoders = MessageEncoder.class)
@ApplicationScoped
public class WebBroadcaster {
    @Inject
    private Logger log;
    private static Set<Session> clients
            = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        clients.add(session);
        log.info("New client, now # of clients: " + clients.size());
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        clients.remove(session);
        log.info("New client, now # of clients: " + clients.size());
    }

    private void broadcast(Message message)
            throws IOException {
        clients.forEach(session -> {
            synchronized (session) {
                try {
                    session.getBasicRemote().
                            sendObject(message);
                } catch (IOException | EncodeException e) {
                    log.log(Level.SEVERE, "broadcast error: " + e);
                }
            }
        });
    }
    public void onBookListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) Book event) throws IOException{
        broadcast(new Message("Created/removed a book with id: " + event.getId()));
        log.info("Broadcast done");
    }

}

