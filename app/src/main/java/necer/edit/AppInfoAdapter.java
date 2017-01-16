package necer.edit;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by necer on 2017/1/16.
 */

public class AppInfoAdapter extends BaseAdapter{


    private List<AppInfo> appInfoList;
    private Context context;

    public AppInfoAdapter(Context context,List<AppInfo> appInfoList) {
        this.appInfoList = appInfoList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return appInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_appinfo, null);
        }

        ImageView iv_icon = NListViewHolder.get(convertView, R.id.iv_icon);
        TextView tv_state = NListViewHolder.get(convertView, R.id.tv_state);
        TextView tv_name = NListViewHolder.get(convertView, R.id.tv_name);


        iv_icon.setImageDrawable(appInfoList.get(position).getAppIcon());
        tv_name.setText(appInfoList.get(position).getAppLabel());
        tv_state.setText(appInfoList.get(position).isEnable() ? "未冻结" : "已冻结");
        tv_state.setTextColor(appInfoList.get(position).isEnable() ? context.getResources().getColor(R.color.enable) : context.getResources().getColor(R.color.unEnable));

        return convertView;
    }
}
