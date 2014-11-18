package com.jagusan.apatxas.sqlite.modelView;

import java.util.List;

public class ApatxaReparto extends ApatxaDetalle {
	
	List<PersonaListadoReparto> listaPersonasResultadoReparto;

	public List<PersonaListadoReparto> getListaPersonasResultadoReparto() {
		return listaPersonasResultadoReparto;
	}

	public void setListaPersonasResultadoReparto(List<PersonaListadoReparto> listaPersonasResultadoReparto) {
		this.listaPersonasResultadoReparto = listaPersonasResultadoReparto;
	}

}
