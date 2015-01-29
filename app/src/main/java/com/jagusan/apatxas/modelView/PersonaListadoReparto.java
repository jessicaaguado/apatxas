package com.jagusan.apatxas.modelView;

import com.jagusan.apatxas.modelView.PersonaListado;

public class PersonaListadoReparto extends PersonaListado {

    private Double cantidadPago;
    private Double pagado;
    private Double gastosPagados;
    private Boolean repartoPagado;

    public Double getCantidadPago() {
        return cantidadPago;
    }

    public void setCantidadPago(Double cantidadPago) {
        this.cantidadPago = cantidadPago;
    }

    public Double getPagado() {
        return pagado;
    }

    public void setPagado(Double pagado) {
        this.pagado = pagado;
    }

    public Double getGastosPagados() {
        return gastosPagados;
    }

    public void setGastosPagados(Double gastosPagados) {
        this.gastosPagados = gastosPagados;
    }

    public Boolean getRepartoPagado() {
        return repartoPagado;
    }

    public void setRepartoPagado(Boolean repartoPagado) {
        this.repartoPagado = repartoPagado;
    }
}
