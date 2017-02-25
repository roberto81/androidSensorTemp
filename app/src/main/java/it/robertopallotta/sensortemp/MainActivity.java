package it.robertopallotta.sensortemp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ListView listView;

    private SensorUpdate sensorUpdateTask;
    private JSONArray sensorArray;

    private Handler infoHandler;
    private Runnable sensorInfoRunnable;

    private List<JSONObject> sensorList;
    private Map<Integer,List<JSONObject>> sensorMapInfo;
    private SensorListAdapter sensorListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listSensor);

        sensorList = new ArrayList<>();

        sensorMapInfo = new HashMap<>();

        sensorUpdateTask = new SensorUpdate();
        sensorUpdateTask.execute("http://ppl.eln.uniroma2.it/pjdm/sensorlist.php");

        infoHandler = new Handler();


        try {
            sensorArray = this.sensorUpdateTask.get();
            for (int i = 0 ; i < sensorArray.length() ; i++){
                sensorMapInfo.put(i,new ArrayList<JSONObject>());

            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        sensorInfoRunnable = new Runnable() {
            @Override
            public void run() {


                for (int i = 0 ; i < sensorArray.length() ; i++){
                    List<JSONObject> list = sensorMapInfo.get(i);
                    if (list.size() == 10){
                        list.remove(0);
                        try {
                            list.add(new SensorInfoUpdate().execute(sensorArray.getJSONObject(i).getString("url")).get());
                            sensorMapInfo.remove(i);
                            sensorMapInfo.put(i,list);
                        } catch (JSONException | ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else {
                        try {
                            sensorMapInfo.get(i).add(new SensorInfoUpdate().execute(sensorArray.getJSONObject(i).getString("url")).get());
                        } catch (JSONException | ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                for (Map.Entry<Integer , List<JSONObject> > entry: sensorMapInfo.entrySet()){
                    List<JSONObject> list = entry.getValue();
                    if (sensorList.size() == sensorMapInfo.size()) {

                        int i = sensorList.size();
                        int j = 0;
                        while (j < i) {
                            sensorList.remove(0);
                            j += 1;
                        }
                        sensorList.add(list.get(0));

                    }else{
                        sensorList.add(list.get(0));
                    }

                }


                //questo for mi serve per fare il log poi andra eliminato.
                Log.i(TAG, "Nel Runnable sensorList");
                for (JSONObject oj: sensorList){
                    try {
                        Log.i(TAG, oj.getString("id") + " " + oj.getString("temp") + " " + oj.getString("time"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                //devo chiamare l'adapter per aggiornare la lista

                sensorListAdapter.notifyDataSetChanged();
                listView.invalidate();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Log.i(TAG, "cliccata la riga: " + position);
                        Intent intent = new Intent(MainActivity.this,InfoSensor.class);
                        startActivity(intent);
                    }
                });
                infoHandler.postDelayed(this,5000);

            }
        };

        sensorListAdapter = new SensorListAdapter(sensorList,this);
        listView.setAdapter(sensorListAdapter);
        infoHandler.postDelayed(sensorInfoRunnable,0);

    }

    @Override
    protected void onStop() {
        super.onStop();
        infoHandler.removeCallbacks(sensorInfoRunnable);
    }
}
