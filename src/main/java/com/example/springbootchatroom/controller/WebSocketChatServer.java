package com.example.springbootchatroom.controller;




import com.example.springbootchatroom.model.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint(value="/chat/{user}")
public class WebSocketChatServer {

    private Session session;
    private static Set<WebSocketChatServer>
            openEndpoints= new CopyOnWriteArraySet<>();





    //All chat sessions.
    private static Map<Session, String> onlineSessions = new ConcurrentHashMap<>();

    //TODO: add send message method.
    private static void sendMessageToAll(Message message) throws IOException{
        openEndpoints.forEach(endpoint -> {
            synchronized (endpoint){
                try {
                    endpoint.session.getBasicRemote().sendObject(message);
                } catch (IOException|EncodeException e){
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("user") String user) throws IOException {


       openEndpoints.add(this);
       onlineSessions.put(session, user);



       Message message = new Message();
       message.setUser(onlineSessions.get(session.getId()));
       message.setContent("Connected!");
       sendMessageToAll(message);

        //TODO: add on open connection.

    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session,  @PathParam("user") String jsonStr) throws IOException {
        //TODO: add send message.
        this.session = session;
        Message message = new Message();
        message.setUser(jsonStr);
        sendMessageToAll(message);
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        openEndpoints.remove(this);
        Message message = new Message();
        message.setUser(onlineSessions.get(session.getId()));
        message.setContent("Disconnected!");
        sendMessageToAll(message);
    }
        //TODO: add close connection.


    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }





}

