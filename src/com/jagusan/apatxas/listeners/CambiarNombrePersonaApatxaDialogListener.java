package com.jagusan.apatxas.listeners;

import android.support.v4.app.DialogFragment;

public interface CambiarNombrePersonaApatxaDialogListener {

	public void onClickListoCambiarNombrePersona(int posicionPersonaNombreCambiar, String nuevoNombrePersona);

	public void onClickCancelarCambiarNombrePersona(DialogFragment dialog);
	
}
