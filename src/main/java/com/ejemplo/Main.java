package com.ejemplo;

import com.ejemplo.AST.Programa;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String ruta = args.length > 0 ? args[0] : "src/main/resources/entrada.txt";
        String rutaAsm = args.length > 1 ? args[1] : "salida.s";

        try {
            // 1. Analisis lexico + sintactico -> AST
            Lexer lexer = new Lexer(new FileReader(ruta));
            Parser parser = new Parser(lexer);
            Programa prog = (Programa) parser.parse().value;
            System.out.println("=== AST ===");
            System.out.print(prog);

            // 2. Analisis semantico + tabla de simbolos
            TablaSimbolos tabla = new AnalizadorSemantico().analizar(prog);
            System.out.println("=== Chequeo semantico: OK ===");
            System.out.println("=== Tabla de simbolos ===");
            System.out.print(tabla);

            // 3. Interprete
            System.out.println("=== Ejecucion (interprete) ===");
            Interprete in = new Interprete(tabla);
            in.ejecutar(prog);
            System.out.println("Valor retornado: " + in.resultado);

            // 4. Pseudo-assembly de pila
            GeneradorAssembly gen = new GeneradorAssembly();
            System.out.println("=== Pseudo-assembly (pila) ===");
            System.out.print(gen.generarPseudo(prog));

            // 5. Codigo intermedio de tres direcciones
            GeneradorC3D c3d = new GeneradorC3D(tabla);
            List<InstruccionC3D> codigo = c3d.generar(prog);
            System.out.println("=== Codigo intermedio (tres direcciones) ===");
            System.out.print(c3d.imprimir());

            // 6. Assembly x86-64
            String asm = gen.generarX86(prog, codigo);
            try (FileWriter fw = new FileWriter(rutaAsm)) { fw.write(asm); }
            System.out.println("=== Assembly x86-64 (guardado en " + rutaAsm + ") ===");
            System.out.print(asm);

        } catch (java.io.FileNotFoundException e) {
            System.out.println("No se encontro el archivo " + ruta);
        } catch (RuntimeException | Error e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error sintactico: " + e.getMessage());
        }
    }
}
