package it.robertopallotta.sensortemp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by roberto on 18/02/17.
 */

public class SensorInfoUpdate extends AsyncTask<String, Void,JSONObject> {

    private static final String TAG = SensorInfoUpdate.class.getSimpleName();

    @Override
    protected JSONObject doInBackground(String... params) {

        String strSensorInfo = getSensorByUrl(params[0]);
        try {

            JSONObject jsonObjectInfo = new JSONObject(strSensorInfo);
            return jsonObjectInfo;
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
