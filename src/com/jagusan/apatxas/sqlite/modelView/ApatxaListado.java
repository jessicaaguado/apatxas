package com.jagusan.apatxas.sqlite.modelView;

import java.util.Date;

import com.jagusan.apatxas.utils.EstadoApatxa;

public class ApatxaListado {

	private Long id;
	private String nombre;
	private Date fecha;
	private Double gastoTotal = 0.0;
	private Double pagado = 0.0;
	private Double boteInicial = 0.0;
	private Boolean repartoRealizado;
	private Integer personasPendientesPagarCobrar;

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
		apatxaListadoString = apatxaListadoString + "\nID: " + this.getId();
		apatxaListadoString = apatxaListadoString + "\nNOMBRE: " + this.getNombre();
		apatxaListadoString = apatxaListadoString + "\nFECHA: " + this.getFecha();
		apatxaListadoString = apatxaListadoString + "\n-------------------";
		return apatxaListadoString;
	}

	public EstadoApatxa getEstadoApatxa() {
		EstadoApatxa estadoApatxa = EstadoApatxa.HECHO;
		if (!repartoRealizado) {
			estadoApatxa = EstadoApatxa.SIN_REPARTIR;
		} else if (personasPendientesPagarCobrar > 0) {
			estadoApatxa = EstadoApatxa.PENDIENTE;
		}
		return estadoApatxa;
	}

	public Double getBoteInicial() {
		return boteInicial;
	}

	public void setBoteInicial(Double boteInicial) {
		this.boteInicial = boteInicial;
	}

	public Boolean getRepartoRealizado() {
		return repartoRealizado;
	}

	public void setRepartoRealizado(Boolean repartoRealizado) {
		this.repartoRealizado = repartoRealizado;
	}

	public Integer getPersonasPendientesPagarCobrar() {
		return personasPendientesPagarCobrar;
	}

	public void setPersonasPendientesPagarCobrar(Integer personasPendientesPagarCobrar) {
		this.personasPendientesPagarCobrar = personasPendientesPagarCobrar;
	}

}
