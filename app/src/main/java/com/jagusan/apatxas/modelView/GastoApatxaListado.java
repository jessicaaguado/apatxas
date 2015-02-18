package com.jagusan.apatxas.modelView;

import java.io.Serializable;

public class GastoApatxaListado implements Serializable {

	private static final long serialVersionUID = 1L;

    public Long id;
    public String concepto;
    public Double total;
    public Long idPagadoPor;
    public String pagadoPor;
    public Long idContactoPersonaPagadoPor;

    public GastoApatxaListado(String concepto, Double total, PersonaListado pagadoPor) {
        super();
        this.concepto = concepto;
        this.total = total;
        this.idPagadoPor = pagadoPor != null ? pagadoPor.id : null;
        this.idContactoPersonaPagadoPor = pagadoPor != null ? pagadoPor.idContacto : null;
        this.pagadoPor = pagadoPor != null ? pagadoPor.nombre : null;
    }

    public GastoApatxaListado() {
        super();
    }

}
