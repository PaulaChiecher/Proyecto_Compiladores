package com.ejemplo;

import com.ejemplo.AST.*;

/** Calcula el tipo de una expresion (se usa en las etapas posteriores al chequeo semantico). */
public class TipoExpr {
    private TipoExpr() { }

    public static TipoNodo de(Expr e, TablaSimbolos tabla) {
        if (e instanceof Num) return TipoNodo.INT;
        if (e instanceof Bool) return TipoNodo.BOOL;
        if (e instanceof Var v) return tabla.tipoDe(v.nombre);
        if (e instanceof Suma s) return de(s.izq, tabla);   // int+int -> int ; bool+bool -> bool
        if (e instanceof Mult) return TipoNodo.INT;
        throw new RuntimeException("Expresion desconocida");
    }
}