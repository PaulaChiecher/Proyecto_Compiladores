package com.ejemplo;

/** Operaciones del codigo intermedio de tres direcciones. */
public enum OperacionC3D {
    ASIGNAR,      // resultado = arg1
    SUMA,         // resultado = arg1 + arg2
    MULT,         // resultado = arg1 * arg2
    OR,           // resultado = arg1 or arg2 (suma de booleanos)
    PRINT_INT,    // print arg1 (entero)
    PRINT_BOOL,   // print arg1 (true/false)
    RETURN,        // return arg1   (arg1 == null si es "return;")
    LABEL,        // define la etiqueta arg1
    SALTO,        // goto arg1 (salto incondicional)
    IFFALSO       // si arg1 es falso (0), salta a la etiqueta arg2
}
