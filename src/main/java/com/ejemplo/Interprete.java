package com.ejemplo;

import com.ejemplo.AST.*;
import java.util.List;

/** Interprete: evalua el AST directamente, usando la tabla de simbolos como memoria. */
public class Interprete {
    private static final int MAX_ITERACIONES = 1_000_000;   // proteccion contra bucles infinitos

    private final TablaSimbolos tabla;
    private int iteraciones = 0;
    public Object resultado = null;

    public Interprete(TablaSimbolos tabla) {
        this.tabla = tabla;
    }

    public void ejecutar(Programa p) {
        ejecutarBloque(p.sents);
    }

    /** Ejecuta las sentencias en orden. Devuelve true si se ejecuto un return (hay que cortar todo). */
    private boolean ejecutarBloque(List<Sent> sents) {
        for (Sent s : sents) {
            if (s instanceof Asig a) {
                tabla.asignar(a.nombre, eval(a.expr));
            } else if (s instanceof Print pr) {
                System.out.println(eval(pr.expr));
            } else if (s instanceof Ret r) {
                resultado = (r.expr == null) ? null : eval(r.expr);
                return true;
            } else if (s instanceof If i) {
                if ((boolean) eval(i.cond)) {
                    if (ejecutarBloque(i.entonces)) return true;
                } else if (i.sino != null) {
                    if (ejecutarBloque(i.sino)) return true;
                }
            } else if (s instanceof While w) {
                while ((boolean) eval(w.cond)) {
                    if (++iteraciones > MAX_ITERACIONES)
                        throw new RuntimeException("Error de ejecucion: posible bucle infinito (mas de "
                                + MAX_ITERACIONES + " iteraciones)");
                    if (ejecutarBloque(w.cuerpo)) return true;
                }
            }
        }
        return false;
    }

    private Object eval(Expr e) {
        if (e instanceof Num n) return n.valor;
        if (e instanceof Bool b) return b.valor;
        if (e instanceof Var v) return tabla.valorDe(v.nombre);
        if (e instanceof Suma s) {
            Object a = eval(s.izq), b = eval(s.der);
            if (a instanceof Boolean x && b instanceof Boolean y) return x || y;   // bool + bool = OR
            return (int) a + (int) b;
        }
        if (e instanceof Mult m) return (int) eval(m.izq) * (int) eval(m.der);
        throw new RuntimeException("Expresion desconocida");
    }
}