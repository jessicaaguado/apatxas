package com.jagusan.apatxas.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.listeners.CambiarNombrePersonaApatxaDialogListener;

public class CambiarNombrePersonaApatxaDialogFragment extends DialogFragment {

	private CambiarNombrePersonaApatxaDialogListener listener;

	private int posicionPersonaCambiar;
	private String nombrePersonaCambiar;

	TextView nombreAntiguoTextView;
	EditText nombreNuevoEditText;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		posicionPersonaCambiar = getArguments().getInt("posicionPersonaCambiar");
		nombrePersonaCambiar = getArguments().getString("nombrePersonaCambiar");

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_cambiar_nombre_persona, null);
		nombreAntiguoTextView = (TextView) dialogView.findViewById(R.id.nombreAntiguoPersona);
		nombreAntiguoTextView.setText(nombrePersonaCambiar);
		nombreNuevoEditText = (EditText) dialogView.findViewById(R.id.nuevoNombrePersona);
		builder.setView(dialogView);		
		builder.setPositiveButton(R.string.action_listo, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				listener.onClickListoCambiarNombrePersona(posicionPersonaCambiar, nombreNuevoEditText.getText().toString());
			}
		});
		builder.setNegativeButton(R.string.action_cancelar, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		});
		return builder.create();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			listener = (CambiarNombrePersonaApatxaDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString() + " debe impelementar CambiarNombrePersonaApatxaDialogListener");
		}
	}
}
