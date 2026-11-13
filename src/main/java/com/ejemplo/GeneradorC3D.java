package com.ejemplo;

import com.ejemplo.AST.*;
import java.util.ArrayList;
import java.util.List;

/** AST -> codigo intermedio de tres direcciones (temporales _t1, _t2, ... y etiquetas L1, L2, ...). */
public class GeneradorC3D {
    private final TablaSimbolos tabla;
    private final List<InstruccionC3D> codigo = new ArrayList<>();
    private int contadorTemporales = 0;
    private int contadorEtiquetas = 0;

    public GeneradorC3D(TablaSimbolos tabla) {
        this.tabla = tabla;
    }

    public List<InstruccionC3D> generar(Programa p) {
        generarBloque(p.sents);
        return codigo;
    }

    /** Genera el codigo de una lista de sentencias (main, o el bloque de un if / while). */
    private void generarBloque(List<Sent> sents) {
        for (Sent s : sents) {
            if (s instanceof Asig a) {
                String lugar = gen(a.expr);
                emitir(OperacionC3D.ASIGNAR, lugar, null, a.nombre);
            } else if (s instanceof Print pr) {
                String lugar = gen(pr.expr);
                emitir(esBool(pr.expr) ? OperacionC3D.PRINT_BOOL : OperacionC3D.PRINT_INT, lugar, null, null);
            } else if (s instanceof Ret r) {
                String lugar = (r.expr != null) ? gen(r.expr) : null;
                emitir(OperacionC3D.RETURN, lugar, null, null);
                return;   // lo que viene despues de un return, en este bloque, no se ejecuta
            } else if (s instanceof If i) {
                String lelse = (i.sino != null) ? nuevaEtiqueta() : null;
                String lfin = nuevaEtiqueta();
                String cond = gen(i.cond);
                if (i.sino == null) {
                    emitir(OperacionC3D.IFFALSO, cond, lfin, null);   // si es falso, salta al final
                    generarBloque(i.entonces);
                } else {
                    emitir(OperacionC3D.IFFALSO, cond, lelse, null);  // si es falso, salta al else
                    generarBloque(i.entonces);
                    emitir(OperacionC3D.SALTO, lfin, null, null);     // evita ejecutar el else
                    emitir(OperacionC3D.LABEL, lelse, null, null);
                    generarBloque(i.sino);
                }
                emitir(OperacionC3D.LABEL, lfin, null, null);
            } else if (s instanceof While w) {
                String linicio = nuevaEtiqueta();
                String lfin = nuevaEtiqueta();
                emitir(OperacionC3D.LABEL, linicio, null, null);
                String cond = gen(w.cond);                            // la condicion se evalua en cada vuelta
                emitir(OperacionC3D.IFFALSO, cond, lfin, null);
                generarBloque(w.cuerpo);
                emitir(OperacionC3D.SALTO, linicio, null, null);      // vuelve a evaluar la condicion
                emitir(OperacionC3D.LABEL, lfin, null, null);
            }
        }
    }

    /** Genera el codigo de una expresion y devuelve donde queda su valor (constante, variable o temporal). */
    private String gen(Expr e) {
        if (e instanceof Num n) return String.valueOf(n.valor);
        if (e instanceof Bool b) return b.valor ? "1" : "0";
        if (e instanceof Var v) return v.nombre;
        if (e instanceof Suma s) return binaria(esBool(s) ? OperacionC3D.OR : OperacionC3D.SUMA, s.izq, s.der);
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

    private String nuevaEtiqueta() {
        return "L" + (++contadorEtiquetas);
    }

    private boolean esBool(Expr e) {
        return TipoExpr.de(e, tabla) == TipoNodo.BOOL;
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