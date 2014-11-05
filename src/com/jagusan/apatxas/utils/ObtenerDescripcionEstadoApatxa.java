package com.jagusan.apatxas.utils;

import android.content.res.Resources;
import android.util.Log;

import com.jagusan.apatxas.R;

public class ObtenerDescripcionEstadoApatxa {

	public static String getDescripcionParaListado(Resources res, Double gastoTotal, Double gastoPagado, Double boteInicial) {
		Double estadoGasto = calcularEstadoGasto(gastoTotal, gastoPagado, boteInicial);
		Log.d("APATXAS", " estado " + estadoGasto);
		String descripcionEstadoGasto = res.getString(R.string.estado_gasto_pagado);
		if (estadoGasto < 0) {
			descripcionEstadoGasto = String.format(res.getString(R.string.estado_gasto_sobra), estadoGasto * -1);
		} else if (estadoGasto > 0) {
			descripcionEstadoGasto = String.format(res.getString(R.string.estado_gasto_falta_pagar), estadoGasto);
		}
		return descripcionEstadoGasto;
	}

	public static String getDescripcionParaDetalle(Resources res, Double gastoTotal, Double gastoPagado, Double boteInicial) {
		Double estadoGasto = calcularEstadoGasto(gastoTotal, gastoPagado, boteInicial);
		String descripcionEstadoGasto = res.getString(R.string.estado_gasto_pagado);
		if (estadoGasto > 0) {
			descripcionEstadoGasto = res.getString(R.string.estado_gasto_pendiente);
		} else if (estadoGasto < 0) {
			descripcionEstadoGasto = String.format(res.getString(R.string.estado_gasto_pagado_bote), estadoGasto * -1);
		}
		return descripcionEstadoGasto;
	}

	private static double calcularEstadoGasto(Double gastoTotal, Double gastoPagado, Double boteInicial) {
		return gastoTotal - gastoPagado - boteInicial;
	}
}
