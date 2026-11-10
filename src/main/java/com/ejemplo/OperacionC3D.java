package com.ejemplo;

/** Operaciones del codigo intermedio de tres direcciones. */
public enum OperacionC3D {
    ASIGNAR,      // resultado = arg1
    SUMA,         // resultado = arg1 + arg2
    MULT,         // resultado = arg1 * arg2
    PRINT_INT,    // print arg1 (entero)
    PRINT_BOOL,   // print arg1 (true/false)
    RETURN        // return arg1   (arg1 == null si es "return;")
}
