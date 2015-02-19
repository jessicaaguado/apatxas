package com.jagusan.apatxas.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.logicaNegocio.servicios.ApatxaService;
import com.jagusan.apatxas.utils.ConvertirFecha;
import com.jagusan.apatxas.utils.FormatearFecha;
import com.jagusan.apatxas.utils.ValidacionActivity;

import java.util.Calendar;
import java.util.Date;

public class EditarInformacionBasicaApatxaActivity extends ApatxasActionBarActivity {

    private EditText nombreApatxaEditText;
    private TextView fechaInicioApatxaTextView;
    private TextView fechaFinApatxaTextView;
    private DatePickerDialog fechaInicioDatePickerDialog;
    private DatePickerDialog fechaFinDatePickerDialog;
    private Switch soloUnDiaSwitch;

    private long idApatxa;
    private String nombre;
    private Long fechaInicio;
    private Long fechaFin;
    private Boolean soloUnDia;

    private Resources resources;
    private ApatxaService apatxaService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editar_informacion_basica_apatxa);

        inicializarServicios();

        personalizarActionBar(R.string.title_activity_editar_apatxa, MostrarTituloPantalla.EDITAR_APATXA);

        recuperarDatosPasoAnterior();

        cargarElementosLayout();

        cargarInformacionApatxa();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editar_informacion_basica_apatxa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_guardar_informacion_basica:
                if (validacionesCorrectas()) {
                    String nombre = getNombreIntroducido();
                    Long fechaInicio = getFechaInicioIntroducida();
                    Long fechaFin = getFechaFinIntroducida();
                    Boolean soloUnDia = getSoloUnDiaSeleccionado();
                    apatxaService.actualizarApatxa(idApatxa, nombre, fechaInicio, fechaFin, soloUnDia);
                    setResult(RESULT_OK);
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void inicializarServicios() {
        resources = getResources();
        apatxaService = new ApatxaService(this);
    }

    private void cargarElementosLayout() {
        nombreApatxaEditText = (EditText) findViewById(R.id.nombreApatxa);
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

    private void recuperarDatosPasoAnterior() {
        Intent intent = getIntent();
        idApatxa = intent.getLongExtra("idApatxa", -1);
        nombre = intent.getStringExtra("nombre");
        fechaInicio = intent.getLongExtra("fechaInicio", -1);
        fechaFin = intent.getLongExtra("fechaFin", -1);
        soloUnDia = intent.getBooleanExtra("soloUnDia", false);
    }

    private void cargarInformacionApatxa() {
        nombreApatxaEditText.setText(nombre);

        soloUnDiaSwitch.setChecked(soloUnDia);
        gestionarSoloUnDiaSwitch(soloUnDia);


        fechaInicioApatxaTextView.setText(FormatearFecha.formatearFecha(resources, new Date(fechaInicio)));
        fechaFinApatxaTextView.setText(FormatearFecha.formatearFecha(resources, new Date(fechaFin)));

        Calendar fechaInicioCalendar = Calendar.getInstance();
        fechaInicioCalendar.setTime(new Date(fechaInicio));
        fechaInicioDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar fechaSeleccionada = Calendar.getInstance();
                fechaSeleccionada.set(year, monthOfYear, dayOfMonth);
                fechaInicioApatxaTextView.setText(FormatearFecha.formatearFecha(resources, fechaSeleccionada.getTime()));
            }
        }, fechaInicioCalendar.get(Calendar.YEAR), fechaInicioCalendar.get(Calendar.MONTH), fechaInicioCalendar.get(Calendar.DAY_OF_MONTH));

        Calendar fechaFinCalendar = Calendar.getInstance();
        fechaFinCalendar.setTime(new Date(fechaFin));
        fechaFinDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar fechaSeleccionada = Calendar.getInstance();
                fechaSeleccionada.set(year, monthOfYear, dayOfMonth);
                fechaFinApatxaTextView.setText(FormatearFecha.formatearFecha(resources, fechaSeleccionada.getTime()));
            }
        }, fechaFinCalendar.get(Calendar.YEAR), fechaFinCalendar.get(Calendar.MONTH), fechaFinCalendar.get(Calendar.DAY_OF_MONTH));


    }

    public void mostrarDatePicker(View view) {
        if (view == fechaInicioApatxaTextView) {
            fechaInicioDatePickerDialog.show();
        }
        if (view == fechaFinApatxaTextView) {
            fechaFinDatePickerDialog.show();
        }
    }

    String getNombreIntroducido() {
        return nombreApatxaEditText.getText().toString();
    }

    Long getFechaInicioIntroducida() {
        return ConvertirFecha.getTime(resources, fechaInicioApatxaTextView.getText().toString());

    }

    Long getFechaFinIntroducida() {
        return ConvertirFecha.getTime(resources, fechaFinApatxaTextView.getText().toString());
    }

    Boolean getSoloUnDiaSeleccionado() {
        return soloUnDiaSwitch.isChecked();
    }

    private Boolean validacionesCorrectas() {
        return ValidacionActivity.validarTituloObligatorio(nombreApatxaEditText, resources);
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
