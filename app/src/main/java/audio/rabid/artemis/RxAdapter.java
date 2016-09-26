package audio.rabid.artemis;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.jakewharton.rxbinding.widget.RxAdapterView;

import java.util.ArrayList;
import java.util.Collection;

import rx.Observable;
import rx.Observer;

/**
 * Created by  charles  on 9/25/16.
 */
public abstract class RxAdapter<T, H extends ViewHolderArrayAdapter.ViewHolder<T>> extends ViewHolderArrayAdapter<T, H> implements Observer<Collection<T>> {

    public RxAdapter(Context context, int resourceId) {
        super(context, resourceId, new ArrayList<>());
    }

    @Override
    public void onNext(Collection<T> ts) {
        clear();
        addAll(ts);
//        notifyDataSetChanged();
    }

    @Override
    public void onError(Throwable e) {
        throw new RuntimeException("Unhandled exception", e);
    }

    @Override
    public void onCompleted() {

    }

    @SuppressWarnings("unchecked")
    public static <T, A extends ArrayAdapter<T>> Observable<T> itemOnClick(AdapterView<A> view) {
        return RxAdapterView.itemClickEvents(view).map(event -> ((A) event.view().getAdapter()).getItem(event.position()));
    }
}
