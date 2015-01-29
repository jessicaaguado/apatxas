package com.jagusan.apatxas.logicaNegocio.servicios;

import com.jagusan.apatxas.modelView.ApatxaDetalle;

public class CalcularRepartoService {

	public Double calcularParteProporcional(ApatxaDetalle apatxaDetalle) {
		Double gastoTotal = apatxaDetalle.getGastoTotal();
		if (apatxaDetalle.getDescontarBoteInicialGastoTotal()){
			gastoTotal = gastoTotal - apatxaDetalle.getBoteInicial();			
		}
		Integer numeroPersonas = apatxaDetalle.getPersonas().size();
		return gastoTotal / numeroPersonas;
	}

}
