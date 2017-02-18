package it.robertopallotta.sensortemp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by roberto on 18/02/17.
 */

public class SensorUpdate extends AsyncTask<String, Void, JSONArray> {

    private static final String TAG = SensorUpdate.class.getSimpleName();

    @Override
    protected JSONArray doInBackground(String... params) {

        String strSensorArray = getSensorByUrl(params[0]);
        try {

            JSONArray jsonArraySensor = new JSONArray(strSensorArray);
            return jsonArraySensor;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    private String getSensorByUrl(String url) {
        try {
            URL urlLocation = new URL(url);
            URLConnection urlConnection = urlLocation.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            return stringBuffer.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}