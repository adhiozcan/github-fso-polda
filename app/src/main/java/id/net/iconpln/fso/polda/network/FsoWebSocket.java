package id.net.iconpln.fso.polda.network;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

/**
 * Created by Ozcan on 02/03/2017.
 */

public class FsoWebSocket {
    private static FsoWebSocket fso = new FsoWebSocket();
    private OkHttpClient client;
    private WebSocket    ws;

    private FsoWebSocket() {
    }

    public static void init() {
        Request       request  = new Request.Builder().url("ws://echo.websocket.org").build();
        EchoWebSocket listener = new EchoWebSocket();
        fso.client = new OkHttpClient();
        fso.ws = fso.client.newWebSocket(request, listener);
    }

    public static void send(String message) {
        if (fso.ws == null)
            throw new NullPointerException("[Null] Client has not created");
        fso.ws.send(message);
    }


    public static void closeAndShutdown() {
        fso.ws.close(1000, "Goodbye!");
        fso.client.dispatcher().executorService().shutdown();
    }
}
