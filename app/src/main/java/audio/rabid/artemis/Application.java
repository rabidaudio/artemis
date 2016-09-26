package audio.rabid.artemis;

import com.squareup.leakcanary.LeakCanary;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rapid.decoder.BitmapDecoder;

/**
 * Created by  charles  on 6/3/16.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        BitmapDecoder.initMemoryCache(this);
        BitmapDecoder.initDiskCache(this, 50 * 1024 * 1024);

        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build());
    }
}
