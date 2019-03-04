package teaching.tutor.education.tutor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import teaching.tutor.education.tutor.Listeners.GeneralAdapterListener;

public class JSONAdapter extends BaseAdapter {
    private Activity mActivity;
    private JSONArray itemsData;
    Context context;

    private GeneralAdapterListener mListener;


    public JSONAdapter(Activity activity, GeneralAdapterListener listener) {
        mActivity = activity;
        mListener = listener;
    }

    public void setArray(JSONArray list) {
        this.itemsData = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if (itemsData == null)

            return 0;
        return itemsData.length();

    }

    @Override
    public int getItemViewType(int position) {

        return 5;
    }

    @Override
    public JSONObject getItem(int arg0) {
        JSONObject jsonitem = null;
        try {
            jsonitem = itemsData.getJSONObject(arg0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonitem;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v=inflater.inflate(R.layout.custom_row,parent,false);
        return mListener.getView(position, convertView, parent);
    }

}
