package id.net.iconpln.fso.polda.network;

import com.squareup.otto.Produce;

import id.net.iconpln.fso.polda.model.NotifEvent;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Created by Ozcan on 02/03/2017.
 */

public class EchoWebSocket extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        System.out.println("Connected to websocket!");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        System.out.println("Receiving: " + text);

        //TODO please continue
        produceNotifEvent(new NotifEvent());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        System.out.println("Closing: " + code + " " + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
    }

    @Produce
    public NotifEvent produceNotifEvent(NotifEvent notifEvent) {
        return notifEvent;
    }

}
