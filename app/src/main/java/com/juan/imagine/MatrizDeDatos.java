package com.juan.imagine;

/**
 * Created by Juan on 10/11/2017.
 */

/*
El fichero con la matriz de transformación podría tener líneas con este formato

        X=xxx,Y=yyy,R=rrr,D=ddd

        Donde:
        X = translación X en pixels
        Y = translación Y en pixels
        R = rotación en grados (numero con signo y posibles decimales)
        D= Grado de difuminado. Hay varios tipos pero empezaríamos con el más sencillo
*/

public class MatrizDeDatos {
    public MatrizDeDatos() {
    }

    float coordenadaX = 0;
    float coordenadaY = 0;
    float rotacion = 0.0f;
    float zoom = 1.0f;
    float gradoDeDifuminado = 0;

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getCoordenadaX() {
        return coordenadaX;
    }

    public void setCoordenadaX(float coordenadaX) {
        this.coordenadaX = coordenadaX;
    }

    public float getCoordenadaY() {
        return coordenadaY;
    }

    public void setCoordenadaY(float coordenadaY) {
        this.coordenadaY = coordenadaY;
    }

    public float getRotacion() {
        return rotacion;
    }

    public void setRotacion(float rotacion) {
        this.rotacion = rotacion;
    }

    public float getGradoDeDifuminado() {
        return gradoDeDifuminado;
    }

    public void setGradoDeDifuminado(float gradoDeDifuminado) {
        this.gradoDeDifuminado = gradoDeDifuminado;
    }

    @Override
    public String toString() {
        return "MatrizDeDatos{" +
                "coordenadaX=" + coordenadaX +
                ", coordenadaY=" + coordenadaY +
                ", rotacion=" + rotacion +
                ", zoom=" + zoom +
                ", gradoDeDifuminado=" + gradoDeDifuminado +
                '}';
    }
}
