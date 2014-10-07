package com.jagusan.apatxas.sqlite.modelView;

import java.util.Date;

public class ApatxaListado {

	private Long id;
	private String nombre;
	private Date fecha;
	private Double gastoTotal = 0.0;
	private Double pagado = 0.0;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		String apatxaListadoString = "-------------------";
		apatxaListadoString = apatxaListadoString + "\nID: "+this.getId();
		apatxaListadoString = apatxaListadoString + "\nNOMBRE: "+this.getNombre();
		apatxaListadoString = apatxaListadoString + "\nFECHA: "+this.getFecha();
		apatxaListadoString = apatxaListadoString + "\n-------------------";
		return apatxaListadoString;
	}

}
