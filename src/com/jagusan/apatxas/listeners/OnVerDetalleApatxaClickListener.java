package com.jagusan.apatxas.listeners;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.jagusan.apatxas.activities.DetalleApatxaActivity;
import com.jagusan.apatxas.sqlite.modelView.ApatxaListado;

public class OnVerDetalleApatxaClickListener implements OnItemClickListener {

	@Override
	public void onItemClick(AdapterView<?> apatxaListadoListView, View apatxaListadoRow, int position, long id) {
		ApatxaListado apatxaClicked = (ApatxaListado) apatxaListadoListView.getItemAtPosition(position);
				
		Context context = apatxaListadoListView.getContext();
		Intent intent = new Intent(context, DetalleApatxaActivity.class);
		intent.putExtra("id", apatxaClicked.getId());
		context.startActivity(intent);		
	}

}
