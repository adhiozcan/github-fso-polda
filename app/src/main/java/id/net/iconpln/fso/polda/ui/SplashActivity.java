package id.net.iconpln.fso.polda.ui;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import id.net.iconpln.fso.polda.AppConfig;
import id.net.iconpln.fso.polda.R;
import id.net.iconpln.fso.polda.model.SocketEntity;
import id.net.iconpln.fso.polda.network.EchoWebSocket;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

/**
 * Created by Ozcan createNew 22/11/2016.
 */

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //testWebSocket();

        //test();


        /**
         * Check Developer Configuration.
         * This configuration can be update.
         * For more information, please refer to #AppConfig.Class
         */

        if (AppConfig.checkByPassSplash()) {
            goToLoginActivity();
            return;
        }

        displaySplash();
    }

    private void displaySplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void testWebSocket() {
        String        url      = "ws://apps.iconpln.co.id:6063/ws";
        String        url2     = "ws://echo.websocket.org";
        OkHttpClient  client   = new OkHttpClient();
        Request       request  = new Request.Builder().url(url).build();
        EchoWebSocket listener = new EchoWebSocket();

        String message = "\"type\": \"event\",\n" +
                "  \"event\": \"Device_Message\",\n" +
                "  \"params\": {\n" +
                "    \"method\": \"guesttgk\",\n" +
                "    \"serial\": \"0000000000000000\",\n" +
                "    \"versi\": \"200\",\n" +
                "    \"idpel\": \"535718378961\",\n" +
                "    \"id\": \"0000000000000000\"\n" +
                "  }";
        WebSocket ws = client.newWebSocket(request, listener);
        //ws.send("Hello");
        ws.send(message);
        ws.close(1000, "Goodbye!");
        client.dispatcher().executorService().shutdown();
    }

    private void test() {

        Gson         gson = new Gson();
        SocketEntity socketEntity;

        /**
         * #1
         * Open local files and convert into InputStream
         */
        try {
            AssetManager assetManager = getAssets();
            InputStream  ims          = assetManager.open("socket_raw.txt");
            Reader       reader       = new InputStreamReader(ims);

            socketEntity = gson.fromJson(reader, SocketEntity.class);
            socketEntity.prettyPrintSocketEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * #2
         * Convert String to Reader
         */
        String rawText = "{\n" +
                "        \"prop\": \"Received\",\n" +
                "            \"etype\": \"runFunction\",\n" +
                "            \"value\": [{\n" +
                "        \"method\": \"GUESTTGK\",\n" +
                "                \"rows\": 1,\n" +
                "                \"content\": [{\n" +
                "            \"TGK\": \"Anda memiliki Tunggakan sebanyak 1 lembar\"\n" +
                "        }]\n" +
                "    }]\n" +
                "    }";
        StringReader stringReader = new StringReader(rawText);
        socketEntity = gson.fromJson(stringReader, SocketEntity.class);
        socketEntity.prettyPrintSocketEntity();

        /**
         * #3
         * Reform to UTF8 encoding because sometimes GSON has trouble with this type
         */
        String rawTextUtf8;
        try {
            rawTextUtf8 = new String(rawText.getBytes("UTF-8"));
            socketEntity = gson.fromJson(rawTextUtf8, SocketEntity.class);
            socketEntity.prettyPrintSocketEntity();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}