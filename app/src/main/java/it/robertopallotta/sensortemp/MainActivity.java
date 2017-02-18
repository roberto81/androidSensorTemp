package it.robertopallotta.sensortemp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

    private SensorUpdate sensorUpdateTask;
    private JSONArray sensorArray;

    private Handler infoHandler;
    private Runnable sensorInfoRunnable;

    private List<JSONObject> sensorList;
    private Map<Integer,List<JSONObject>> sensorMapInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    if (sensorList.size() == 0 || sensorList.size() < sensorMapInfo.size()) {

                        sensorList.add(list.get(0));

                    }else{

                        sensorList = new ArrayList<>();
                        sensorList.add(list.get(0));

                    }

                }


/*              //questo for mi serve per fare il log poi andra eliminato.
                Log.i(TAG, "Nel Runnable sensorList");
                for (JSONObject oj: sensorList){
                    try {
                        Log.i(TAG, oj.getString("id") + " " + oj.getString("temp") + " " + oj.getString("time"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

*/


                infoHandler.postDelayed(this,10000);

            }
        };
        infoHandler.postDelayed(sensorInfoRunnable,0);

    }

    @Override
    protected void onStop() {
        super.onStop();
        infoHandler.removeCallbacks(sensorInfoRunnable);
    }
}