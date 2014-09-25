package com.jagusan.apatxas.sqlite.modelView;

import java.util.Date;

public class GastoListado {

	private String nombre;
	private Date fecha;
	private Double gastoTotal;
	private Double pagado;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Double getGastoTotal() {
		return gastoTotal;
	}

	public void setGastoTotal(Double gastoTotal) {
		this.gastoTotal = gastoTotal;
	}

	public Double getPagado() {
		return pagado;
	}

	public void setPagado(Double pagado) {
		this.pagado = pagado;
	}

}
