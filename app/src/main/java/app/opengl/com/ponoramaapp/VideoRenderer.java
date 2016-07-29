package app.opengl.com.ponoramaapp;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import org.r3d.cardboard.RCardboardRenderer;
import org.r3d.materials.Material;
import org.r3d.materials.textures.ATexture;
import org.r3d.materials.textures.StreamingTexture;
import org.r3d.math.vector.Vector3;
import org.r3d.primitives.Sphere;

import java.io.File;

/**
 * Created by ll on 2016/4/27.
 */
public class VideoRenderer extends RCardboardRenderer {

    // Context mContext;
    VRVideoActivity vrVideoActivity;
    String videopath;
    private MediaPlayer mMediaPlayer;
    private StreamingTexture mVideoTexture;

    public VideoRenderer(Activity activity, String _path) {
        super(activity.getApplicationContext());

        videopath = _path;
        vrVideoActivity = (VRVideoActivity) activity;
    }

    @Override
    protected void initScene() {

        mMediaPlayer = MediaPlayer.create(getContext(),
               R.raw.jungle);
//        File file = new File(videopath);
//        Uri uri = Uri.fromFile(file);
//        Log.e("bis", "uri= " + uri.toString());
//        mMediaPlayer = MediaPlayer.create(getContext(),uri);

        mMediaPlayer.setLooping(true);

        mVideoTexture = new StreamingTexture("sintelTrailer", mMediaPlayer);
        Material material = new Material();
        material.setColorInfluence(0);
        try {
            material.addTexture(mVideoTexture);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        Sphere sphere = new Sphere(50, 64, 32);
        sphere.setScaleX(-1);
        sphere.setMaterial(material);

        getCurrentScene().addChild(sphere);

        getCurrentCamera().setPosition(Vector3.ZERO);

        getCurrentCamera().setFieldOfView(75);

        mMediaPlayer.start();

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("bis", "video completed");
                mp.stop();
                vrVideoActivity.finish();
            }
        });
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                Log.e("bis", "video seek completed");
                mp.stop();
                vrVideoActivity.finish();
            }
        });

    }

    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {
        super.onRender(ellapsedRealtime, deltaTime);
        mVideoTexture.update();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMediaPlayer != null)
            mMediaPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMediaPlayer != null)
            mMediaPlayer.start();
    }

    @Override
    public void onRenderSurfaceDestroyed(SurfaceTexture surfaceTexture) {
        super.onRenderSurfaceDestroyed(surfaceTexture);
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }
}