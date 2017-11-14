package com.juan.imagine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

/**
 * Created by Juan on 13/11/2017.
 */

public class ModificarImagenes {
    public String xxx = this.getClass().getSimpleName();
    public Context context;


    public ModificarImagenes(Context context) {
        this.context = context;
        Log.d(xxx, "nueva instancia de ModificarImagenes ");

    }


    //public static Bitmap escalarImagen(Bitmap bitmap, float escala) {
    public Bitmap escalarImagen(Bitmap bitmap, float escala) {
        int wantedWidth = (int)(bitmap.getWidth() * escala);
        int wantedHeight = (int)(bitmap.getHeight() * escala);
        Log.d(xxx, "escalarImagen, nuevo ancho nueva altura: " +wantedWidth );
        Log.d(xxx, "escalarImagen, nueva altura: " +wantedHeight);

        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }

    //como en
    //https://stackoverflow.com/questions/8712652/rotating-image-on-a-canvas-in-android
    public Bitmap Rotate(Bitmap source, float angle)
    {
        //Bitmap bitmap = source;
        Bitmap bitmap = Bitmap.createBitmap(source.getWidth(),source.getHeight(), source.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Rect rect = new Rect(0,0,bitmap.getWidth(), bitmap.getHeight());
        Matrix matrix = new Matrix();
        float px = rect.exactCenterX();
        float py = rect.exactCenterY();
        matrix.postTranslate(-source.getWidth()/2, -source.getHeight()/2);
        matrix.postRotate(angle);
        matrix.postTranslate(px, py);
        //8 nov 2017, usamos anti aliasing
        //canvas.drawBitmap(source, matrix, null);
        canvas.drawBitmap(source, matrix, new Paint( Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        matrix.reset();

        return bitmap;
    }

    public Bitmap Rotate2(Bitmap source, float angle)
    {
        //Bitmap bitmap = source;
        Bitmap bitmap = Bitmap.createBitmap(source.getWidth(),source.getHeight(), source.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Rect rect = new Rect(0,0,bitmap.getWidth(), bitmap.getHeight());
        Matrix matrix = new Matrix();
        float px = rect.exactCenterX();
        float py = rect.exactCenterY();
        //matrix.postTranslate(-source.getWidth()/2, -source.getHeight()/2);
        matrix.postRotate(angle);
        //matrix.postTranslate(px, py);
        //8 nov 2017, usamos anti aliasing
        //canvas.drawBitmap(source, matrix, null);
        canvas.drawBitmap(source, matrix, new Paint( Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        matrix.reset();

        return bitmap;
    }



    //Hacer blur a un bitmap
    //Como en:
    //https://www.quora.com/How-do-I-add-a-blur-effect-to-an-image-in-Android-development
    private static final float BITMAP_SCALE = 0.6f;
    private static final float BLUR_RADIUS = 15f;//Entre 1 y 25//

    public static Bitmap blur(Context context, Bitmap image, float radius) {
        //Con este metodo no me uestra la imagen, hace algo raro con el scale bitmap. uso blur", sin escalar

        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);

        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        //intrinsicBlur.setRadius(BLUR_RADIUS);
        intrinsicBlur.setRadius(radius);
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    //Sin escalar la imagen
    //Como en:
    //http://stacktips.com/tutorials/android/how-to-create-bitmap-blur-effect-in-android-using-renderscript
    public static Bitmap blur2(Context context, Bitmap image, float radius) {
        //radius es un valor float entre Entre 1 y 25//
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        //Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(image);

        RenderScript rs = RenderScript.create(context);

        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, image);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        //intrinsicBlur.setRadius(BLUR_RADIUS);
        intrinsicBlur.setRadius(radius);
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        //Reciclamos
        tmpIn.destroy();
        tmpOut.destroy();
        intrinsicBlur.destroy();

        return outputBitmap;
    }

}
