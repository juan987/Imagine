package com.juan.imagine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import java.io.File;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

public class ActivityVideo extends AppCompatActivity {
    String xxx = this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Generating video", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                crearVideo();

            }
        });
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    public void crearVideo() {
        //Como en:
        //https://medium.com/@giuder91/how-to-use-javacv-on-android-studio-1e681990e4e9
        //https://github.com/gderaco/JavaCVTest/blob/master/app/src/main/java/co/gdera/javacvtest/MainActivity.java

        //**************************
        /*
        File DCIMDir = Environment.getExternalStorageDirectory();
        String directorio = DCIMDir.getAbsolutePath();
        directorio += pathToImage;
        Log.d(xxx, "El directorio es: " + directorio);
        //Toast.makeText(context,
        //directorio, Toast.LENGTH_SHORT).show();
        bitmap = BitmapFactory.decodeFile(directorio);

        */
        //**********************
        Log.d(xxx, "isExternalStorageWritable(): " +isExternalStorageWritable());



        File myDirectory = new File(Environment.getExternalStorageDirectory() + "/HacerCosas/");
        Log.d(xxx, "crearVideo, myDirectory.exists(): " +myDirectory.exists());

        if (!myDirectory.exists()) {
            myDirectory.mkdirs();

        }

        Log.d(xxx, "crearVideo, myDirectory.exists(): " +myDirectory.exists());


        File Dir = Environment.getExternalStorageDirectory();
        String directorio = Dir.getAbsolutePath();
        //directorio += "/HacerCosas/";
        directorio += "/CesaralMagic/ImageC/";
        directorio += "origin1.jpg";



        //File myDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.) + "/HacerCosas/");


        Log.d(xxx, "crearVideo, myDirectory: " +myDirectory.getAbsolutePath());
        Log.d(xxx, "crearVideo, fichero: " +myDirectory.getAbsolutePath() + "origin1.jpg");
        Log.d(xxx, "crearVideo, directorio: " +directorio);


        //Bitmap bitmap = BitmapFactory.decodeFile(myDirectory.getAbsolutePath() + "origin1.jpg");
        //Bitmap bitmap = BitmapFactory.decodeFile(directorio + "origin1.jpg");
        Bitmap bitmap = BitmapFactory.decodeFile(directorio);
        if(bitmap != null){
            //Toast.makeText(context,
            //"Imagen cargada", Toast.LENGTH_SHORT).show();
            Log.d(xxx, "Imagen cargada");
        }else{
            //Toast.makeText(context,
            //"ERROR Imagen NO cargada", Toast.LENGTH_SHORT).show();
            Log.d(xxx, "ERROR: Imagen NO cargada" );
        }


        opencv_core.IplImage img = cvLoadImage(myDirectory.getAbsolutePath() + "/origin1.jpg");

        if(img == null){
            Log.d(xxx, "crearVideo, img es null");

        }

        //OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
        OpenCVFrameConverter.ToMat grabberConverter = new OpenCVFrameConverter.ToMat();

        Frame frame  = grabberConverter.convert(img);
        //Frame frame = OpenCVFrameConverter<I>.convert(img);



        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(myDirectory.getAbsolutePath() + "/prueba2.mp4", 200, 150);
        if(recorder == null){
            Log.d(xxx, "crearVideo, recorder es null");

        }

        try {
            recorder.setFrameRate(30);
            recorder.start();

            for (int i = 0; i < 100; i++) {
                recorder.record(frame);
                //recorder.record(grabberConverter.convert(img));
            }
            recorder.stop();
        } catch (Exception e) {
            //e.printStackTrace();
            Log.d(xxx, "En try, recorder.start, error: " +e.getMessage());


        }

    }


}
