package com.ejemplo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/** Tabla de simbolos: nombre de variable -> Info. */
public class TablaSimbolos {
    private final Map<String, Info> simbolos = new LinkedHashMap<>();

    public void declarar(String nombre, TipoNodo tipo) {
        if (simbolos.containsKey(nombre))
            throw new RuntimeException("Error semantico: variable ya declarada: " + nombre);
        simbolos.put(nombre, new Info(tipo));
    }

    public Info buscar(String nombre) {
        Info info = simbolos.get(nombre);
        if (info == null)
            throw new RuntimeException("Error semantico: variable no declarada: " + nombre);
        return info;
    }

    public TipoNodo tipoDe(String nombre) {
        return buscar(nombre).tipo;
    }

    public void asignar(String nombre, Object valor) {
        Info info = buscar(nombre);
        info.valor = valor;
        info.inicializada = true;
    }

    public Object valorDe(String nombre) {
        Info info = buscar(nombre);
        if (!info.inicializada)
            throw new RuntimeException("Error de ejecucion: variable sin inicializar: " + nombre);
        return info.valor;
    }

    public Set<String> nombres() {
        return simbolos.keySet();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Info> e : simbolos.entrySet())
            sb.append("    ").append(e.getValue().tipo).append(" ").append(e.getKey()).append("\n");
        return sb.toString();
    }
}
