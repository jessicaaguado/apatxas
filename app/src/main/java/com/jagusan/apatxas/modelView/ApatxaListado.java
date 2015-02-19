package com.jagusan.apatxas.modelView;

import com.jagusan.apatxas.utils.EstadoApatxa;

import java.util.Date;

public class ApatxaListado {

	public Long id;
	public String nombre;
	public Date fechaInicio;
    public Date fechaFin;
    public Boolean soloUnDia;
	public Double gastoTotal = 0.0;
	public Boolean repartoRealizado;
	public Integer personasPendientesPagarCobrar;


	public EstadoApatxa getEstadoApatxa() {
		EstadoApatxa estadoApatxa = EstadoApatxa.HECHO;
		if (!repartoRealizado) {
			estadoApatxa = EstadoApatxa.SIN_REPARTIR;
		} else if (personasPendientesPagarCobrar > 0) {
			estadoApatxa = EstadoApatxa.PENDIENTE;
		}
		return estadoApatxa;
	}



}
