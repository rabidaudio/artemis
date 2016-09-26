package audio.rabid.artemis.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ListView;

import audio.rabid.artemis.ArtemisActivity;
import audio.rabid.artemis.R;
import audio.rabid.artemis.RxAdapter;
import audio.rabid.artemis.models.Album;
import audio.rabid.artemis.models.Track;
import audio.rabid.artemis.ui.binders.TrackBinder;
import butterknife.BindView;
import io.realm.Realm;

public class TracksActivity extends ArtemisActivity {

    private static final String EXTRA_ALBUM_ID = "EXTRA_ALBUM_ID";

    @BindView(R.id.track_list)
    ListView trackList;

    @Override
    public void prepareContentView() {
        setContentView(R.layout.activity_tracks);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TracksAdapter tracksAdapter = new TracksAdapter();

        trackList.setAdapter(tracksAdapter);

        ViewCompat.setNestedScrollingEnabled(trackList, true);

        long albumId = getIntent().getLongExtra(EXTRA_ALBUM_ID, -1);

        if(albumId == -1){
            getDisposeBag().add(Track.getAllByTitle().subscribe(tracksAdapter));
            setTitle(getString(R.string.tracks));
        }else{
            getDisposeBag().add(Track.getForAlbum(albumId).subscribe(tracksAdapter));
            setTitle(Realm.getDefaultInstance().where(Album.class).equalTo("id", albumId).findFirst().getName());
        }
    }

    public static void launchForAlbum(Context context, Album album){
        Intent i = new Intent(context, TracksActivity.class);
        i.putExtra(EXTRA_ALBUM_ID, album.getId());
        context.startActivity(i);
    }

    @Override
    public void onFabClick(FloatingActionButton fab) {

    }

    private class TracksAdapter extends RxAdapter<Track, TrackBinder> {
        TracksAdapter(){
            super(TracksActivity.this, R.layout.item_track);
        }

        @Override
        public TrackBinder getHolder(View v) {
            return new TrackBinder(v);
        }
    }
}
