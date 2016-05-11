package com.propkaro.rssfeed;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.propkaro.R;

public class ViewVideo extends Activity {
      private String filename = "", GET_VIDEO_FILE = "";
      VideoView videoView;
      
      @Override
      public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.video_main);
//    		requestWindowFeature(Window.FEATURE_NO_TITLE);
//            getWindow().requestFeature(Window.FEATURE_PROGRESS);
//    		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            System.gc();
            Intent i = getIntent();
            Bundle extras = i.getExtras();
            GET_VIDEO_FILE = extras.getString("GET_VIDEO_FILE");
            
            filename = "http://mrpopat.in/embed.php?id=23456&wth=640&hgt=385&autoplay=true";
//            + "&wth=640&hgt=385&autoplay=true%22%20class=%22strobemediaplayback-video-player";
 
            System.out.println(filename);
            VideoView vidView = (VideoView)findViewById(R.id.myVideo);
//            String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
            String vidAddress = "http://mrpopat.in/embed.php?id=23456";
            Uri vidUri = Uri.parse(GET_VIDEO_FILE);
            vidView.setVideoURI(vidUri);
            vidView.start();
            MediaController vidControl = new MediaController(this);
            vidControl.setAnchorView(vidView);
            vidView.setMediaController(vidControl);
            vidView.requestFocus();
      }
}