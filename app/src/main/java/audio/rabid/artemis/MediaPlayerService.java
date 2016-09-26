package audio.rabid.artemis;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import audio.rabid.artemis.models.Track;

/**
 * Created by  charles  on 6/3/16.
 */

public class MediaPlayerService extends Service implements AudioManager.OnAudioFocusChangeListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    MediaPlayer mediaPlayer;
    AudioManager audioManager;

    MediaBinder binder = new MediaBinder();

    State state = State.NO_MEDIA_PLAYER;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void setState(State state){
        if(!this.state.canChangeTo(state)){
            throw new IllegalStateException("Tried to change from "+this.state+" to "+state);
        }
        this.state = state;
    }

    private void prepare(){
        setState(State.PREPARING);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }

    private void playTrack(Track track){
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // could not get audio focus.
        }

        try {
            mediaPlayer.setDataSource(this, track.getUri());
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.prepare();
        }catch (IOException e){
            // file not found
        }
        setState(State.STARTED);
        mediaPlayer.start();
    }

    private void pause(){
        setState(State.PAUSED);
        mediaPlayer.pause();
    }

    private void resume(){
        setState(State.STARTED);
        mediaPlayer.start();
    }

    private void stop(){
        setState(State.STOPPED);
        mediaPlayer.stop();
    }

    // TODO seek, next, previous, etc

    //            mediaPlayer.setOnSeekCompleteListener(this);

    // On end of queue, stopForeground(true);

    // mediaPlayer.setNextMediaPlayer();


            /*
        Uri contentUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDataSource(getApplicationContext(), contentUri);
         */


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(state != State.NO_MEDIA_PLAYER) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if(state != State.STARTED){
            return;
        }
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
//                if (!mMediaPlayer.isPlaying()) mMediaPlayer.start();
//                mMediaPlayer.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
//                if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
//                mMediaPlayer.release();
//                mMediaPlayer = null;
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
//                if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
//                if (mMediaPlayer.isPlaying()) mMediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    public class MediaBinder extends Binder {

        private List<Track> queue = new LinkedList<>();

//        public void clearQueue(){
//            if(mediaPlayer.isPlaying()){
//                // Stop
//            }
//            queue.clear();
//        }
//
//        public void addToQueue(Track... tracks){
//            queue.addAll(Arrays.asList(tracks));
//        }
//
//        public void setQueue(Track... tracks){
//            clearQueue();
//            addToQueue(tracks);
//        }
//
//        public boolean isPlaying(){
//            return mediaPlayer.isPlaying();
//        }
//
//        public void play(){
//
//        }
//
//        public void pause(){
//            mediaPlayer.pause();
//        }
    }

    private enum State {
        NO_MEDIA_PLAYER,
        PREPARING,
        STARTED,
        PAUSED,
        STOPPED;


        public boolean canChangeTo(State to){
            switch (to){
                case NO_MEDIA_PLAYER:
                    return this == State.STOPPED;
                case PREPARING:
                    return this == State.NO_MEDIA_PLAYER;
                case STARTED:
                    return (this == State.PREPARING || this == State.PAUSED || this == State.STOPPED);
                case PAUSED:
                    return this == State.STARTED;
                case STOPPED:
                    return this == State.PAUSED || this == State.STARTED;
                default:
                    return false;
            }
        }
    }
}
