package com.jagusan.apatxas.sqlite.modelView;


public class ApatxaDetalle extends ApatxaListado {	
	
	private Integer numeroPersonas;
	
	public Integer getNumeroPersonas() {
		return numeroPersonas;
	}
	
	public void setNumeroPersonas(Integer numeroPersonas) {
		this.numeroPersonas = numeroPersonas;
	}	
		
	public Double getBote() {
		Double gastoLiquidado = this.getBoteInicial() + this.getPagado();
		Double gastoTotal = this.getGastoTotal();
		return (gastoTotal - gastoLiquidado) < 0 ? (gastoLiquidado - gastoTotal) : 0.0;		
	}
	

		
	
}
