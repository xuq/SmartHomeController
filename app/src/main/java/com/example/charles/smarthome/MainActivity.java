package com.example.charles.smarthome;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Context ctx;
    private TextView tvDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize
        this.ctx = this;
        tvDisplay = findViewById(R.id.textView);
    }

    // send an request in different thread
    public class OkHttpHandler extends AsyncTask {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected Object doInBackground(Object[] objects) {
            Request.Builder builder = new Request.Builder();

            String maker_url = (String)objects[0];

            builder.url(maker_url);
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();

                // if success raise a toast on UI thread
                if(response.code() == 200){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ctx, "Success!", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                return response.body().string();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public void onButton1(View view) throws IOException{
        // event name defined in IFTTT
        String event_name = "send_email";

        //build the maker URL
        String base_url = "https://maker.ifttt.com/trigger/%s/with/key/FDnT3zrDz-yIc6FDlGwqq";
        String maker_url = String.format(base_url, event_name);

        OkHttpHandler okHttpHandler= new OkHttpHandler();
        okHttpHandler.execute(maker_url);
    }

    public void onButton2(View view){

    }

    public void onButton3(View view){

    }

    public void onButton4(View view){

    }

    public void onGetAllReminders(View view) throws ExecutionException, InterruptedException {
        String base_url = "http://192.168.12.4:8008/setup/assistant/alarms";
        OkHttpHandler okHttpHandler= new OkHttpHandler();
        String response  = (String) okHttpHandler.execute(base_url).get();

        // Visualize the timer info
        tvDisplay.setText(response);
    }
}
