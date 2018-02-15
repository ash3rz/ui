package org.iplantc.de.server.websocket;

import org.iplantc.de.server.util.CasUtils;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.websocket.WebSocket;
import org.atmosphere.websocket.WebSocketEventListenerAdapter;
import org.atmosphere.websocket.WebSocketHandlerAdapter;
import org.atmosphere.websocket.WebSocketProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by sriram on 4/8/16.
 */
public abstract class MessageHandler extends WebSocketHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    protected ReceiveNotificationsDirect notificationReceiver;

    /**
     *
     * Bind channel to specific queue
     * @param username
     * @param msgChannel
     * @return  Queue name
     */
    public abstract String bindQueue(String username, Channel msgChannel);


    @Override
    public void onOpen(WebSocket webSocket) {
        try {
            notificationReceiver = new ReceiveNotificationsDirect();
            logger.debug("Web socket connection opened!");
            String username = getUserName(webSocket);
            logger.debug("user name:" + username);

            final Channel msgChannel = notificationReceiver.createChannel();
            String queue = bindQueue(username, msgChannel);
            consumeMessage(msgChannel, queue, webSocket);

            webSocket.resource().addEventListener(new WebSocketEventListenerAdapter() {
                @Override
                public void onDisconnect(AtmosphereResourceEvent event) {
                    try {
                        if (msgChannel != null) {
                            msgChannel.close();
                        }
                    } catch (IOException e) {
                        logger.error("Exception aborting channel", e);

                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (IOException exception) {
            onError(webSocket, new WebSocketProcessor.WebSocketException("AMQP channel was null", null));
            webSocket.close();
        }

    }


    @Override
    public void onClose(WebSocket webSocket) {
        logger.debug("connection closed!");
    }

    @Override
    public void onError(WebSocket webSocket, WebSocketProcessor.WebSocketException t) {
        logger.error("websocket connection error!",t);
    }


    protected final String getUserName(WebSocket webSocket) {
        return CasUtils.attributePrincipalFromServletRequest(webSocket.resource().getRequest())
                       .getName();
    }

    protected void consumeMessage(Channel msgChannel,String queue, final WebSocket webSocket) {
        Consumer consumer = new DefaultConsumer(msgChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                logger.debug("New message to consume: " + message);
                webSocket.write(message);
            }
        };

        notificationReceiver.consumeMessage(msgChannel, consumer, queue);
    }

}
