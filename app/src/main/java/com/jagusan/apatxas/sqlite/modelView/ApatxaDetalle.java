package com.jagusan.apatxas.sqlite.modelView;

import java.util.List;

public class ApatxaDetalle extends ApatxaListado {

	private List<PersonaListado> personas;
	private List<GastoApatxaListado> gastos;

	public Double getBote() {
		Double gastoLiquidado = this.getBoteInicial() + this.getPagado();
		Double gastoTotal = this.getGastoTotal();
		return (gastoTotal - gastoLiquidado) < 0 ? (gastoLiquidado - gastoTotal) : 0.0;
	}

	public List<PersonaListado> getPersonas() {
		return personas;
	}

	public void setPersonas(List<PersonaListado> personas) {
		this.personas = personas;
	}

	public List<GastoApatxaListado> getGastos() {
		return gastos;
	}

	public void setGastos(List<GastoApatxaListado> gastos) {
		this.gastos = gastos;
	}

}
