package com.juan.imagine;

import android.content.Context;
import android.graphics.Bitmap;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import java.nio.Buffer;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

/**
 * Created by Juan on 10/11/2017.
 */


//Por ahora no la uso
public class ClassJavaCv {
    Context context;

    public ClassJavaCv(Context context) {
        this.context = context;
    }

    public Frame obtenerFrameFromIplImage(String pathToImage) {
        opencv_core.IplImage img = cvLoadImage(pathToImage);
        OpenCVFrameConverter.ToMat grabberConverter = new OpenCVFrameConverter.ToMat();
        Frame frame  = grabberConverter.convert(img);
        return frame;
    }

    public opencv_core.IplImage obtenerIplImage(String pathToImage) {
        opencv_core.IplImage img = cvLoadImage(pathToImage);
        return img;
    }

    public void convertirIplToBitmap(opencv_core.IplImage imagenIpl){
        Bitmap bitmap = Bitmap.createBitmap(imagenIpl.width(), imagenIpl.height(), Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(imagenIpl.getByteBuffer());//Esta deprecado

    }

    public void convertirBitmapToIpl(Bitmap imagenBitMap){
        opencv_core.IplImage image = opencv_core.IplImage.create(imagenBitMap.getWidth(), imagenBitMap.getHeight(), IPL_DEPTH_8U, 4);
    }

}
