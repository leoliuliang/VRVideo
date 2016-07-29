/**
 * All you need to know about this class is that, when you give it the projectPos, pos and the context,
 * it will render that particular jpg in 3D split view for viewing.
 * <p/>
 * This class is part of the Rajawali Library, with small additions to it as per our need.
 */
package app.opengl.com.ponoramaapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import org.r3d.cardboard.RCardboardRenderer;
import org.r3d.materials.Material;
import org.r3d.materials.textures.ATexture;
import org.r3d.materials.textures.Texture;
import org.r3d.math.vector.Vector3;
import org.r3d.primitives.Sphere;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ImageRenderer extends RCardboardRenderer {


    public Context contextWrapper;


    public ImageRenderer(Context c) {
        super(c);
        contextWrapper = c;

    }

    private static Sphere createPhotoSphereWithTexture(ATexture texture) {

        Material material = new Material();
        material.setColor(0);

        try {
            material.addTexture(texture);
        } catch (ATexture.TextureException e) {
            throw new RuntimeException(e);
        }

        Sphere sphere = new Sphere(50, 64, 32);
        sphere.setScaleX(-1);
        sphere.setMaterial(material);

        return sphere;
    }

    @Override
    protected void initScene() {
//        InputStream fis = null;
//        try {
//            FileInputStream fiss = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + "ss.jpg");
//
//            fis = new BufferedInputStream(fiss);
//
//        } catch (Exception e) {
//
//            Log.d("bis", "my render image e=" + e.getLocalizedMessage());
//        }


        InputStream inputStream =  contextWrapper.getResources().openRawResource(R.raw.ee);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(inputStream, options);
        BitmapFactory.decodeStream(inputStream,null,options);
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inSampleSize = 1;
        int w = 2560;
        int h = 1920;
        h = w*height/width;//计算出宽高等比率
        int a = options.outWidth/ w;
        int b = options.outHeight / h;
        options.inSampleSize = Math.max(a, b);
        options.inJustDecodeBounds = false;
        Bitmap bitmap =   BitmapFactory.decodeStream(inputStream, null, options);


//        Bitmap bitmap = BitmapFactory.decodeResource(contextWrapper.getResources(),R.raw.ss);
        // Bitmap bitmap = BitmapFactory.decodeResource(contextWrapper.getResources(), R.raw.ss);
        Log.d("bis", "my render image bitmap" + bitmap.toString());
        Log.e("--------------===1==", bitmap.getByteCount() + "");
//        bitmap = BitmapCpmpree.getInstan().comp(bitmap);
        Log.e("--------------===2==",bitmap.getByteCount()+"");
        Sphere sphere = createPhotoSphereWithTexture(new Texture("photo", bitmap));

        ///////////////////////////////////////////////////////////////////////////

        getCurrentScene().addChild(sphere);
        getCurrentCamera().setPosition(Vector3.ZERO);
        getCurrentCamera().setFieldOfView(75);
    }
}