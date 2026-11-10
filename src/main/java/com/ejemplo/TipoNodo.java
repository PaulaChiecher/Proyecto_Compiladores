package com.ejemplo;

/** Tipos del lenguaje. */
public enum TipoNodo {
    INT("int"), BOOL("bool"), VOID("void");

    private final String nombre;

    TipoNodo(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() { return nombre; }
}
