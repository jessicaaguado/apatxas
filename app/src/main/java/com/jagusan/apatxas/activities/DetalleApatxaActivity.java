package com.jagusan.apatxas.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.logicaNegocio.servicios.ApatxaService;
import com.jagusan.apatxas.modelView.ApatxaDetalle;
import com.jagusan.apatxas.utils.FormatearFecha;
import com.jagusan.apatxas.utils.ObtenerDescripcionEstadoApatxa;

import java.util.ArrayList;
import java.util.Date;

public abstract class DetalleApatxaActivity extends ApatxasActionBarActivity {

    ApatxaService apatxaService;
    Resources resources;
    Long idApatxa;

    ViewGroup headerInformacionDetalle;
    TextView numeroPersonasTextView;
    ApatxaDetalle apatxa;
    int EDITAR_INFORMACION_BASICA_REQUEST_CODE = 1;
    int EDITAR_INFORMACION_LISTA_PERSONAS_REQUEST_CODE = 10;
    private TextView nombreApatxaTextView;
    private TextView fechaApatxaTextView;
    private TextView estadoApatxaTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idApatxa = getIntent().getLongExtra("id", -1);

        setContentView();

        inicializarServicios();

        personalizarActionBar(R.string.title_activity_detalle_apatxa, MostrarTituloPantalla.DETALLE);

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

    void inicializarServicios() {
        apatxaService = new ApatxaService(this);
        resources = getResources();
    }

    void cargarElementosLayout() {
        nombreApatxaTextView = (TextView) findViewById(R.id.nombreApatxaDetalle);
        fechaApatxaTextView = (TextView) findViewById(R.id.fechaApatxaDetalle);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable iconoCalendario = getResources().getDrawable(R.drawable.ic_apatxas_calendario_blanco);
            iconoCalendario.setColorFilter(resources.getColor(R.color.apatxascolors_color_claro), PorterDuff.Mode.MULTIPLY);
            fechaApatxaTextView.setCompoundDrawablesWithIntrinsicBounds(iconoCalendario, null, null, null);
        }
        headerInformacionDetalle = (ViewGroup) getLayoutInflater().inflate(R.layout.detalle_apatxa_resumen_header, null);
        numeroPersonasTextView = (TextView) headerInformacionDetalle.findViewById(R.id.numeroPersonasApatxaDetalle);
        estadoApatxaTextView = (TextView) headerInformacionDetalle.findViewById(R.id.estadoApatxaDetalle);
    }

    void cargarInformacionApatxa() {
        apatxa = apatxaService.getApatxaDetalle(idApatxa);
        cargarInformacionTitulo();
        cargarInformacionFecha();
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
            fechaDescripcion += " - " + FormatearFecha.formatearFecha(resources, fechaFin);
        }
        fechaApatxaTextView.setText(fechaDescripcion);
    }


    private void cargarInformacionPersonas() {
        int numPersonas = apatxa.personas.size();
        String titulo = resources.getQuantityString(R.plurals.numero_personas_apatxa, numPersonas, numPersonas);
        numeroPersonasTextView.setText(titulo);
    }

    private void cargarInformacionEstado() {
        String estadoApatxa = ObtenerDescripcionEstadoApatxa.getDescripcionParaDetalle(getResources(), apatxa.getEstadoApatxa(), apatxa.personasPendientesPagarCobrar);
        estadoApatxaTextView.setText(estadoApatxa);
    }

    void irEditarInformacionBasicaApatxa() {
        Intent intent = new Intent(this, EditarInformacionBasicaApatxaActivity.class);
        intent.putExtra("idApatxa", apatxa.id);
        intent.putExtra("nombre", apatxa.nombre);
        intent.putExtra("fechaInicio", apatxa.fechaInicio.getTime());
        intent.putExtra("fechaFin", apatxa.fechaFin.getTime());
        intent.putExtra("soloUnDia", apatxa.soloUnDia);
        startActivityForResult(intent, EDITAR_INFORMACION_BASICA_REQUEST_CODE);
    }

    void irEditarListaPersonasApatxa() {
        Intent intent = new Intent(this, ListaPersonasApatxaActivity.class);
        intent.putExtra("personas", new ArrayList<>(apatxa.personas));
        intent.putExtra("idApatxa", apatxa.id);
        startActivityForResult(intent, EDITAR_INFORMACION_LISTA_PERSONAS_REQUEST_CODE);
    }


}
