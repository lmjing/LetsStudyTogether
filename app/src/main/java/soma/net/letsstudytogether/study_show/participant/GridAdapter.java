package soma.net.letsstudytogether.study_show.participant;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import soma.net.letsstudytogether.R;
import soma.net.letsstudytogether.model.Participant;

/**
 * Created by inbiz02 on 2016-01-13.
 */
public class GridAdapter extends BaseAdapter {
    
    Context context;
    int layout;
    List<Participant> persons;
    LayoutInflater inf;


    public GridAdapter(Context context, int layout, List<Participant> persons) {
        this.context = context;
        this.layout = layout;
        this.persons = persons;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        Log.i("Show", "a_inside 1");
    }

    @Override
    public int getCount() {
        Log.i("Show","a_inside 2");
        return persons.size();
    }

    @Override
    public Object getItem(int position) {
        Log.i("Show","a_inside 3");
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {

        Log.i("Show","a_inside 4");
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null)
                convertView = inf.inflate(layout, null);
            Log.i("Show", "getView 실행 " + persons.get(position).getName());
            TextView p_text = (TextView) convertView.findViewById(R.id.name);
            final CircularImageView imageView = (CircularImageView) convertView.findViewById(R.id.img);

            p_text.setText(persons.get(position).getName());
            Glide.with(context).load("https://graph.facebook.com/" + persons.get(position).getUserid() + "/picture?type=normal").into(imageView);

        }catch(Exception e){
            Log.v("Show GridAdapter error", "오류내용 :"+e.toString());
        }
        return convertView;
    }
}