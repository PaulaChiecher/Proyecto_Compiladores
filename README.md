# Proyecto de Compiladores

**Materia:** Compiladores — Ingeniería en Sistemas de Información — UNViMe  
**Docente:** Francisco Bavera  
**Integrante:** Paula Vanina Chiecher  
**Repositorio:** [github.com/PaulaChiecher/Proyecto_Compiladores](https://github.com/PaulaChiecher/Proyecto_Compiladores)

Guía completa para **descargar, instalar, compilar, ejecutar y probar** el proyecto en Windows, Linux, macOS y WSL, tanto por **terminal** como desde **Visual Studio Code** e **IntelliJ IDEA**.

---

## Contenido

1. [Qué es este proyecto](#1-qué-es-este-proyecto)
2. [Inicio rápido](#2-inicio-rápido)
3. [Requisitos](#3-requisitos)
4. [Descargar el proyecto](#4-descargar-el-proyecto)
5. [Instalar Java 21](#5-instalar-java-21)
6. [Compilar y ejecutar por terminal](#6-compilar-y-ejecutar-por-terminal)
7. [Usar el proyecto en Visual Studio Code](#7-usar-el-proyecto-en-visual-studio-code)
8. [Usar el proyecto en IntelliJ IDEA](#8-usar-el-proyecto-en-intellij-idea)
9. [El lenguaje implementado](#9-el-lenguaje-implementado)
10. [Etapas internas del compilador](#10-etapas-internas-del-compilador)
11. [Pruebas incluidas y resultados reales](#11-pruebas-incluidas-y-resultados-reales)
12. [Ensamblar y ejecutar el assembly x86-64](#12-ensamblar-y-ejecutar-el-assembly-x86-64)
13. [Solución de problemas](#13-solución-de-problemas)
14. [Checklist de entrega](#14-checklist-de-entrega)
15. [Actualizar el README en GitHub](#15-actualizar-el-readme-en-github)
16. [Guía para explicar el proyecto](#16-guía-para-explicar-el-proyecto)

---

## 1. Qué es este proyecto

Este proyecto implementa un **compilador para un mini lenguaje** en **Java 21**. El analizador léxico se genera con **JFlex** y el analizador sintáctico con **Java CUP**.

A partir de un programa fuente, el compilador realiza estas etapas:

1. **Análisis léxico:** reconoce los tokens del lenguaje.
2. **Análisis sintáctico:** verifica la gramática y construye el AST (árbol sintáctico abstracto).
3. **Análisis semántico:** comprueba declaraciones, usos y compatibilidad de tipos.
4. **Interpretación:** ejecuta el AST y muestra los resultados.
5. **Pseudo-assembly:** genera una representación didáctica de máquina de pila.
6. **Código intermedio:** genera código de tres direcciones (C3D).
7. **Assembly x86-64:** traduce el C3D a assembly AT&T para GNU/Linux y lo guarda en `salida.s`.

El objetivo es mostrar el funcionamiento de las etapas principales de un compilador, desde el código fuente hasta la generación de código de bajo nivel.

### Qué incluye

- Analizador léxico generado con JFlex.
- Analizador sintáctico generado con CUP.
- AST con nodos para declaraciones, asignaciones, expresiones, `print`, `return`, `if`, `else` y `while`.
- Chequeo semántico con tabla de símbolos.
- Intérprete con protección contra bucles infinitos.
- Generación de pseudo-assembly, código de tres direcciones y assembly x86-64.
- 9 programas correctos, 9 programas con errores y 5 archivos de ejemplo en `src/main/resources/`.

### Qué no incluye

- Operadores relacionales (`<`, `>`, `<=`, `>=`, `==`, `!=`).
- División, resta, módulo, incremento, decremento.
- Operadores lógicos `&&` y `||` (el OR lógico se escribe como `bool + bool`).
- Funciones adicionales, arreglos, punteros, estructuras, clases.
- Comentarios (`//` o `/* ... */`).
- Declaraciones dentro de bloques.
- Varios programas o varios `main`.

---

## 2. Inicio rápido

Si ya tenés **JDK 21** instalado y estás en la carpeta raíz del proyecto, estos son los comandos mínimos.

### Windows (CMD o PowerShell)

```bat
mkdir out
javac -encoding UTF-8 -cp "lib\java-cup-11b-runtime.jar" -d out src\main\java\com\ejemplo\*.java
java -cp "out;lib\java-cup-11b-runtime.jar" com.ejemplo.Main src\main\resources\entrada.txt
```

### Linux, macOS o WSL

```bash
mkdir -p out
javac -encoding UTF-8 -cp "lib/java-cup-11b-runtime.jar" -d out src/main/java/com/ejemplo/*.java
java -cp "out:lib/java-cup-11b-runtime.jar" com.ejemplo.Main src/main/resources/entrada.txt
```

### Resultado esperado

El programa imprime las etapas del compilador y, para `entrada.txt`, termina mostrando:

```text
=== Chequeo semantico: OK ===
...
14
Valor retornado: 14
...
=== Assembly x86-64 (guardado en salida.s) ===
```

Además, genera el archivo `salida.s` en la carpeta desde donde se ejecutó el comando (normalmente, la raíz del proyecto).

> **Importante:** el separador del classpath depende del sistema operativo donde corre Java, no de la terminal.
>
> - Windows (CMD, PowerShell o Git Bash): `;`
> - Linux, macOS y WSL: `:`

---

## 3. Requisitos

| Componente | ¿Obligatorio? | Para qué sirve | Cómo verificar |
|---|---|---|---|
| **JDK 21** | Sí | Compilar y ejecutar el proyecto | `java -version` y `javac -version` |
| Terminal | Sí | Ejecutar los comandos | CMD, PowerShell, Bash, etc. |
| Git | No | Clonar el repositorio | `git --version` |
| GCC | No | Ensamblar y ejecutar `salida.s` | `gcc --version` |
| WSL | No | Ejecutar el assembly GNU/Linux en Windows | `wsl --version` |
| Visual Studio Code | No | Editar y ejecutar desde IDE | `code --version` |
| IntelliJ IDEA | No | Editar y ejecutar desde IDE | Abrir la aplicación |
| Internet | Solo para descargar | Bajar el ZIP, Git o el JDK | — |

### JDK, no JRE

Es importante instalar el **JDK** (Java Development Kit), no solamente el JRE (Java Runtime Environment). El proyecto necesita `javac` para compilar. Si `java -version` funciona pero `javac -version` no, probablemente tengas solo el JRE.

El proyecto fue desarrollado y probado con **Java 21**. El `pom.xml` también está configurado para Java 21.

---

## 4. Descargar el proyecto

### Opción A: Descargar el ZIP desde GitHub

1. Abrir el repositorio:
   [https://github.com/PaulaChiecher/Proyecto_Compiladores](https://github.com/PaulaChiecher/Proyecto_Compiladores)
2. Hacer clic en **Code**.
3. Elegir **Download ZIP**.
4. Descomprimir el archivo.
5. Abrir una terminal dentro de la carpeta descomprimida.

La carpeta correcta es la que contiene:

```text
lib/
src/
tests/
pom.xml
README.md
```

### Opción B: Clonar con Git

```bash
git clone https://github.com/PaulaChiecher/Proyecto_Compiladores.git
cd Proyecto_Compiladores
```

### Verificar que estás en la carpeta correcta

**Windows:**

```bat
dir
```

**Linux, macOS o WSL:**

```bash
ls
```

Debés ver, entre otras cosas:

```text
lib  src  tests  pom.xml  README.md
```

> Todos los comandos de esta guía se ejecutan desde la **raíz del proyecto**, es decir, desde la carpeta que contiene `lib/`, `src/`, `tests/` y `pom.xml`.

---

## 5. Instalar Java 21

### 5.1 Windows

#### Opción recomendada: winget

Abrir **PowerShell** o **CMD** y ejecutar una de estas opciones:

**Microsoft Build of OpenJDK 21:**

```powershell
winget install --id Microsoft.OpenJDK.21 -e --accept-source-agreements --accept-package-agreements
```

**Eclipse Temurin 21 (Adoptium):**

```powershell
winget install --id EclipseAdoptium.Temurin.21.JDK -e --accept-source-agreements --accept-package-agreements
```

Después de instalar, **cerrar y volver a abrir la terminal** para que se actualice el `PATH`.

Verificar:

```bat
java -version
javac -version
```

Deberías ver algo parecido a:

```text
openjdk version "21.0.x" ...
javac 21.0.x
```

#### Opción manual

1. Descargar el JDK 21 desde:
   - [Eclipse Adoptium — Temurin 21](https://adoptium.net/temurin/releases/?version=21)
   - [Oracle JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
2. Ejecutar el instalador.
3. Si el instalador no agrega Java al `PATH`, agregar manualmente la carpeta `bin` del JDK.
4. Cerrar y reabrir la terminal.
5. Verificar con `java -version` y `javac -version`.

#### Si `javac` no se reconoce

- Cerrar y reabrir la terminal.
- Verificar que instalaste el JDK y no solo el JRE.
- Buscar `javac.exe`:

```powershell
where.exe javac
```

- Si aparece una ruta, agregar su carpeta al `PATH`.
- Si no aparece, reinstalar el JDK 21.

Configurar `JAVA_HOME` es opcional para este proyecto. Si lo necesitás:

```powershell
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Microsoft\jdk-21.0.13.21-hotspot", "User")
```

La ruta exacta puede variar según el fabricante y la versión. Después de cambiarla, cerrar y reabrir la terminal.

### 5.2 Linux

#### Ubuntu 24.04 / Debian recientes

```bash
sudo apt update
sudo apt install -y openjdk-21-jdk
```

#### Fedora

```bash
sudo dnf install -y java-21-openjdk-devel
```

#### Arch Linux / Manjaro

```bash
sudo pacman -S --needed jdk21-openjdk
```

#### Si tu distribución no tiene OpenJDK 21

Usar **SDKMAN**:

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk list java
sdk install java 21.0.5-tem
```

> El identificador `21.0.5-tem` puede cambiar. Elegí una versión **21.x** de Temurin que aparezca en `sdk list java`.

También se puede usar el instalador de [Adoptium](https://adoptium.net/temurin/releases/?version=21).

Verificar:

```bash
java -version
javac -version
```

### 5.3 macOS

Con Homebrew:

```bash
brew install openjdk@21
```

En Apple Silicon (M1/M2/M3/M4), agregar al `PATH`:

```bash
echo 'export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

En Mac con procesador Intel, la ruta suele ser `/usr/local/opt/openjdk@21/bin`.

También se puede instalar Temurin 21 desde [Adoptium](https://adoptium.net/temurin/releases/?version=21).

Verificar:

```bash
java -version
javac -version
```

> El assembly generado es para **GNU/Linux**. En macOS se puede compilar el proyecto Java, pero para ejecutar `salida.s` conviene usar Linux, WSL o Docker.

### 5.4 WSL en Windows (opcional, para el assembly)

Si querés ejecutar el assembly x86-64 en Windows, podés usar WSL con Ubuntu.

1. Abrir **PowerShell como administrador**.
2. Instalar WSL y Ubuntu:

```powershell
wsl --install -d Ubuntu
```

3. Reiniciar si el sistema lo pide.
4. Abrir Ubuntu desde el menú Inicio y crear un usuario.
5. Dentro de Ubuntu, instalar el JDK:

```bash
sudo apt update
sudo apt install -y openjdk-21-jdk
```

6. Verificar:

```bash
java -version
javac -version
```

#### Recomendación para WSL

Para mejor rendimiento, clonar el proyecto **dentro del sistema de archivos de Linux**:

```bash
cd ~
git clone https://github.com/PaulaChiecher/Proyecto_Compiladores.git
cd Proyecto_Compiladores
```

Los archivos de Windows se ven desde WSL en `/mnt/c/...`. Por ejemplo:

```bash
cd "/mnt/c/Users/TuUsuario/Desktop/Proyecto_Compiladores"
```

> Si el proyecto está en `/mnt/c`, la compilación y la ejecución pueden ser más lentas que si está dentro de `~`.

---

## 6. Compilar y ejecutar por terminal

### 6.1 Windows: CMD

Abrir **CMD** y ubicarse en la raíz del proyecto:

```bat
cd C:\ruta\al\Proyecto_Compiladores
```

Crear la carpeta `out`:

```bat
mkdir out
```

> Si `out` ya existe, CMD puede mostrar un mensaje diciendo que ya existe. No es un error grave: podés continuar con `javac` u omitir `mkdir out`.

Compilar:

```bat
javac -encoding UTF-8 -cp "lib\java-cup-11b-runtime.jar" -d out src\main\java\com\ejemplo\*.java
```

Ejecutar con el archivo de ejemplo:

```bat
java -cp "out;lib\java-cup-11b-runtime.jar" com.ejemplo.Main src\main\resources\entrada.txt
```

### 6.2 Windows: PowerShell

Abrir **PowerShell** y ubicarse en la raíz del proyecto:

```powershell
cd "C:\ruta\al\Proyecto_Compiladores"
```

Crear la carpeta `out` (si ya existe, no falla):

```powershell
New-Item -ItemType Directory -Force -Path out | Out-Null
```

Compilar:

```powershell
javac -encoding UTF-8 -cp "lib\java-cup-11b-runtime.jar" -d out src\main\java\com\ejemplo\*.java
```

Ejecutar:

```powershell
java -cp "out;lib\java-cup-11b-runtime.jar" com.ejemplo.Main src\main\resources\entrada.txt
```

### 6.3 Linux, macOS o WSL

```bash
cd ~/Proyecto_Compiladores
mkdir -p out
javac -encoding UTF-8 -cp "lib/java-cup-11b-runtime.jar" -d out src/main/java/com/ejemplo/*.java
java -cp "out:lib/java-cup-11b-runtime.jar" com.ejemplo.Main src/main/resources/entrada.txt
```

### 6.4 Qué muestra el programa

Para `entrada.txt`, la salida tiene esta estructura:

```text
=== AST ===
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
=== Chequeo semantico: OK ===
=== Tabla de simbolos ===
    int x
    int y
=== Ejecucion (interprete) ===
14
Valor retornado: 14
=== Pseudo-assembly (pila) ===
...
=== Codigo intermedio (tres direcciones) ===
  1: x = 2
  2: y = 3
  3: _t1 = y * 4
  4: _t2 = x + _t1
  5: x = _t2
  6: print x
  7: return x
=== Assembly x86-64 (guardado en salida.s) ===
...
```

El programa **no se detiene** después de una etapa: muestra todas las etapas y al final guarda el assembly.

### 6.5 Archivo de entrada por defecto

Si no se indica ningún archivo, se usa `src/main/resources/entrada.txt`:

**Windows:**

```bat
java -cp "out;lib\java-cup-11b-runtime.jar" com.ejemplo.Main
```

**Linux, macOS o WSL:**

```bash
java -cp "out:lib/java-cup-11b-runtime.jar" com.ejemplo.Main
```

### 6.6 Elegir el archivo de salida del assembly

Se puede indicar un segundo argumento con la ruta del assembly:

**Windows:**

```bat
java -cp "out;lib\java-cup-11b-runtime.jar" com.ejemplo.Main src\main\resources\entrada.txt mi_salida.s
```

**Linux, macOS o WSL:**

```bash
java -cp "out:lib/java-cup-11b-runtime.jar" com.ejemplo.Main src/main/resources/entrada.txt mi_salida.s
```

Si se omite, se usa `salida.s`.

### 6.7 Ejecutar un caso de prueba

**Windows:**

```bat
java -cp "out;lib\java-cup-11b-runtime.jar" com.ejemplo.Main tests\ok\02_if_else.txt
```

**Linux, macOS o WSL:**

```bash
java -cp "out:lib/java-cup-11b-runtime.jar" com.ejemplo.Main tests/ok/02_if_else.txt
```

Para un caso de error:

**Windows:**

```bat
java -cp "out;lib\java-cup-11b-runtime.jar" com.ejemplo.Main tests\error\01_condicion_while_int.txt
```

**Linux, macOS o WSL:**

```bash
java -cp "out:lib/java-cup-11b-runtime.jar" com.ejemplo.Main tests/error/01_condicion_while_int.txt
```

### 6.8 Limpiar y recompilar desde cero

Si cambiaste archivos o querés descartar clases viejas:

**Windows CMD:**

```bat
rmdir /s /q out
mkdir out
javac -encoding UTF-8 -cp "lib\java-cup-11b-runtime.jar" -d out src\main\java\com\ejemplo\*.java
```

**Windows PowerShell:**

```powershell
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force -Path out | Out-Null
javac -encoding UTF-8 -cp "lib\java-cup-11b-runtime.jar" -d out src\main\java\com\ejemplo\*.java
```

**Linux, macOS o WSL:**

```bash
rm -rf out
mkdir -p out
javac -encoding UTF-8 -cp "lib/java-cup-11b-runtime.jar" -d out src/main/java/com/ejemplo/*.java
```

### 6.9 Regenerar Lexer.java, Parser.java y Sym.java (opcional)

Este paso **no es necesario** para compilar por primera vez, porque los archivos generados ya están incluidos en `src/main/java/com/ejemplo/`.

Solo hay que regenerarlos si se modifican:

- `src/main/jflex/Lexer.flex`
- `src/main/cup/parser.cup`

#### Con los scripts incluidos

**Windows:**

```bat
generar.bat
```

**Linux, macOS o WSL:**

```bash
sh generar.sh
```

#### Con los comandos manuales

**Windows:**

```bat
java -jar lib\jflex-full-1.9.1.jar -d src\main\java\com\ejemplo src\main\jflex\Lexer.flex
java -cp lib\java-cup-11b-2015.03.26.jar java_cup.Main -destdir src\main\java\com\ejemplo -parser Parser -symbols Sym src\main\cup\parser.cup
```

**Linux, macOS o WSL:**

```bash
java -jar lib/jflex-full-1.9.1.jar -d src/main/java/com/ejemplo src/main/jflex/Lexer.flex
java -cp lib/java-cup-11b-2015.03.26.jar java_cup.Main -destdir src/main/java/com/ejemplo -parser Parser -symbols Sym src/main/cup/parser.cup
```

CUP debe informar:

```text
0 errors and 0 warnings
...
0 conflicts detected (0 expected).
```

Después de regenerar, volver a compilar con `javac`.

---

## 7. Usar el proyecto en Visual Studio Code

### 7.1 Instalar Visual Studio Code

**Windows:**

```powershell
winget install --id Microsoft.VisualStudioCode -e --accept-source-agreements --accept-package-agreements
```

También se puede descargar desde [code.visualstudio.com](https://code.visualstudio.com/).

**Ubuntu/Debian:**

Descargar el `.deb` desde la página oficial e instalarlo con:

```bash
sudo apt install ./code_*.deb
```

**macOS:**

```bash
brew install --cask visual-studio-code
```

### 7.2 Instalar la extensión de Java

Abrir VS Code y instalar **Extension Pack for Java** desde el panel de extensiones, o ejecutar:

```bash
code --install-extension vscjava.vscode-java-pack
```

El pack incluye soporte de lenguaje Java, depurador, Maven, pruebas y gestión de proyectos.

### 7.3 Abrir el proyecto

Desde la terminal, en la raíz del proyecto:

```bash
code .
```

O en VS Code: **File → Open Folder** y elegir la carpeta que contiene `pom.xml`, `lib/`, `src/` y `tests/`.

### 7.4 Verificar el JDK

1. Abrir la paleta de comandos con `Ctrl+Shift+P`.
2. Ejecutar **Java: Configure Java Runtime**.
3. Verificar que esté seleccionado un **JDK 21**.
4. Si no aparece, instalar el JDK 21 (ver sección 5) y volver a abrir VS Code.

También podés ver la versión de Java en la barra inferior de VS Code.

### 7.5 Ejecutar desde la terminal integrada (método recomendado)

Abrir la terminal integrada con `Ctrl+`` y ejecutar los comandos de la sección 6 según tu sistema operativo.

Este método es el más confiable porque usa exactamente el mismo `javac` y el mismo classpath que la guía principal.

### 7.6 Ejecutar con el botón Run o con Launch

1. Abrir `src/main/java/com/ejemplo/Main.java`.
2. Hacer clic en **Run** sobre el método `main`.
3. Si VS Code no encuentra `java_cup.runtime`, crear el archivo `.vscode/launch.json` con:

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Main - entrada.txt",
      "request": "launch",
      "mainClass": "com.ejemplo.Main",
      "cwd": "${workspaceFolder}",
      "args": ["src/main/resources/entrada.txt"],
      "classPaths": ["lib/java-cup-11b-runtime.jar"]
    }
  ]
}
```

Para ejecutar una prueba, cambiar `args`:

```json
"args": ["tests/ok/01_suma_bool.txt"]
```

También se puede agregar una segunda configuración para un caso de error:

```json
{
  "type": "java",
  "name": "Main - error semantico",
  "request": "launch",
  "mainClass": "com.ejemplo.Main",
  "cwd": "${workspaceFolder}",
  "args": ["tests/error/01_condicion_while_int.txt"],
  "classPaths": ["lib/java-cup-11b-runtime.jar"]
}
```

> El `cwd` debe ser la raíz del proyecto para que funcionen las rutas relativas como `src/main/resources/entrada.txt` y para que `salida.s` se genere en el lugar esperado.

### 7.7 Si VS Code no reconoce `java_cup`

Probar, en este orden:

1. Verificar que existe `lib/java-cup-11b-runtime.jar`.
2. Usar el `launch.json` de la sección anterior con `"classPaths"`.
3. Ejecutar desde la terminal integrada con `javac` y `java`.
4. Si el proyecto se importó como Maven, revisar que el `pom.xml` tenga la dependencia `system` apuntando al jar.
5. Recargar la ventana: `Ctrl+Shift+P` → **Developer: Reload Window**.

### 7.8 Si VS Code no encuentra el JDK

En **Settings** (`Ctrl+,`), buscar `java.jdt.ls.java.home` y configurar la ruta del JDK 21. Ejemplos:

**Windows:**

```json
"java.jdt.ls.java.home": "C:\\Program Files\\Microsoft\\jdk-21.0.13.21-hotspot"
```

**Linux:**

```json
"java.jdt.ls.java.home": "/usr/lib/jvm/java-21-openjdk-amd64"
```

La ruta exacta puede variar. Verificarla con `where.exe javac` (Windows) o `readlink -f $(which javac)` (Linux).

---

## 8. Usar el proyecto en IntelliJ IDEA

### 8.1 Instalar IntelliJ IDEA

**Windows:**

```powershell
winget install --id JetBrains.IntelliJIDEA.Community -e --accept-source-agreements --accept-package-agreements
```

**Ubuntu:**

```bash
sudo snap install intellij-idea-community --classic
```

**macOS:**

```bash
brew install --cask intellij-idea-ce
```

También se puede usar **JetBrains Toolbox** o descargar desde [jetbrains.com/idea](https://www.jetbrains.com/idea/download/).

### 8.2 Abrir el proyecto

1. Abrir IntelliJ IDEA.
2. Elegir **File → Open**.
3. Seleccionar la carpeta raíz del proyecto, la que contiene `pom.xml`, `lib/` y `src/`.
4. Si aparece un aviso de confianza, aceptar.

Si IntelliJ detecta el `pom.xml`, puede importar el proyecto como Maven automáticamente.

### 8.3 Configurar el JDK 21

1. Ir a **File → Project Structure**.
2. En **Project**, seleccionar **SDK**.
3. Hacer clic en **Add SDK → JDK**.
4. Elegir la carpeta del JDK 21 instalado.
5. Si no aparece, usar **Download JDK**, elegir versión **21** y un fabricante como **Eclipse Temurin** o **Microsoft OpenJDK**.
6. Verificar que **Language level** sea **21**.
7. Aplicar los cambios.

### 8.4 Agregar el jar de CUP

Si IntelliJ no reconoce `java_cup.runtime`:

1. Ir a **File → Project Structure → Libraries**.
2. Hacer clic en **+ → Java**.
3. Seleccionar `lib/java-cup-11b-runtime.jar`.
4. Aceptar y agregar la biblioteca al módulo del proyecto.
5. Aplicar los cambios.

Si el proyecto se importó como Maven, la dependencia del `pom.xml` debería agregar el jar automáticamente. Si no lo hace, usar el método manual.

### 8.5 Marcar las fuentes Java

Si IntelliJ no reconoce el paquete `com.ejemplo`:

1. En el árbol de archivos, ir a `src/main/java`.
2. Clic derecho → **Mark Directory as → Sources Root**.

### 8.6 Crear la configuración de ejecución

1. Ir a **Run → Edit Configurations**.
2. Hacer clic en **+ → Application**.
3. Completar:

| Campo | Valor |
|---|---|
| Name | `Main - entrada.txt` |
| Main class | `com.ejemplo.Main` |
| Program arguments | `src/main/resources/entrada.txt` |
| Working directory | La raíz del proyecto |
| Use classpath of module | El módulo del proyecto |

4. Aplicar y aceptar.
5. Ejecutar con el botón verde.

Para ejecutar un test, crear otra configuración o cambiar **Program arguments** por:

```text
tests/ok/02_if_else.txt
```

Para un caso de error:

```text
tests/error/01_condicion_while_int.txt
```

El resultado esperado para `entrada.txt` es `14` y `Valor retornado: 14`. El archivo `salida.s` se genera en el **Working directory** configurado (la raíz del proyecto).

### 8.7 Regenerar Lexer, Parser y Sym desde IntelliJ

Abrir la terminal integrada de IntelliJ (**View → Tool Windows → Terminal**) y ejecutar:

**Windows:**

```bat
generar.bat
```

**Linux, macOS o WSL:**

```bash
sh generar.sh
```

Después, reconstruir el proyecto con **Build → Rebuild Project**.

### 8.8 Problemas comunes en IntelliJ

| Problema | Solución |
|---|---|
| `Cannot resolve symbol 'java_cup'` | Agregar `lib/java-cup-11b-runtime.jar` como biblioteca |
| `Cannot resolve symbol 'com.ejemplo'` | Marcar `src/main/java` como **Sources Root** |
| `Main class not found` | Verificar el nombre `com.ejemplo.Main` y reconstruir |
| `salida.s` no aparece | Revisar el **Working directory** de la configuración |
| No aparece el JDK 21 | Descargarlo desde **Project Structure → SDK** |
| El proyecto no compila por encoding | Verificar que el archivo esté guardado en UTF-8 |

---

## 9. El lenguaje implementado

### 9.1 Estructura de un programa

Todo programa tiene exactamente esta forma:

```text
tipo_ret main() {
    declaraciones
    sentencias
}
```

- `tipo_ret` puede ser `int`, `bool` o `void`.
- El nombre `main` es obligatorio.
- `main` no recibe parámetros.
- Se exige **al menos una declaración**.
- Las declaraciones van **al comienzo** del cuerpo de `main`.
- Las sentencias pueden ser cero o más.
- No se pueden declarar variables dentro de bloques `if`, `else` o `while`.

### 9.2 Tipos de datos

| Tipo | Uso |
|---|---|
| `int` | Números enteros |
| `bool` | Valores booleanos: `true` y `false` |
| `void` | Tipo de retorno de `main` cuando no devuelve un valor |

Las variables solo pueden declararse como `int` o `bool`.

### 9.3 Palabras reservadas

```text
int  bool  void  main  return  print
if  else  while  true  false
```

Las palabras reservadas se escriben en minúsculas. `main` no puede usarse como nombre de variable.

### 9.4 Operadores y símbolos

| Símbolo | Función |
|---|---|
| `+` | Suma de enteros o OR lógico cuando ambos operandos son `bool` |
| `*` | Multiplicación de enteros |
| `=` | Asignación |
| `;` | Fin de declaración o sentencia |
| `()` | Agrupación de expresiones y condición de estructuras |
| `{}` | Delimitación de bloques |

La multiplicación tiene mayor precedencia que la suma. Los paréntesis permiten agrupar expresiones.

### 9.5 Gramática

La gramática implementada en `parser.cup` es, en notación EBNF:

```text
program  ::= tipo_ret "main" "(" ")" "{" decls sents "}"
tipo_ret ::= "int" | "bool" | "void"

decls    ::= decl { decl }          // al menos una declaración
decl     ::= ("int" | "bool") ID ";"

sents    ::= { sent }               // cero o más sentencias
sent     ::= ID "=" expr ";"
           | "print" expr ";"
           | "return" expr ";"
           | "return" ";"
           | "if" "(" expr ")" "{" sents "}"
           | "if" "(" expr ")" "{" sents "}" "else" "{" sents "}"
           | "while" "(" expr ")" "{" sents "}"

expr     ::= expr "+" expr
           | expr "*" expr
           | "(" expr ")"
           | NUMBER
           | "true"
           | "false"
           | ID
```

> Las llaves `{ }` de la gramática son símbolos literales del lenguaje. En la notación EBNF, `{ ... }` significa repetición; para evitar confusión, en este README se usan comillas para los literales.

### 9.6 Precedencia

1. `*` tiene mayor precedencia.
2. `+` tiene menor precedencia.
3. Los paréntesis agrupan y alteran la precedencia.

Ejemplo:

```c
x = 2 + 3 * 4;    // x = 14
x = (2 + 3) * 4;  // x = 20
```

### 9.7 Declaraciones y asignaciones

```c
int main() {
    int numero;
    bool activo;

    numero = 10;
    activo = true;

    print numero;
    print activo;

    return numero;
}
```

- Una variable debe estar declarada para poder usarse.
- No se puede declarar dos veces la misma variable.
- Una asignación debe respetar el tipo de la variable.
- El intérprete inicializa las variables en `0` o `false` a nivel de assembly, pero en el intérprete Java una variable no inicializada produce un error de ejecución si se lee antes de asignarle un valor.

### 9.8 Condicional `if` / `else`

```c
int main() {
    int resultado;
    bool condicion;

    condicion = true;

    if (condicion) {
        resultado = 10;
    } else {
        resultado = 20;
    }

    print resultado;
    return resultado;
}
```

También se admite `if` sin `else`:

```c
if (condicion) {
    resultado = 10;
}
```

La condición de `if` debe ser de tipo `bool`.

### 9.9 Bucle `while`

```c
int main() {
    int contador;
    bool continuar;

    contador = 0;
    continuar = true;

    while (continuar) {
        contador = contador + 1;
        continuar = false;
    }

    print contador;
    return contador;
}
```

La condición de `while` debe ser de tipo `bool`.

### 9.10 `print`

`print` acepta una expresión `int` o `bool`:

```c
print 10;          // imprime 10
print true;        // imprime true
print 2 + 3 * 4;   // imprime 14
```

### 9.11 `return`

- `return expr;` devuelve un valor.
- `return;` no devuelve valor y solo se admite en `void main`.
- El tipo de la expresión debe coincidir con el tipo de retorno de `main`.
- No se exige que todos los caminos posibles terminen con `return`. La implementación comprueba los `return` que aparecen, pero no analiza todos los caminos.

Ejemplos:

```c
int main() {
    int x;
    x = 7;
    return x;
}
```

```c
void main() {
    int x;
    x = 1;
    print x;
    return;
}
```

### 9.12 Reglas semánticas principales

- Una variable debe estar declarada para poder utilizarse.
- No se permite declarar dos veces una variable con el mismo nombre.
- Una asignación debe respetar el tipo de la variable.
- `int + int` produce un entero.
- `bool + bool` produce un booleano y se interpreta como OR lógico (`||`).
- No se permite mezclar `int` y `bool` con el operador `+`.
- El operador `*` solamente admite operandos `int`.
- La condición de `if` y `while` debe ser de tipo `bool`.
- El valor de `return`, cuando existe, debe coincidir con el tipo declarado para `main`.
- `return;` se admite solamente con `void main`.
- No se permiten declaraciones nuevas dentro de bloques.
- No se permiten comentarios.

### 9.13 Limitaciones del lenguaje

El lenguaje **no** implementa:

- Operadores relacionales: `<`, `>`, `<=`, `>=`, `==`, `!=`.
- División `/`, resta `-`, módulo `%`.
- Operadores lógicos `&&` y `||` (el OR se hace con `+` entre `bool`).
- Operador unario `!`, `++`, `--`.
- Funciones adicionales.
- Arreglos, punteros, estructuras, clases.
- Comentarios.
- Declaraciones dentro de bloques.
- Varios programas o varios `main`.

---

## 10. Etapas internas del compilador

### 10.1 Análisis léxico — JFlex

Archivo: `src/main/jflex/Lexer.flex`  
Archivo generado: `src/main/java/com/ejemplo/Lexer.java`

El lexer reconoce:

- Palabras reservadas.
- Identificadores: `[A-Za-z][A-Za-z0-9]*`.
- Números enteros: `0` o `[1-9][0-9]*`.
- Operadores y delimitadores: `+`, `*`, `=`, `;`, `(`, `)`, `{`, `}`.
- Espacios en blanco: se ignoran.

Los identificadores comienzan con una letra y continúan con letras o números. No se admite `_` ni acentos.

Ante un carácter no reconocido, se informa un error léxico con línea y columna. Ejemplo:

```text
Error lexico en linea 1, columna 26: caracter ilegal <$>
```

### 10.2 Análisis sintáctico — CUP

Archivo: `src/main/cup/parser.cup`  
Archivos generados: `src/main/java/com/ejemplo/Parser.java` y `Sym.java`

CUP usa los tokens del lexer para verificar la estructura del programa y construir los nodos del AST.

Los errores sintácticos se informan con línea y columna. Ejemplo:

```text
Error sintactico en linea 1, columna 52
```

Si el problema es un fin de archivo inesperado, el mensaje es:

```text
Error sintactico: fin de archivo inesperado
```

> En ese caso particular no se muestra línea ni columna, porque el error se detecta al terminar el archivo.

La gramática actual genera **0 conflictos** en CUP.

### 10.3 Árbol sintáctico abstracto (AST)

Archivo: `src/main/java/com/ejemplo/AST.java`

Contiene las clases que representan:

- `Programa`
- `Decl` y `ListaDecl`
- `Asig`, `Print`, `Ret`, `If`, `While` y `ListaSent`
- `Num`, `Bool`, `Var`, `Suma` y `Mult`

El AST permite que las etapas siguientes trabajen con la estructura del programa sin volver a analizar el texto original.

### 10.4 Análisis semántico y tabla de símbolos

Archivos:

- `src/main/java/com/ejemplo/AnalizadorSemantico.java`
- `src/main/java/com/ejemplo/TablaSimbolos.java`
- `src/main/java/com/ejemplo/Info.java`
- `src/main/java/com/ejemplo/TipoNodo.java`

`AnalizadorSemantico` recorre el AST y verifica las reglas de tipos y declaraciones. `TablaSimbolos` almacena cada variable con su tipo y su valor durante la interpretación.

### 10.5 Interpretación

Archivo: `src/main/java/com/ejemplo/Interprete.java`

El intérprete ejecuta las sentencias del AST:

- Evalúa expresiones.
- Realiza asignaciones.
- Procesa condiciones y bucles.
- Muestra los valores de `print`.
- Conserva el resultado de `return`.
- Limita la ejecución a **1.000.000 de iteraciones** para detectar posibles bucles infinitos.

Si se supera el límite, informa:

```text
Error de ejecucion: posible bucle infinito (mas de 1000000 iteraciones)
```

### 10.6 Pseudo-assembly de pila

Archivo: `src/main/java/com/ejemplo/GeneradorAssembly.java`  
Método: `generarPseudo`

Genera una representación didáctica de máquina de pila con instrucciones como:

```text
PUSHC  valor
PUSHA  variable
LOAD
STORE
ADD
OR
MUL
PRINT
IFFALSO etiqueta
SALTO etiqueta
LABEL etiqueta
RET
HALT
```

Ejemplo para `entrada.txt`:

```text
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

Esta salida **no** es el assembly x86-64 que se ejecuta con GCC. Es una representación intermedia didáctica.

### 10.7 Código intermedio de tres direcciones (C3D)

Archivo: `src/main/java/com/ejemplo/GeneradorC3D.java`

Traduce las expresiones y sentencias a instrucciones de tres direcciones. Usa temporales (`_t1`, `_t2`, etc.) y etiquetas (`L1`, `L2`, etc.).

Ejemplo:

```c
x = x + y * 4;
```

se transforma en:

```text
_t1 = y * 4
_t2 = x + _t1
x = _t2
```

Salida completa para `entrada.txt`:

```text
  1: x = 2
  2: y = 3
  3: _t1 = y * 4
  4: _t2 = x + _t1
  5: x = _t2
  6: print x
  7: return x
```

### 10.8 Assembly x86-64

Archivo: `src/main/java/com/ejemplo/GeneradorAssembly.java`  
Método: `generarX86`

Traduce el código de tres direcciones a assembly x86-64 con sintaxis AT&T, destinado a GNU/Linux. El resultado se guarda en `salida.s`.

Características:

- Las variables y los temporales se ubican en la pila.
- Se usan instrucciones de salto para `if`, `else` y `while`.
- Las sentencias `print` se traducen mediante llamadas a `printf`.
- La pila se mantiene alineada a 16 bytes.

Fragmento para `entrada.txt`:

```asm
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

---

## 11. Pruebas incluidas y resultados reales

Los programas de prueba están en:

- `tests/ok/` — 9 programas correctos.
- `tests/error/` — 9 programas con errores.
- `src/main/resources/` — 5 archivos de ejemplo adicionales.

### 11.1 Pruebas correctas (`tests/ok/`)

| Archivo | Qué verifica | Imprime | Retorna (intérprete) | ¿Coincide con este README? |
|---|---|---|---|---|
| `01_suma_bool.txt` | Suma de booleanos con semántica OR | `true` | `true` | Sí |
| `02_if_else.txt` | Condicional con `else` | `3` | `3` | Sí |
| `03_if_sin_else.txt` | Condicional sin `else` | `11` | `11` | Sí |
| `04_while.txt` | Bucle `while` | `1` | `1` | Sí |
| `05_while_con_if.txt` | `while` combinado con `if` / `else` | `5` | `5` | Sí |
| `06_return_en_if.txt` | Retorno dentro de un bloque condicional | — | `7` | Sí |
| `07_return_en_while.txt` | Retorno dentro de un bucle | — | `8` | Sí |
| `08_while_expresion_suma.txt` | Condición booleana que utiliza `+` | — | `false` | Sí |
| `09_bucle_infinito.txt` | Protección del intérprete ante un bucle infinito | — | — (error de ejecución) | Sí |

Mensaje exacto de `09_bucle_infinito.txt`:

```text
Error de ejecucion: posible bucle infinito (mas de 1000000 iteraciones)
```

Después de ese mensaje, el compilador **continúa** con las etapas de generación de código. **No se debe ejecutar el assembly generado para esta prueba**, porque el assembly no incorpora el límite de iteraciones del intérprete.

### 11.2 Pruebas de error (`tests/error/`)

| Archivo | Error que verifica | Mensaje real | ¿Coincide con este README? |
|---|---|---|---|
| `01_condicion_while_int.txt` | Condición de `while` de tipo `int` | `Error semantico: la condicion del while debe ser bool (es int)` | Sí |
| `02_condicion_if_int.txt` | Condición de `if` de tipo `int` | `Error semantico: la condicion del if debe ser bool (es int)` | Sí |
| `03_bool_mas_int.txt` | Uso de `+` entre `bool` e `int` | `Error semantico: el operador + no mezcla tipos (int + bool)` | Sí |
| `04_int_recibe_bool.txt` | Asignación de `bool` a una variable `int` | `Error semantico: tipos incompatibles en la asignacion a x (int = bool)` | Sí |
| `05_variable_no_declarada_en_bloque.txt` | Uso de una variable no declarada dentro de un bloque | `Error semantico: variable no declarada: w` | Sí |
| `06_if_sin_llaves.txt` | Estructura `if` sin llaves | `Error sintactico en linea 1, columna 52` | Sí |
| `07_falta_parentesis.txt` | Paréntesis faltante en la condición de `while` | `Error sintactico en linea 1, columna 54` | Sí |
| `08_caracter_ilegal.txt` | Carácter no reconocido por el analizador léxico | `Error lexico en linea 1, columna 26: caracter ilegal <$>` | Sí |
| `09_return_incorrecto_en_if.txt` | Retorno con valor dentro de un `main void` | `Error semantico: return con valor en un main void` | Sí |

En los errores semánticos se imprime primero el AST y después el mensaje de error. En los errores sintácticos y léxicos se imprime directamente el mensaje.

### 11.3 Archivos de `src/main/resources/`

| Archivo | Qué contiene | Imprime | Retorna (intérprete) |
|---|---|---|---|
| `entrada.txt` | Ejemplo principal con `x + y * 4` | `14` | `14` |
| `entrada2.txt` | `void main` con `x + 3 * 2 * y` | `7` | `null` (void) |
| `entrada3.txt` | Variables `bool`, `print` de `bool` y expresión entera | `true` y `11` | `6` |
| `error_semantico.txt` | Uso de variable no declarada | `Error semantico: variable no declarada: z` | — |
| `error_sintactico.txt` | Falta un `;` en una declaración | `Error sintactico en linea 1, columna 19` | — |

### 11.4 Ejecutar todas las pruebas de una vez

#### PowerShell (Windows)

```powershell
$jar = "lib\java-cup-11b-runtime.jar"
$asm = Join-Path $env:TEMP "salida_test.s"

Get-ChildItem tests\ok\*.txt, tests\error\*.txt | ForEach-Object {
    Write-Host "`n===== $($_.FullName) ====="
    java -cp "out;$jar" com.ejemplo.Main $_.FullName $asm
}
```

#### Bash (Linux, macOS o WSL)

```bash
for f in tests/ok/*.txt tests/error/*.txt; do
    echo
    echo "===== $f ====="
    java -cp "out:lib/java-cup-11b-runtime.jar" com.ejemplo.Main "$f" /tmp/salida_test.s
done
```

#### CMD (Windows)

En CMD interactivo se puede usar:

```bat
for %f in (tests\ok\*.txt tests\error\*.txt) do (
    echo ===== %f =====
    java -cp "out;lib\java-cup-11b-runtime.jar" com.ejemplo.Main "%f"
)
```

> Si el bucle se guarda en un archivo `.bat`, hay que duplicar el porcentaje: `%%f`.

### 11.5 Sobre el código de salida del proceso

`Main` captura los errores léxicos, sintácticos, semánticos y de ejecución, imprime el mensaje y **termina con código 0** en todos los casos. Por eso:

- El código de salida del proceso **no** distingue entre un programa correcto y uno con error.
- Para verificar una prueba, hay que mirar la **salida impresa** (`Valor retornado`, mensaje de error, etc.).
- Si se quisiera usar en un sistema de integración continua, habría que modificar `Main` para devolver un código distinto ante errores.

### 11.6 Advertencia sobre `09_bucle_infinito.txt`

El intérprete se detiene después de 1.000.000 de iteraciones, pero el assembly x86-64 generado **no** tiene ese límite. Por eso, no se debe ejecutar el binario producido a partir de esta prueba.

---

## 12. Ensamblar y ejecutar el assembly x86-64

Esta etapa es **opcional** e independiente de la interpretación Java. Primero hay que generar `salida.s` con un programa válido.

### 12.1 Instalar GCC

**Ubuntu / Debian / WSL:**

```bash
sudo apt update
sudo apt install -y gcc
```

**Fedora:**

```bash
sudo dnf install -y gcc
```

**Arch Linux / Manjaro:**

```bash
sudo pacman -S --needed gcc
```

**macOS:**

El assembly generado es para GNU/Linux. En macOS se puede instalar GCC con Homebrew, pero lo más recomendable es usar Linux, WSL o Docker para ejecutar `salida.s`.

Verificar:

```bash
gcc --version
```

### 12.2 Generar `salida.s`

Ejecutar el compilador con un programa válido. Por ejemplo, con `entrada.txt`:

**Windows:**

```bat
java -cp "out;lib\java-cup-11b-runtime.jar" com.ejemplo.Main src\main\resources\entrada.txt
```

**Linux, macOS o WSL:**

```bash
java -cp "out:lib/java-cup-11b-runtime.jar" com.ejemplo.Main src/main/resources/entrada.txt
```

Se genera `salida.s` en la carpeta desde donde se ejecutó el comando.

### 12.3 Ensamblar, ejecutar y ver el código de salida

En Linux, macOS o WSL:

```bash
gcc salida.s -o salida
./salida
echo $?
```

Para `entrada.txt`:

- `./salida` imprime `14`.
- `echo $?` muestra `14`.

El código de salida corresponde al valor retornado por `main` en los ejemplos con retorno entero.

> Los códigos de salida del sistema operativo están en el rango 0–255. Si `main` devuelve un número mayor, el sistema lo trunca. Para los ejemplos del proyecto (14, 3, 11, 1, 5, 7, 8, 6) no hay problema.

### 12.4 Ejecutar desde Windows con WSL

Si el proyecto está en Windows y WSL está instalado:

1. Abrir Ubuntu desde el menú Inicio.
2. Ir a la carpeta del proyecto:

```bash
cd "/mnt/c/Users/TuUsuario/Desktop/Proyecto_Compiladores"
```

3. Ensamblar y ejecutar:

```bash
gcc salida.s -o salida
./salida
echo $?
```

Si WSL no tiene GCC:

```bash
sudo apt update
sudo apt install -y gcc
```

### 12.5 Alternativa con Docker

Si no querés instalar WSL, se puede usar Docker con una imagen de GCC.

**Linux/macOS:**

```bash
docker run --rm -v "$PWD:/work" -w /work gcc:latest bash -c "gcc salida.s -o salida && ./salida; echo \$?"
```

**Windows PowerShell:**

```powershell
docker run --rm -v "${PWD}:/work" -w /work gcc:latest bash -c "gcc salida.s -o salida && ./salida; echo \$?"
```

### 12.6 Advertencia

No ejecutar el assembly generado a partir de `tests/ok/09_bucle_infinito.txt`, porque no tiene la protección de 1.000.000 de iteraciones del intérprete.

---

## 13. Solución de problemas

| Síntoma | Causa probable | Solución |
|---|---|---|
| `'javac' no se reconoce como un comando...` | JDK no instalado o `PATH` desactualizado | Instalar JDK 21 y reabrir la terminal |
| `java` funciona pero `javac` no | Solo está instalado el JRE | Instalar el JDK completo |
| `package java_cup.runtime does not exist` | Falta el jar en el classpath | Usar `-cp "lib\java-cup-11b-runtime.jar"` o `-cp "lib/java-cup-11b-runtime.jar"` |
| `Could not find or load main class com.ejemplo.Main` | No se compiló, `out` no existe o el classpath está mal | Compilar primero y usar `-cp "out;..."` o `-cp "out:..."` |
| `No se encontro el archivo ...` | Ruta incorrecta o no estás en la raíz del proyecto | Verificar `dir`/`ls` y usar la ruta correcta |
| `Error sintactico en linea ...` | El programa tiene un error de sintaxis | Revisar paréntesis, llaves, `;`, declaraciones y estructura |
| `Error lexico ... caracter ilegal <$>` | Carácter no reconocido | Eliminar el carácter o usar solo los símbolos del lenguaje |
| `mkdir: cannot create directory 'out': File exists` | La carpeta `out` ya existe | Omitir `mkdir` o usar `mkdir -p` / `New-Item -Force` |
| `The system cannot find the path specified` | La terminal no está en la carpeta correcta | Usar `cd` a la raíz del proyecto |
| `salida.s` no se genera | Hubo un error léxico, sintáctico o semántico | Corregir el programa; la generación solo ocurre si esas etapas pasan |
| `gcc: command not found` | GCC no instalado | Instalar GCC o usar WSL/Linux/Docker |
| `cannot execute binary file` en Windows | El binario es ELF de Linux | Ejecutarlo en Linux, WSL o Docker |
| `Permission denied` al ejecutar `./salida` | Permisos o carpeta montada sin ejecución | Ejecutar en una carpeta de Linux/WSL, no en `/mnt/c` si hay problemas |
| `java_cup` no se reconoce en VS Code | Falta el jar en el classpath del IDE | Usar `launch.json` con `classPaths` o la terminal integrada |
| `java_cup` no se reconoce en IntelliJ | Falta la biblioteca | Agregar `lib/java-cup-11b-runtime.jar` en Project Structure |
| `Cannot resolve symbol 'com.ejemplo'` en IntelliJ | `src/main/java` no está marcado como Sources Root | Clic derecho → Mark Directory as → Sources Root |
| El classpath no funciona | Separador incorrecto | Windows: `;` — Linux/macOS/WSL: `:` |
| Se ven caracteres raros al compilar | Encoding incorrecto | Usar `-encoding UTF-8` |
| Quedaron clases viejas | `out` desactualizado | Borrar `out` y recompilar |
| `Valor retornado: null` | `main` es `void` o no ejecutó ningún `return` con valor | Es el comportamiento esperado en esos casos |

### 13.1 Verificar la instalación completa

```bash
java -version
javac -version
git --version
gcc --version
```

No todos son obligatorios. Los dos primeros sí.

### 13.2 Verificar que el jar de CUP existe

**Windows:**

```bat
dir lib\java-cup-11b-runtime.jar
```

**Linux, macOS o WSL:**

```bash
ls -l lib/java-cup-11b-runtime.jar
```

### 13.3 Verificar que se generaron las clases

**Windows:**

```bat
dir out\com\ejemplo\Main.class
```

**Linux, macOS o WSL:**

```bash
ls -l out/com/ejemplo/Main.class
```

Si no existe, la compilación no se ejecutó correctamente.

---

## 14. Checklist de entrega

Antes de entregar o defender el proyecto, verificar:

- [ ] Estoy en la carpeta raíz correcta (la que tiene `lib/`, `src/`, `tests/`, `pom.xml`, `README.md`).
- [ ] `java -version` muestra Java 21.
- [ ] `javac -version` muestra Java 21.
- [ ] La compilación termina sin errores.
- [ ] `entrada.txt` imprime `14` y muestra `Valor retornado: 14`.
- [ ] Se genera `salida.s` al ejecutar un programa válido.
- [ ] Las 9 pruebas de `tests/ok/` dan los resultados de la tabla.
- [ ] Las 9 pruebas de `tests/error/` dan los mensajes de la tabla.
- [ ] Los 5 archivos de `src/main/resources/` dan los resultados de la tabla.
- [ ] El README coincide con el comportamiento real.
- [ ] Si se usó GCC/WSL, el assembly de `entrada.txt` imprime `14` y devuelve código 14.
- [ ] No se ejecutó el assembly de `09_bucle_infinito.txt`.
- [ ] El README de GitHub está actualizado (ver sección 15).

---

## 15. Actualizar el README en GitHub

El README que estás leyendo es el de la **copia local**. El repositorio de GitHub puede tener una versión anterior hasta que se haga un `commit` y un `push`.

### Opción A: Git desde la terminal

Desde la raíz del proyecto:

```bash
git status
git diff -- README.md
git add README.md
git commit -m "docs: completa guía de instalación, uso y pruebas"
git push origin main
```

> Ejecutar estos comandos solamente si tenés permisos de escritura en el repositorio y si ya revisaste los cambios con `git diff`.

### Opción B: Editar directamente en GitHub

1. Abrir el repositorio en GitHub.
2. Entrar a `README.md`.
3. Hacer clic en el lápiz (**Edit this file**).
4. Reemplazar el contenido por el de este README.
5. Hacer clic en **Commit changes**.

### Verificar que GitHub quedó actualizado

1. Abrir [https://github.com/PaulaChiecher/Proyecto_Compiladores](https://github.com/PaulaChiecher/Proyecto_Compiladores).
2. Verificar que el README muestre esta guía completa.
3. Si no se ve, revisar que el `push` haya terminado correctamente y que estés mirando la rama `main`.

---

## 16. Guía para explicar el proyecto

Esta sección sirve para preparar la defensa oral o una explicación rápida ante el docente.

### 16.1 Explicación en 5 minutos

1. **Objetivo:** construir un compilador para un mini lenguaje con `int`, `bool`, `void`, asignaciones, `print`, `return`, `if`, `else` y `while`.
2. **Tecnologías:** Java 21, JFlex para el lexer y CUP para el parser.
3. **Pipeline:** código fuente → lexer → parser → AST → análisis semántico → intérprete → pseudo-assembly → C3D → assembly x86-64.
4. **Lexer:** reconoce tokens, ignora espacios y reporta caracteres ilegales con línea y columna.
5. **Parser:** valida la gramática, construye el AST y reporta errores sintácticos con línea y columna.
6. **Semántico:** usa una tabla de símbolos para verificar declaraciones, usos y tipos.
7. **Intérprete:** ejecuta el AST, imprime resultados, conserva el `return` y protege contra bucles infinitos.
8. **Generación de código:** primero pseudo-assembly didáctico, luego C3D con temporales y etiquetas, y finalmente assembly x86-64 AT&T.
9. **Pruebas:** 9 casos correctos, 9 casos de error y 5 archivos de ejemplo.
10. **Limitaciones:** no hay operadores relacionales, división, resta, funciones adicionales ni comentarios.

### 16.2 Preguntas probables y respuestas

**¿Por qué usaron JFlex y CUP?**  
Porque son herramientas estándar para generar lexers y parsers en Java, permiten separar la especificación léxica de la gramática y generan código reproducible.

**¿Qué hace el lexer?**  
Convierte el texto en tokens, controla línea y columna, ignora espacios y detecta caracteres ilegales.

**¿Qué hace el parser?**  
Verifica que la secuencia de tokens respete la gramática y construye el AST. La gramática actual tiene 0 conflictos.

**¿Cómo se detecta una variable no declarada?**  
La tabla de símbolos guarda las variables declaradas. Cuando el analizador semántico o el intérprete busca una variable que no está, lanza un error.

**¿Cómo se controlan los tipos?**  
El analizador semántico calcula el tipo de cada expresión: `int`, `bool` o `void`. Verifica asignaciones, condiciones, `return`, `+` y `*`.

**¿Por qué `bool + bool` es OR lógico?**  
Es una decisión de diseño del lenguaje: el operador `+` está sobrecargado. Si ambos operandos son `bool`, se interpreta como OR; si ambos son `int`, se suma.

**¿Qué diferencia hay entre el pseudo-assembly y el assembly x86-64?**  
El pseudo-assembly es una representación didáctica de máquina de pila. El assembly x86-64 es código real para GNU/Linux, con registros, pila y llamadas a `printf`.

**¿Cómo se generan los temporales y las etiquetas?**  
`GeneradorC3D` usa contadores: `_t1`, `_t2`, ... para temporales y `L1`, `L2`, ... para etiquetas.

**¿Qué pasa con los bucles infinitos?**  
El intérprete cuenta las iteraciones y se detiene después de 1.000.000. El assembly generado no tiene ese límite, por eso no se debe ejecutar el assembly de esa prueba.

**¿Se exige que todos los caminos tengan `return`?**  
No. Se comprueban los `return` que aparecen, pero no se analizan todos los caminos posibles.

**¿Cómo se probó el proyecto?**  
Con 9 programas correctos, 9 programas con errores y 5 archivos de ejemplo. Las tablas de la sección 11 muestran la salida real de cada uno.

**¿Cómo se extendería el lenguaje?**  
Agregando tokens en `Lexer.flex`, producciones en `parser.cup`, nodos en `AST.java`, reglas en `AnalizadorSemantico.java`, evaluación en `Interprete.java` y generación en `GeneradorC3D.java` y `GeneradorAssembly.java`.

### 16.3 Demostración sugerida

1. Mostrar la estructura del proyecto.
2. Compilar en vivo con el comando de la sección 6.
3. Ejecutar `entrada.txt` y mostrar el AST, la tabla de símbolos, el resultado `14` y el assembly generado.
4. Ejecutar `tests/ok/02_if_else.txt` y mostrar `3`.
5. Ejecutar `tests/error/01_condicion_while_int.txt` y mostrar el error semántico.
6. Ejecutar `tests/error/08_caracter_ilegal.txt` y mostrar el error léxico.
7. Mostrar `salida.s` y explicar las instrucciones principales.
8. Si hay GCC/WSL, ensamblar y ejecutar `salida.s`.

---

## Estado de verificación

La versión actual del proyecto fue verificada con:

- **Java:** OpenJDK 21.0.7 (Corretto 21.0.7).
- **Compilación:** `javac -encoding UTF-8 -cp "lib/java-cup-11b-runtime.jar" -d out src/main/java/com/ejemplo/*.java` → sin errores (solo un aviso de API obsoleta en `Parser.java`).
- **Pruebas:** se ejecutaron los 9 archivos de `tests/ok/`, los 9 de `tests/error/` y los 5 de `src/main/resources/`. Los resultados son los de las tablas de la sección 11.
- **JFlex/CUP:** la regeneración produce `0 errors`, `0 warnings` y `0 conflicts detected`. Los archivos generados coinciden con los incluidos, salvo por la fecha de generación.
- **GCC/WSL:** no se pudo ejecutar el paso de ensamblado en el entorno de verificación porque no hay GCC ni WSL instalados. Los comandos de la sección 12 son los estándar para GNU/Linux.

---

**Proyecto académico desarrollado para la materia Compiladores — Ingeniería en Sistemas de Información — UNViMe.**
