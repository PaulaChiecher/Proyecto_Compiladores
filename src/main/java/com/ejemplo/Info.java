package com.ejemplo;

/** Informacion que la tabla de simbolos guarda de cada variable declarada. */
public class Info {
    public final TipoNodo tipo;
    public Object valor;            // valor actual (lo usa el interprete)
    public boolean inicializada;    // true cuando ya se le asigno algo

    public Info(TipoNodo tipo) {
        this.tipo = tipo;
        // Valor inicial por defecto (igual que en el assembly): 0 para int, false para bool
        this.valor = (tipo == TipoNodo.BOOL) ? (Object) Boolean.FALSE : (Object) Integer.valueOf(0);
        this.inicializada = true;
    }
}
