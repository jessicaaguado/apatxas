package com.jagusan.apatxas.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.logicaNegocio.servicios.ApatxaService;
import com.jagusan.apatxas.utils.FormatearFecha;
import com.jagusan.apatxas.utils.ValidacionActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class NuevoApatxaPaso1Activity extends ApatxasActionBarActivity {

    private AutoCompleteTextView nombreApatxaAutoComplete;
    private TextView fechaInicioApatxaTextView;
    private TextView fechaFinApatxaTextView;
    private DatePickerDialog fechaInicioDatePickerDialog;
    private DatePickerDialog fechaFinDatePickerDialog;
    private Switch soloUnDiaSwitch;

    private Resources resources;
    private ApatxaService apatxaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_apatxa_paso1);

        inicializarServicios();

        personalizarActionBar(R.string.title_activity_nuevo_apatxa_paso1, MostrarTituloPantalla.NUEVO_APATXA_PASO1);

        cargarElementosLayout();

        inicializarElementosLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nuevo_apatxa_paso1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_siguiente_paso:
                continuarAnadirApatxas();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void continuarAnadirApatxas() {
        String titulo = nombreApatxaAutoComplete.getText().toString();
        Long fechaInicio = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(resources.getString(R.string.patron_fecha_dia_mes_ano));
            fechaInicio = sdf.parse(fechaInicioApatxaTextView.getText().toString()).getTime();
        } catch (Exception e) {
            // sin fecha
        }
        Boolean soloUnDia = soloUnDiaSwitch.isChecked();
        Long fechaFin = fechaInicio;
        if (!soloUnDia) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(resources.getString(R.string.patron_fecha_dia_mes_ano));
                fechaFin = sdf.parse(fechaFinApatxaTextView.getText().toString()).getTime();
            } catch (Exception e) {
                // sin fecha
            }
        }

        if (validacionesCorrectas()) {
            Intent intent = new Intent(this, NuevoApatxaPaso2Activity.class);
            intent.putExtra("titulo", titulo);
            intent.putExtra("fechaInicio", fechaInicio);
            intent.putExtra("fechaFin", fechaFin);
            intent.putExtra("soloUnDia", soloUnDia);
            startActivity(intent);
        }
    }

    private Boolean validacionesCorrectas() {
        return ValidacionActivity.validarTituloObligatorio(nombreApatxaAutoComplete, resources);
    }

    private void inicializarServicios() {
        resources = getResources();
        apatxaService = new ApatxaService(this);
    }

    private void cargarElementosLayout() {
        nombreApatxaAutoComplete = (AutoCompleteTextView) findViewById(R.id.nombreApatxa);
        fechaInicioApatxaTextView = (TextView) findViewById(R.id.fechaInicioApatxa);
        fechaFinApatxaTextView = (TextView) findViewById(R.id.fechaFinApatxa);

        soloUnDiaSwitch = (Switch) findViewById(R.id.switchUnSoloDia);
        soloUnDiaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gestionarSoloUnDiaSwitch(isChecked);
            }
        });

    }


    private void inicializarElementosLayout() {
        Calendar hoy = Calendar.getInstance();

        fechaInicioApatxaTextView.setText(FormatearFecha.formatearFecha(resources, hoy.getTime()));
        fechaFinApatxaTextView.setText(FormatearFecha.formatearFecha(resources, hoy.getTime()));

        fechaInicioDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar fechaSeleccionada = Calendar.getInstance();
                fechaSeleccionada.set(year, monthOfYear, dayOfMonth);
                fechaInicioApatxaTextView.setText(FormatearFecha.formatearFecha(resources, fechaSeleccionada.getTime()));
            }
        }, hoy.get(Calendar.YEAR), hoy.get(Calendar.MONTH), hoy.get(Calendar.DAY_OF_MONTH));

        fechaFinDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar fechaSeleccionada = Calendar.getInstance();
                fechaSeleccionada.set(year, monthOfYear, dayOfMonth);
                fechaFinApatxaTextView.setText(FormatearFecha.formatearFecha(resources, fechaSeleccionada.getTime()));
            }
        }, hoy.get(Calendar.YEAR), hoy.get(Calendar.MONTH), hoy.get(Calendar.DAY_OF_MONTH));


        List<String> todosTitulos = apatxaService.recuperarTodosTitulos();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todosTitulos);
        nombreApatxaAutoComplete.setAdapter(adapter);
        nombreApatxaAutoComplete.setThreshold(3);
    }

    public void mostrarDatePicker(View view) {
        if (view == fechaInicioApatxaTextView) {
            fechaInicioDatePickerDialog.show();
        }
        if (view == fechaFinApatxaTextView) {
            fechaFinDatePickerDialog.show();
        }
    }

    private void gestionarSoloUnDiaSwitch(boolean soloUnDia) {
        if (soloUnDia) {
            soloUnDiaSwitch.setText(resources.getString(R.string.solo_un_dia));
            soloUnDiaSwitch.setTextColor(resources.getColor(R.color.apatxascolors_color));
            fechaFinApatxaTextView.setVisibility(View.GONE);
        } else {
            soloUnDiaSwitch.setText(resources.getString(R.string.varios_dias));
            soloUnDiaSwitch.setTextColor(resources.getColor(R.color.apatxascolors_gris_medio));
            fechaFinApatxaTextView.setVisibility(View.VISIBLE);
        }
    }


}
