package audio.rabid.artemis.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.jakewharton.rxbinding.widget.RxAdapterView;

import audio.rabid.artemis.ArtemisActivity;
import audio.rabid.artemis.R;
import audio.rabid.artemis.RxAdapter;
import audio.rabid.artemis.models.Artist;
import audio.rabid.artemis.ui.binders.ArtistBinder;
import butterknife.BindView;

public class ArtistsActivity extends ArtemisActivity {

    @BindView(R.id.artist_list)
    ListView artistList;

    @Override
    public void prepareContentView() {
        setContentView(R.layout.activity_artists);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.artists));

//        MediaContainer.getPermissions(this);

        ArtistAdapter artistAdapter = new ArtistAdapter();
        artistList.setAdapter(artistAdapter);

        getDisposeBag().addAll(
                Artist.getAllBySortName().subscribe(artistAdapter),
                RxAdapterView.itemClicks(artistList).map(artistAdapter::getItem)
                        .subscribe(a -> AlbumsActivity.launchForArtist(this, a))
        );
    }

    @Override
    public void onFabClick(FloatingActionButton fab) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_artists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                // open settings
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//
//    }

    private class ArtistAdapter extends RxAdapter<Artist,ArtistBinder> {
        public ArtistAdapter() {
            super(ArtistsActivity.this, R.layout.item_artist);
        }

        @Override
        public ArtistBinder getHolder(View v) {
            return new ArtistBinder(v);
        }
    }
}