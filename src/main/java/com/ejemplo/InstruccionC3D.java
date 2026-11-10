package com.ejemplo;

/** Una instruccion de tres direcciones: resultado = arg1 op arg2. */
public class InstruccionC3D {
    public final OperacionC3D op;
    public final String arg1, arg2, resultado;

    public InstruccionC3D(OperacionC3D op, String arg1, String arg2, String resultado) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        switch (op) {
            case ASIGNAR:    return resultado + " = " + arg1;
            case SUMA:       return resultado + " = " + arg1 + " + " + arg2;
            case MULT:       return resultado + " = " + arg1 + " * " + arg2;
            case PRINT_INT:  return "print " + arg1;
            case PRINT_BOOL: return "print_bool " + arg1;
            case RETURN:     return arg1 == null ? "return" : "return " + arg1;
        }
        return "?";
    }
}
