package com.jagusan.apatxas.logicaNegocio.servicios;

import com.jagusan.apatxas.modelView.ApatxaDetalle;

public class CalcularRepartoService {

	public static Double calcularParteProporcional(ApatxaDetalle apatxaDetalle) {
		Double gastoTotal = apatxaDetalle.gastoTotal;
		if (apatxaDetalle.descontarBoteInicialGastoTotal){
			gastoTotal = gastoTotal - apatxaDetalle.boteInicial;
		}
		Integer numeroPersonas = apatxaDetalle.getPersonas().size();
		return gastoTotal / numeroPersonas;
	}

}
