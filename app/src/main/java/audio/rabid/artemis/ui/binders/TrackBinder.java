package audio.rabid.artemis.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import audio.rabid.artemis.R;
import audio.rabid.artemis.ViewHolderArrayAdapter;
import audio.rabid.artemis.models.Track;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by  charles  on 9/25/16.
 */
public class TrackBinder implements ViewHolderArrayAdapter.ViewHolder<Track> {

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.length)
    TextView length;

    public TrackBinder(View v){
        ButterKnife.bind(this, v);
    }

    @Override
    public void bind(Track item, Context context) {
        name.setText(item.getTitle());
        length.setText(String.format(Locale.US, "%d:%02d",
                (int)(item.getLength()/1000) / 60,
                (int)(item.getLength()/1000) % 60)); // TODO localization, tracks longer than an hour
    }
}
