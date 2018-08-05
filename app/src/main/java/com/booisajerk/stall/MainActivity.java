package com.booisajerk.stall;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static android.R.attr.max;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.booisajerk.stall.R.mipmap.random;


public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static String LOG_TAG = "MYTAG " + MainActivity.class.getSimpleName();
    private String PLAYLIST_ID = null;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    private YouTubePlayer player;
    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);
    private LinearLayout playerHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logger.debug(LOG_TAG, "OnCreate called.");

        //Init YouTubePlayerHolder
        playerHolder = (LinearLayout) findViewById(R.id.youtube_player_holder);
        Log.d(LOG_TAG, "Initializing playerHolder");

        //Init YouTubePlayerView
        Log.d(LOG_TAG, "Instantiating YouTubePlayer.");
        YouTubePlayerFragment youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager().
                findFragmentById(R.id.youtube_player);
        Log.d(LOG_TAG, "Initializing YouTubePlayer.");
        youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, this);
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "OnStart called");
        super.onStart();

        Log.d(LOG_TAG, "Init playerStateChangeListener.");
        playerStateChangeListener = new MyPlayerStateChangeListener();

        Log.d(LOG_TAG, "Init playbackEventListener.");
        playbackEventListener = new MyPlaybackEventListener();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "OnRestoreInstanceState called. ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "OnPause Called.");
        if (player != null) {
            int videoTime = player.getCurrentTimeMillis();
            Log.d(LOG_TAG, "Video time: " + videoTime);
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, "Back pressed.");
        if (player != null) {
            player.setFullscreen(false);
            playerHolder.setVisibility(View.GONE);
        }
    }

    public void onBtnClick(View view) {
        int id = view.getId();

        Log.d(LOG_TAG, "setting player to fullscreen.");

        switch (id) {
            case R.id.happy_button:
                PLAYLIST_ID = "PLQfkOEjJ1om5OSGSbzabXEG9X0m0XAInR";
                Log.d(LOG_TAG, "Happy button clicked.");
                break;
            case R.id.fail_button:
                PLAYLIST_ID = "PLQfkOEjJ1om4_EnfHPcreRcgKUgkUW-xx";
                Log.d(LOG_TAG, "Fail button clicked.");
                break;
            case R.id.random_button:
                PLAYLIST_ID = "LLtg5C-d_3rPUgMaxr285mQQ";
                Log.d(LOG_TAG, "Random button clicked.");
                break;
            default:
                Log.d(LOG_TAG, "Default video selected.");
                break;
        }
        playPlaylist(PLAYLIST_ID);
        Log.d(LOG_TAG, "Play playlist");
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        Log.d(LOG_TAG, "Setting PlayerStateChangeListener.");
        player.setPlayerStateChangeListener(playerStateChangeListener);
        Log.d(LOG_TAG, "Setting PlaybackEventListener.");
        player.setPlaybackEventListener(playbackEventListener);

        Log.d(LOG_TAG, "Setting PlayerStyle");
        player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);

        Log.d(LOG_TAG, "Assigning player.");
        this.player = player;
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.d(LOG_TAG, "onInitializationFailed called");
        Toast.makeText(this, "Initialization failed.", Toast.LENGTH_LONG).show();
    }

    private void showMessage(String message) {
        logger.debug("Entering showMessage(message={})", message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {
        @Override
        public void onPlaying() {
            Log.d(LOG_TAG, "Playing video.");
            showMessage("Playing");
        }

        @Override
        public void onPaused() {
            Log.d(LOG_TAG, "Video Paused.");
        }

        @Override
        public void onStopped() {
            Log.d(LOG_TAG, "Stopped.");
        }

        @Override
        public void onBuffering(boolean b) {
            Log.d(LOG_TAG, "Buffering.");
            showMessage("Buffering");
        }

        @Override
        public void onSeekTo(int i) {
            Log.d(LOG_TAG, "Seeking to " + i);
        }
    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {
        @Override
        public void onLoading() {
            Log.d(LOG_TAG, "Loading");
            showMessage("Loading");
        }

        @Override
        public void onLoaded(String s) {
            Log.d(LOG_TAG, "Loaded");
        }

        @Override
        public void onAdStarted() {
            Log.d(LOG_TAG, "Ad Ended");
        }

        @Override
        public void onVideoStarted() {
            Log.d(LOG_TAG, "Video Started");
        }

        @Override
        public void onVideoEnded() {
            Log.d(LOG_TAG, "Video Ended");
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            Log.d(LOG_TAG, "Error received: " + errorReason);
            showMessage("Sorry, there has been an error.");
        }
    }

    private static int getRandomNumber() {
        int min = 1;
        int max = 5;
        Random random = new Random();
        int randomIndex = random.nextInt(max - min + 1) + min;
        Log.d(LOG_TAG, "Random index of video selected: " + randomIndex);
        return randomIndex;
    }

    private void playPlaylist(String PLAYLIST_ID) {
        playerHolder.setVisibility(View.VISIBLE);
        int startTime = 0;
        player.loadPlaylist(PLAYLIST_ID, getRandomNumber(), startTime);
        Log.d(LOG_TAG, "Cue selected playlist.");
        player.setFullscreen(true);
        Log.d(LOG_TAG, "Setting full screen.");
        player.play();
    }
}