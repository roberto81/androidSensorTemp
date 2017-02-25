package it.robertopallotta.sensortemp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InfoSensor extends AppCompatActivity {

    private static final String TAG = InfoSensor.class.getSimpleName();
    private List<JSONObject> sensorInfo;
    private ListView sensorList;
    private SensorListAdapter infoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_sensor);
        sensorList = (ListView) findViewById(R.id.listInfo);

        Log.i(TAG,"recupero le info del sensore");
        String sensor = getIntent().getExtras().getString("info");
        Log.i(TAG,"recupero delle informazioni terminato");
        try {
            Log.i(TAG,"conversione stringa in JSONArray");
            JSONArray jsonArray = new JSONArray(sensor);
            Log.i(TAG,"conversione stringa in JSONArray terminata");
            Log.i(TAG,"sensor info: " + jsonArray);

            sensorInfo = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++){
                sensorInfo.add(jsonArray.getJSONObject(i));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        infoListAdapter = new SensorListAdapter(sensorInfo,this);
        sensorList.setAdapter(infoListAdapter);

    }
}
