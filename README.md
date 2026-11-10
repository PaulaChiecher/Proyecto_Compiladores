# Proyecto Compiladores — Guía paso a paso

- **Materia:** Compiladores — Ingeniería en Sistemas de Información — UNViMe
- **Docente:** Francisco Bavera
- **Integrante:** Paula Vanina Chiecher

Compilador para un mini lenguaje con declaraciones `int` / `bool`, asignaciones, `print`, `return` y los operadores `+` y `*`. Está hecho en **Java 21** con **JFlex** (análisis léxico) y **CUP** (análisis sintáctico). A partir de un programa fuente produce: AST, chequeo semántico con tabla de símbolos, ejecución con intérprete, pseudo-assembly de pila, código intermedio de tres direcciones y assembly x86-64.

---

## 1. Requisitos

| Herramienta | Para qué | Cómo verificar |
|---|---|---|
| JDK 21 (o superior) | compilar y ejecutar | `java -version` y `javac -version` |
| `gcc` en Linux o WSL (opcional) | ensamblar y correr el assembly x86-64 | `gcc --version` |
| Git (opcional) | versionar y subir a GitHub | `git --version` |

Los jars necesarios ya están en la carpeta `lib/`:

- `jflex-full-1.9.1.jar` — generador de analizadores léxicos
- `java-cup-11b-2015.03.26.jar` — generador de analizadores sintácticos
- `java-cup-11b-runtime.jar` — librería que necesita el parser al ejecutarse

## 2. Estructura del proyecto

```
Proyecto_Compiladores/
├── lib/                          jars de JFlex y CUP
├── src/main/
│   ├── jflex/Lexer.flex          especificación léxica (tokens)
│   ├── cup/parser.cup            gramática + construcción del AST
│   ├── resources/                programas de prueba (*.txt)
│   └── java/com/ejemplo/
│       ├── Lexer.java            (generado por JFlex)
│       ├── Parser.java, Sym.java (generados por CUP)
│       ├── AST.java              nodos del árbol sintáctico
│       ├── TipoNodo.java         tipos: int, bool, void
│       ├── Info.java             información de cada variable
│       ├── TablaSimbolos.java    tabla de símbolos
│       ├── AnalizadorSemantico.java
│       ├── Interprete.java
│       ├── GeneradorC3D.java, InstruccionC3D.java, OperacionC3D.java
│       ├── GeneradorAssembly.java
│       └── Main.java             ejecuta todas las etapas
├── generar.bat / generar.sh      regeneran Lexer, Parser y Sym
└── pom.xml                       configuración para IntelliJ / Maven
```

## 3. Paso a paso por terminal

Todos los comandos se ejecutan **desde la carpeta raíz del proyecto** (la que contiene `lib/` y `src/`).

### Paso 1 — Generar el analizador léxico con JFlex

Convierte `Lexer.flex` en `Lexer.java`.

```
java -jar lib/jflex-full-1.9.1.jar -d src/main/java/com/ejemplo src/main/jflex/Lexer.flex
```

### Paso 2 — Generar el analizador sintáctico con CUP

Convierte `parser.cup` en `Parser.java` (el parser) y `Sym.java` (las constantes de los tokens).

```
java -cp lib/java-cup-11b-2015.03.26.jar java_cup.Main -destdir src/main/java/com/ejemplo -parser Parser -symbols Sym src/main/cup/parser.cup
```

CUP informa `0 conflicts detected`: la gramática no tiene conflictos shift/reduce. Los pasos 1 y 2 solo hay que repetirlos si se modifica `Lexer.flex` o `parser.cup`; los scripts `generar.bat` (Windows) y `generar.sh` (Linux) hacen ambos.

### Paso 3 — Compilar todo el código Java

Windows (cmd o PowerShell):

```
mkdir out
javac -cp lib\java-cup-11b-runtime.jar -d out src\main\java\com\ejemplo\*.java
```

Linux / macOS / WSL:

