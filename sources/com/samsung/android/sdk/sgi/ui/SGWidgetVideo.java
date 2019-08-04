package com.samsung.android.sdk.sgi.ui;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Surface;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.render.SGResourceShaderProperty;
import com.samsung.android.sdk.sgi.render.SGShaderProgramProperty;
import com.samsung.android.sdk.sgi.render.SGShaderType;
import com.samsung.android.sdk.sgi.render.SGSurfaceTextureProperty;
import com.samsung.android.sdk.sgi.render.SGSurfaceTextureProperty.SurfaceTextureCallback;
import java.io.IOException;

public class SGWidgetVideo extends SGWidgetImage {
    private static final String FRAGMENT_SHADER_NAME = "VideoTexture.frag";
    private static final String SURFACE_TEXTURE_PROPERTY = "SGTexture";
    private static final String TAG = SGWidgetVideo.class.getName();
    private static final String VERTEX_SHADER_NAME = "VideoTexture.vert";
    private static final String VIDEO_RECT_PROPERTY = "SGVideoTextureRect";
    private volatile boolean mIsMediaPlayerAvailable = false;
    private SGShaderProgramProperty mShaderProgram = new SGShaderProgramProperty(new SGResourceShaderProperty(SGShaderType.VERTEX, VERTEX_SHADER_NAME), new SGResourceShaderProperty(SGShaderType.FRAGMENT, FRAGMENT_SHADER_NAME));
    private MediaPlayerSurfaceTextureCallback mSurfaceTextureCallback = new MediaPlayerSurfaceTextureCallback();
    private SGSurfaceTextureProperty mSurfaceTextureProperty = new SGSurfaceTextureProperty(this.mSurfaceTextureCallback, VIDEO_RECT_PROPERTY);
    private SGWidgetDecorator mWidgetDecorator = new SGWidgetDecorator(this);

    private final class MediaPlayerSurfaceTextureCallback implements SurfaceTextureCallback {
        private MediaPlayer mMediaPlayer = new MediaPlayer();
        private AssetFileDescriptor mSource;
        private SurfaceTexture mSurfaceTexture;

        public boolean destroyMediaPlayer() {
            if (this.mMediaPlayer == null || !SGWidgetVideo.this.mIsMediaPlayerAvailable) {
                return false;
            }
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            return true;
        }

        public MediaPlayer getMediaPlayer() {
            return this.mMediaPlayer;
        }

        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture) {
            this.mSurfaceTexture = surfaceTexture;
            Surface surface = new Surface(this.mSurfaceTexture);
            this.mMediaPlayer.setSurface(surface);
            this.mMediaPlayer.setLooping(true);
            surface.release();
            SGWidgetVideo.this.mIsMediaPlayerAvailable = true;
        }

        public void onSurfaceTextureDestroy() {
            this.mSurfaceTexture = null;
            destroyMediaPlayer();
        }

        public void prepareMediaPlayer() {
            try {
                this.mMediaPlayer.prepare();
            } catch (IllegalStateException e) {
                Log.e(SGWidgetVideo.TAG, "Media player prepare failed " + e.getMessage());
            } catch (IOException e2) {
                Log.e(SGWidgetVideo.TAG, "Media player prepare failed " + e2.getMessage());
            }
        }

        public void resetMediaPlayer() {
            try {
                this.mMediaPlayer.reset();
            } catch (IllegalStateException e) {
                Log.e(SGWidgetVideo.TAG, "Media player prepare failed " + e.getMessage());
            }
        }

        public void setSource(AssetFileDescriptor source) {
            this.mSource = source;
            if (this.mMediaPlayer != null) {
                try {
                    if (this.mMediaPlayer.isPlaying()) {
                        this.mMediaPlayer.stop();
                    }
                    this.mMediaPlayer.reset();
                    this.mMediaPlayer.setDataSource(this.mSource.getFileDescriptor(), this.mSource.getStartOffset(), this.mSource.getLength());
                } catch (IOException e) {
                    Log.d(SGWidgetVideo.TAG, e.getMessage());
                }
            }
        }

        public void startMediaPlayer() {
            if (this.mMediaPlayer != null && !this.mMediaPlayer.isPlaying()) {
                this.mMediaPlayer.start();
            }
        }

        public void stopMediaPlayer() {
            if (this.mMediaPlayer != null && this.mMediaPlayer.isPlaying()) {
                this.mMediaPlayer.pause();
            }
        }
    }

    public SGWidgetVideo(SGVector2f size) {
        super(size, -1);
        this.mWidgetDecorator.setProgramProperty(this.mShaderProgram);
        this.mWidgetDecorator.setProperty("SGTexture", this.mSurfaceTextureProperty);
    }

    public final boolean destroyMediaPlayer() {
        return this.mSurfaceTextureCallback.destroyMediaPlayer();
    }

    public void finalize() {
        super.finalize();
        this.mSurfaceTextureCallback.destroyMediaPlayer();
    }

    public final MediaPlayer getMediaPlayer() {
        return this.mSurfaceTextureCallback.getMediaPlayer();
    }

    public final void prepareMediaPlayer() {
        this.mSurfaceTextureCallback.prepareMediaPlayer();
    }

    public final void resetMediaPlayer() {
        this.mSurfaceTextureCallback.resetMediaPlayer();
    }

    public final void setSource(AssetFileDescriptor source) {
        this.mSurfaceTextureCallback.setSource(source);
    }

    public final void startMediaPlayer() {
        this.mSurfaceTextureCallback.startMediaPlayer();
    }

    public final void stopMediaPlayer() {
        this.mSurfaceTextureCallback.stopMediaPlayer();
    }
}
