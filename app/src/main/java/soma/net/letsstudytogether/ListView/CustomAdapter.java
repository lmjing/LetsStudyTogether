package soma.net.letsstudytogether.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import soma.net.letsstudytogether.R;
import soma.net.letsstudytogether.model.Study;

/**
 * Created by LG on 2015-10-31.
 */
/*ArrayList??view?*/
public class CustomAdapter extends BaseAdapter {

    String dday = null;
    Context ctx;
    private ArrayList<Study> itemDatas = null;
    private LayoutInflater layoutInflater=  null;

    public CustomAdapter(Context ctx){
        this.ctx = ctx;
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //layoutInflater = LayoutInflater.from(ctx);
    }

    public void setItemDatas(ArrayList<Study> itemDatas){
        this.itemDatas = itemDatas;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemDatas != null ? itemDatas.size() : 0 ;
    }

    @Override
    public Object getItem(int position) {
        return (Study) (itemDatas != null && (position >= 0 && position<itemDatas.size()) ? itemDatas.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return (itemDatas != null && (position >= 0 && position<itemDatas.size())? position: 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.list_item, parent, false); //레이아웃정렬

            viewHolder.txtMember_item = (TextView) convertView.findViewById(R.id.textMember_item);
            viewHolder.txtTitle_item = (TextView) convertView.findViewById(R.id.textTitle_item);
            viewHolder.txtLocation_item = (TextView ) convertView.findViewById(R.id.textLocation_item);
            viewHolder.txtType_item = (TextView) convertView.findViewById(R.id.textType_item);


        }
        else{ // convertView != null)
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Study itemData_temp = itemDatas.get(position);
        viewHolder.txtTitle_item.setText(itemData_temp.getTitle());
        viewHolder.txtMember_item.setText(itemData_temp.getMember()+"명");
        viewHolder.txtType_item.setText(itemData_temp.getType());

        String location = itemData_temp.getLocation();
        int i = location.indexOf("-");
        viewHolder.txtLocation_item.setText(location.substring(0,i));

        convertView.setTag(viewHolder);

        return convertView;
    }
}
