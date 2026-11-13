package com.ejemplo;

import com.ejemplo.AST.*;

/** Chequeo semantico: variables declaradas una sola vez, usadas despues de declararse, y tipos. */
public class AnalizadorSemantico {
    private final TablaSimbolos tabla = new TablaSimbolos();
    private TipoNodo tipoRet;   // tipo que devuelve main

    /** Chequea el programa y devuelve la tabla de simbolos. Lanza RuntimeException ante un error. */
    public TablaSimbolos analizar(Programa p) {
        tipoRet = p.tipoRet;
        for (Decl d : p.decls) tabla.declarar(d.nombre, d.tipo);
        chequearBloque(p.sents);
        return tabla;
    }

    /** Chequea una lista de sentencias (el cuerpo de main, o el bloque de un if / while). */
    private void chequearBloque(ListaSent sents) {
        for (Sent s : sents) {
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
                    if (tipoRet != TipoNodo.VOID)
                        throw new RuntimeException("Error semantico: return sin valor en un main " + tipoRet);
                } else {
                    if (tipoRet == TipoNodo.VOID)
                        throw new RuntimeException("Error semantico: return con valor en un main void");
                    if (tipo(r.expr) != tipoRet)
                        throw new RuntimeException("Error semantico: el return no coincide con el tipo de main ("
                                + tipoRet + ")");
                }
            } else if (s instanceof If i) {
                exigirBool(i.cond, "if");
                chequearBloque(i.entonces);
                if (i.sino != null) chequearBloque(i.sino);
            } else if (s instanceof While w) {
                exigirBool(w.cond, "while");
                chequearBloque(w.cuerpo);
            }
        }
    }

    private TipoNodo tipo(Expr e) {
        if (e instanceof Num) return TipoNodo.INT;
        if (e instanceof Bool) return TipoNodo.BOOL;
        if (e instanceof Var v) return tabla.tipoDe(v.nombre);
        if (e instanceof Suma s) {
            TipoNodo ti = tipo(s.izq), td = tipo(s.der);
            if (ti != td)
                throw new RuntimeException("Error semantico: el operador + no mezcla tipos (" + ti + " + " + td + ")");
            return ti;   // int + int -> int ; bool + bool -> bool (OR logico)
        }
        if (e instanceof Mult m) { exigirInt(m.izq, "*"); exigirInt(m.der, "*"); return TipoNodo.INT; }
        throw new RuntimeException("Error semantico: expresion desconocida");
    }

    private void exigirInt(Expr e, String op) {
        if (tipo(e) != TipoNodo.INT)
            throw new RuntimeException("Error semantico: el operador " + op + " solo se aplica a int: " + e);
    }

    private void exigirBool(Expr e, String sentencia) {
        TipoNodo t = tipo(e);
        if (t != TipoNodo.BOOL)
            throw new RuntimeException("Error semantico: la condicion del " + sentencia + " debe ser bool (es " + t + ")");
    }
}