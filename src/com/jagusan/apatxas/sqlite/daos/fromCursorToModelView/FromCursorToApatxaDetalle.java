package com.jagusan.apatxas.sqlite.daos.fromCursorToModelView;

import android.database.Cursor;

import com.jagusan.apatxas.sqlite.modelView.ApatxaDetalle;

public class FromCursorToApatxaDetalle {

	public static ApatxaDetalle convertir(Cursor cursor) {
		ApatxaDetalle apatxaDetalle = new ApatxaDetalle();
		TablaApatxaCursor tablaApatxaCursor = new TablaApatxaCursor(cursor);
		apatxaDetalle.setId(tablaApatxaCursor.getId());
		apatxaDetalle.setNombre(tablaApatxaCursor.getNombre());
		apatxaDetalle.setFecha(tablaApatxaCursor.getFecha());	
		Double boteInicial = tablaApatxaCursor.getBoteInicial();
		apatxaDetalle.setBoteInicial(boteInicial);
		Double gastoTotal = tablaApatxaCursor.getGastoTotal();
		apatxaDetalle.setGastoTotal(gastoTotal);
		Double gastoPagado = tablaApatxaCursor.getGastoPagado();
		apatxaDetalle.setGastoTotal(gastoPagado);
		//bote
		Double gastoLiquidado = boteInicial + gastoPagado;
		Double bote = (gastoTotal - gastoLiquidado) < 0 ? (gastoLiquidado - gastoTotal) : 0.0;
		apatxaDetalle.setBote(bote);
		
		return apatxaDetalle;
	}

}
