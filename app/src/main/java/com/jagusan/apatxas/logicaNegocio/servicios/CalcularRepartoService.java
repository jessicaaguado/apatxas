package com.jagusan.apatxas.logicaNegocio.servicios;

import com.jagusan.apatxas.modelView.ApatxaDetalle;

public class CalcularRepartoService {

	public static Double calcularParteProporcional(ApatxaDetalle apatxaDetalle) {
		Double gastoTotal = apatxaDetalle.gastoTotal;
		Integer numeroPersonas = apatxaDetalle.personas.size();
		return gastoTotal / numeroPersonas;
	}

}