```
mkdir -p out
javac -cp lib/java-cup-11b-runtime.jar -d out src/main/java/com/ejemplo/*.java
```

### Paso 4 — Ejecutar el compilador

Recibe como argumento el programa fuente (y, opcionalmente, el nombre del archivo assembly de salida).

Windows:

```
java -cp "out;lib\java-cup-11b-runtime.jar" com.ejemplo.Main src\main\resources\entrada.txt
```

Linux / macOS / WSL:

```
java -cp "out:lib/java-cup-11b-runtime.jar" com.ejemplo.Main src/main/resources/entrada.txt
```

(El separador del classpath es `;` en Windows y `:` en Linux/macOS.)

El programa imprime cada etapa y guarda el assembly x86-64 en `salida.s`.

### Paso 5 — Ensamblar y ejecutar el assembly x86-64

El assembly generado es de Linux (sintaxis AT&T, GNU as). En Windows se puede usar WSL:

```
gcc salida.s -o salida
./salida
echo $?
```

`./salida` muestra lo que imprimen los `print` y `echo $?` el valor retornado por `main`. Con `wsl gcc salida.s -o salida` y `wsl ./salida` también se puede lanzar desde la terminal de Windows.

## 4. Paso a paso en IntelliJ IDEA

1. **File → Open** y elegir la carpeta del proyecto (IntelliJ detecta el `pom.xml`).
2. Si el IDE no reconoce `java_cup`: **File → Project Structure → Libraries → + → Java** y elegir `lib/java-cup-11b-runtime.jar`.
3. **Run → Edit Configurations → + → Application**:
   - Main class: `com.ejemplo.Main`
   - Program arguments: `src/main/resources/entrada.txt`
   - Working directory: la raíz del proyecto
4. Ejecutar con el botón verde. Si se cambia `Lexer.flex` o `parser.cup`, correr antes `generar.bat`.

## 5. El lenguaje

### Tokens (expresiones regulares)

| Token | Expresión regular |
|---|---|
| Palabras reservadas | `int`, `bool`, `void`, `main`, `return`, `print`, `true`, `false` |
| Operadores y delimitadores | `+` `*` `=` `;` `(` `)` `{` `}` |
| Número entero | `0 \| [1-9][0-9]*` |
| Identificador | `[A-Za-z][A-Za-z0-9]*` (empieza con letra, sigue con letras o números) |
| Espacios | `[ \t\f\r\n]` (se ignoran) |

### Gramática

```
program  ::= tipo_ret MAIN ( ) { decls sents }
tipo_ret ::= int | bool | void
decls    ::= decls decl | decl
decl     ::= tipo ID ;
tipo     ::= int | bool
sents    ::= sents sent | ε
sent     ::= ID = expr ;  |  print expr ;  |  return expr ;  |  return ;
expr     ::= expr + expr | expr * expr | ( expr ) | NUMBER | true | false | ID
```

La precedencia se resuelve con `precedence left PLUS;` y `precedence left TIMES;` (el `*` liga más fuerte que el `+`).

### Ejemplo (`entrada.txt`)

```
int main(){int x; int y; x = 2; y = 3; x = x+y*4; print x; return x; }
```

## 6. Etapas del compilador

El `Main` encadena las etapas en este orden. Salida real para el ejemplo anterior:

### 6.1 Análisis léxico y sintáctico → AST

`Lexer` (JFlex) convierte el texto en tokens y `Parser` (CUP) verifica la gramática y construye el árbol con las clases de `AST.java`.

```
Programa (retorna int)
  Declaraciones:
    int x
    int y
  Sentencias:
    x = Num(2)
    y = Num(3)
    x = Suma(Var(x), Mult(Var(y), Num(4)))
    print Var(x)
    return Var(x)
```

### 6.2 Análisis semántico y tabla de símbolos

