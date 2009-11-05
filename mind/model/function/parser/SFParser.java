// Output created by jacc on Wed Sep 08 10:56:24 CEST 2004

package mind.model.function.parser;

    import mind.model.*;
    import mind.model.function.*;
    import mind.model.function.parser.*;
    import java.util.*;

/*
 * throws Exception - this needs to bee added to the parse() method in the
 * generatde source code otherwise it will Not compile.
 */

public
class SFParser implements SFParserTokens {
    private int yyss = 100;
    private int yytok;
    private int yysp = 0;
    private int[] yyst;
    protected int yyerrno = (-1);
    private Object[] yysv;
    private Object yyrv;

    public boolean parse()throws Exception {
        int yyn = 0;
        yysp = 0;
        yyst = new int[yyss];
        yyerrno = (-1);
        yysv = new Object[yyss];
        yytok = (lexer.token
                 );
    loop:
        for (;;) {
            switch (yyn) {
                case 0:
                    yyst[yysp] = 0;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 126:
                    yyn = yys0();
                    continue;

                case 1:
                    yyst[yysp] = 1;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 127:
                    switch (yytok) {
                        case ENDINPUT:
                            yyn = 252;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 2:
                    yyst[yysp] = 2;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 128:
                    switch (yytok) {
                        case RPAR:
                            yyn = yyerr(9, 255);
                            continue;
                        case ADD:
                            yyn = 22;
                            continue;
                        case NEG:
                            yyn = 23;
                            continue;
                        case OP:
                        case ENDINPUT:
                            yyn = yyr2();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 3:
                    yyst[yysp] = 3;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 129:
                    switch (yytok) {
                        case RPAR:
                            yyn = yyerr(9, 255);
                            continue;
                        case ADD:
                            yyn = 24;
                            continue;
                        case NEG:
                            yyn = 25;
                            continue;
                        case OP:
                        case ENDINPUT:
                            yyn = yyr3();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 4:
                    yyst[yysp] = 4;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 130:
                    yyn = yys4();
                    continue;

                case 5:
                    yyst[yysp] = 5;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 131:
                    switch (yytok) {
                        case RPAR:
                        case OP:
                        case NEG:
                        case ENDINPUT:
                        case ADD:
                            yyn = yyr12();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 6:
                    yyst[yysp] = 6;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 132:
                    yyn = yys6();
                    continue;

                case 7:
                    yyst[yysp] = 7;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 133:
                    switch (yytok) {
                        case RPAR:
                        case OP:
                        case NEG:
                        case ENDINPUT:
                        case ADD:
                            yyn = yyr10();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 8:
                    yyst[yysp] = 8;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 134:
                    yyn = yys8();
                    continue;

                case 9:
                    yyst[yysp] = 9;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 135:
                    switch (yytok) {
                        case RPAR:
                        case OP:
                        case NEG:
                        case ENDINPUT:
                        case ADD:
                            yyn = yyr11();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 10:
                    yyst[yysp] = 10;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 136:
                    switch (yytok) {
                        case OP:
                            yyn = 32;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 11:
                    yyst[yysp] = 11;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 137:
                    yyn = yys11();
                    continue;

                case 12:
                    yyst[yysp] = 12;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 138:
                    switch (yytok) {
                        case RPAR:
                        case OP:
                        case NEG:
                        case ENDINPUT:
                        case ADD:
                            yyn = yyr13();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 13:
                    yyst[yysp] = 13;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 139:
                    yyn = yys13();
                    continue;

                case 14:
                    yyst[yysp] = 14;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 140:
                    yyn = yys14();
                    continue;

                case 15:
                    yyst[yysp] = 15;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 141:
                    yyn = yys15();
                    continue;

                case 16:
                    yyst[yysp] = 16;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 142:
                    yyn = yys16();
                    continue;

                case 17:
                    yyst[yysp] = 17;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 143:
                    yyn = yys17();
                    continue;

                case 18:
                    yyst[yysp] = 18;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 144:
                    yyn = yys18();
                    continue;

                case 19:
                    yyst[yysp] = 19;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 145:
                    yyn = yys19();
                    continue;

                case 20:
                    yyst[yysp] = 20;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 146:
                    yyn = yys20();
                    continue;

                case 21:
                    yyst[yysp] = 21;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 147:
                    yyn = yys21();
                    continue;

                case 22:
                    yyst[yysp] = 22;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 148:
                    yyn = yys22();
                    continue;

                case 23:
                    yyst[yysp] = 23;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 149:
                    yyn = yys23();
                    continue;

                case 24:
                    yyst[yysp] = 24;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 150:
                    yyn = yys24();
                    continue;

                case 25:
                    yyst[yysp] = 25;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 151:
                    yyn = yys25();
                    continue;

                case 26:
                    yyst[yysp] = 26;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 152:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 27:
                    yyst[yysp] = 27;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 153:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 28:
                    yyst[yysp] = 28;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 154:
                    yyn = yys28();
                    continue;

                case 29:
                    yyst[yysp] = 29;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 155:
                    yyn = yys29();
                    continue;

                case 30:
                    yyst[yysp] = 30;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 156:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 31:
                    yyst[yysp] = 31;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 157:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 32:
                    yyst[yysp] = 32;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 158:
                    yyn = yys32();
                    continue;

                case 33:
                    yyst[yysp] = 33;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 159:
                    yyn = yys33();
                    continue;

                case 34:
                    yyst[yysp] = 34;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 160:
                    yyn = yys34();
                    continue;

                case 35:
                    yyst[yysp] = 35;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 161:
                    switch (yytok) {
                        case ENDINPUT:
                            yyn = yyerr(10, 255);
                            continue;
                        case ADD:
                            yyn = 22;
                            continue;
                        case NEG:
                            yyn = 23;
                            continue;
                        case RPAR:
                            yyn = 66;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 36:
                    yyst[yysp] = 36;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 162:
                    switch (yytok) {
                        case OP:
                        case ENDINPUT:
                            yyn = yyerr(10, 255);
                            continue;
                        case ADD:
                            yyn = 24;
                            continue;
                        case NEG:
                            yyn = 25;
                            continue;
                        case RPAR:
                            yyn = 67;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 37:
                    yyst[yysp] = 37;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 163:
                    yyn = yys37();
                    continue;

                case 38:
                    yyst[yysp] = 38;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 164:
                    yyn = yys38();
                    continue;

                case 39:
                    yyst[yysp] = 39;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 165:
                    yyn = yys39();
                    continue;

                case 40:
                    yyst[yysp] = 40;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 166:
                    yyn = yys40();
                    continue;

                case 41:
                    yyst[yysp] = 41;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 167:
                    yyn = yys41();
                    continue;

                case 42:
                    yyst[yysp] = 42;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 168:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 43:
                    yyst[yysp] = 43;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 169:
                    switch (yytok) {
                        case RPAR:
                        case OP:
                        case NEG:
                        case ENDINPUT:
                        case ADD:
                            yyn = yyr14();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 44:
                    yyst[yysp] = 44;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 170:
                    switch (yytok) {
                        case RPAR:
                        case OP:
                        case NEG:
                        case ENDINPUT:
                        case ADD:
                            yyn = yyr6();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 45:
                    yyst[yysp] = 45;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 171:
                    switch (yytok) {
                        case RPAR:
                        case OP:
                        case NEG:
                        case ENDINPUT:
                        case ADD:
                            yyn = yyr15();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 46:
                    yyst[yysp] = 46;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 172:
                    switch (yytok) {
                        case RPAR:
                        case OP:
                        case NEG:
                        case ENDINPUT:
                        case ADD:
                            yyn = yyr7();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 47:
                    yyst[yysp] = 47;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 173:
                    switch (yytok) {
                        case RPAR:
                        case OP:
                        case NEG:
                        case ENDINPUT:
                        case ADD:
                            yyn = yyr4();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 48:
                    yyst[yysp] = 48;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 174:
                    switch (yytok) {
                        case RPAR:
                        case OP:
                        case NEG:
                        case ENDINPUT:
                        case ADD:
                            yyn = yyr8();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 49:
                    yyst[yysp] = 49;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 175:
                    switch (yytok) {
                        case RPAR:
                        case OP:
                        case NEG:
                        case ENDINPUT:
                        case ADD:
                            yyn = yyr5();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 50:
                    yyst[yysp] = 50;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 176:
                    switch (yytok) {
                        case RPAR:
                        case OP:
                        case NEG:
                        case ENDINPUT:
                        case ADD:
                            yyn = yyr9();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 51:
                    yyst[yysp] = 51;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 177:
                    yyn = yys51();
                    continue;

                case 52:
                    yyst[yysp] = 52;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 178:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 53:
                    yyst[yysp] = 53;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 179:
                    yyn = yys53();
                    continue;

                case 54:
                    yyst[yysp] = 54;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 180:
                    yyn = yys54();
                    continue;

                case 55:
                    yyst[yysp] = 55;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 181:
                    yyn = yys55();
                    continue;

                case 56:
                    yyst[yysp] = 56;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 182:
                    yyn = yys56();
                    continue;

                case 57:
                    yyst[yysp] = 57;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 183:
                    yyn = yys57();
                    continue;

                case 58:
                    yyst[yysp] = 58;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 184:
                    switch (yytok) {
                        case ENDINPUT:
                            yyn = yyr1();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 59:
                    yyst[yysp] = 59;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 185:
                    yyn = yys59();
                    continue;

                case 60:
                    yyst[yysp] = 60;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 186:
                    yyn = yys60();
                    continue;

                case 61:
                    yyst[yysp] = 61;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 187:
                    yyn = yys61();
                    continue;

                case 62:
                    yyst[yysp] = 62;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 188:
                    yyn = yys62();
                    continue;

                case 63:
                    yyst[yysp] = 63;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 189:
                    yyn = yys63();
                    continue;

                case 64:
                    yyst[yysp] = 64;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 190:
                    yyn = yys64();
                    continue;

                case 65:
                    yyst[yysp] = 65;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 191:
                    yyn = yys65();
                    continue;

                case 66:
                    yyst[yysp] = 66;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 192:
                    yyn = yys66();
                    continue;

                case 67:
                    yyst[yysp] = 67;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 193:
                    yyn = yys67();
                    continue;

                case 68:
                    yyst[yysp] = 68;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 194:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 69:
                    yyst[yysp] = 69;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 195:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 70:
                    yyst[yysp] = 70;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 196:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 71:
                    yyst[yysp] = 71;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 197:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 72:
                    yyst[yysp] = 72;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 198:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 73:
                    yyst[yysp] = 73;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 199:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 74:
                    yyst[yysp] = 74;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 200:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 75:
                    yyst[yysp] = 75;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 201:
                    switch (yytok) {
                        case ADD:
                            yyn = 24;
                            continue;
                        case NEG:
                            yyn = 25;
                            continue;
                        case RPAR:
                            yyn = 96;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 76:
                    yyst[yysp] = 76;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 202:
                    switch (yytok) {
                        case RPAR:
                            yyn = 66;
                            continue;
                        case ADD:
                            yyn = 97;
                            continue;
                        case NEG:
                            yyn = 98;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 77:
                    yyst[yysp] = 77;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 203:
                    switch (yytok) {
                        case DIV:
                            yyn = 33;
                            continue;
                        case MUL:
                            yyn = 74;
                            continue;
                        case RPAR:
                        case NEG:
                        case ADD:
                            yyn = yyr16();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 78:
                    yyst[yysp] = 78;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 204:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 79:
                    yyst[yysp] = 79;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 205:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 80:
                    yyst[yysp] = 80;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 206:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 81:
                    yyst[yysp] = 81;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 207:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 82:
                    yyst[yysp] = 82;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 208:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 83:
                    yyst[yysp] = 83;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 209:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 84:
                    yyst[yysp] = 84;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 210:
                    switch (yytok) {
                        case ADD:
                            yyn = 24;
                            continue;
                        case NEG:
                            yyn = 25;
                            continue;
                        case RPAR:
                            yyn = 105;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 85:
                    yyst[yysp] = 85;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 211:
                    yyn = yys85();
                    continue;

                case 86:
                    yyst[yysp] = 86;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 212:
                    yyn = yys86();
                    continue;

                case 87:
                    yyst[yysp] = 87;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 213:
                    yyn = yys87();
                    continue;

                case 88:
                    yyst[yysp] = 88;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 214:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 89:
                    yyst[yysp] = 89;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 215:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 90:
                    yyst[yysp] = 90;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 216:
                    yyn = yys90();
                    continue;

                case 91:
                    yyst[yysp] = 91;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 217:
                    yyn = yys91();
                    continue;

                case 92:
                    yyst[yysp] = 92;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 218:
                    yyn = yys92();
                    continue;

                case 93:
                    yyst[yysp] = 93;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 219:
                    yyn = yys93();
                    continue;

                case 94:
                    yyst[yysp] = 94;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 220:
                    yyn = yys94();
                    continue;

                case 95:
                    yyst[yysp] = 95;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 221:
                    yyn = yys95();
                    continue;

                case 96:
                    yyst[yysp] = 96;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 222:
                    switch (yytok) {
                        case RPAR:
                        case OP:
                        case NEG:
                        case ENDINPUT:
                        case ADD:
                            yyn = yyr65();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 97:
                    yyst[yysp] = 97;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 223:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 98:
                    yyst[yysp] = 98;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 224:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 99:
                    yyst[yysp] = 99;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 225:
                    yyn = yys99();
                    continue;

                case 100:
                    yyst[yysp] = 100;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 226:
                    yyn = yys100();
                    continue;

                case 101:
                    yyst[yysp] = 101;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 227:
                    yyn = yys101();
                    continue;

                case 102:
                    yyst[yysp] = 102;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 228:
                    yyn = yys102();
                    continue;

                case 103:
                    yyst[yysp] = 103;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 229:
                    yyn = yys103();
                    continue;

                case 104:
                    yyst[yysp] = 104;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 230:
                    yyn = yys104();
                    continue;

                case 105:
                    yyst[yysp] = 105;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 231:
                    yyn = yys105();
                    continue;

                case 106:
                    yyst[yysp] = 106;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 232:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 107:
                    yyst[yysp] = 107;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 233:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 108:
                    yyst[yysp] = 108;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 234:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 109:
                    yyst[yysp] = 109;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 235:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 110:
                    yyst[yysp] = 110;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 236:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 111:
                    yyst[yysp] = 111;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 237:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 112:
                    yyst[yysp] = 112;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 238:
                    yyn = yys112();
                    continue;

                case 113:
                    yyst[yysp] = 113;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 239:
                    yyn = yys113();
                    continue;

                case 114:
                    yyst[yysp] = 114;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 240:
                    switch (yytok) {
                        case RPAR:
                        case NEG:
                        case ADD:
                            yyn = yyr14();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 115:
                    yyst[yysp] = 115;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 241:
                    switch (yytok) {
                        case RPAR:
                        case NEG:
                        case ADD:
                            yyn = yyr15();
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 116:
                    yyst[yysp] = 116;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 242:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 117:
                    yyst[yysp] = 117;
                    yysv[yysp] = (lexer.semVal
                                 );
                    yytok = (lex()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 243:
                    switch (yytok) {
                        case CONST:
                            yyn = 13;
                            continue;
                        case NEG:
                            yyn = 42;
                            continue;
                        case LPAR:
                            yyn = 52;
                            continue;
                    }
                    yyn = 255;
                    continue;

                case 118:
                    yyst[yysp] = 118;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 244:
                    yyn = yys118();
                    continue;

                case 119:
                    yyst[yysp] = 119;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 245:
                    yyn = yys119();
                    continue;

                case 120:
                    yyst[yysp] = 120;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 246:
                    yyn = yys120();
                    continue;

                case 121:
                    yyst[yysp] = 121;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 247:
                    yyn = yys121();
                    continue;

                case 122:
                    yyst[yysp] = 122;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 248:
                    yyn = yys122();
                    continue;

                case 123:
                    yyst[yysp] = 123;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 249:
                    yyn = yys123();
                    continue;

                case 124:
                    yyst[yysp] = 124;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 250:
                    yyn = yys124();
                    continue;

                case 125:
                    yyst[yysp] = 125;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 251:
                    yyn = yys125();
                    continue;

                case 252:
                    return true;
                case 253:
                    yyerror("stack overflow");
                case 254:
                    return false;
                case 255:
                    yyerror("syntax error");
                    return false;
            }
        }
    }

    protected void yyexpand() {
        int[] newyyst = new int[2*yyst.length];
        Object[] newyysv = new Object[2*yyst.length];
        for (int i=0; i<yyst.length; i++) {
            newyyst[i] = yyst[i];
            newyysv[i] = yysv[i];
        }
        yyst = newyyst;
        yysv = newyysv;
    }

    private int yys0() {
        switch (yytok) {
            case OP:
                return yyerr(1, 255);
            case ADD:
                return yyerr(6, 255);
            case MUL:
                return yyerr(7, 255);
            case DIV:
                return yyerr(8, 255);
            case CONST:
                return 13;
            case FLOATVAR:
                return 14;
            case FLOATVARTSO:
                return 15;
            case FLOWVAR:
                return 16;
            case FLOWVARTSO:
                return 17;
            case INTVAR:
                return 18;
            case INTVARTSO:
                return 19;
            case LPAR:
                return 20;
            case NEG:
                return 21;
        }
        return 255;
    }

    private int yys4() {
        switch (yytok) {
            case DIV:
                return 26;
            case MUL:
                return 27;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr61();
        }
        return 255;
    }

    private int yys6() {
        switch (yytok) {
            case DIV:
                return 28;
            case MUL:
                return 29;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr33();
        }
        return 255;
    }

    private int yys8() {
        switch (yytok) {
            case DIV:
                return 30;
            case MUL:
                return 31;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr47();
        }
        return 255;
    }

    private int yys11() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 34;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr16();
        }
        return 255;
    }

    private int yys13() {
        switch (yytok) {
            case RPAR:
            case OP:
            case NEG:
            case MUL:
            case ENDINPUT:
            case DIV:
            case ADD:
                return yyr21();
        }
        return 255;
    }

    private int yys14() {
        switch (yytok) {
            case RPAR:
            case OP:
            case NEG:
            case MUL:
            case ENDINPUT:
            case DIV:
            case ADD:
                return yyr62();
        }
        return 255;
    }

    private int yys15() {
        switch (yytok) {
            case RPAR:
            case OP:
            case NEG:
            case MUL:
            case ENDINPUT:
            case DIV:
            case ADD:
                return yyr63();
        }
        return 255;
    }

    private int yys16() {
        switch (yytok) {
            case RPAR:
            case OP:
            case NEG:
            case MUL:
            case ENDINPUT:
            case DIV:
            case ADD:
                return yyr34();
        }
        return 255;
    }

    private int yys17() {
        switch (yytok) {
            case RPAR:
            case OP:
            case NEG:
            case MUL:
            case ENDINPUT:
            case DIV:
            case ADD:
                return yyr35();
        }
        return 255;
    }

    private int yys18() {
        switch (yytok) {
            case RPAR:
            case OP:
            case NEG:
            case MUL:
            case ENDINPUT:
            case DIV:
            case ADD:
                return yyr48();
        }
        return 255;
    }

    private int yys19() {
        switch (yytok) {
            case RPAR:
            case OP:
            case NEG:
            case MUL:
            case ENDINPUT:
            case DIV:
            case ADD:
                return yyr49();
        }
        return 255;
    }

    private int yys20() {
        switch (yytok) {
            case ADD:
                return yyerr(6, 255);
            case MUL:
                return yyerr(7, 255);
            case DIV:
                return yyerr(8, 255);
            case CONST:
                return 13;
            case FLOATVAR:
                return 14;
            case FLOATVARTSO:
                return 15;
            case FLOWVAR:
                return 16;
            case FLOWVARTSO:
                return 17;
            case INTVAR:
                return 18;
            case INTVARTSO:
                return 19;
            case LPAR:
                return 20;
            case NEG:
                return 21;
        }
        return 255;
    }

    private int yys21() {
        switch (yytok) {
            case CONST:
                return 13;
            case FLOATVAR:
                return 14;
            case FLOATVARTSO:
                return 15;
            case FLOWVAR:
                return 16;
            case FLOWVARTSO:
                return 17;
            case INTVAR:
                return 18;
            case INTVARTSO:
                return 19;
            case LPAR:
                return 41;
            case NEG:
                return 42;
        }
        return 255;
    }

    private int yys22() {
        switch (yytok) {
            case OP:
            case RPAR:
            case ENDINPUT:
                return yyerr(2, 255);
            case CONST:
                return 13;
            case FLOATVAR:
                return 14;
            case FLOATVARTSO:
                return 15;
            case FLOWVAR:
                return 16;
            case FLOWVARTSO:
                return 17;
            case INTVAR:
                return 18;
            case INTVARTSO:
                return 19;
            case LPAR:
                return 20;
            case NEG:
                return 21;
        }
        return 255;
    }

    private int yys23() {
        switch (yytok) {
            case OP:
            case RPAR:
            case ENDINPUT:
                return yyerr(3, 255);
            case CONST:
                return 13;
            case FLOATVAR:
                return 14;
            case FLOATVARTSO:
                return 15;
            case FLOWVAR:
                return 16;
            case FLOWVARTSO:
                return 17;
            case INTVAR:
                return 18;
            case INTVARTSO:
                return 19;
            case LPAR:
                return 20;
            case NEG:
                return 21;
        }
        return 255;
    }

    private int yys24() {
        switch (yytok) {
            case OP:
            case RPAR:
            case ENDINPUT:
                return yyerr(2, 255);
            case CONST:
                return 13;
            case FLOATVAR:
                return 14;
            case FLOATVARTSO:
                return 15;
            case FLOWVAR:
                return 16;
            case FLOWVARTSO:
                return 17;
            case INTVAR:
                return 18;
            case INTVARTSO:
                return 19;
            case LPAR:
                return 20;
            case NEG:
                return 21;
        }
        return 255;
    }

    private int yys25() {
        switch (yytok) {
            case OP:
            case RPAR:
            case ENDINPUT:
                return yyerr(3, 255);
            case CONST:
                return 13;
            case FLOATVAR:
                return 14;
            case FLOATVARTSO:
                return 15;
            case FLOWVAR:
                return 16;
            case FLOWVARTSO:
                return 17;
            case INTVAR:
                return 18;
            case INTVARTSO:
                return 19;
            case LPAR:
                return 20;
            case NEG:
                return 21;
        }
        return 255;
    }

    private int yys28() {
        switch (yytok) {
            case RPAR:
            case OP:
            case ENDINPUT:
                return yyerr(5, 255);
            case CONST:
                return 13;
            case NEG:
                return 42;
            case LPAR:
                return 52;
        }
        return 255;
    }

    private int yys29() {
        switch (yytok) {
            case RPAR:
            case OP:
            case ENDINPUT:
                return yyerr(4, 255);
            case CONST:
                return 13;
            case NEG:
                return 42;
            case LPAR:
                return 52;
        }
        return 255;
    }

    private int yys32() {
        switch (yytok) {
            case ENDINPUT:
                return yyerr(0, 255);
            case ADD:
                return yyerr(6, 255);
            case MUL:
                return yyerr(7, 255);
            case DIV:
                return yyerr(8, 255);
            case CONST:
                return 13;
            case FLOATVAR:
                return 14;
            case FLOATVARTSO:
                return 15;
            case FLOWVAR:
                return 16;
            case FLOWVARTSO:
                return 17;
            case INTVAR:
                return 18;
            case INTVARTSO:
                return 19;
            case LPAR:
                return 20;
            case NEG:
                return 21;
        }
        return 255;
    }

    private int yys33() {
        switch (yytok) {
            case RPAR:
            case OP:
            case ENDINPUT:
                return yyerr(5, 255);
            case CONST:
                return 13;
            case NEG:
                return 42;
            case LPAR:
                return 52;
        }
        return 255;
    }

    private int yys34() {
        switch (yytok) {
            case OP:
            case RPAR:
            case ENDINPUT:
                return yyerr(4, 255);
            case CONST:
                return 13;
            case FLOATVAR:
                return 14;
            case FLOATVARTSO:
                return 15;
            case FLOWVAR:
                return 16;
            case FLOWVARTSO:
                return 17;
            case INTVAR:
                return 18;
            case INTVARTSO:
                return 19;
            case LPAR:
                return 64;
            case NEG:
                return 65;
        }
        return 255;
    }

    private int yys37() {
        switch (yytok) {
            case DIV:
                return 68;
            case MUL:
                return 69;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr58();
        }
        return 255;
    }

    private int yys38() {
        switch (yytok) {
            case DIV:
                return 70;
            case MUL:
                return 71;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr30();
        }
        return 255;
    }

    private int yys39() {
        switch (yytok) {
            case DIV:
                return 72;
            case MUL:
                return 73;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr44();
        }
        return 255;
    }

    private int yys40() {
        switch (yytok) {
            case RPAR:
            case OP:
            case NEG:
            case MUL:
            case ENDINPUT:
            case DIV:
            case ADD:
                return yyr20();
        }
        return 255;
    }

    private int yys41() {
        switch (yytok) {
            case CONST:
                return 13;
            case FLOATVAR:
                return 14;
            case FLOATVARTSO:
                return 15;
            case FLOWVAR:
                return 16;
            case FLOWVARTSO:
                return 17;
            case INTVAR:
                return 18;
            case INTVARTSO:
                return 19;
            case LPAR:
                return 20;
            case NEG:
                return 21;
        }
        return 255;
    }

    private int yys51() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr60();
        }
        return 255;
    }

    private int yys53() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr59();
        }
        return 255;
    }

    private int yys54() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr32();
        }
        return 255;
    }

    private int yys55() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr31();
        }
        return 255;
    }

    private int yys56() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr46();
        }
        return 255;
    }

    private int yys57() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr45();
        }
        return 255;
    }

    private int yys59() {
        switch (yytok) {
            case RPAR:
            case OP:
            case NEG:
            case MUL:
            case ENDINPUT:
            case DIV:
            case ADD:
                return yyr18();
        }
        return 255;
    }

    private int yys60() {
        switch (yytok) {
            case DIV:
                return 78;
            case MUL:
                return 79;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr52();
        }
        return 255;
    }

    private int yys61() {
        switch (yytok) {
            case DIV:
                return 80;
            case MUL:
                return 81;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr24();
        }
        return 255;
    }

    private int yys62() {
        switch (yytok) {
            case DIV:
                return 82;
            case MUL:
                return 83;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr38();
        }
        return 255;
    }

    private int yys63() {
        switch (yytok) {
            case RPAR:
            case OP:
            case NEG:
            case MUL:
            case ENDINPUT:
            case DIV:
            case ADD:
                return yyr17();
        }
        return 255;
    }

    private int yys64() {
        switch (yytok) {
            case CONST:
                return 13;
            case FLOATVAR:
                return 14;
            case FLOATVARTSO:
                return 15;
            case FLOWVAR:
                return 16;
            case FLOWVARTSO:
                return 17;
            case INTVAR:
                return 18;
            case INTVARTSO:
                return 19;
            case LPAR:
                return 20;
            case NEG:
                return 21;
        }
        return 255;
    }

    private int yys65() {
        switch (yytok) {
            case CONST:
                return 13;
            case FLOATVAR:
                return 14;
            case FLOATVARTSO:
                return 15;
            case FLOWVAR:
                return 16;
            case FLOWVARTSO:
                return 17;
            case INTVAR:
                return 18;
            case INTVARTSO:
                return 19;
            case NEG:
                return 42;
            case LPAR:
                return 52;
        }
        return 255;
    }

    private int yys66() {
        switch (yytok) {
            case RPAR:
            case OP:
            case NEG:
            case MUL:
            case ENDINPUT:
            case DIV:
            case ADD:
                return yyr19();
        }
        return 255;
    }

    private int yys67() {
        switch (yytok) {
            case DIV:
                return 88;
            case MUL:
                return 89;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr64();
        }
        return 255;
    }

    private int yys85() {
        switch (yytok) {
            case DIV:
                return 106;
            case MUL:
                return 107;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr55();
        }
        return 255;
    }

    private int yys86() {
        switch (yytok) {
            case DIV:
                return 108;
            case MUL:
                return 109;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr27();
        }
        return 255;
    }

    private int yys87() {
        switch (yytok) {
            case DIV:
                return 110;
            case MUL:
                return 111;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr41();
        }
        return 255;
    }

    private int yys90() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr57();
        }
        return 255;
    }

    private int yys91() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr56();
        }
        return 255;
    }

    private int yys92() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr29();
        }
        return 255;
    }

    private int yys93() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr28();
        }
        return 255;
    }

    private int yys94() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr43();
        }
        return 255;
    }

    private int yys95() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr42();
        }
        return 255;
    }

    private int yys99() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr51();
        }
        return 255;
    }

    private int yys100() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr50();
        }
        return 255;
    }

    private int yys101() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr23();
        }
        return 255;
    }

    private int yys102() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr22();
        }
        return 255;
    }

    private int yys103() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr37();
        }
        return 255;
    }

    private int yys104() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr36();
        }
        return 255;
    }

    private int yys105() {
        switch (yytok) {
            case DIV:
                return 116;
            case MUL:
                return 117;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr66();
        }
        return 255;
    }

    private int yys112() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr67();
        }
        return 255;
    }

    private int yys113() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr68();
        }
        return 255;
    }

    private int yys118() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr54();
        }
        return 255;
    }

    private int yys119() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr53();
        }
        return 255;
    }

    private int yys120() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr26();
        }
        return 255;
    }

    private int yys121() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr25();
        }
        return 255;
    }

    private int yys122() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr40();
        }
        return 255;
    }

    private int yys123() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr39();
        }
        return 255;
    }

    private int yys124() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr70();
        }
        return 255;
    }

    private int yys125() {
        switch (yytok) {
            case DIV:
                return 33;
            case MUL:
                return 74;
            case RPAR:
            case OP:
            case NEG:
            case ENDINPUT:
            case ADD:
                return yyr69();
        }
        return 255;
    }

    private int yyr1() { // equation : lrexpr OP lrexpr
        { sfeq = new SFEquation(((Expr)yysv[yysp-3]),(String)yysv[yysp-2], ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return 1;
    }

    private int yyr14() { // addconst : addconst ADD addconst
        { yyrv = new SFFloat(((SFFloat)yysv[yysp-3]).add(((SFFloat)yysv[yysp-1]))); }
        yysv[yysp-=3] = yyrv;
        return yypaddconst();
    }

    private int yyr15() { // addconst : addconst NEG addconst
        { yyrv = new SFFloat(((SFFloat)yysv[yysp-3]).sub(((SFFloat)yysv[yysp-1]))); }
        yysv[yysp-=3] = yyrv;
        return yypaddconst();
    }

    private int yyr16() { // addconst : mulconst
        { yyrv = ((SFFloat)yysv[yysp-1]);  }
        yysv[yysp-=1] = yyrv;
        return yypaddconst();
    }

    private int yypaddconst() {
        switch (yyst[yysp-1]) {
            case 98: return 115;
            case 97: return 114;
            case 52: return 76;
            case 32: return 2;
            case 25: return 49;
            case 24: return 47;
            case 23: return 45;
            case 22: return 43;
            case 0: return 2;
            default: return 35;
        }
    }

    private int yyr4() { // expression : expression ADD addconst
        { yyrv = new AddExpr(((Expr)yysv[yysp-3]), new ConstExpr(((SFFloat)yysv[yysp-1]).floatValue())); }
        yysv[yysp-=3] = yyrv;
        return yypexpression();
    }

    private int yyr5() { // expression : expression NEG addconst
        { yyrv = new AddExpr(((Expr)yysv[yysp-3]), new ConstExpr(-((SFFloat)yysv[yysp-1]).floatValue())); }
        yysv[yysp-=3] = yyrv;
        return yypexpression();
    }

    private int yyr6() { // expression : addconst ADD expression
        { yyrv = new AddExpr(new ConstExpr(((SFFloat)yysv[yysp-3]).floatValue()), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpression();
    }

    private int yyr7() { // expression : addconst NEG expression
        { ((Expr)yysv[yysp-1]).neg(); yyrv = new AddExpr(new ConstExpr(((SFFloat)yysv[yysp-3]).floatValue()), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpression();
    }

    private int yyr8() { // expression : expression ADD expression
        { yyrv = new AddExpr(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpression();
    }

    private int yyr9() { // expression : expression NEG expression
        { ((Expr)yysv[yysp-1]).neg(); yyrv = new AddExpr(((Expr)yysv[yysp-3]), ((Expr)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypexpression();
    }

    private int yyr10() { // expression : flowvar
        { yyrv = ((Expr)yysv[yysp-1]); }
        yysv[yysp-=1] = yyrv;
        return yypexpression();
    }

    private int yyr11() { // expression : intvar
        { yyrv = ((Expr)yysv[yysp-1]); }
        yysv[yysp-=1] = yyrv;
        return yypexpression();
    }

    private int yyr12() { // expression : floatvar
        { yyrv = ((Expr)yysv[yysp-1]); }
        yysv[yysp-=1] = yyrv;
        return yypexpression();
    }

    private int yyr13() { // expression : parenthesis
        { yyrv = ((Expr)yysv[yysp-1]); }
        yysv[yysp-=1] = yyrv;
        return yypexpression();
    }

    private int yypexpression() {
        switch (yyst[yysp-1]) {
            case 64: return 84;
            case 41: return 75;
            case 25: return 50;
            case 24: return 48;
            case 23: return 46;
            case 22: return 44;
            case 20: return 36;
            default: return 3;
        }
    }

    private int yyr62() { // float : FLOATVAR
        { yyrv = (String)yysv[yysp-1]; }
        yysv[yysp-=1] = yyrv;
        return yypfloat();
    }

    private int yyr63() { // float : FLOATVARTSO
        { yyrv = timestepOffsetCheck((String)yysv[yysp-1]); }
        yysv[yysp-=1] = yyrv;
        return yypfloat();
    }

    private int yypfloat() {
        switch (yyst[yysp-1]) {
            case 65: return 85;
            case 34: return 60;
            case 21: return 37;
            default: return 4;
        }
    }

    private int yyr50() { // floatvar : mulconst MUL float MUL mulconst
        { yyrv = new FloatExpr(((SFFloat)yysv[yysp-5]).floatValue()*((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=5] = yyrv;
        return 5;
    }

    private int yyr51() { // floatvar : mulconst MUL float DIV mulconst
        { yyrv = new FloatExpr(((SFFloat)yysv[yysp-5]).floatValue()/((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=5] = yyrv;
        return 5;
    }

    private int yyr52() { // floatvar : mulconst MUL float
        { yyrv = new FloatExpr(((SFFloat)yysv[yysp-3]).floatValue(), ((String)yysv[yysp-1]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=3] = yyrv;
        return 5;
    }

    private int yyr53() { // floatvar : mulconst MUL NEG float MUL mulconst
        { yyrv = new FloatExpr(-(((SFFloat)yysv[yysp-6]).floatValue()*((SFFloat)yysv[yysp-1]).floatValue()), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=6] = yyrv;
        return 5;
    }

    private int yyr54() { // floatvar : mulconst MUL NEG float DIV mulconst
        { yyrv = new FloatExpr(-(((SFFloat)yysv[yysp-6]).floatValue()/((SFFloat)yysv[yysp-1]).floatValue()), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=6] = yyrv;
        return 5;
    }

    private int yyr55() { // floatvar : mulconst MUL NEG float
        { yyrv = new FloatExpr(-((SFFloat)yysv[yysp-4]).floatValue(), ((String)yysv[yysp-1]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=4] = yyrv;
        return 5;
    }

    private int yyr56() { // floatvar : NEG float MUL mulconst
        { yyrv = new FloatExpr(-((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=4] = yyrv;
        return 5;
    }

    private int yyr57() { // floatvar : NEG float DIV mulconst
        { yyrv = new FloatExpr(-1/((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=4] = yyrv;
        return 5;
    }

    private int yyr58() { // floatvar : NEG float
        { yyrv = new FloatExpr(-1f, ((String)yysv[yysp-1]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=2] = yyrv;
        return 5;
    }

    private int yyr59() { // floatvar : float MUL mulconst
        { yyrv = new FloatExpr(((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=3] = yyrv;
        return 5;
    }

    private int yyr60() { // floatvar : float DIV mulconst
        { yyrv = new FloatExpr(1/((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=3] = yyrv;
        return 5;
    }

    private int yyr61() { // floatvar : float
        { yyrv = new FloatExpr(1f, ((String)yysv[yysp-1]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=1] = yyrv;
        return 5;
    }

    private int yyr34() { // flow : FLOWVAR
        { yyrv = (String)yysv[yysp-1]; }
        yysv[yysp-=1] = yyrv;
        return yypflow();
    }

    private int yyr35() { // flow : FLOWVARTSO
        { yyrv = timestepOffsetCheck((String)yysv[yysp-1]); }
        yysv[yysp-=1] = yyrv;
        return yypflow();
    }

    private int yypflow() {
        switch (yyst[yysp-1]) {
            case 65: return 86;
            case 34: return 61;
            case 21: return 38;
            default: return 6;
        }
    }

    private int yyr22() { // flowvar : mulconst MUL flow MUL mulconst
        { yyrv = new FlowExpr(((SFFloat)yysv[yysp-5]).floatValue()*((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=5] = yyrv;
        return 7;
    }

    private int yyr23() { // flowvar : mulconst MUL flow DIV mulconst
        { yyrv = new FlowExpr(((SFFloat)yysv[yysp-5]).floatValue()/((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=5] = yyrv;
        return 7;
    }

    private int yyr24() { // flowvar : mulconst MUL flow
        { yyrv = new FlowExpr(((SFFloat)yysv[yysp-3]).floatValue(), ((String)yysv[yysp-1]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=3] = yyrv;
        return 7;
    }

    private int yyr25() { // flowvar : mulconst MUL NEG flow MUL mulconst
        { yyrv = new FlowExpr(-(((SFFloat)yysv[yysp-6]).floatValue()*((SFFloat)yysv[yysp-1]).floatValue()), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=6] = yyrv;
        return 7;
    }

    private int yyr26() { // flowvar : mulconst MUL NEG flow DIV mulconst
        { yyrv = new FlowExpr(-(((SFFloat)yysv[yysp-6]).floatValue()/((SFFloat)yysv[yysp-1]).floatValue()), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=6] = yyrv;
        return 7;
    }

    private int yyr27() { // flowvar : mulconst MUL NEG flow
        { yyrv = new FlowExpr(-((SFFloat)yysv[yysp-4]).floatValue(), ((String)yysv[yysp-1]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=4] = yyrv;
        return 7;
    }

    private int yyr28() { // flowvar : NEG flow MUL mulconst
        { yyrv = new FlowExpr(-((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=4] = yyrv;
        return 7;
    }

    private int yyr29() { // flowvar : NEG flow DIV mulconst
        { yyrv = new FlowExpr(-1/((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=4] = yyrv;
        return 7;
    }

    private int yyr30() { // flowvar : NEG flow
        { yyrv = new FlowExpr(-1f, ((String)yysv[yysp-1]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=2] = yyrv;
        return 7;
    }

    private int yyr31() { // flowvar : flow MUL mulconst
        { yyrv = new FlowExpr(((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=3] = yyrv;
        return 7;
    }

    private int yyr32() { // flowvar : flow DIV mulconst
        { yyrv = new FlowExpr(1/((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=3] = yyrv;
        return 7;
    }

    private int yyr33() { // flowvar : flow
        { yyrv = new FlowExpr(1f, ((String)yysv[yysp-1]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=1] = yyrv;
        return 7;
    }

    private int yyr48() { // int : INTVAR
        { yyrv = (String)yysv[yysp-1]; }
        yysv[yysp-=1] = yyrv;
        return yypint();
    }

    private int yyr49() { // int : INTVARTSO
        { yyrv = timestepOffsetCheck((String)yysv[yysp-1]); }
        yysv[yysp-=1] = yyrv;
        return yypint();
    }

    private int yypint() {
        switch (yyst[yysp-1]) {
            case 65: return 87;
            case 34: return 62;
            case 21: return 39;
            default: return 8;
        }
    }

    private int yyr36() { // intvar : mulconst MUL int MUL mulconst
        { yyrv = new IntExpr(((SFFloat)yysv[yysp-5]).floatValue()*((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=5] = yyrv;
        return 9;
    }

    private int yyr37() { // intvar : mulconst MUL int DIV mulconst
        { yyrv = new IntExpr(((SFFloat)yysv[yysp-5]).floatValue()/((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=5] = yyrv;
        return 9;
    }

    private int yyr38() { // intvar : mulconst MUL int
        { yyrv = new IntExpr(((SFFloat)yysv[yysp-3]).floatValue(), ((String)yysv[yysp-1]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=3] = yyrv;
        return 9;
    }

    private int yyr39() { // intvar : mulconst MUL NEG int MUL mulconst
        { yyrv = new IntExpr(-(((SFFloat)yysv[yysp-6]).floatValue()*((SFFloat)yysv[yysp-1]).floatValue()), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=6] = yyrv;
        return 9;
    }

    private int yyr40() { // intvar : mulconst MUL NEG int DIV mulconst
        { yyrv = new IntExpr(-(((SFFloat)yysv[yysp-6]).floatValue()/((SFFloat)yysv[yysp-1]).floatValue()), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=6] = yyrv;
        return 9;
    }

    private int yyr41() { // intvar : mulconst MUL NEG int
        { yyrv = new IntExpr(-((SFFloat)yysv[yysp-4]).floatValue(), ((String)yysv[yysp-1]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=4] = yyrv;
        return 9;
    }

    private int yyr42() { // intvar : NEG int MUL mulconst
        { yyrv = new IntExpr(-((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=4] = yyrv;
        return 9;
    }

    private int yyr43() { // intvar : NEG int DIV mulconst
        { yyrv = new IntExpr(-1/((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=4] = yyrv;
        return 9;
    }

    private int yyr44() { // intvar : NEG int
        { yyrv = new IntExpr(-1f, ((String)yysv[yysp-1]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=2] = yyrv;
        return 9;
    }

    private int yyr45() { // intvar : int MUL mulconst
        { yyrv = new IntExpr(((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=3] = yyrv;
        return 9;
    }

    private int yyr46() { // intvar : int DIV mulconst
        { yyrv = new IntExpr(1/((SFFloat)yysv[yysp-1]).floatValue(), ((String)yysv[yysp-3]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=3] = yyrv;
        return 9;
    }

    private int yyr47() { // intvar : int
        { yyrv = new IntExpr(1f, ((String)yysv[yysp-1]), tStepOffset); tStepOffset = 0; }
        yysv[yysp-=1] = yyrv;
        return 9;
    }

    private int yyr2() { // lrexpr : addconst
        { yyrv =  new ConstExpr(((SFFloat)yysv[yysp-1]).floatValue()); }
        yysv[yysp-=1] = yyrv;
        return yyplrexpr();
    }

    private int yyr3() { // lrexpr : expression
        { yyrv = ((Expr)yysv[yysp-1]); }
        yysv[yysp-=1] = yyrv;
        return yyplrexpr();
    }

    private int yyplrexpr() {
        switch (yyst[yysp-1]) {
            case 0: return 10;
            default: return 58;
        }
    }

    private int yyr17() { // mulconst : mulconst MUL mulconst
        { yyrv = new SFFloat(((SFFloat)yysv[yysp-3]).mult(((SFFloat)yysv[yysp-1]))); }
        yysv[yysp-=3] = yyrv;
        return yypmulconst();
    }

    private int yyr18() { // mulconst : mulconst DIV mulconst
        { yyrv = new SFFloat(((SFFloat)yysv[yysp-3]).div(((SFFloat)yysv[yysp-1]))); }
        yysv[yysp-=3] = yyrv;
        return yypmulconst();
    }

    private int yyr19() { // mulconst : LPAR addconst RPAR
        { yyrv = ((SFFloat)yysv[yysp-2]); }
        yysv[yysp-=3] = yyrv;
        return yypmulconst();
    }

    private int yyr20() { // mulconst : NEG mulconst
        { yyrv = new SFFloat(-((SFFloat)yysv[yysp-1]).floatValue()); }
        yysv[yysp-=2] = yyrv;
        return yypmulconst();
    }

    private int yyr21() { // mulconst : CONST
        { yyrv = yysv[yysp-1]; }
        yysv[yysp-=1] = yyrv;
        return yypmulconst();
    }

    private int yypmulconst() {
        switch (yyst[yysp-1]) {
            case 117: return 125;
            case 116: return 124;
            case 111: return 123;
            case 110: return 122;
            case 109: return 121;
            case 108: return 120;
            case 107: return 119;
            case 106: return 118;
            case 98: return 77;
            case 97: return 77;
            case 89: return 113;
            case 88: return 112;
            case 83: return 104;
            case 82: return 103;
            case 81: return 102;
            case 80: return 101;
            case 79: return 100;
            case 78: return 99;
            case 74: return 63;
            case 73: return 95;
            case 72: return 94;
            case 71: return 93;
            case 70: return 92;
            case 69: return 91;
            case 68: return 90;
            case 65: return 40;
            case 52: return 77;
            case 42: return 40;
            case 34: return 63;
            case 33: return 59;
            case 31: return 57;
            case 30: return 56;
            case 29: return 55;
            case 28: return 54;
            case 27: return 53;
            case 26: return 51;
            case 21: return 40;
            default: return 11;
        }
    }

    private int yyr64() { // parenthesis : LPAR expression RPAR
        { yyrv = new Parenthesis(new ConstExpr(1f), ((Expr)yysv[yysp-2]), new ConstExpr(1f)); }
        yysv[yysp-=3] = yyrv;
        return 12;
    }

    private int yyr65() { // parenthesis : NEG LPAR expression RPAR
        { yyrv = new Parenthesis(new ConstExpr(-1f), ((Expr)yysv[yysp-2]), new ConstExpr(1f)); }
        yysv[yysp-=4] = yyrv;
        return 12;
    }

    private int yyr66() { // parenthesis : mulconst MUL LPAR expression RPAR
        { yyrv = new Parenthesis(new ConstExpr(((SFFloat)yysv[yysp-5]).floatValue()), ((Expr)yysv[yysp-2]), new ConstExpr(1f)); }
        yysv[yysp-=5] = yyrv;
        return 12;
    }

    private int yyr67() { // parenthesis : LPAR expression RPAR DIV mulconst
        { yyrv = new Parenthesis(new ConstExpr(1f), ((Expr)yysv[yysp-4]), new ConstExpr(1/((SFFloat)yysv[yysp-1]).floatValue())); }
        yysv[yysp-=5] = yyrv;
        return 12;
    }

    private int yyr68() { // parenthesis : LPAR expression RPAR MUL mulconst
        { yyrv = new Parenthesis(new ConstExpr(1f), ((Expr)yysv[yysp-4]), new ConstExpr(((SFFloat)yysv[yysp-1]).floatValue())); }
        yysv[yysp-=5] = yyrv;
        return 12;
    }

    private int yyr69() { // parenthesis : mulconst MUL LPAR expression RPAR MUL mulconst
        { yyrv = new Parenthesis(new ConstExpr(((SFFloat)yysv[yysp-7]).floatValue()), ((Expr)yysv[yysp-4]), new ConstExpr(((SFFloat)yysv[yysp-1]).floatValue())); }
        yysv[yysp-=7] = yyrv;
        return 12;
    }

    private int yyr70() { // parenthesis : mulconst MUL LPAR expression RPAR DIV mulconst
        { yyrv = new Parenthesis(new ConstExpr(((SFFloat)yysv[yysp-7]).floatValue()), ((Expr)yysv[yysp-4]), new ConstExpr(1/((SFFloat)yysv[yysp-1]).floatValue())); }
        yysv[yysp-=7] = yyrv;
        return 12;
    }

    private int yyerr(int e, int n) {
        yyerrno = e;
        return n;
    }
    protected String[] yyerrmsgs = {
        "RHS missing in",
        "LHS missing in",
        "Operand missing after '+' in",
        "Operand missing after '-' in",
        "Operand missing after '*' in",
        "Operand missing after '/' in",
        "Operand missing before '+' in",
        "Operand missing before '*' in",
        "Operand missing before '/' in",
        "Unexpected closing parenthesis in",
        "Unexpected opening parenthesis in"
    };


/*
 * Method that handels all errors generated by the parser
 */

private void yyerror(String msg) throws Exception {
    throw new Exception((yyerrno<0 ? msg : yyerrmsgs[yyerrno]));
}

/*
 * Method that gets the next token from the lexer
 */

public int lex() throws Exception {
    int val = 0;
    try {
        val = lexer.nextToken();
    }
    catch (Exception e) {
        throw new Exception(e.getMessage());
    }
    return val;
}

/*
 * Declaration of private variables
 */

private static SFLexer lexer;
private Equation eq;
private SFEquation sfeq;
private String flow = "";
private Flow [] c_inFlows;
private Flow [] c_outFlows;
private int c_timestep, c_numOfTimesteps, tStepOffset=0;
private float c_const = 0, tmp_const = 0;
private Vector c_floatVars, c_intVars;


/*
 * Gets the ID for a flow
 * @param flow - name of the flow
 * @return ID for the flow or null if the flow doesn't exist
 */
private ID getFlowID(String flow) {
    
    for (int i = 0; i<c_inFlows.length;i++) {
        if(c_inFlows[i].toString().equalsIgnoreCase(flow))
            return c_inFlows[i].getID();
    }
    
    for (int i = 0; i<c_outFlows.length;i++) {
        if(c_outFlows[i].toString().equalsIgnoreCase(flow))
            return c_outFlows[i].getID();
    }
    
    return null;
}

/*
 * Checks what kind of offset the variable should be given
 */

private String timestepOffsetCheck(String var) {
    
    StringTokenizer st = new StringTokenizer(var, "[");
    String s1 = st.nextToken();
    String s2 = st.nextToken();
    if (s2.equals("-]")) {
        tStepOffset = -1;
    }
    if (s2.equals("+]")) {
        tStepOffset = 1;
    }
    return s1;
}

/*
 * Returns this parsers Equation. Used to get the generated equation
 * after the parsing is finished.
 */

public Equation getEquation() throws Exception {
    try {
        sfeq.eval();
    }
    catch (Exception e) {
        throw new Exception(e.getMessage());
    }
    return eq;
}

/*
 * Constructor for SFParser
 */

public SFParser(SFLexer l, Flow[] inFlows, Flow [] outFlows, int timestep, 
        int numOfTimesteps, Vector intVars, Vector floatVars){
    lexer = l;
    c_outFlows = outFlows;
    c_inFlows = inFlows;
    eq = new Equation();
    c_timestep = timestep;
    c_numOfTimesteps = numOfTimesteps;
    c_intVars = intVars;
    c_floatVars = floatVars;
}

/*
 * Parser Tree
 */

private class SFEquation {
    Expr rhs;
    String op;
    Expr lhs;
    
    public SFEquation (Expr LHS, String operator,Expr RHS){
        rhs = RHS;
        op = operator;
        lhs = LHS;
    }
    
    private void eval() throws Exception {
        rhs.solvePar(1f);
        lhs.solvePar(1f);
        /* Set operator and right hand side */
        eq.setOperator(op);
        eq.setRHS(rhs.eval(true) - lhs.eval(false));
    }
}


private abstract class Expr{
    
    /* Simplifies paranteses */
    public abstract float solvePar(float coeff);
    
    /* Evaluates RHS constant and generates equation variables */
    public abstract float eval(boolean isRHS) throws Exception;
    
    /* Negates an expression */
    public abstract void neg();
}


// Repressents an paranthesis with preceding and following multiplication
private class Parenthesis extends Expr{
    ConstExpr pre;
    Expr cont;
    ConstExpr post;             
    
    private Parenthesis(ConstExpr preCoeff, Expr content, ConstExpr postCoeff){
        pre = preCoeff;
        post = postCoeff;
        cont = content;
    }   
    
    public float solvePar(float coeff){
        float tmp = coeff * pre.solvePar(1f) * post.solvePar(1f);
        cont.solvePar(tmp);
        return tmp;
    }
    
    public float eval(boolean isRHS) throws Exception {
        return cont.eval(isRHS);
    }
    
    public void neg(){
        pre.neg();
    }
    
}

// flow and coefficient
private class FlowExpr extends Expr{            
    private ID flow; 
    private float c_flowCoeff;
    private int c_timestepOffset;
    
    private FlowExpr(float flowCoeff, String flowName, int timestepOffset){
        flow = getFlowID(flowName);
        c_flowCoeff = flowCoeff;
        c_timestepOffset = timestepOffset;
    }   
    
    public float solvePar(float coeff){
        c_flowCoeff *= coeff;
        return 1f;
    }
    
    public float eval(boolean isRHS) throws Exception {
        if(flow == null)
            throw new Exception("Invalid Flow in");
        if(c_timestep+c_timestepOffset < 1 || c_timestep+c_timestepOffset > c_numOfTimesteps)
            throw new Exception("Invalid timestep reference in");
        if(isRHS){
            c_flowCoeff *= -1; // change sign
        }
        
        // Ensure that doubles of variables never occure
        Vector tmp = eq.getAllVariables();
        for (int i = 0; i < tmp.size(); i++){
            Variable var = (Variable) tmp.elementAt(i);
            if((var.getFlow() != null) && var.getFlow().equals(flow)){
                var.add(c_flowCoeff);
                return 0f;
            }
        }           
        eq.addVariable(new Variable(flow, c_timestep+c_timestepOffset, c_flowCoeff));
        return 0f;
    }   
    
    public void neg(){
        c_flowCoeff *=-1f;
    }
}

// Constant
private class ConstExpr extends Expr{
    private float value;
    
    private ConstExpr (float constValue){
        value = constValue;
    }
    
    public float solvePar(float coeff){
        value *= coeff;
        return value;
    }
    
    public float eval(boolean isRHS) throws Exception {
        return value;
    }
    
    public void neg(){
        value *=-1f;
    }   
}

// Addition 
private class AddExpr extends Expr{
    private Expr lhs, rhs;
    
    public AddExpr(Expr left, Expr right){
        lhs = left;
        rhs = right;
    }
    
    public float solvePar(float coeff){
        return lhs.solvePar(coeff) + rhs.solvePar(coeff);
    }
    
    public float eval(boolean isRHS) throws Exception {
        return lhs.eval(isRHS) + rhs.eval(isRHS);
    }
    
    public void neg(){
        lhs.neg();
        rhs.neg();
    }
}

// Integer Variable 
private class IntExpr extends Expr{
    private String c_name;
    private float c_flowCoeff;
    private int c_timestepOffset;
    
    public IntExpr(float flowCoeff, String name, int timestepOffset){
        c_name = name;
        c_flowCoeff = flowCoeff;
        c_timestepOffset = timestepOffset;
    }
    
    public float solvePar(float coeff){
        c_flowCoeff *= coeff;
        return 1f;
    }
    
    public float eval(boolean isRHS) throws Exception {
        if(isRHS){
            c_flowCoeff *= -1; // change sign
        }
        if(c_timestep+c_timestepOffset < 1 || c_timestep+c_timestepOffset > c_numOfTimesteps)
            throw new Exception("Invalid timestep reference in");       
        if(c_intVars.contains(c_name))
            eq.addVariable(new Variable(c_flowCoeff, c_timestep+c_timestepOffset, c_name));
        else throw new Exception("Invalid Integer Variable");
        
        return 0f;
    }
    
    public void neg(){
        c_flowCoeff *=-1f;
    }
    
}

// Float Variable 
private class FloatExpr extends Expr{
    private String c_name;
    private float c_flowCoeff;
    private int c_timestepOffset;
    
    public FloatExpr(float flowCoeff, String name, int timestepOffset){
        c_name = name;
        c_flowCoeff = flowCoeff;
        c_timestepOffset = timestepOffset;
    }
    
    public float solvePar(float coeff){
        c_flowCoeff *= coeff;
        return 1f;
    }
    
    public float eval(boolean isRHS) throws Exception {
        if(isRHS){
            c_flowCoeff *= -1; // change sign
        }
        if(c_timestep+c_timestepOffset < 1 || c_timestep+c_timestepOffset > c_numOfTimesteps)
            throw new Exception("Invalid timestep reference in");
        if(c_floatVars.contains(c_name))
            eq.addVariable(new Variable(c_name, c_timestep+c_timestepOffset, c_flowCoeff));
        else throw new Exception("Invalid Float Variable");
        
        return 0f;
    }
    
    public void neg(){
        c_flowCoeff *=-1f;
    }
    
}

}
