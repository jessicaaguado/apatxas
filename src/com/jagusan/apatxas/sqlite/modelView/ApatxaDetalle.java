package com.jagusan.apatxas.sqlite.modelView;

import java.util.Date;

public class ApatxaDetalle {

	private Long id;
	private String nombre;
	private Date fecha;
	private Double boteInicial = 0.0;
	private Double gastoTotal = 0.0;
	private Double pagado = 0.0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Double getBoteInicial() {
		return boteInicial;
	}

	public void setBoteInicial(Double boteInicial) {
		this.boteInicial = boteInicial;
	}

	public Double getBote() {
		Double gastoLiquidado = boteInicial + pagado;
		return (gastoTotal - gastoLiquidado) < 0 ? (gastoLiquidado - gastoTotal) : 0.0;		
	}
}
