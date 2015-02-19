package com.jagusan.apatxas.utils;

import com.jagusan.apatxas.modelView.PersonaListado;

import java.util.ArrayList;
import java.util.List;


public class RecupararInformacionPersonas {

    public static List<Long> obtenerIdsContactos(List<PersonaListado> listaPersonas) {
        List<Long> listaIdsContactos = new ArrayList<>();
        for (PersonaListado persona : listaPersonas) {
            if (persona.idContacto != null) {
                listaIdsContactos.add(persona.idContacto);
            }
        }
        return listaIdsContactos;
    }
}
