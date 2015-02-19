package com.jagusan.apatxas.logicaNegocio.servicios;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.jagusan.apatxas.R;
import com.jagusan.apatxas.logicaNegocio.cursorReader.ExtraerInformacionContactoDeCursor;
import com.jagusan.apatxas.modelView.ContactoListado;

import java.util.ArrayList;
import java.util.List;

public class ContactoService {

    private final static String[] COLUMNAS = {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY, ContactsContract.Contacts.PHOTO_URI};
    private final static String WHERE_TIENEN_TELEFONO = ContactsContract.Contacts.HAS_PHONE_NUMBER + "= 1";
    private Context context;

    public ContactoService(Context context) {
        this.context = context;
    }


    public List<ContactoListado> obtenerTodosContactosTelefono(List<Long> idsContactosYaSeleccionados) {
        List<ContactoListado> contactos = new ArrayList<>();
        String AND_NO_ESTAN_EN_LISTA_SELECCIONADOS = " and " + ContactsContract.Contacts._ID + " not in (" + TextUtils.join(",", idsContactosYaSeleccionados) + ")";
        String WHERE = idsContactosYaSeleccionados.isEmpty() ? WHERE_TIENEN_TELEFONO : WHERE_TIENEN_TELEFONO + AND_NO_ESTAN_EN_LISTA_SELECCIONADOS;
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, COLUMNAS, WHERE, null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " COLLATE LOCALIZED ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ContactoListado contacto = new ContactoListado();
            contacto = ExtraerInformacionContactoDeCursor.extraer(cursor, contacto);
            contactos.add(contacto);
            cursor.moveToNext();
        }
        cursor.close();
        if (!idsContactosYaSeleccionados.contains(Long.MIN_VALUE)) {
            ContactoListado yo = new ContactoListado();
            yo.nombre = context.getString(R.string.yo_mayusculas);
            yo.id = Long.MIN_VALUE;
            contactos.add(0, yo);
        }
        return contactos;
    }


}
