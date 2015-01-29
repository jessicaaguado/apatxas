package com.jagusan.apatxas.listeners;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.jagusan.apatxas.activities.DetalleApatxaConRepartoActivity;
import com.jagusan.apatxas.activities.DetalleApatxaSinRepartoActivity;
import com.jagusan.apatxas.modelView.ApatxaListado;

public class OnVerDetalleApatxaClickListener implements OnItemClickListener {

	@Override
	public void onItemClick(AdapterView<?> apatxaListadoListView, View apatxaListadoRow, int position, long id) {
		ApatxaListado apatxaClicked = (ApatxaListado) apatxaListadoListView.getItemAtPosition(position);

		Context context = apatxaListadoListView.getContext();
		Class activityToGo = DetalleApatxaConRepartoActivity.class;
		if (!apatxaClicked.getRepartoRealizado()) {
			activityToGo = DetalleApatxaSinRepartoActivity.class;
		}
		Intent intent = new Intent(context, activityToGo);
		intent.putExtra("id", apatxaClicked.getId());
		context.startActivity(intent);
	}

}
