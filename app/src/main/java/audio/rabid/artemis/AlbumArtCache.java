//package audio.rabid.artemis;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//
//import com.jakewharton.disklrucache.DiskLruCache;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//import rx.Single;
//import rx.SingleSubscriber;
//
///**
// * Created by  charles  on 9/25/16.
// */
//public class AlbumArtCache {
//
//    private static AlbumArtCache instance;
//
//    public static void setUp(Context context){
//        if(instance == null){
//            instance = new AlbumArtCache(context);
//        }
//    }
//
//    private DiskLruCache cache;
//    private Executor executor = Executors.newSingleThreadExecutor();
//
//    private AlbumArtCache(Context context) {
//        try {
//            cache = DiskLruCache.open(context.getCacheDir(), 1, 1024, 1024*1024*50);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public Single<Bitmap> getBitmap(String key){
//        return Single.create(new BitmapGetter(key));
//    }
//
//    private static class BitmapGetter implements Single.OnSubscribe<Bitmap>, Runnable {
//
//        private SingleSubscriber<? super Bitmap> singleSubscriber;
//        private String key;
//
//        BitmapGetter(String key){
//            this.key = key;
//        }
//
//        @Override
//        public void call(SingleSubscriber<? super Bitmap> singleSubscriber) {
//            this.singleSubscriber = singleSubscriber;
//            instance.executor.execute(this);
//        }
//
//        @Override
//        public void run() {
//            try {
//                DiskLruCache.Snapshot s = instance.cache.get(key);
//                if (s == null) {
//                    singleSubscriber.onSuccess(null);
//                    return;
//                }
//                InputStream is = s.getInputStream(0);
//                singleSubscriber.onSuccess(BitmapFactory.decodeStream(is));
//            }catch (Exception e){
//                singleSubscriber.onError(e);
//            }
//        }
//    }
//
//    public void setBitmap(String key, Bitmap bitmap){
//
//    }
//
//    private static class BitmapSaver implements Runnable {
//
//        private String key;
//        private Bitmap bitmap;
//
//        BitmapSaver(String key, Bitmap bitmap){
//            this.key = key;
//            this.bitmap = bitmap;
//        }
//
//        @Override
//        public void run() {
//            DiskLruCache.Editor e = instance.cache.edit(key);
//            if(e != null){
//                OutputStream os = e.newOutputStream(0);
//                bitmap.
//            }
//        }
//    }
//}
