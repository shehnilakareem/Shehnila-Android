package teaching.tutor.education.tutor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import teaching.tutor.education.tutor.Listeners.GeneralAdapterListener;

public class SimpleAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList<String> itemsData;
    Context context;

    @SuppressWarnings("unused")
    private GeneralAdapterListener mListener;


    public SimpleAdapter(Activity activity, GeneralAdapterListener listener )
    {
        mActivity = activity;
        mListener = listener;
    }
    public void setArray(ArrayList<String> list)
    {
        this.itemsData = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (itemsData == null)

            return 0;
        return itemsData.size();

    }

    @Override
    public int getItemViewType(int position) {

        return 5;
    }

    @Override
    public String  getItem(int arg0) {
        return itemsData.get(arg0);
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
