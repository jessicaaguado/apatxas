package com.jagusan.apatxas.utils;

import com.jagusan.apatxas.modelView.GastoApatxaListado;

import java.util.List;

public class CalcularSumaTotalGastos {

    public static Double calcular(List<GastoApatxaListado> gastos) {
        Double total = 0.0;
        for (GastoApatxaListado gasto : gastos) {
            total += gasto.total;
        }
        return total;
    }
}
