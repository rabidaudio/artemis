package audio.rabid.artemis.models;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by  charles  on 6/3/16.
 */

public class Album extends RealmObject {

    @PrimaryKey
    private long id;

    private Artist artist;

    private String name;

    private int year;

    private String artUrl;

    public int getYear() {
        return year;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public Artist getArtist() {
        return artist;
    }

    public String getArtUrl(){
        return artUrl;
    }

    public long getTrackCount(){
        return Realm.getDefaultInstance().where(Track.class).equalTo("album.id", id).count();
    }

    public static Observable<RealmResults<Album>> getByYearForArtist(long artistId){
        return Realm.getDefaultInstance().where(Album.class)
                .equalTo("artist.id", artistId)
                .findAllSortedAsync("year", Sort.ASCENDING)
                .asObservable()
                .filter(RealmResults::isLoaded)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<RealmResults<Album>> getAllByYear(){
        return Realm.getDefaultInstance().where(Album.class)
                .findAllSortedAsync("year", Sort.ASCENDING)
                .asObservable()
                .filter(RealmResults::isLoaded)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
