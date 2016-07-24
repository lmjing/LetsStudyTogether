package soma.net.letsstudytogether.ListView;

import android.widget.TextView;

/**
 * Created by LG on 2015-10-31.
 */


/* ViewHolder 은 없어도 됨 ConvertView 아이템 하나하나 */
public class ViewHolder {
    TextView txtTitle_item;

    TextView txtType_item;
    TextView txtLocation_item;
    TextView txtMember_item;

    public TextView getTxtTitle_item() {
        return txtTitle_item;
    }

    public void setTxtTitle_item(TextView txtTitle_item) {
        this.txtTitle_item = txtTitle_item;
    }

    public TextView getTxtType_item() {
        return txtType_item;
    }

    public void setTxtType_item(TextView txtType_item) {
        this.txtType_item = txtType_item;
    }

    public TextView getTxtLocation_item() {
        return txtLocation_item;
    }

    public void setTxtLocation_item(TextView txtLocation_item) {
        this.txtLocation_item = txtLocation_item;
    }

    public TextView getTxtMember_item() {
        return txtMember_item;
    }

    public void setTxtMember_item(TextView txtMember_item) {
        this.txtMember_item = txtMember_item;
    }


}
