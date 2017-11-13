package com.juan.imagine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

public class ActivityVideo extends AppCompatActivity {
    String xxx = this.getClass().getSimpleName();
    Bitmap imagenSuperponerBitMap;
    ProgressBar progressBar;
    EditText numDeImagenes;
    int intNumeroImagenes;


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
             //crearVideo();

            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        numDeImagenes   = (EditText)findViewById(R.id.numImagenes);


        Button button = (Button) findViewById(R.id.button_id_1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ((numDeImagenes.length() == 0)) {
                    Snackbar.make(findViewById(R.id.coord), "Enter number of images", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {

                    intNumeroImagenes = Integer.parseInt(numDeImagenes.getText().toString());
                    Log.d(xxx, "onCreate, intNumeroImagenes: " +intNumeroImagenes);

                    progressBar.setVisibility(View.VISIBLE);
                    //Lanzamos el asynctask de componer imagen
                    new ComponerVideoAsyncTask().execute("string1", "string2", "string3");
                }
            }
        });



    }





    //Version original de prueba con el panda, ya no la uso mas
    public void crearVideo() {
        //Como en:
        //https://medium.com/@giuder91/how-to-use-javacv-on-android-studio-1e681990e4e9
        //https://github.com/gderaco/JavaCVTest/blob/master/app/src/main/java/co/gdera/javacvtest/MainActivity.java

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

    }//FIN de crearVideo


    //Como en:
    //https://cooltrickshome.blogspot.com.es/2016/12/images-to-movie-converter-using-java.html

    //Para usar este metodo, le paso el vidPath, donde quiero guardar el video
    //y le paso el array the links donde estan las imagenes a usar para crear el video.
    //Seria este codigo:
    /*
    ArrayList<String> links = new ArrayList<>();
    File f=new File(imgPath);
    File[] f2=f.listFiles();
                 for(File f3:f2)
    {
        links.add(f3.getAbsolutePath());
    }
    convertJPGtoMovie(links, vidPath);
    */


    //convertJPGtoMovie method:
    public static void convertJPGtoMovie(ArrayList<String> links, String vidPath)
    {
        OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(vidPath,640,720);
        try {
            recorder.setFrameRate(1);
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
            recorder.setVideoBitrate(9000);
            recorder.setFormat("mp4");
            recorder.setVideoQuality(0); // maximum quality
            recorder.start();
            for (int i=0;i<links.size();i++)
            {
                recorder.record(grabberConverter.convert(cvLoadImage(links.get(i))));
            }
            recorder.stop();
        }
        catch (org.bytedeco.javacv.FrameRecorder.Exception e){
            e.printStackTrace();
        }
    }

    //******************************************************************************************************
    //******************************************************************************************************

    long tiempoGeneracionVideo = 0;
    long tiempoConcatenacionVideo = 0;



    //Todos los metodos para generar la imagen
    String vidPathConcatenado;
    String vidPath;
    private void metodoPrincipal(){
        //Recuperar el fichero con la matriz
        //Obtener todas las lineas del fichero CONFIG.txt en el dir del dispositivo: pathCesaralMagicImageC
        LeerFicheroTxt leerFicheroTxt = new LeerFicheroTxt(ActivityVideo.this);
        //arrayLineasTexto contiene todas las lineas de CONFIG.txt
        List<String> arrayLineasTexto = leerFicheroTxt.getFileContentsLineByLineMethod("/HacerCosas/"  +"MatrizConfig.txt");
        //Imprimimos MatrizConfig
        for(int i = 0; i < arrayLineasTexto.size(); i++){
            Log.d(xxx, "metodoPrincipal arrayLineasTexto: " +arrayLineasTexto.get(i));
        }



        //Crear los datos necesarios de la matriz, solo hay una matriz
        MatrizDeDatos matrizDeDatos = new MatrizDeDatos();
        matrizDeDatos.setCoordenadaX(300f);
        matrizDeDatos.setCoordenadaY(1300f);
        ArrayList<MatrizDeDatos> arrayMatrizDeDatos = new ArrayList<>();
        arrayMatrizDeDatos.add(matrizDeDatos);
        //Crear el array de links, solo hay una imagen
        String imagenGrande = Environment.getExternalStorageDirectory() + "/HacerCosas/"  +"lotto2_00493.jpg";
        //Obtener el tamaño de la primera imagen grande para pasarlo a crearVideoPrototipo
        Bitmap bitmap1 = getBitmapFromStore(imagenGrande);
        int imageWidth = bitmap1.getWidth();
        int imageHeight = bitmap1.getHeight();
        Log.d(xxx, "metodoPrincipal ancho imagenes imageWidth: " +imageWidth);
        Log.d(xxx, "metodoPrincipal alto imagenes imageHeight: " +imageHeight);

        bitmap1.recycle();
        //FIN Obtener el tamaño de la primera imagen grande

        ArrayList<String> arrayImagenesGrandesLinks = new ArrayList<>();
        arrayImagenesGrandesLinks.add(imagenGrande);
        //Path y nombre del video a generar, lo hacemos con la hora
        Date date = new Date();
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd_MM_HHmmss");
         vidPath = Environment.getExternalStorageDirectory() + "/HacerCosas/" +sdf2.format(date) +".mp4";
        Log.d(xxx, "metodoPrincipal el noimbre del video es: " +vidPath);
        //recuperar y hacer transparente la imagen pequeña
        String pathSmallImage = Environment.getExternalStorageDirectory() + "/HacerCosas/"  +"datos.bmp";
        Bitmap bitmapSmallImageTransparente = getBitmapFromStore(pathSmallImage);
        bitmapSmallImageTransparente = changeSomePixelsToTransparent(bitmapSmallImageTransparente);
        //Inicio de medicion de tiempo de execucion
        long startTime = System.currentTimeMillis();
        //Generar el video
        crearVideoPrototipo(arrayImagenesGrandesLinks, vidPath,
                          bitmapSmallImageTransparente, arrayMatrizDeDatos, imageWidth, imageHeight);
        Log.d(xxx, "metodoPrincipal, video generado");
        //Calculo tiempo de ejecucion
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        tiempoGeneracionVideo = elapsedTime;
        Log.d(xxx, "metodoPrincipal, tiempo de ejecucion crearVideoPrototipo: " +elapsedTime);
        //Path y nombre del video 1 para el merge
        String vidPath_1 = Environment.getExternalStorageDirectory() + "/HacerCosas/" +"prueba.mp4";
        //Path y nombre del video concatenado
        date = new Date();
        vidPathConcatenado = Environment.getExternalStorageDirectory() + "/HacerCosas/"
                            +"VideoConcatenado_" +sdf2.format(date) +".mp4";

        //Pasamos los path y strings vidPath_1, que se concatena con vidPath y el resultado es vidPathConcatenado
         startTime = System.currentTimeMillis();
        merge( vidPath_1,  vidPath,  vidPathConcatenado);
         stopTime = System.currentTimeMillis();
         elapsedTime = stopTime - startTime;
         tiempoConcatenacionVideo = elapsedTime;
        Log.d(xxx, "metodoPrincipal, tiempo de ejecucion merge: " +elapsedTime);
    }//Fin de metooo principal


    //Como en:
    //https://stackoverflow.com/questions/18437085/how-to-merge-more-than-two-videos-using-framerecorder-javacv
    void merge(String vidPath_1, String vidPath, String vidPathConcatenado) {
        try {
            FFmpegFrameGrabber grabber1 = new FFmpegFrameGrabber(vidPath_1);
            grabber1.start();

            FrameGrabber grabber2 = new FFmpegFrameGrabber(vidPath);
            grabber2.start();

            FrameRecorder recorder2 = new FFmpegFrameRecorder(vidPathConcatenado, grabber1.getImageWidth(),
                    grabber1.getImageHeight(), grabber1.getAudioChannels());
            recorder2.setFrameRate(grabber1.getFrameRate());
            recorder2.setSampleFormat(grabber1.getSampleFormat());
            recorder2.setSampleRate(grabber1.getSampleRate());
            recorder2.start();

            Frame frame;
            int j = 0;
            while ((frame = grabber1.grabFrame()) != null) {
                j++;
                recorder2.record(frame);
            }
            while ((frame = grabber2.grabFrame()) != null) {
                recorder2.record(frame);
            }

            recorder2.stop();
            grabber2.stop();
            grabber1.stop();


        }catch (FrameGrabber.Exception e) {
            Log.e(xxx, "merge:  "  +e.getMessage());
        }catch (FrameRecorder.Exception e) {
            Log.e(xxx, "merge:  "  +e.getMessage());
        }

    }

    private Bitmap changeSomePixelsToTransparent(Bitmap originalImage){
        Bitmap bitmap2 = originalImage.copy(Bitmap.Config.ARGB_8888,true);
        bitmap2.setHasAlpha(true);
        for(int x=0;x<bitmap2.getWidth();x++){
            for(int y=0;y<bitmap2.getHeight();y++){
                //Solo busca pixeles blancos
                if(bitmap2.getPixel(x, y)== Color.rgb(0xff, 0xff, 0xff))
                {
                    int alpha = 0x00;
                    bitmap2.setPixel(x, y , Color.argb(alpha,0xff,0xff,0xff));  // changing the transparency of pixel(x,y)
                }
            }
        }
        return bitmap2;
    }



    //10 nov 2017, metodo de juan para crear un video
    //Notas: en este metodo prototipo,
    //links solo contiene el path de la imagen grande: solo hay una que se usa en el loop, es el elemento 0
    //el bitmap de la imagen pequeña ya se envia transparente
    //el array de matriz de datos solo tiene el elemento cero que se usa en el loop

    //public static void crearVideoPrototipo(ArrayList<String> links, String vidPath,
    public void crearVideoPrototipo(ArrayList<String> links, String vidPath,
                                                Bitmap bitmapSmallImageTransparente,
                                                ArrayList<MatrizDeDatos> matrizDeDatos,
                                                int imageWidth, int imageHeight)
    {
        OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
        //FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(vidPath,640,720);
        //FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(vidPath,1080,1920);
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(vidPath,imageWidth,imageHeight);
        //path de la imagen compuesta para cada frame:
        String imagenParaElVideo = Environment.getExternalStorageDirectory() + "/HacerCosas/"  +"imagenVideo.jpg";
        try {
            recorder.setFrameRate(28);
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
            recorder.setVideoBitrate(9000);
            recorder.setFormat("mp4");
            recorder.setVideoQuality(0); // maximum quality
            recorder.start();
            //Asi sera en la version final
            //for (int i=0;i<links.size();i++)
            //Ahora ponemos un nemero fijo
            //for (int i=0; i < 100;i++)
            //Ponemos el numero de imagenes del edittext
            Bitmap imagenFinalBitmap;
            Bitmap bitmap = null;//Bitmap para la imagen grande
            Bitmap bitmapSmallImageEscalada = null;//Bitmap para bitmapSmallImageTransparente escalada
            //Prueba con AndroidFrameConverter converterToBitmap = new AndroidFrameConverter();
            AndroidFrameConverter converterToBitmap = new AndroidFrameConverter();
            ModificarImagenes modificarImagenes = new ModificarImagenes(ActivityVideo.this);
            for (int i=0; i < intNumeroImagenes;i++)
            {
                //TODO: en cada ciclo del loop,
                //hay que:
                //hacer zoom, rotar o difuminar la imagen transparente
                //escalar la imagen transparente
                bitmapSmallImageEscalada = modificarImagenes.escalarImagen(bitmapSmallImageTransparente, 1.0f);
                //Rotar la imagen
                bitmapSmallImageEscalada = modificarImagenes.Rotate(bitmapSmallImageEscalada, 30.0f);


                //Posicionar la imagen pequeña sobre la grande
                //Le paso un solo objeto bitmap para la imagen grande
                //imagenFinalBitmap = overlapImages2(matrizDeDatos.get(0), bitmapSmallImageTransparente, links.get(0), bitmap);
                imagenFinalBitmap = overlapImages2(matrizDeDatos.get(0), bitmapSmallImageEscalada, links.get(0), bitmap);

                //Asi lo hacia originalmente, tardaba mucho, por que guardaba y volvia a recuperar
                //la imagen de la memoria externa
                //Guardar la imagen final
                //guardarImagenMethod(imagenParaElVideo, imagenFinalBitmap);
                //Original con el convertidor de ipl a frame
                //recorder.record(grabberConverter.convert(cvLoadImage(imagenParaElVideo)));


                //Prueba con AndroidFrameConverter converterToBitmap = new AndroidFrameConverter();
                recorder.record(converterToBitmap.convert(imagenFinalBitmap));
                //Prueba con AndroidFrameConverter converterToBitmap = new AndroidFrameConverter();


                //prueba con recycle
                bitmapSmallImageEscalada.recycle();
                imagenFinalBitmap.recycle();


                Log.d(xxx, "crearVideoPrototipo, Imagen grabada: "  +i);

            }
            recorder.stop();
        }
        catch (org.bytedeco.javacv.FrameRecorder.Exception e){
            Log.e(xxx, "crearVideoPrototipo:  "  +e.getMessage());

        }
    }

    private Bitmap overlapImages2(MatrizDeDatos matrizDeDatos, Bitmap bitmapSmallImageTransparente, String pathImagenGrande,
                                                Bitmap bitmap){
        //recuperamos la imagen grande como bitmap
        bitmap  = getBitmapFromStore(pathImagenGrande);
        //Construimos el bitmap final que sera un frame del video
        bitmap = createSingleImageFromMultipleImagesWithCoord(bitmap, bitmapSmallImageTransparente,
                                    matrizDeDatos.getCoordenadaX(), matrizDeDatos.getCoordenadaY());
        return bitmap;
    }



    //Metodo final para la mezcla
    private Bitmap createSingleImageFromMultipleImagesWithCoord(Bitmap firstImage, Bitmap secondImage, float x, float y ){

        Bitmap result = Bitmap.createBitmap(firstImage.getWidth(), firstImage.getHeight(), firstImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, x, y, null);

        return result;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    public Bitmap getBitmapFromStore(String pathToImage){
        Bitmap bitmap = null;
        if(isExternalStorageWritable()) {
            bitmap = BitmapFactory.decodeFile(pathToImage);
            if(bitmap != null){

                Log.d(xxx, "getBitmapFromStore, Imagen cargada " +pathToImage);
            }else{
                Log.d(xxx, "getBitmapFromStore, ERROR: Imagen NO cargada" );
            }

        }else{
            Log.d(xxx, "getBitmapFromStore, El external public storage no esta montado ");
        }
        return bitmap;
    }

    public boolean guardarImagenMethod(String path, Bitmap bitmap){
        //NOTA 1: environmentDir es string del tipo Environment.DIRECTORY_PICTURES o Environment.DIRECTORY_DCIM
        //Nota 2: subDir es un string con barras: /predict/
        //NOTA 3: imageName es el nombre de la imagen a guardar como predict.jpg
        if(isExternalStorageWritable()) {
            if(saveImageToExternalPublicStorage(path, bitmap)){

                Log.d(xxx, "guardarImagenMethod, Imagen guardada: "  +path);
            }else{

                Log.d(xxx, "guardarImagenMethod, ERROR: Imagen NO guardada" );
                return false;
            }

        }else{
            Log.d(xxx, "guardarImagenMethod, El external public storage no esta montado ");
            return false;

        }
        //Si llega aqui, la imagen se ha guardado correctamente
        return true;
    }

    public boolean saveImageToExternalPublicStorage(String path, Bitmap bitmap) {

        try {
            OutputStream fOut = null;
            //Siempre sobreescribe el fichero si ya existe
            File file = new File(path);
            //file.createNewFile();
            fOut = new FileOutputStream(file);
            //Log.d(xxx, "En saveImageToExternalPublicStorage, quality para comprimir, intq siempre 100 ");

            //de la clase bitmap, metodo compress: quality	int: Hint to the compressor, 0-100. 0 meaning compress for small size,
            // 100 meaning compress for max quality.
            // Some formats, like PNG which is lossless, will ignore the quality setting
            // 100 means no compression, the lower you go, the stronger the compression
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            //uso quality del CONFIX.txt
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            //Esta linea inserta dos imagenes bajo el dir images: una  con toda la resolucion y un thumbnail.
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

            return true;

        } catch (Exception e) {
            Log.e(xxx, "saveImageToExternalPublicStorage:  "  +e.getMessage());
            //Toast.makeText(context,
            //e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    //Asynctask para componer la imagen
    private class ComponerVideoAsyncTask extends AsyncTask<String, Integer, Boolean> {
        public ComponerVideoAsyncTask() {
            super();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Log.d(xxx, "estoy en doInBackground de ComponerVideoAsyncTask");

            metodoPrincipal();

            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(Boolean boolResultado) {
            progressBar.setVisibility(View.INVISIBLE);

            Log.d(xxx, "onPostExecute de ComponerVideoAsyncTask, el resultado de doInBackground es: " +boolResultado);
            TextView tiempoGeneracionVideoText = (TextView)findViewById(R.id.textview_2);
            tiempoGeneracionVideoText.setText(Long.toString(tiempoGeneracionVideo));

            TextView tiempoConcatenacionVideoText = (TextView)findViewById(R.id.textview_4);
            tiempoConcatenacionVideoText.setText(Long.toString(tiempoConcatenacionVideo));

            TextView textviewNuevoVideo = (TextView)findViewById(R.id.textview_5);
            textviewNuevoVideo.setText(vidPath);

            TextView textviewNuevoVideo6 = (TextView)findViewById(R.id.textview_6);
            textviewNuevoVideo6.setText(vidPathConcatenado);





        }

        protected void onProgressUpdate(Integer[] values) {
        }

        protected void onCancelled() {
        }
    }//FIN de la clase ComponerImagenAsyncTask
}
