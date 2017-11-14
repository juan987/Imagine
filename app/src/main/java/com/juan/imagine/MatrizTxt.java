package com.juan.imagine;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan on 13/11/2017.
 */

public class MatrizTxt {
    public String xxx = this.getClass().getSimpleName();
    public Context context;

    public MatrizTxt(Context context) {
        this.context = context;
    }

    public List<String> quitarEspaciosBlancos(List<String> arrayLineasTextoLocal){
        for(int i = 0; i < arrayLineasTextoLocal.size(); i++){
            String lineaSinBlancos = "";
            for(int c = 0; c < arrayLineasTextoLocal.get(i).length(); c++){
                if(arrayLineasTextoLocal.get(i).charAt(c) != ' '){
                    lineaSinBlancos += arrayLineasTextoLocal.get(i).charAt(c);
                }

            }
            arrayLineasTextoLocal.set(i, lineaSinBlancos);
        }//Fin de for(int i = 0; i <= arrayLineasTextoLocal.size(); i++)
        return arrayLineasTextoLocal;
    }

    public ArrayList<MatrizDeDatos> obtenerDatosMatriz(List<String> arrayLineasTextoLocal){
        //Limpiar las lineas de MatrizConfig.txt de espacios en blanco
        arrayLineasTextoLocal = quitarEspaciosBlancos(arrayLineasTextoLocal);
        String[] arrayStringSplitDeComa = null;
        String[] arrayStringSplitDeIgual = null;
        //MatrizDeDatos matrizDeDatos = new MatrizDeDatos();
        ArrayList<MatrizDeDatos> arrayMatrizDeDatos = new ArrayList<>();
        for(int i = 0; i < arrayLineasTextoLocal.size(); i++){
            Log.d(xxx, "arrayLineasTextoLocal: " +i +" , " +arrayLineasTextoLocal.get(i));
            MatrizDeDatos matrizDeDatos = null;
            if(arrayLineasTextoLocal.get(i).toLowerCase().startsWith("x")) {
                arrayStringSplitDeComa = arrayLineasTextoLocal.get(i).split(",");
                //MatrizDeDatos matrizDeDatos = new MatrizDeDatos();
                matrizDeDatos = new MatrizDeDatos();
                for(int c = 0; c < arrayStringSplitDeComa.length; c++){
                    arrayStringSplitDeIgual = arrayStringSplitDeComa[c].split("=");

                    for(int igual = 0; igual < arrayStringSplitDeIgual.length; igual++){
                        Log.d(xxx, "arrayStringSplitDeIgual: " +igual +" , " +arrayStringSplitDeIgual[igual]);

                        if(arrayStringSplitDeIgual[igual].toLowerCase().startsWith("x")){
                            matrizDeDatos.setCoordenadaX((float)Integer.parseInt(arrayStringSplitDeIgual[igual+1]));
                        }
                        if(arrayStringSplitDeIgual[igual].toLowerCase().startsWith("y")){
                            matrizDeDatos.setCoordenadaY((float)Integer.parseInt(arrayStringSplitDeIgual[igual+1]));
                        }
                        if(arrayStringSplitDeIgual[igual].toLowerCase().startsWith("s")){
                            matrizDeDatos.setZoom(Float.parseFloat(arrayStringSplitDeIgual[igual+1]));
                        }
                        if(arrayStringSplitDeIgual[igual].toLowerCase().startsWith("r")){
                            matrizDeDatos.setRotacion(Float.parseFloat(arrayStringSplitDeIgual[igual+1]));
                        }
                        if(arrayStringSplitDeIgual[igual].toLowerCase().startsWith("d")){
                            matrizDeDatos.setGradoDeDifuminado((float)Integer.parseInt(arrayStringSplitDeIgual[igual+1]));
                        }


                    }


                }
                arrayMatrizDeDatos.add(matrizDeDatos);
                Log.d(xxx, "obtenerDatosMatriz: " +matrizDeDatos.toString());
            }else{
                //No se procesa una linea que no empiece con x
            }


            //arrayMatrizDeDatos.add(matrizDeDatos);
            //Log.d(xxx, "obtenerDatosMatriz: " +matrizDeDatos.toString());
        }

        return arrayMatrizDeDatos;
    }
}