`AnalizadorSemantico` recorre el AST y verifica: variables declaradas una sola vez, variables usadas después de declararse, tipos compatibles en las asignaciones, operadores `+` y `*` solo sobre `int`, y que el `return` coincida con el tipo de `main`. La `TablaSimbolos` guarda, por cada variable, su tipo (clase `Info`) y la usa luego el intérprete como memoria.

```
    int x
    int y
```

### 6.3 Intérprete

`Interprete` evalúa el AST de forma recursiva (`eval`) y ejecuta las sentencias en orden; un `return` corta la ejecución.

```
14
Valor retornado: 14
```

### 6.4 Pseudo-assembly de pila

`GeneradorAssembly.generarPseudo` recorre el AST y emite instrucciones de una máquina de pila (`PUSHC`, `PUSHA`, `LOAD`, `STORE`, `ADD`, `MUL`, `PRINT`, `RET`).

```
; variables
VAR x
VAR y
; codigo
PUSHA x
PUSHC 2
STORE
PUSHA y
PUSHC 3
STORE
PUSHA x
PUSHA x
LOAD
PUSHA y
LOAD
PUSHC 4
MUL
ADD
STORE
PUSHA x
LOAD
PRINT
PUSHA x
LOAD
RET
HALT
```

### 6.5 Código intermedio de tres direcciones

`GeneradorC3D` descompone cada expresión en instrucciones con a lo sumo una operación, usando temporales (`_t1`, `_t2`, …; los identificadores del lenguaje no pueden empezar con `_`, así que no hay choques de nombres).

```
  1: x = 2
  2: y = 3
  3: _t1 = y * 4
  4: _t2 = x + _t1
  5: x = _t2
  6: print x
  7: return x
```

### 6.6 Assembly x86-64

`GeneradorAssembly.generarX86` traduce el código de tres direcciones a assembly x86-64 (AT&T, 2 direcciones). Cada variable y cada temporal ocupa 4 bytes en la pila (`-4(%rbp)`, `-8(%rbp)`, …), las operaciones pasan por `%eax`, y `print` llama a `printf`. La pila se mantiene alineada a 16 bytes. Fragmento inicial para el ejemplo:

```
main:
    pushq   %rbp
    movq    %rsp, %rbp
    subq    $16, %rsp
    movl    $0, -4(%rbp)
    movl    $0, -8(%rbp)

    # x = 2
    movl    $2, %eax
    movl    %eax, -4(%rbp)

    # y = 3
    movl    $3, %eax
    movl    %eax, -8(%rbp)

    # _t1 = y * 4
    movl    -8(%rbp), %eax
    imull   $4, %eax
    movl    %eax, -12(%rbp)

    # _t2 = x + _t1
    movl    -4(%rbp), %eax
    addl    -12(%rbp), %eax
    movl    %eax, -16(%rbp)

    # x = _t2
    movl    -16(%rbp), %eax
    movl    %eax, -4(%rbp)

    # print x
    movl    -4(%rbp), %esi
    leaq    .LC_int(%rip), %rdi
    movl    $0, %eax
    call    printf@PLT
```

Ejecutado con `gcc`, el programa imprime `14` y termina con código de salida 14, igual que el intérprete.

## 7. Casos de prueba (`src/main/resources`)

| Archivo | Qué prueba | Resultado esperado |
|---|---|---|
| `entrada.txt` | programa del ejemplo | imprime `14`, retorna 14 |
| `entrada2.txt` | `void main` con `x+3*2*y` | imprime `7` |
| `entrada3.txt` | variables `bool` y `print` de un `bool` | imprime `true` y `11`, retorna 6 |
| `error_semantico.txt` | uso de variable no declarada | `Error semantico: variable no declarada: z` |
| `error_sintactico.txt` | falta un `;` en la declaración | `Error sintactico en linea 1, columna N` |

Los errores léxicos (por ejemplo un `$`) se informan con línea y columna: `Error lexico en linea 1, columna 26: caracter ilegal <$>`.
#   P r o y e c t o _ C o m p i l a d o r e s  
 