package audio.rabid.artemis.models;

import android.net.Uri;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by  charles  on 6/3/16.
 */

public class Track extends RealmObject {

    @PrimaryKey
    private long id;

    private int trackNumber;
    private Artist artist;
    private Album album;
    private String title;
    private long length;

    @Required
    private String uri;

    public long getId() {
        return id;
    }

    public Uri getUri(){
        return Uri.parse(uri);
    }

    public Artist getArtist() {
        return artist;
    }

    public Album getAlbum() {
        return album;
    }

    public String getTitle() {
        return title;
    }

    public long getLength() {
        return length;
    }

    public static Observable<RealmResults<Track>> getForAlbum(long albumId){
        return Realm.getDefaultInstance().where(Track.class)
                .equalTo("album.id", albumId)
                .findAllSortedAsync("trackNumber", Sort.ASCENDING)
                .asObservable()
                .filter(RealmResults::isLoaded)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<RealmResults<Track>> getAllByTitle(){
        return Realm.getDefaultInstance().where(Track.class)
                .findAllSortedAsync("title", Sort.ASCENDING)
                .asObservable()
                .filter(RealmResults::isLoaded)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
