package com.ejemplo;

import java_cup.runtime.*;

%%

%class Lexer
%cupsym Sym
%line
%column
%cup

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]

dec_int_lit = 0 | [1-9][0-9]*
dec_int_id  = [A-Za-z][A-Za-z0-9]*

%%

<YYINITIAL> {

    "int"      { return symbol(Sym.INT); }
    "bool"     { return symbol(Sym.BOOL); }
    "void"     { return symbol(Sym.VOID); }
    "main"     { return symbol(Sym.MAIN); }
    "return"   { return symbol(Sym.RETURN); }
    "print"    { return symbol(Sym.PRINT); }
    "if"       { return symbol(Sym.IF); }
    "else"     { return symbol(Sym.ELSE); }
    "while"    { return symbol(Sym.WHILE); }
    "true"     { return symbol(Sym.TRUE); }
    "false"    { return symbol(Sym.FALSE); }

    ";"        { return symbol(Sym.SEMI); }
    "+"        { return symbol(Sym.PLUS); }
    "*"        { return symbol(Sym.TIMES); }
    "="        { return symbol(Sym.EQUAL); }
    "("        { return symbol(Sym.LPAREN); }
    ")"        { return symbol(Sym.RPAREN); }
    "{"        { return symbol(Sym.LBRACE); }
    "}"        { return symbol(Sym.RBRACE); }

    {dec_int_lit}  { return symbol(Sym.NUMBER, Integer.valueOf(yytext())); }
    {dec_int_id}   { return symbol(Sym.ID, yytext()); }

    {WhiteSpace}   { /* ignorar */ }
}

[^]  {
        throw new Error("Error lexico en linea " + (yyline + 1) + ", columna " + (yycolumn + 1)
                        + ": caracter ilegal <" + yytext() + ">");
     }
