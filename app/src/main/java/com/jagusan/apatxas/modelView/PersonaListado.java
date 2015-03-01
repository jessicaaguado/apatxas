package com.jagusan.apatxas.modelView;

import java.io.Serializable;

public class PersonaListado implements Serializable {

    private static final long serialVersionUID = 1L;

    public Long id;
    public String nombre;
    public Long idContacto;
    public String uriFoto;

    public PersonaListado() {

    }

    public PersonaListado(Long id, String nombre, Long idContacto, String uriFoto) {
        this.id = id;
        this.nombre = nombre;
        this.idContacto = idContacto;
        this.uriFoto = uriFoto;
    }


}
