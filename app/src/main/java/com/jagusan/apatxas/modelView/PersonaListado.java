package com.jagusan.apatxas.modelView;

import java.io.Serializable;

public class PersonaListado implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nombre;

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

}
