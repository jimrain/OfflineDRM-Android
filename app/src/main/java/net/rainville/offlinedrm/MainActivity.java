package net.rainville.offlinedrm;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.brightcove.player.edge.Catalog;
import com.brightcove.player.edge.PlaylistListener;
import com.brightcove.player.edge.VideoListener;
import com.brightcove.player.event.EventEmitter;
import com.brightcove.player.model.Playlist;
import com.brightcove.player.view.BrightcovePlayer;
import com.brightcove.player.view.BrightcoveExoPlayerVideoView;
import com.brightcove.player.media.DeliveryType;
import com.brightcove.player.model.Video;

import com.brightcove.player.event.Event;
import com.brightcove.player.media.DeliveryType;
import com.brightcove.player.model.Video;
import com.brightcove.player.network.DownloadManager;
import com.brightcove.player.network.DownloadStatus;
import com.brightcove.player.offline.DashDownloadable;
import com.brightcove.player.offline.MediaDownloadable;
import com.brightcove.player.offline.RequestConfig;
import com.brightcove.player.store.DownloadRequest;
import com.brightcove.player.view.BrightcovePlayer;

import java.io.Serializable;
import java.util.Map;

public class MainActivity extends BrightcovePlayer {

    private static final String TAG = "OfflineDRM";
    private Button mDownloadButton;
    private Button mPlayLocalButton;
    private Button mCancelDownloadButton;
    private Video mTargetVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getVideo(R.string.videoId);
        // getPlaylist(R.string.playlistId);
        getVideo(R.string.drmVideoId);

        mDownloadButton = (Button) findViewById(R.id.download_button);
        mDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestConfig requestConfig = new RequestConfig();
                requestConfig.setNotificationVisibility(RequestConfig.VISIBILITY_VISIBLE);
                requestConfig.setMobileDownloadAllowed(true);

                // Log.i(TAG, String.format("Download path: %s", requestConfig.getDownloadPath().toString()));

                // Create the DashDownloadable with the video object
                DashDownloadable dashDownloadable = new DashDownloadable(getApplicationContext(), mTargetVideo, downloadEventListener, requestConfig);
                dashDownloadable.requestDownload();
            }
        });

        mPlayLocalButton = (Button) findViewById(R.id.playlocal_button);
        mPlayLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something useful.
            }
        });

        mCancelDownloadButton = (Button) findViewById(R.id.cancel_download_button);
        mCancelDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestConfig requestConfig = new RequestConfig();
                requestConfig.setNotificationVisibility(RequestConfig.VISIBILITY_VISIBLE);
                requestConfig.setMobileDownloadAllowed(true);

                // Log.i(TAG, String.format("Download path: %s", requestConfig.getDownloadPath().toString()));

                // Create the DashDownloadable with the video object
                DashDownloadable dashDownloadable = new DashDownloadable(getApplicationContext(), mTargetVideo, downloadEventListener, requestConfig);
                dashDownloadable.cancelDownload();
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
                mTargetVideo = video;
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

    /**
     * Implements a {@link com.brightcove.player.offline.MediaDownloadable.DownloadEventListener} that
     * will show a toast message about the download status and refresh the video list display.
     */
    private final MediaDownloadable.DownloadEventListener downloadEventListener = new MediaDownloadable.DownloadEventListener() {

        @Override
        public void onDownloadRequested(@NonNull final Video video) {
            Log.i(TAG, String.format(
                    "Starting to process '%s' video download request", video.getName()));
            // videoListAdapter.notifyVideoChanged(video);
        }

        @Override
        public void onDownloadStarted(@NonNull Video video, long estimatedSize, @NonNull Map<String, Serializable> mediaProperties) {
            // videoListAdapter.notifyVideoChanged(video);
            String message = showToast(
                    "Started to download '%s' video. Estimated = %s, width = %s, height = %s, mimeType = %s",
                    video.getName(),
                    Formatter.formatFileSize(MainActivity.this, estimatedSize),
                    mediaProperties.get(Event.RENDITION_WIDTH),
                    mediaProperties.get(Event.RENDITION_HEIGHT),
                    mediaProperties.get(Event.RENDITION_MIME_TYPE)
            );
            Log.i(TAG, message);
        }

        @Override
        public void onDownloadProgress(@NonNull final Video video, @NonNull final DownloadStatus status) {
            Log.i(TAG, String.format(
                    "Downloaded %s out of %s of '%s' video. Progress %3.2f",
                    Formatter.formatFileSize(MainActivity.this, status.getBytesDownloaded()),
                    Formatter.formatFileSize(MainActivity.this, status.getMaxSize()),
                    video.getName(), status.getProgress()));
            // videoListAdapter.notifyVideoChanged(video, status);
        }

        @Override
        public void onDownloadPaused(@NonNull final Video video, @NonNull final DownloadStatus status) {
            Log.i(TAG, String.format(
                    "Paused download of '%s' video: Reason #%d", video.getName(), status.getReason()));
            // videoListAdapter.notifyVideoChanged(video, status);
        }

        @Override
        public void onDownloadCompleted(@NonNull final Video video, @NonNull final DownloadStatus status) {
            // videoListAdapter.notifyVideoChanged(video, status);
            String message = showToast(
                    "Successfully saved '%s' video", video.getName());
            Log.i(TAG, message);
        }

        @Override
        public void onDownloadCanceled(@NonNull final Video video) {
            //No need to update UI here because it will be handled by the deleteVideo method.
            String message = showToast(
                    "Cancelled download of '%s' video removed", video.getName());
            Log.i(TAG, message);
            // onDownloadRemoved(video);
        }

        @Override
        public void onDownloadDeleted(@NonNull final Video video) {
            //No need to update UI here because it will be handled by the deleteVideo method.
            String message = showToast(
                    "Offline copy of '%s' video removed", video.getName());
            Log.i(TAG, message);
            // onDownloadRemoved(video);
        }

        @Override
        public void onDownloadFailed(@NonNull final Video video, @NonNull final DownloadStatus status) {
            // videoListAdapter.notifyVideoChanged(video, status);
            String message = showToast(
                    "Failed to download '%s' video: Error #%d", video.getName(), status.getReason());
            Log.e(TAG, message);
        }
    };

    /**
     * Shows a formatted toast message.
     *
     * @param message    the message to be shown. The message may include string format tokens.
     * @param parameters the parameters to be used for formatting the message.
     * @return the formatted message that was shown.
     * @see String#format(String, Object...)
     */
    private String showToast(@NonNull String message, @Nullable Object... parameters) {
        if (parameters != null) {
            message = String.format(message, parameters);
        }
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

        return message;
    }

}
