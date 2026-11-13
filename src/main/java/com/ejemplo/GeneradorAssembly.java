package com.ejemplo;

import com.ejemplo.AST.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Generacion de assembly. Dos salidas:
 *  - generarPseudo: pseudo-assembly de maquina de pila (directo desde el AST).
 *  - generarX86:    assembly x86-64 real (sintaxis AT&T, GNU as) a partir del codigo de tres direcciones.
 */
public class GeneradorAssembly {

    /* ===================== Pseudo-assembly de pila ===================== */

    private final TablaSimbolos tabla;   // para saber si una suma es de int o de bool

    public GeneradorAssembly(TablaSimbolos tabla) {
        this.tabla = tabla;
    }

    private int etiquetas;               // contador de etiquetas del pseudo-assembly

    private StringBuilder ps;

    public String generarPseudo(Programa p) {
        ps = new StringBuilder();
        ps.append("; variables\n");
        for (Decl d : p.decls) ps.append("VAR ").append(d.nombre).append("\n");
        ps.append("; codigo\n");
        etiquetas = 0;
        pseudoBloque(p.sents);
        ps.append("HALT\n");
        return ps.toString();
    }

    /** Pseudo-assembly de una lista de sentencias (main, o el bloque de un if / while). */
    private void pseudoBloque(List<Sent> sents) {
        for (Sent s : sents) {
            if (s instanceof Asig a) {
                ps.append("PUSHA ").append(a.nombre).append("\n");
                pseudo(a.expr);
                ps.append("STORE\n");
            } else if (s instanceof Print pr) {
                pseudo(pr.expr);
                ps.append("PRINT\n");
            } else if (s instanceof Ret r) {
                if (r.expr != null) pseudo(r.expr);
                ps.append("RET\n");
            } else if (s instanceof If i) {
                String lelse = (i.sino != null) ? "L" + (++etiquetas) : null;
                String lfin = "L" + (++etiquetas);
                pseudo(i.cond);
                if (i.sino == null) {
                    ps.append("IFFALSO ").append(lfin).append("\n");
                    pseudoBloque(i.entonces);
                } else {
                    ps.append("IFFALSO ").append(lelse).append("\n");
                    pseudoBloque(i.entonces);
                    ps.append("SALTO ").append(lfin).append("\n");
                    ps.append("LABEL ").append(lelse).append("\n");
                    pseudoBloque(i.sino);
                }
                ps.append("LABEL ").append(lfin).append("\n");
            } else if (s instanceof While w) {
                String linicio = "L" + (++etiquetas);
                String lfin = "L" + (++etiquetas);
                ps.append("LABEL ").append(linicio).append("\n");
                pseudo(w.cond);
                ps.append("IFFALSO ").append(lfin).append("\n");
                pseudoBloque(w.cuerpo);
                ps.append("SALTO ").append(linicio).append("\n");
                ps.append("LABEL ").append(lfin).append("\n");
            }
        }
    }

    private void pseudo(Expr e) {
        if (e instanceof Num n) ps.append("PUSHC ").append(n.valor).append("\n");
        else if (e instanceof Bool b) ps.append("PUSHC ").append(b.valor ? 1 : 0).append("\n");
        else if (e instanceof Var v) ps.append("PUSHA ").append(v.nombre).append("\nLOAD\n");
        else if (e instanceof Suma s) {
            pseudo(s.izq); pseudo(s.der);
            ps.append(TipoExpr.de(s, tabla) == TipoNodo.BOOL ? "OR\n" : "ADD\n");
        }
        else if (e instanceof Mult m) { pseudo(m.izq); pseudo(m.der); ps.append("MUL\n"); }
    }

    /* ===================== x86-64 (AT&T) ===================== */

    private StringBuilder asm;
    private final Map<String, Integer> pila = new LinkedHashMap<>();   // nombre -> desplazamiento respecto de %rbp

