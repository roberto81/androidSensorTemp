package it.robertopallotta.sensortemp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by roberto on 19/02/17.
 */

public class SensorListAdapter extends BaseAdapter{

    private Context ctx;
    private LayoutInflater layoutInflater;
    private List<JSONObject> list;

    public SensorListAdapter(List<JSONObject> list,Context context){
        super();
        this.list = list;
        this.ctx = context;
        this.layoutInflater = ( LayoutInflater )ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.list.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView tw1,tw2,tw3;

        if(convertView == null){
           convertView = layoutInflater.inflate(R.layout.row_item_sensor,parent,false);
        }

        tw1 = (TextView) convertView.findViewById(R.id.ids);
        tw2 = (TextView) convertView.findViewById(R.id.temps);
        tw3 = (TextView) convertView.findViewById(R.id.times);

        try {

            tw1.setText(list.get(position).getString("id"));
            tw2.setText(list.get(position).getString("temp"));
            tw3.setText(list.get(position).getString("time"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

}
