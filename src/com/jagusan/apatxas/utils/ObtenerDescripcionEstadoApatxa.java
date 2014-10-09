package com.jagusan.apatxas.utils;

import android.content.res.Resources;

import com.jagusan.apatxas.R;

public class ObtenerDescripcionEstadoApatxa {

	public static String getDescripcionParaListado(Resources res, Double gastoTotal, Double gastoPagado) {
		Double estadoGasto = gastoTotal - gastoPagado;
		String descripcionEstadoGasto = res.getString(R.string.estado_gasto_pagado);
		if (estadoGasto < 0) {
			descripcionEstadoGasto = String.format(res.getString(R.string.estado_gasto_sobra), estadoGasto * -1);
		} else if (estadoGasto > 0) {
			descripcionEstadoGasto = String.format(res.getString(R.string.estado_gasto_falta_pagar), estadoGasto);
		}
		return descripcionEstadoGasto;
	}
	
	public static String getDescripcionParaDetalle(Resources res, Double gastoTotal, Double gastoPagado) {
		Double estadoGasto = gastoTotal - gastoPagado;
		String descripcionEstadoGasto = res.getString(R.string.estado_gasto_pagado);
		if (estadoGasto > 0) {
			descripcionEstadoGasto = res.getString(R.string.estado_gasto_pendiente);
		}
		return descripcionEstadoGasto;
	}

}
