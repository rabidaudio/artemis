package audio.rabid.artemis.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ListView;

import com.jakewharton.rxbinding.widget.RxAdapterView;

import audio.rabid.artemis.ArtemisActivity;
import audio.rabid.artemis.R;
import audio.rabid.artemis.lib.RxAdapter;
import audio.rabid.artemis.models.Album;
import audio.rabid.artemis.models.Artist;
import audio.rabid.artemis.ui.binders.AlbumBinder;
import butterknife.BindView;
import io.realm.Realm;

public class AlbumsActivity extends ArtemisActivity {

    private static final String EXTRA_ARTIST_ID = "EXTRA_ARTIST_ID";

    @BindView(R.id.albumsList)
    ListView albumsList;

    @Override
    public void prepareContentView() {
        setContentView(R.layout.activity_albums);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewCompat.setNestedScrollingEnabled(albumsList, true);

        AlbumAdapter albumAdapter = new AlbumAdapter();

        albumsList.setAdapter(albumAdapter);

        long artistId = getIntent().getLongExtra(EXTRA_ARTIST_ID, -1);
        if(artistId == -1){
            getDisposeBag().add(
                    Album.getAllByYear().subscribe(albumAdapter)
            );
            setTitle(getString(R.string.albums));
        }else{
            getDisposeBag().add(
                    Album.getByYearForArtist(artistId).subscribe(albumAdapter)
            );
            setTitle(Realm.getDefaultInstance().where(Artist.class).equalTo("id", artistId).findFirst().getName());
        }

        getDisposeBag().add(
                RxAdapterView.itemClicks(albumsList).map(albumAdapter::getItem)
                        .subscribe(a -> TracksActivity.launchForAlbum(this, a))
        );
    }

    public static void launchForArtist(Context context, Artist artist){
        Intent i = new Intent(context, AlbumsActivity.class);
        i.putExtra(EXTRA_ARTIST_ID, artist.getId());
        context.startActivity(i);
    }

    @Override
    public void onFabClick(FloatingActionButton fab) {

    }

    private class AlbumAdapter extends RxAdapter<Album, AlbumBinder> {

        public AlbumAdapter(){
            super(AlbumsActivity.this, R.layout.item_album);
        }

        @Override
        public AlbumBinder getHolder(View v) {
            return new AlbumBinder(v);
        }
    }
}
