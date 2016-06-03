package audio.rabid.artemis.ui;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by  charles  on 6/3/16.
 */

public class MediaPlayerService extends Service {

    

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
