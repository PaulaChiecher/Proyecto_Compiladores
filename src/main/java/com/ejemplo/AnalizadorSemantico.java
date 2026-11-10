package com.ejemplo;

import com.ejemplo.AST.*;

/** Chequeo semantico: variables declaradas una sola vez, usadas despues de declararse, y tipos. */
public class AnalizadorSemantico {
    private final TablaSimbolos tabla = new TablaSimbolos();

    /** Chequea el programa y devuelve la tabla de simbolos. Lanza RuntimeException ante un error. */
    public TablaSimbolos analizar(Programa p) {
        for (Decl d : p.decls) tabla.declarar(d.nombre, d.tipo);

        for (Sent s : p.sents) {
            if (s instanceof Asig a) {
                TipoNodo td = tabla.tipoDe(a.nombre);
                TipoNodo te = tipo(a.expr);
                if (td != te)
                    throw new RuntimeException("Error semantico: tipos incompatibles en la asignacion a "
                            + a.nombre + " (" + td + " = " + te + ")");
            } else if (s instanceof Print pr) {
                tipo(pr.expr);
            } else if (s instanceof Ret r) {
                if (r.expr == null) {
                    if (p.tipoRet != TipoNodo.VOID)
                        throw new RuntimeException("Error semantico: return sin valor en un main " + p.tipoRet);
                } else {
                    if (p.tipoRet == TipoNodo.VOID)
                        throw new RuntimeException("Error semantico: return con valor en un main void");
                    if (tipo(r.expr) != p.tipoRet)
                        throw new RuntimeException("Error semantico: el return no coincide con el tipo de main ("
                                + p.tipoRet + ")");
                }
            }
        }
        return tabla;
    }

    private TipoNodo tipo(Expr e) {
        if (e instanceof Num) return TipoNodo.INT;
        if (e instanceof Bool) return TipoNodo.BOOL;
        if (e instanceof Var v) return tabla.tipoDe(v.nombre);
        if (e instanceof Suma s) { exigirInt(s.izq, "+"); exigirInt(s.der, "+"); return TipoNodo.INT; }
        if (e instanceof Mult m) { exigirInt(m.izq, "*"); exigirInt(m.der, "*"); return TipoNodo.INT; }
        throw new RuntimeException("Error semantico: expresion desconocida");
    }

    private void exigirInt(Expr e, String op) {
        if (tipo(e) != TipoNodo.INT)
            throw new RuntimeException("Error semantico: el operador " + op + " solo se aplica a int: " + e);
    }
}
