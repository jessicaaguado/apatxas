package com.jagusan.apatxas.utils;

import com.jagusan.apatxas.sqlite.modelView.GastoApatxaListado;

import java.util.List;

public class CalcularSumaTotalGastos {

    public static Double calcular(List<GastoApatxaListado> gastos) {
        Double total = 0.0;
        for (GastoApatxaListado gasto : gastos) {
            total += gasto.getTotal();
        }
        return total;
    }
}
