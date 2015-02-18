package com.jagusan.apatxas.modelView;

public class PersonaListadoReparto extends PersonaListado {

    private Double cantidadPago;
    private Boolean repartoPagado;

    public Double getCantidadPago() {
        return cantidadPago;
    }

    public void setCantidadPago(Double cantidadPago) {
        this.cantidadPago = cantidadPago;
    }

     public Boolean getRepartoPagado() {
        return repartoPagado;
    }

    public void setRepartoPagado(Boolean repartoPagado) {
        this.repartoPagado = repartoPagado;
    }
}
