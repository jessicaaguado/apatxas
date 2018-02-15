package com.jagusan.apatxas.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.adapters.ListaPersonasRepartoApatxaArrayAdapter;
import com.jagusan.apatxas.logicaNegocio.servicios.PersonaService;
import com.jagusan.apatxas.modelView.GastoApatxaListado;
import com.jagusan.apatxas.modelView.PersonaListado;
import com.jagusan.apatxas.modelView.PersonaListadoReparto;
import com.jagusan.apatxas.utils.CalcularSumaTotalGastos;
import com.jagusan.apatxas.utils.FormatearFecha;
import com.jagusan.apatxas.utils.FormatearNumero;
import com.jagusan.apatxas.utils.SettingsUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.*;
import jxl.write.Number;

public class DetalleApatxaConRepartoActivity extends DetalleApatxaActivity implements AdapterView.OnItemClickListener {

    private ListView personasRepartoListView;

    private int EDITAR_INFORMACION_LISTA_GASTOS_REQUEST_CODE = 20;

    private PersonaService personaService;
    private TextView tituloRepartoApatxaListViewHeader;
    private ListaPersonasRepartoApatxaArrayAdapter listaPersonasRepartoApatxaArrayAdapter;

    private ShareActionProvider mShareActionProvider;


    private ModeCallback modeCallback;
    private ActionMode actionMode;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detalle_apatxa_con_reparto, menu);

        MenuItem shareItem = menu.findItem(R.id.action_compartir);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(getDefaultShareIntent());
        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private Intent getDefaultShareIntent() {
        Intent sendIntent = new Intent();
        //sendIntent.setAction(Intent.ACTION_SEND);
        //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        //sendIntent.setType("text/plain");

        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setData(Uri.parse("mailto:"));
        sendIntent.setType("text/html");
        sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(crearBodyApatxa()));
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, crearSubjectApatxa());
        sendIntent.putExtra(Intent.EXTRA_STREAM, generateExcel());
        return sendIntent;
    }

    private String crearBodyApatxa() {

        StringBuilder sb = new StringBuilder();
        sb.append("<p><big><font color='#673ab7'><b>" + apatxa.nombre.toUpperCase() + "</b></font></big><br>");
        sb.append(getInformacionFechas()).append("</p>");
        sb.append("<br/><br/>");
        sb.append("<h5>" + resources.getString(R.string.email_titulo_gastos).toUpperCase() + "</h5>");

        for (GastoApatxaListado gasto : apatxa.gastos) {
            sb.append("<p>").append(gasto.concepto).append(" - ");
            sb.append("<font color='#6E6E6E'>").append(FormatearNumero.aDineroConMoneda(getBaseContext(), gasto.total)).append("</font><br>");
            sb.append("<font color='#808080'>");
            if (gasto.pagadoPor == null || gasto.pagadoPor.isEmpty()) {
                sb.append(" &#9758; " + resources.getString(R.string.email_no_paga_nadie));
            } else {
                sb.append(" &#9758; " + gasto.pagadoPor);
            }
            sb.append("</font>");
            sb.append("</p>");
        }

        sb.append("<br><br><hr>");
        sb.append("<h5><b>" + resources.getString(R.string.email_titulo_reparto).toUpperCase() + "</b></h5>");
        List<PersonaListadoReparto> listaPersonasReparto = apatxaService.getResultadoReparto(idApatxa);
        for (PersonaListadoReparto reparto : listaPersonasReparto) {
            sb.append("<p>" + reparto.nombre).append("<br>");
            if (reparto.repartoPagado) {
                sb.append("<font color='#088A29'>");
            } else {
                sb.append("<font color='#B40404'>&#9888;&nbsp;");
            }
            sb.append(FormatearNumero.aDescripcionRepartoDineroEurosEnFuncionEstado(getResources(), reparto.cantidadPago, reparto.repartoPagado));
            sb.append("</font>");
            sb.append("&nbsp;&nbsp;");
            sb.append("<font color='#6E6E6E'>").append(FormatearNumero.aDineroConMoneda(this, reparto.cantidadPago)).append("</font>");


            sb.append("</p>");
        }
        return sb.toString();
    }

    private String crearSubjectApatxa() {
        return "APATXAS: " + apatxa.nombre;
    }

    private String getInformacionFechas() {
        Date fechaInicio = apatxa.fechaInicio;
        Date fechaFin = apatxa.fechaFin;
        boolean soloUnDia = apatxa.soloUnDia;
        String fechaDescripcion = FormatearFecha.formatearFecha(resources, fechaInicio);
        if (!soloUnDia) {
            fechaDescripcion += " - " + FormatearFecha.formatearFecha(resources, fechaFin);
        }
        return fechaDescripcion;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_acceso_gastos:
                irEditarListaGastosApatxa();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_detalle_apatxa_con_reparto);
    }

    @Override
    protected void inicializarServicios() {
        super.inicializarServicios();
        personaService = new PersonaService(this);
    }

    @Override
    protected void cargarElementosLayout() {
        super.cargarElementosLayout();
        personasRepartoListView = (ListView) findViewById(R.id.listaDesgloseRepartoApatxa);
        personasRepartoListView.addHeaderView(headerInformacionDetalle, null, false);

        ViewGroup cabeceraTituloReparto = (ViewGroup) getLayoutInflater().inflate(R.layout.detalle_apatxa_lista_resultado_reparto_header, null);
        personasRepartoListView.addHeaderView(cabeceraTituloReparto, null, false);
        tituloRepartoApatxaListViewHeader = (TextView) cabeceraTituloReparto.findViewById(R.id.listaRepartoApatxaCabecera);

        numeroPersonasTextView.setVisibility(View.GONE);
        headerInformacionDetalle.findViewById(R.id.separador2DetalleApatxa).setVisibility(View.GONE);
        findViewById(R.id.listaVaciaInfoSubactivity).setVisibility(View.GONE);
        personasRepartoListView.addHeaderView(getLayoutInflater().inflate(R.layout.subactivity_lista_vacia, null), null, false);


    }

    @Override
    protected void cargarInformacionApatxa() {
        super.cargarInformacionApatxa();
        cargarInformacionReparto();
        setShareIntent(getDefaultShareIntent());
    }


    private void cargarInformacionReparto() {
        List<PersonaListadoReparto> listaPersonasReparto = apatxaService.getResultadoReparto(idApatxa);

        personasRepartoListView.setAdapter(new ListaPersonasRepartoApatxaArrayAdapter(this, R.layout.lista_personas_resultado_reparto_row, listaPersonasReparto));
        listaPersonasRepartoApatxaArrayAdapter = (ListaPersonasRepartoApatxaArrayAdapter) ((HeaderViewListAdapter) personasRepartoListView.getAdapter()).getWrappedAdapter();
        actualizarTituloCabeceraListaReparto();

        modeCallback = new ModeCallback();
        asignarContextualActionBar(personasRepartoListView);

        gestionarListaVacia(listaPersonasRepartoApatxaArrayAdapter, false, R.string.lista_vacia_personas_reparto, null);

    }


    private void actualizarTituloCabeceraListaReparto() {
        List<GastoApatxaListado> gastosApatxa = apatxa.gastos;
        String gastoFormateado = FormatearNumero.aDinero(this, CalcularSumaTotalGastos.calcular(gastosApatxa));
        String titulo = resources.getQuantityString(R.plurals.titulo_cabecera_lista_reparto_detalle_apatxa, gastosApatxa.size(), gastosApatxa.size(), gastoFormateado, SettingsUtils.getMoneda(this));
        tituloRepartoApatxaListViewHeader.setText(titulo);
    }

    void irEditarListaGastosApatxa() {
        Intent intent = new Intent(this, ListaGastosApatxaActivity.class);
        intent.putExtra("personas", new ArrayList<>(apatxa.personas));
        intent.putExtra("idApatxa", apatxa.id);
        intent.putExtra("gastos", new ArrayList<>(apatxa.gastos));
        startActivityForResult(intent, EDITAR_INFORMACION_LISTA_GASTOS_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITAR_INFORMACION_BASICA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                cargarInformacionApatxa();
                MensajesToast.mostrarConfirmacionGuardado(this.getApplicationContext(), R.string.mensaje_confirmacion_apatxa_guardada);
            }
        }
        if (requestCode == EDITAR_INFORMACION_LISTA_GASTOS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                actualizarReparto();
                MensajesToast.mostrarConfirmacionGuardado(this.getApplicationContext(), R.string.mensaje_confirmacion_gastos_actualizados_reparto_actualizado);
            }
        }
        if (requestCode == EDITAR_INFORMACION_LISTA_PERSONAS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                actualizarReparto();
                MensajesToast.mostrarConfirmacionGuardado(this.getApplicationContext(), R.string.mensaje_confirmacion_personas_actualizadas_reparto_actualizado);

            }
        }
    }

    private void actualizarReparto() {
        apatxa = apatxaService.getApatxaDetalle(idApatxa);
        apatxaService.realizarReparto(apatxa);
        cargarInformacionApatxa();

    }


    private void asignarContextualActionBar(final ListView personasRepartoListView) {
        personasRepartoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        personasRepartoListView.setMultiChoiceModeListener(modeCallback);
        personasRepartoListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int numHeaders = ((HeaderViewListAdapter) personasRepartoListView.getAdapter()).getHeadersCount();
        boolean seleccionadoAnteriormente = listaPersonasRepartoApatxaArrayAdapter.getPersonasSeleccionadas().contains(listaPersonasRepartoApatxaArrayAdapter.getItem(position - numHeaders));
        personasRepartoListView.setItemChecked(position, !seleccionadoAnteriormente);
    }

    private final class ModeCallback implements AbsListView.MultiChoiceModeListener {

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            //ponemos -numHeaders porque tenemos header
            int numHeaders = ((HeaderViewListAdapter) personasRepartoListView.getAdapter()).getHeadersCount();
            listaPersonasRepartoApatxaArrayAdapter.toggleSeleccion(position - numHeaders, checked);
            mode.setTitle(resources.getQuantityString(R.plurals.seleccionadas, listaPersonasRepartoApatxaArrayAdapter.numeroPersonasSeleccionadas(), listaPersonasRepartoApatxaArrayAdapter.numeroPersonasSeleccionadas()));
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            actionMode = mode;
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu_reparto_apatxa, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            if (actionMode == null) {
                actionMode = mode;
            }
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_marcar_hecho:
                    marcarPersonasEstadoReparto(mode, true);
                    return true;
                case R.id.action_marcar_pendiente:
                    marcarPersonasEstadoReparto(mode, false);
                    return true;
                default:
                    return false;
            }
        }

        private void marcarPersonasEstadoReparto(ActionMode mode, boolean pagado) {
            personaService.marcarPersonasEstadoReparto(listaPersonasRepartoApatxaArrayAdapter.getPersonasSeleccionadas(), pagado);
            cargarInformacionApatxa();
            String mensaje = apatxa.personasPendientesPagarCobrar == 0 ? getResources().getString(R.string.mensaje_confirmacion_pagos_actualizados_sin_pendientes) : getResources().getQuantityString(R.plurals.mensaje_confirmacion_pagos_actualizados, apatxa.personasPendientesPagarCobrar, apatxa.personasPendientesPagarCobrar);
            MensajesToast.mostrarMensaje(getApplicationContext(), mensaje);
            mode.finish();
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            listaPersonasRepartoApatxaArrayAdapter.resetearSeleccion();
        }

    }


    private Uri generateExcel() {
        final String fileName = "apatxa_" + apatxa.nombre.replaceAll(" ", "_") + ".xls";

        //Saving file in external storage
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/apatxas");

        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        //file path
        File file = new File(directory, fileName);

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(SettingsUtils.getLocale(this));
        WritableWorkbook workbook;

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet(apatxa.nombre, 0);


            try {
                List<Long> idsPersonasOrdenadas = new ArrayList<Long>();
                addTitulo(sheet);
                final int filaInicio = 1;
                final int colInicio = 0;
                addContenido(sheet,idsPersonasOrdenadas, colInicio, filaInicio);
                addResultadoReparto(sheet,idsPersonasOrdenadas, filaInicio,colInicio);



            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(file);
    }

    private void addTitulo(WritableSheet sheet) throws WriteException {

    }

    private void addContenido(WritableSheet sheet, List<Long> idsPersonasOrdenadas, int colInicio, int filaInicio) throws WriteException {

        sheet.addCell(new Label(colInicio, filaInicio, "Gastos/Personas //TODO")); // column and row

        anadirPersonas(sheet,idsPersonasOrdenadas, filaInicio, colInicio+1);
        anadirGastos(sheet, idsPersonasOrdenadas, filaInicio+1, colInicio, colInicio+1);
    }

    private void anadirGastos(WritableSheet sheet, List<Long> idsPersonasOrdenadas, int filaInicio, int colInicio, int columnaInicioPersonas) throws WriteException {
        int row = filaInicio;
        for (GastoApatxaListado gasto : apatxa.gastos) {
            sheet.addCell(new Label(colInicio, row, gasto.concepto));
            int posicionPersonaPagado = getColumnaPersonaExcel(idsPersonasOrdenadas, gasto.idPagadoPor, columnaInicioPersonas);
            sheet.addCell(new Number(posicionPersonaPagado,row, gasto.total));
            row++;
        }
    }

    private void anadirPersonas(WritableSheet sheet, List<Long> idsPersonasOrdenadas,int filaInicio, int colInicio) throws WriteException {
        int col = colInicio;

        for (PersonaListado persona : apatxa.personas) {
            System.out.println(" -> "+persona.nombre+" "+col+","+filaInicio);
            sheet.addCell(new Label(col++, filaInicio, persona.nombre));
            idsPersonasOrdenadas.add(persona.id);
        }
        sheet.addCell(new Label(col++, filaInicio, "SIN PAGAR //TODO"));
    }

    private void addResultadoReparto(WritableSheet sheet, List<Long> idsPersonasOrdenadas, int filaInicio, int columnaInicioPersonas) throws WriteException {
        List<PersonaListadoReparto> listaPersonasReparto = apatxaService.getResultadoReparto(idApatxa);
        for (PersonaListadoReparto reparto : listaPersonasReparto){
            int fila = filaInicio + apatxa.gastos.size()+3;
            int col = getColumnaPersonaExcel(idsPersonasOrdenadas,reparto.id, columnaInicioPersonas );
            sheet.addCell(new Number(col,fila, reparto.cantidadPago));
        }
    }

    private int getColumnaPersonaExcel(List<Long> lista, Long elto, int columnaInicioPersonas) {
        if (lista.contains(elto)) {
            return lista.indexOf(elto) +  columnaInicioPersonas;
        } else {
            return lista.size() +  columnaInicioPersonas;
        }
    }


}
