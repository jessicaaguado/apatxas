package com.jagusan.apatxas.sqlite.modelView;

public class PersonaListadoReparto extends PersonaListado {

	private Double cantidadPago;
	private Double pagado;
	private Double gastosPagados;

	public Double getCantidadPago() {
		return cantidadPago;
	}

	public void setCantidadPago(Double cantidadPago) {
		this.cantidadPago = cantidadPago;
	}

	public Double getPagado() {
		return pagado;
	}

	public void setPagado(Double pagado) {
		this.pagado = pagado;
	}

	public Double getGastosPagados() {
		return gastosPagados;
	}

	public void setGastosPagados(Double gastosPagados) {
		this.gastosPagados = gastosPagados;
	}

}
