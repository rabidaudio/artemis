package audio.rabid.artemis.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import audio.rabid.artemis.R;
import audio.rabid.artemis.lib.ViewHolder;
import audio.rabid.artemis.models.Artist;
import butterknife.BindView;

/**
 * Created by  charles  on 9/25/16.
 */
public class ArtistBinder extends ViewHolder<Artist> {

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.albums)
    TextView albums;

    public ArtistBinder(View v){
        super(v);
    }

    @Override
    public void bind(Artist item, Context context) {
        name.setText(item.getName());
        albums.setText(String.format(Locale.getDefault(), context.getString(R.string.n_albums), item.getAlbumCount()));
    }
}
