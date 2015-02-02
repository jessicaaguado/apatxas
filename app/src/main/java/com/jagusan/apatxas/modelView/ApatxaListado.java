package com.jagusan.apatxas.modelView;

import java.util.Date;

import com.jagusan.apatxas.utils.EstadoApatxa;

public class ApatxaListado {

	public Long id;
	public String nombre;
	public Date fechaInicio;
    public Date fechaFin;
    public Boolean soloUnDia;
	public Double gastoTotal = 0.0;
	public Double boteInicial = 0.0;
	public Boolean descontarBoteInicialGastoTotal;
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
