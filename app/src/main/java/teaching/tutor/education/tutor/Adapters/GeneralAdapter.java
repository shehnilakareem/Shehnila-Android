package teaching.tutor.education.tutor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;

import teaching.tutor.education.tutor.Listeners.GeneralAdapterListener;
import teaching.tutor.education.tutor.Utils.GeneralModel;

/**
 * Created by Shehnila Kareem on 15-Jan-17.
 */

public class GeneralAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<GeneralModel> itemsData;
    Context context;

    private GeneralAdapterListener mListener;


    public GeneralAdapter(Activity activity, GeneralAdapterListener listener )
    {
        mActivity = activity;
        mListener = listener;
    }
    public void setArray(ArrayList<GeneralModel> list)
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
    public GeneralModel  getItem(int arg0) {
        GeneralModel jsonitem = null;
        jsonitem= itemsData.get(arg0);

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
