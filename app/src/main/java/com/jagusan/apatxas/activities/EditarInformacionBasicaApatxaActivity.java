package com.jagusan.apatxas.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
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

public class EditarInformacionBasicaApatxaActivity extends ActionBarActivity {

    private final Boolean MOSTRAR_TITULO_PANTALLA = true;

    private EditText nombreApatxaEditText;
    private TextView fechaInicioApatxaTextView;
    private TextView fechaFinApatxaTextView;
    private DatePickerDialog fechaInicioDatePickerDialog;
    private DatePickerDialog fechaFinDatePickerDialog;
    private EditText boteInicialEditText;
    private CheckBox descontarBoteInicialCheckBox;
    private Switch soloUnDiaSwitch;

    private long idApatxa;
    private String nombre;
    private Long fechaInicio;
    private Long fechaFin;
    private Boolean soloUnDia;
    private Double boteInicial;
    private Boolean descontarBoteInicial;

    private Resources resources;
    private ApatxaService apatxaService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editar_informacion_basica_apatxa);

        inicializarServicios();

        personalizarActionBar();

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
                    Double boteInicial = getBoteIntroducido();
                    Boolean descontarBoteInicial = getDescontarBoteInicial();
                    apatxaService.actualizarApatxa(idApatxa, nombre, fechaInicio, fechaFin, soloUnDia, boteInicial, descontarBoteInicial);
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

    private void personalizarActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setDisplayShowTitleEnabled(MOSTRAR_TITULO_PANTALLA);
    }

    private void cargarElementosLayout() {
        nombreApatxaEditText = (EditText) findViewById(R.id.nombreApatxa);
        fechaInicioApatxaTextView = (TextView) findViewById(R.id.fechaInicioApatxa);
        fechaFinApatxaTextView = (TextView) findViewById(R.id.fechaFinApatxa);
        boteInicialEditText = (EditText) findViewById(R.id.boteInicialApatxa);
        descontarBoteInicialCheckBox = (CheckBox) findViewById(R.id.descontarBoteInicial);

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
        boteInicial = intent.getDoubleExtra("boteInicial", 0.0);
        descontarBoteInicial = intent.getBooleanExtra("descontarBoteInicial", false);
    }

    private void cargarInformacionApatxa() {
        nombreApatxaEditText.setText(nombre);

        boteInicialEditText.setText(boteInicial.toString());
        descontarBoteInicialCheckBox.setChecked(descontarBoteInicial);

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
        if (view == fechaInicioApatxaTextView){
            fechaInicioDatePickerDialog.show();
        }
        if (view == fechaFinApatxaTextView){
            fechaFinDatePickerDialog.show();
        }
    }

    protected String getNombreIntroducido() {
        return nombreApatxaEditText.getText().toString();
    }

    protected Long getFechaInicioIntroducida() {
        return ConvertirFecha.getTime(resources, fechaInicioApatxaTextView.getText().toString());

    }

    protected Long getFechaFinIntroducida() {
        return ConvertirFecha.getTime(resources, fechaFinApatxaTextView.getText().toString());
    }

    protected Boolean getSoloUnDiaSeleccionado() {
        return soloUnDiaSwitch.isChecked();
    }

    protected Double getBoteIntroducido() {
        Double boteInicial = 0.0;
        try {
            boteInicial = Double.parseDouble(boteInicialEditText.getText().toString());
        } catch (Exception e) {
            // mantenemos bote inicial a 0
        }
        return boteInicial;
    }

    protected Boolean getDescontarBoteInicial() {
        return descontarBoteInicialCheckBox.isChecked();
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
