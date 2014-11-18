package com.jagusan.apatxas.logicaNegocio;

import com.jagusan.apatxas.sqlite.modelView.ApatxaDetalle;

public class CalcularRepartoService {

	public Double calcularParteProporcional(ApatxaDetalle apatxaDetalle) {
		Double gastoTotal = apatxaDetalle.getGastoTotal();
		Integer numeroPersonas = apatxaDetalle.getPersonas().size();
		return gastoTotal / numeroPersonas;
	}

}
