package audio.rabid.artemis.lib;

import android.content.Context;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by  charles  on 9/26/16.
 */
public abstract class ViewHolder<T> {

    public ViewHolder(View v){
        ButterKnife.bind(this, v);
    }

    public abstract void bind(T item, Context context);
}
