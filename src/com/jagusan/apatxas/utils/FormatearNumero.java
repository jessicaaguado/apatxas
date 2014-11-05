package com.jagusan.apatxas.utils;

import android.content.res.Resources;

import com.jagusan.apatxas.R;

public class FormatearNumero {
	
	public static String aDinero(Resources res, Double cantidad) {
		return res.getString(R.string.cantidad_dinero, cantidad);
	}

	public static String aDineroEuros(Resources res, Double cantidad) {
		return res.getString(R.string.cantidad_dinero_euros, cantidad);
	}
}
