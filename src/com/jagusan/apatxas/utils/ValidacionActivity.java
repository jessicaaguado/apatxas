package com.jagusan.apatxas.utils;

import java.util.regex.Pattern;

import android.content.res.Resources;
import android.widget.EditText;

import com.jagusan.apatxas.R;

public class ValidacionActivity {

	private static final String EMPTY = "";

	public static final Integer MENS_OBLIGATORIO = R.string.validacion_mensaje_obligatorio;
	public static final Integer MENS_FORMATO_FECHA_INCORRECTA = R.string.validacion_mensaje_formato_fecha_incorrecto;

	public static final String REGEX_DEFAULT = ".*";

	public static boolean validarNombre() {
		return false;
	}

	public static boolean validarTituloObligatorio(EditText editText, Resources resources) {
		String mensaje = resources.getString(MENS_OBLIGATORIO);
		return esValido(editText, mensaje, true, REGEX_DEFAULT);
	}

	public static boolean validarConceptoObligatorio(EditText editText, Resources resources) {
		String mensaje = resources.getString(MENS_OBLIGATORIO);
		return esValido(editText, mensaje, true, REGEX_DEFAULT);
	}

	public static boolean validarFechaObligatoria(EditText editText, Resources resources) {
		String mensaje = resources.getString(MENS_OBLIGATORIO);
		return esValido(editText, mensaje, true, REGEX_DEFAULT);
	}

	private static boolean esValido(EditText editText, String mensaje, boolean obligatorio, String regex) {
		Boolean valido = true;
		String text = editText.getText().toString().trim();

		// editText.setError(null);

		if (obligatorio && EMPTY.equals(text)) {
			editText.setError(mensaje);
			valido = false;
		}
		// pattern doesn't match so returning false
		if (obligatorio && !Pattern.matches(regex, text)) {			
			editText.setError(mensaje);
			valido = false;
		}

		return valido;
	}

}
