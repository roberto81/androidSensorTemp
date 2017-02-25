package it.robertopallotta.sensortemp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.util.List;

public class InfoSensor extends AppCompatActivity {

    private static final String TAG = InfoSensor.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_sensor);

        Log.i(TAG,"aperta seconda activity");



    }
}
