package com.jagusan.apatxas.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.logicaNegocio.servicios.ApatxaService;
import com.jagusan.apatxas.modelView.ApatxaDetalle;
import com.jagusan.apatxas.utils.FormatearFecha;
import com.jagusan.apatxas.utils.FormatearNumero;
import com.jagusan.apatxas.utils.ObtenerDescripcionEstadoApatxa;

public abstract class DetalleApatxaActivity extends ActionBarActivity {

    private final Boolean MOSTRAR_TITULO_PANTALLA = false;

    protected ApatxaService apatxaService;
    protected Resources resources;
    protected Long idApatxa;

    private TextView nombreApatxaTextView;
    private TextView fechaApatxaTextView;
    private TextView boteInicialEditText;
    private TextView numeroPersonasTextView;
    private TextView estadoApatxaTextView;
    private TextView usoBoteInicialTextView;

    protected ApatxaDetalle apatxa;

    public int EDITAR_INFORMACION_BASICA_REQUEST_CODE = 1;
    public int EDITAR_INFORMACION_LISTA_PERSONAS_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idApatxa = getIntent().getLongExtra("id", -1);

        setContentView();

        inicializarServicios();

        personalizarActionBar();

        cargarElementosLayout();

        cargarInformacionApatxa();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_editar_detalle_apatxa:
                irEditarInformacionBasicaApatxa();
                return true;
            case R.id.action_acceso_personas:
                irEditarListaPersonasApatxa();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    abstract protected void setContentView();

    protected void inicializarServicios() {
        apatxaService = new ApatxaService(this);
        resources = getResources();
    }

    protected void personalizarActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setDisplayShowTitleEnabled(MOSTRAR_TITULO_PANTALLA);
        // boton para ir a la actividad anterior
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    protected void cargarElementosLayout() {
        nombreApatxaTextView = (TextView) findViewById(R.id.nombreApatxaDetalle);
        fechaApatxaTextView = (TextView) findViewById(R.id.fechaApatxaDetalle);
        boteInicialEditText = (TextView) findViewById(R.id.boteInicialApatxaDetalle);
        numeroPersonasTextView = (TextView) findViewById(R.id.numeroPersonasApatxaDetalle);
        estadoApatxaTextView = (TextView) findViewById(R.id.estadoApatxaDetalle);
        usoBoteInicialTextView = (TextView) findViewById(R.id.usoBoteInicialApatxaDetalle);
    }

    protected void cargarInformacionApatxa() {
        apatxa = apatxaService.getApatxaDetalle(idApatxa);
        cargarInformacionTitulo();
        cargarInformacionFecha();
        cargarInformacionBoteInicial();
        cargarInformacionPersonas();
        cargarInformacionEstado();
    }

    private void cargarInformacionTitulo() {
        nombreApatxaTextView.setText(apatxa.nombre);
    }

    private void cargarInformacionFecha() {
        Date fechaInicio = apatxa.fechaInicio;
        Date fechaFin = apatxa.fechaFin;
        boolean soloUnDia = apatxa.soloUnDia;
        String fechaDescripcion = FormatearFecha.formatearFecha(resources, fechaInicio);
        if (!soloUnDia) {
            fechaDescripcion += " ~ " + FormatearFecha.formatearFecha(resources, fechaFin);
        }
        fechaApatxaTextView.setText(fechaDescripcion);
    }

    private void cargarInformacionBoteInicial() {
        Double boteInicial = apatxa.boteInicial;
        boteInicialEditText.setText(FormatearNumero.aDineroEuros(resources, boteInicial));
        String usoBoteInicial = apatxa.descontarBoteInicialGastoTotal ? resources.getString(R.string.bote_inicial_a_descontar) : "";
        usoBoteInicialTextView.setText(usoBoteInicial);
    }

    private void cargarInformacionPersonas() {
        int numPersonas = apatxa.getPersonas().size();
        String titulo = resources.getQuantityString(R.plurals.numero_personas_apatxa, numPersonas, numPersonas);
        numeroPersonasTextView.setText(titulo);
    }

    private void cargarInformacionEstado() {
        String estadoApatxa = ObtenerDescripcionEstadoApatxa.getDescripcionParaDetalle(getResources(), apatxa.getEstadoApatxa(), apatxa.personasPendientesPagarCobrar);
        estadoApatxaTextView.setText(estadoApatxa);
    }

    public void irEditarInformacionBasicaApatxa() {
        Intent intent = new Intent(this, EditarInformacionBasicaApatxaActivity.class);
        intent.putExtra("idApatxa", apatxa.id);
        intent.putExtra("nombre", apatxa.nombre);
        intent.putExtra("fechaInicio", apatxa.fechaInicio.getTime());
        intent.putExtra("fechaFin", apatxa.fechaFin.getTime());
        intent.putExtra("soloUnDia", apatxa.soloUnDia);
        intent.putExtra("boteInicial", apatxa.boteInicial);
        intent.putExtra("descontarBoteInicial", apatxa.descontarBoteInicialGastoTotal);
        startActivityForResult(intent, EDITAR_INFORMACION_BASICA_REQUEST_CODE);
    }

    public void irEditarListaPersonasApatxa() {
        Intent intent = new Intent(this, ListaPersonasApatxaActivity.class);
        intent.putExtra("personas", new ArrayList(apatxa.getPersonas()));
        intent.putExtra("idApatxa", apatxa.id);
        startActivityForResult(intent, EDITAR_INFORMACION_LISTA_PERSONAS_REQUEST_CODE);
    }


}
