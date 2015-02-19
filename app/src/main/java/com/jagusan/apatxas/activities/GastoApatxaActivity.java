package com.jagusan.apatxas.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaPersonasPaganGastoApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.servicios.GastoService;
import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.utils.ValidacionActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class GastoApatxaActivity extends ApatxasActionBarActivity {

    protected ListaPersonasPaganGastoApatxaArrayAdapter listaPersonasApatxaArrayAdapter;
    AutoCompleteTextView conceptoGastoAutoComplete;
    EditText totalGastoEditText;
    Spinner personasSpinner;
    List<PersonaListado> personasApatxa;
    private Resources resources;
    private GastoService gastoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nuevo_gasto_apatxa);

        inicializarServicios();

        personalizarActionBar();

        recuperarDatosPasoAnterior();

        cargarElementosLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }


    private void inicializarServicios() {
        resources = getResources();
        gastoService = new GastoService(this);
    }

    abstract void personalizarActionBar();

    protected void recuperarDatosPasoAnterior() {
        personasApatxa = getIntent().getSerializableExtra("personas") != null ? (ArrayList<PersonaListado>) getIntent().getSerializableExtra("personas") : new ArrayList<PersonaListado>();
    }

    protected void cargarElementosLayout() {
        conceptoGastoAutoComplete = (AutoCompleteTextView) findViewById(R.id.concepto);
        totalGastoEditText = (EditText) findViewById(R.id.totalGasto);
        TextView labelPagadoPorTextView = (TextView) findViewById(R.id.labelGastoPagadoPor);

        personasSpinner = (Spinner) findViewById(R.id.listaPersonasApatxaPagadoGasto);
        if (!personasApatxa.isEmpty()) {
            List<PersonaListado> personasApatxaConOpcionSinPagar = new ArrayList<>();

            PersonaListado personaSinPagar = new PersonaListado();
            personaSinPagar.nombre = resources.getString(R.string.sin_pagar);
            personasApatxaConOpcionSinPagar.add(personaSinPagar);
            personasApatxaConOpcionSinPagar.addAll(personasApatxa);
            listaPersonasApatxaArrayAdapter = new ListaPersonasPaganGastoApatxaArrayAdapter(this, R.layout.lista_personas_apatxa_row, personasApatxaConOpcionSinPagar);
            personasSpinner.setAdapter(listaPersonasApatxaArrayAdapter);
        } else {
            labelPagadoPorTextView.setVisibility(View.GONE);
            personasSpinner.setVisibility(View.GONE);
        }

        List<String> todosConceptos = gastoService.recuperarTodosConceptos();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todosConceptos);
        conceptoGastoAutoComplete.setAdapter(adapter);
        conceptoGastoAutoComplete.setThreshold(3);

    }

    protected Boolean validacionesCorrectas() {
        Boolean tituloOk = ValidacionActivity.validarTituloObligatorio(conceptoGastoAutoComplete, resources);
        Boolean fechaOk = ValidacionActivity.validarCantidadObligatoria(totalGastoEditText, resources);
        return tituloOk && fechaOk;
    }

    protected PersonaListado getPagadorSeleccionado() {
        return (PersonaListado) personasSpinner.getSelectedItem();
    }

    protected Double getImporteIntroducido() {
        Double totalGasto = 0.0;
        try {
            totalGasto = Double.parseDouble(totalGastoEditText.getText().toString());
        } catch (Exception e) {
            // mantenemos total a 0
        }
        return totalGasto;
    }

    protected String getConceptoIntroducido() {
        return conceptoGastoAutoComplete.getText().toString();
    }

}
