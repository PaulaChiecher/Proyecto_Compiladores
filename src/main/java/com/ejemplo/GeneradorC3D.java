package com.ejemplo;

import com.ejemplo.AST.*;
import java.util.ArrayList;
import java.util.List;

/** AST -> codigo intermedio de tres direcciones (usa temporales _t1, _t2, ...). */
public class GeneradorC3D {
    private final TablaSimbolos tabla;
    private final List<InstruccionC3D> codigo = new ArrayList<>();
    private int contadorTemporales = 0;

    public GeneradorC3D(TablaSimbolos tabla) {
        this.tabla = tabla;
    }

    public List<InstruccionC3D> generar(Programa p) {
        for (Sent s : p.sents) {
            if (s instanceof Asig a) {
                String lugar = gen(a.expr);
                emitir(OperacionC3D.ASIGNAR, lugar, null, a.nombre);
            } else if (s instanceof Print pr) {
                String lugar = gen(pr.expr);
                emitir(esBool(pr.expr) ? OperacionC3D.PRINT_BOOL : OperacionC3D.PRINT_INT, lugar, null, null);
            } else if (s instanceof Ret r) {
                String lugar = (r.expr != null) ? gen(r.expr) : null;
                emitir(OperacionC3D.RETURN, lugar, null, null);
                break;   // lo que viene despues de un return no se ejecuta
            }
        }
        return codigo;
    }

    /** Genera el codigo de una expresion y devuelve donde queda su valor (constante, variable o temporal). */
    private String gen(Expr e) {
        if (e instanceof Num n) return String.valueOf(n.valor);
        if (e instanceof Bool b) return b.valor ? "1" : "0";
        if (e instanceof Var v) return v.nombre;
        if (e instanceof Suma s) return binaria(OperacionC3D.SUMA, s.izq, s.der);
        if (e instanceof Mult m) return binaria(OperacionC3D.MULT, m.izq, m.der);
        throw new RuntimeException("Expresion desconocida");
    }

    private String binaria(OperacionC3D op, Expr izq, Expr der) {
        String a = gen(izq);
        String b = gen(der);
        String t = "_t" + (++contadorTemporales);   // los ids del lenguaje no empiezan con _, no chocan
        emitir(op, a, b, t);
        return t;
    }

    private boolean esBool(Expr e) {
        if (e instanceof Bool) return true;
        if (e instanceof Var v) return tabla.tipoDe(v.nombre) == TipoNodo.BOOL;
        return false;
    }

    private void emitir(OperacionC3D op, String a1, String a2, String res) {
        codigo.add(new InstruccionC3D(op, a1, a2, res));
    }

    public String imprimir() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < codigo.size(); i++)
            sb.append(String.format("%3d: %s%n", i + 1, codigo.get(i)));
        return sb.toString();
    }
}
