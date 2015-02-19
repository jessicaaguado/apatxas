package com.jagusan.apatxas.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.modelView.PersonaListado;

public class NuevoGastoApatxaActivity extends GastoApatxaActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nuevo_gasto_apatxa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_asociar_gasto_apatxa:
                if (validacionesCorrectas()) {
                    String concepto = getConceptoIntroducido();
                    Double totalGasto = getImporteIntroducido();
                    PersonaListado pagador = getPagadorSeleccionado();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("concepto", concepto);
                    returnIntent.putExtra("total", totalGasto);
                    returnIntent.putExtra("pagadoPor", pagador);
                    setResult(RESULT_OK, returnIntent);

                    finish();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    protected void personalizarActionBar() {
        super.personalizarActionBar(R.string.title_activity_nuevo_gasto, MostrarTituloPantalla.NUEVO_GASTO);
    }

}
