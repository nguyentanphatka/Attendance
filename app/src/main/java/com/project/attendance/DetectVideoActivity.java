package com.project.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

public class DetectVideoActivity extends AppCompatActivity {

    private VideoView videoPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_video);

        videoPreview = (VideoView)findViewById(R.id.videoPreview);
        Bundle bundle = getIntent().getExtras();
        String videoPath = bundle.getString("videoPath");
        if (videoPath != null)
        {
            try {
                videoPreview.setVisibility(View.VISIBLE);
                videoPreview.setVideoPath(videoPath);
                videoPreview.start();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
