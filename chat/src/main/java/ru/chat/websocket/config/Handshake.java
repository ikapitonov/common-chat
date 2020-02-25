package ru.chat.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import ru.chat.model.Site;

import java.net.URLDecoder;
import java.util.Map;

public class Handshake implements HandshakeInterceptor {

    @Autowired
    private Site sqlSite;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest && !request.getURI().toString().isEmpty()) {

            MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUriString(request.getURI().toString()).build().getQueryParams();
            int siteId = Integer.parseInt(parameters.get("site").get(0));
            String username = parameters.get("username").get(0).trim();
            String token;

            if (!username.isEmpty()) {
                username = URLDecoder.decode(username, "UTF-8");
            }

            token = sqlSite.validateById(siteId);

            if (siteId > 0 && token != null) {
                attributes.put("parent_id", siteId);

                try {
                    String key = parameters.get("token").get(0);

                    if (key.equals(token)) {
                        attributes.put("admin",  (byte) 1);
                    }
                    else {
                        return closeConnection(response);
                    }
                }
                catch (Exception e) {
                    attributes.put("admin", (byte) 0);
                }

            } else {
                return closeConnection(response);
            }

            attributes.put("username", !username.isEmpty() ? (username.length() > 30 ? username.substring(0, 30) : username) : "Гость");

            return true;
        }
        return closeConnection(response);
    }

    private boolean closeConnection (ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.NOT_FOUND);
        response.close();
        return false;
    }

    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        // пригодится
    }
}
