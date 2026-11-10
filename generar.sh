#!/bin/sh
# Regenera Lexer.java, Parser.java y Sym.java (solo hace falta si se cambia Lexer.flex o parser.cup)
java -jar lib/jflex-full-1.9.1.jar -d src/main/java/com/ejemplo src/main/jflex/Lexer.flex
java -cp lib/java-cup-11b-2015.03.26.jar java_cup.Main -destdir src/main/java/com/ejemplo -parser Parser -symbols Sym src/main/cup/parser.cup
