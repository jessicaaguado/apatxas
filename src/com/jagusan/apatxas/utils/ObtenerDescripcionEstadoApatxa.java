package com.jagusan.apatxas.utils;

import android.content.res.Resources;

import com.jagusan.apatxas.R;

public class ObtenerDescripcionEstadoApatxa {

	public static String getDescripcionParaDetalle(Resources res, EstadoApatxa estadoApatxa, Integer numeroPersonasPendientes, Double gastoTotal, Double gastoPagado, Double boteInicial) {
		String descripcion = "";
		switch (estadoApatxa) {
		case HECHO:
			Double bote = calcularBote(gastoTotal, gastoPagado, boteInicial);
			if (bote > 0) {
				descripcion = res.getString(R.string.estado_apatxa_hecho_detalle);
			} else {
				descripcion = String.format(res.getString(R.string.estado_apatxa_hecho_detalle_sobra_bote), bote);
			}
			break;
		case PENDIENTE:
			descripcion = res.getQuantityString(R.plurals.estado_apatxa_pendiente_detalle_numero_personas, numeroPersonasPendientes, numeroPersonasPendientes);
			break;
		case SIN_REPARTIR:
			descripcion = res.getString(R.string.estado_apatxa_sin_repartir_detalle);
			break;
		default:
			break;
		}
		return descripcion;
	}

	public static String getDescripcionParaListado(Resources res, EstadoApatxa estadoApatxa) {
		String descripcion = "";
		switch (estadoApatxa) {
		case HECHO:
			descripcion = res.getString(R.string.estado_apatxa_hecho);
			break;
		case PENDIENTE:
			descripcion = res.getString(R.string.estado_apatxa_pendiente);
			break;
		case SIN_REPARTIR:
			descripcion = res.getString(R.string.estado_apatxa_sin_repartir);
			break;
		default:
			break;
		}
		return descripcion;
	}

	private static double calcularBote(Double gastoTotal, Double gastoPagado, Double boteInicial) {
		Double bote = (boteInicial + gastoPagado) - gastoTotal;
		return bote < 0 ? 0 : bote;
	}
}
