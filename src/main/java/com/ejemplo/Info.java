package com.ejemplo;

/** Informacion que la tabla de simbolos guarda de cada variable declarada. */
public class Info {
    public final TipoNodo tipo;
    public Object valor;            // valor actual (lo usa el interprete)
    public boolean inicializada;    // true cuando ya se le asigno algo

    public Info(TipoNodo tipo) {
        this.tipo = tipo;
        this.valor = null;
        this.inicializada = false;
    }
}
