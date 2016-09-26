package audio.rabid.artemis.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import audio.rabid.artemis.R;
import audio.rabid.artemis.lib.ViewHolder;
import audio.rabid.artemis.models.Album;
import butterknife.BindView;
import rapid.decoder.BitmapDecoder;
import rapid.decoder.binder.Effect;
import rapid.decoder.binder.ImageViewBinder;
import rapid.decoder.binder.ViewBinder;

/**
 * Created by  charles  on 9/25/16.
 */
public class AlbumBinder extends ViewHolder<Album> {

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.year)
    TextView year;

    @BindView(R.id.tracks)
    TextView tracks;

    @BindView(R.id.art)
    ImageView art;

    ViewBinder<ImageView> viewBinder;

    public AlbumBinder(View v){
        super(v);
        ImageViewBinder.obtain(art).effect(Effect.FADE_IN);
    }

    public void bind(Album album, Context context){
        name.setText(album.getName());
        year.setText(String.valueOf(album.getYear()));
        tracks.setText(String.format(context.getString(R.string.n_tracks), album.getTrackCount()));
        if(album.getArtUrl() != null) {
            BitmapDecoder.from(album.getArtUrl()).into(viewBinder);
        }
    }
}
