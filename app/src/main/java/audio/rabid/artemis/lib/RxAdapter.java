package audio.rabid.artemis.lib;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;

import rx.Observer;

/**
 * Created by  charles  on 9/25/16.
 */
public abstract class RxAdapter<T, H extends ViewHolder<T>> extends ViewHolderArrayAdapter<T, H>
        implements Observer<Collection<T>> {

    public RxAdapter(Context context, int resourceId) {
        super(context, resourceId, new ArrayList<>());
    }

    @Override
    public void onNext(Collection<T> ts) {
        clear();
        addAll(ts);
    }

    @Override
    public void onError(Throwable e) {
        throw new RuntimeException("Unhandled exception", e);
    }

    @Override
    public void onCompleted() {

    }
}
