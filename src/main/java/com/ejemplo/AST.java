package com.ejemplo;

import java.util.ArrayList;

/** Nodos del Arbol Sintactico Abstracto (AST). */
public class AST {
    private AST() { }

    /* ---------- Expresiones ---------- */
    public static abstract class Expr { }

    public static class Num extends Expr {
        public final int valor;
        public Num(int valor) { this.valor = valor; }
        public String toString() { return "Num(" + valor + ")"; }
    }

    public static class Bool extends Expr {
        public final boolean valor;
        public Bool(boolean valor) { this.valor = valor; }
        public String toString() { return "Bool(" + valor + ")"; }
    }

    public static class Var extends Expr {
        public final String nombre;
        public Var(String nombre) { this.nombre = nombre; }
        public String toString() { return "Var(" + nombre + ")"; }
    }

    public static class Suma extends Expr {
        public final Expr izq, der;
        public Suma(Expr izq, Expr der) { this.izq = izq; this.der = der; }
        public String toString() { return "Suma(" + izq + ", " + der + ")"; }
    }

    public static class Mult extends Expr {
        public final Expr izq, der;
        public Mult(Expr izq, Expr der) { this.izq = izq; this.der = der; }
        public String toString() { return "Mult(" + izq + ", " + der + ")"; }
    }

    /* ---------- Sentencias ---------- */
    public static abstract class Sent { }

    public static class Asig extends Sent {
        public final String nombre;
        public final Expr expr;
        public Asig(String nombre, Expr expr) { this.nombre = nombre; this.expr = expr; }
        public String toString() { return nombre + " = " + expr; }
    }

    public static class Print extends Sent {
        public final Expr expr;
        public Print(Expr expr) { this.expr = expr; }
        public String toString() { return "print " + expr; }
    }

    public static class Ret extends Sent {
        public final Expr expr;   // null si es "return;"
        public Ret(Expr expr) { this.expr = expr; }
        public String toString() { return expr == null ? "return" : "return " + expr; }
    }

    /* ---------- Declaraciones y programa ---------- */
    public static class Decl {
        public final TipoNodo tipo;
        public final String nombre;
        public Decl(TipoNodo tipo, String nombre) { this.tipo = tipo; this.nombre = nombre; }
        public String toString() { return tipo + " " + nombre; }
    }

    public static class ListaDecl extends ArrayList<Decl> { }
    public static class ListaSent extends ArrayList<Sent> { }

    public static class Programa {
        public final TipoNodo tipoRet;
        public final ListaDecl decls;
        public final ListaSent sents;
        public Programa(TipoNodo tipoRet, ListaDecl decls, ListaSent sents) {
            this.tipoRet = tipoRet; this.decls = decls; this.sents = sents;
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Programa (retorna ").append(tipoRet).append(")\n");
            sb.append("  Declaraciones:\n");
            for (Decl d : decls) sb.append("    ").append(d).append("\n");
            sb.append("  Sentencias:\n");
            for (Sent s : sents) sb.append("    ").append(s).append("\n");
            return sb.toString();
        }
    }
}