    public String generarX86(Programa p, List<InstruccionC3D> codigo) {
        asm = new StringBuilder();
        pila.clear();

        // Cada variable declarada y cada temporal tiene un lugar de 4 bytes en la pila.
        for (Decl d : p.decls) reservar(d.nombre);
        for (InstruccionC3D ins : codigo) if (ins.resultado != null) reservar(ins.resultado);
        int tamanio = ((pila.size() * 4 + 15) / 16) * 16;   // la pila se mantiene alineada a 16 bytes

        linea("    .section .rodata");
        linea(".LC_int:");
        linea("    .string \"%d\\n\"");
        linea(".LC_str:");
        linea("    .string \"%s\\n\"");
        linea(".LC_true:");
        linea("    .string \"true\"");
        linea(".LC_false:");
        linea("    .string \"false\"");
        linea("");
        linea("    .text");
        linea("    .globl  main");
        linea("    .type   main, @function");
        linea("main:");
        linea("    pushq   %rbp");
        linea("    movq    %rsp, %rbp");
        if (tamanio > 0) linea("    subq    $" + tamanio + ", %rsp");
        for (Decl d : p.decls) linea("    movl    $0, " + dir(d.nombre));

        for (InstruccionC3D ins : codigo) {
            linea("");
            linea("    # " + ins);
            switch (ins.op) {
                case ASIGNAR:
                    linea("    movl    " + operando(ins.arg1) + ", %eax");
                    linea("    movl    %eax, " + dir(ins.resultado));
                    break;
                case SUMA:
                    linea("    movl    " + operando(ins.arg1) + ", %eax");
                    linea("    addl    " + operando(ins.arg2) + ", %eax");
                    linea("    movl    %eax, " + dir(ins.resultado));
                    break;
                case OR:
                    linea("    movl    " + operando(ins.arg1) + ", %eax");
                    linea("    orl     " + operando(ins.arg2) + ", %eax");
                    linea("    movl    %eax, " + dir(ins.resultado));
                    break;
                case MULT:
                    linea("    movl    " + operando(ins.arg1) + ", %eax");
                    linea("    imull   " + operando(ins.arg2) + ", %eax");
                    linea("    movl    %eax, " + dir(ins.resultado));
                    break;
                case PRINT_INT:
                    linea("    movl    " + operando(ins.arg1) + ", %esi");
                    linea("    leaq    .LC_int(%rip), %rdi");
                    linea("    movl    $0, %eax");
                    linea("    call    printf@PLT");
                    break;
                case PRINT_BOOL:
                    linea("    movl    " + operando(ins.arg1) + ", %eax");
                    linea("    cmpl    $0, %eax");
                    linea("    leaq    .LC_false(%rip), %rsi");
                    linea("    leaq    .LC_true(%rip), %rcx");
                    linea("    cmovne  %rcx, %rsi");
                    linea("    leaq    .LC_str(%rip), %rdi");
                    linea("    movl    $0, %eax");
                    linea("    call    printf@PLT");
                    break;
                case LABEL:
                    linea("." + ins.arg1 + ":");
                    break;
                case SALTO:
                    linea("    jmp     ." + ins.arg1);
                    break;
                case IFFALSO:
                    linea("    movl    " + operando(ins.arg1) + ", %eax");
                    linea("    cmpl    $0, %eax");
                    linea("    je      ." + ins.arg2);
                    break;
                case RETURN:
                    if (ins.arg1 != null) linea("    movl    " + operando(ins.arg1) + ", %eax");
                    else linea("    movl    $0, %eax");
                    linea("    leave");
                    linea("    ret");
                    break;
            }
        }

        linea("");
        linea("    # fin del programa");
        linea("    movl    $0, %eax");
        linea("    leave");
        linea("    ret");
        linea("    .size   main, .-main");
        linea("    .section .note.GNU-stack,\"\",@progbits");
        return asm.toString();
    }

    private void reservar(String nombre) {
        if (!pila.containsKey(nombre)) pila.put(nombre, -4 * (pila.size() + 1));
    }

    private String dir(String nombre) {
        return pila.get(nombre) + "(%rbp)";
    }

    /** Constante -> $valor ; variable o temporal -> su lugar en la pila. */
    private String operando(String arg) {
        if (arg.matches("-?\\d+")) return "$" + arg;
        return dir(arg);
    }

    private void linea(String s) {
        asm.append(s).append("\n");
    }
}
