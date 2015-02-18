package com.jagusan.apatxas.utils;

import android.content.res.Resources;
import android.widget.EditText;

import com.jagusan.apatxas.R;

import java.util.regex.Pattern;

public class ValidacionActivity {

	private static final String EMPTY = "";

	public static final Integer MENS_OBLIGATORIO = R.string.validacion_mensaje_obligatorio;


	public static final String REGEX_DEFAULT = ".*";

	public static boolean validarTituloObligatorio(EditText editText, Resources resources) {
		return validarCampoObligatorio(editText, resources);
	}



	public static Boolean validarCantidadObligatoria(EditText editText, Resources resources) {
		return validarCampoObligatorio(editText, resources);
	}

	private static Boolean validarCampoObligatorio(EditText editText, Resources resources) {
		String mensaje = resources.getString(MENS_OBLIGATORIO);
		return esValido(editText, mensaje, true, REGEX_DEFAULT);
	}

	private static boolean esValido(EditText editText, String mensaje, boolean obligatorio, String regex) {
		Boolean valido = true;
		String text = editText.getText().toString().trim();

        if (obligatorio && EMPTY.equals(text)) {
			editText.setError(mensaje);
			valido = false;
		}
		if (obligatorio && !Pattern.matches(regex, text)) {
			editText.setError(mensaje);
			valido = false;
		}

		return valido;
	}

}
