package com.juan.imagine;

import android.content.Context;

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

    public List<PojoCoordenadas> getCoordenadasN(List<String> arrayLineasTextoLocal){

        return null;
    }
}
