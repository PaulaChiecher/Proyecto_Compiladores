package com.ejemplo;

import com.ejemplo.AST.*;

/** Interprete: evalua el AST directamente, usando la tabla de simbolos como memoria. */
public class Interprete {
    private final TablaSimbolos tabla;
    public Object resultado = null;

    public Interprete(TablaSimbolos tabla) {
        this.tabla = tabla;
    }

    public void ejecutar(Programa p) {
        for (Sent s : p.sents) {
            if (s instanceof Asig a) {
                tabla.asignar(a.nombre, eval(a.expr));
            } else if (s instanceof Print pr) {
                System.out.println(eval(pr.expr));
            } else if (s instanceof Ret r) {
                resultado = (r.expr == null) ? null : eval(r.expr);
                return;   // el return corta la ejecucion
            }
        }
    }

    private Object eval(Expr e) {
        if (e instanceof Num n) return n.valor;
        if (e instanceof Bool b) return b.valor;
        if (e instanceof Var v) return tabla.valorDe(v.nombre);
        if (e instanceof Suma s) return (int) eval(s.izq) + (int) eval(s.der);
        if (e instanceof Mult m) return (int) eval(m.izq) * (int) eval(m.der);
        throw new RuntimeException("Expresion desconocida");
    }
}
