package com.example.bear.videoview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {


    String TAG = "MainActivity";
    private VideoView videoView;
    private int position = 0;
    private MediaController mediaController;
    private Button startButton;
    private Button pauseButton;
    private Button resumeButton;
    private int mCurrentPosition = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        videoView = (VideoView) findViewById(R.id.videoView);
        startButton = (Button) findViewById(R.id.button);
        pauseButton = (Button) findViewById(R.id.button2);
        resumeButton = (Button) findViewById(R.id.button3);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "startButton()");
                videoView.start();
                Log.v(TAG, "startButton() after videoView.start()");
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "pauseButton()");
                mCurrentPosition = videoView.getCurrentPosition();
                Log.v(TAG, "pauseButton() , mCurrentPosition ="+mCurrentPosition);
                videoView.pause();
                Log.v(TAG, "pauseButton() , videoView.pause() after , mCurrentPosition ="+mCurrentPosition);
            }
        });
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.v(TAG, "resumeButton(),mCurrentPosition ="+mCurrentPosition);
//                videoView.seekTo(mCurrentPosition);
                Log.v(TAG, "resumeButton(),before videoView.start()");
                videoView.start();
                Log.v(TAG, "resumeButton(),after videoView.start()");
//                Log.v(TAG, "resumeButton(),before videoView.resume()");
//                videoView.resume();
//                Log.v(TAG, "resumeButton(),after videoView.resume()");
            }
        });

        // Set the media controller buttons
        if (mediaController == null) {
            mediaController = new MediaController(MainActivity.this);

            // Set the videoView that acts as the anchor for the MediaController.
            mediaController.setAnchorView(videoView);


            // Set MediaController for VideoView
            videoView.setMediaController(mediaController);
        }


        try {
            // ID of video file.
//            int id = this.getRawResIdByName("myvideo");
//            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + id));

            //http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8
            String videoUrl = "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
            Uri video = Uri.parse(videoUrl);
            videoView.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();


        // When the video file ready for playback.
        videoView.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {


                Log.v(TAG, "onPrepared()");
                videoView.seekTo(position);
                Log.v(TAG, "onPrepared() position =" + position);
                if (position == 0) {
                    Log.v(TAG, "onPrepared() before videoView.start()");
//                    videoView.start();
                }

                // When video Screen change size.
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                        Log.v(TAG, "onVideoSizeChanged() before mediaController.setAnchorView(videoView)");
                        // Re-Set the videoView that acts as the anchor for the MediaController
                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });

    }

    // Find ID corresponding to the name of the resource (in the directory raw).
    public int getRawResIdByName(String resName) {
        String pkgName = this.getPackageName();
        // Return 0 if not found.
        int resID = this.getResources().getIdentifier(resName, "raw", pkgName);
        Log.i("AndroidVideoView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }


    // When you change direction of phone, this method will be called.
    // It store the state of video (Current position)
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Store current position.
        savedInstanceState.putInt("CurrentPosition", videoView.getCurrentPosition());
        Log.v(TAG, "onSaveInstanceState() setCurrentPosition before videoView.pause()");
        videoView.pause();
    }


    // After rotating the phone. This method is called.
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Get saved position.
        position = savedInstanceState.getInt("CurrentPosition");
        Log.v(TAG, "onRestoreInstanceState() getCurrentPosition before videoView.seekTo(position)");
        videoView.seekTo(position);
    }

}