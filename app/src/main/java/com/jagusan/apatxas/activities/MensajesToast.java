package com.jagusan.apatxas.activities;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Jessica on 04/02/2015.
 */
public class MensajesToast {

    public static void mostrarConfirmacionBorrados(Context contexto, int mensajeId, int numElementosBorrados) {
        Toast toast = Toast.makeText(contexto, contexto.getResources().getQuantityString(mensajeId, numElementosBorrados, numElementosBorrados), Toast.LENGTH_SHORT);
        toast.show();
    }
}
