package net.rainville.offlinedrm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.brightcove.player.edge.Catalog;
import com.brightcove.player.edge.PlaylistListener;
import com.brightcove.player.edge.VideoListener;
import com.brightcove.player.event.EventEmitter;
import com.brightcove.player.model.Playlist;
import com.brightcove.player.view.BrightcovePlayer;
import com.brightcove.player.view.BrightcoveExoPlayerVideoView;
import com.brightcove.player.media.DeliveryType;
import com.brightcove.player.model.Video;

public class MainActivity extends BrightcovePlayer {

    private Button mDownloadButton;
    private Button mPlayLocalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getVideo(R.string.videoId);
        // getPlaylist(R.string.playlistId);
        getVideo(R.string.drmVideoId);

        mDownloadButton = (Button) findViewById(R.id.true_button);
        mDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something useful.
            }
        });

    }

    public void getVideo(int video_id) {
        brightcoveVideoView = (BrightcoveExoPlayerVideoView) findViewById(R.id.brightcove_video_view);
        EventEmitter eventEmitter = brightcoveVideoView.getEventEmitter();
        Catalog catalog = new Catalog(eventEmitter, getString(R.string.account), getString(R.string.policy));

        catalog.findVideoByID(getString(video_id), new VideoListener() {

            // Add the video found to the queue with add().
            // Start playback of the video with start().
            @Override
            public void onVideo(Video video) {
                brightcoveVideoView.add(video);
                brightcoveVideoView.start();
            }

            @Override
            public void onError(String s) {
                throw new RuntimeException(s);
            }
        });

    }

    public void getPlaylist(int playlist_id) {
        brightcoveVideoView = (BrightcoveExoPlayerVideoView) findViewById(R.id.brightcove_video_view);
        EventEmitter eventEmitter = brightcoveVideoView.getEventEmitter();
        Catalog catalog = new Catalog(eventEmitter, getString(R.string.account), getString(R.string.policy));

        catalog.findPlaylistByID(getString(playlist_id), new PlaylistListener() {

            // Add the video found to the queue with add().
            // Start playback of the video with start().
            @Override
            public void onPlaylist(Playlist playlist){
                brightcoveVideoView.addAll(playlist.getVideos());
                brightcoveVideoView.start();
            }

            @Override
            public void onError(String s) {
                throw new RuntimeException(s);
            }
        });

    }

}
