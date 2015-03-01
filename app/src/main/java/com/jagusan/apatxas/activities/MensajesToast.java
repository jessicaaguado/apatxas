package com.jagusan.apatxas.activities;

import android.content.Context;
import android.widget.Toast;


public class MensajesToast {

    public static void mostrarConfirmacionBorrados(Context contexto, int mensajeId, int numElementosBorrados) {
        Toast toast = Toast.makeText(contexto, contexto.getResources().getQuantityString(mensajeId, numElementosBorrados, numElementosBorrados), Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void mostrarConfirmacionAnadidos(Context contexto, int mensajeId, int numElementosAnadidoss) {
        Toast toast = Toast.makeText(contexto, contexto.getResources().getQuantityString(mensajeId, numElementosAnadidoss, numElementosAnadidoss), Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void mostrarConfirmacionGuardado(Context contexto, int mensajeId) {
        Toast toast = Toast.makeText(contexto, contexto.getResources().getString(mensajeId), Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void mostrarMensaje(Context contexto, String mensaje) {
        Toast toast = Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT);
        toast.show();
    }
}
