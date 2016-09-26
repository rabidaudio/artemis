package audio.rabid.artemis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by  charles  on 9/25/16.
 */
public abstract class ViewHolderArrayAdapter<T, H extends ViewHolderArrayAdapter.ViewHolder<T>> extends ArrayAdapter<T> {

    private int resourceId;

    public ViewHolderArrayAdapter(Context context, int resourceId, List<T> items) {
        super(context, resourceId, items);
        this.resourceId = resourceId;
    }

    public abstract H getHolder(View v);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if(convertView != null){
            v = convertView;
        }else{
            v = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        }
        H holder = (H) v.getTag();
        if(holder == null){
            holder = getHolder(v);
            v.setTag(holder);
        }

        holder.bind(getItem(position), getContext());

        return v;
    }

    public interface ViewHolder<T> {
        void bind(T item, Context context);
    }
}
