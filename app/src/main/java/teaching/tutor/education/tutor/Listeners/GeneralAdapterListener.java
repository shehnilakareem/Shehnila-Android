package teaching.tutor.education.tutor.Listeners;

import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

public interface GeneralAdapterListener {
    View getView(int position, View convertView, ViewGroup parent);
}
