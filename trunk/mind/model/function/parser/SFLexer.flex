/*
 * Copyright 2004: 
 * Marcus Bergendorff <amaebe-1@student.luth.se> 
 * Jan Sköllermark <jansok-1@student.luth.se>
 * 
 * This file is part of reMIND.
 *
 * reMIND is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * reMIND is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with reMIND; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package mind.model.function.parser;

import mind.model.*;
import java.io.*;
import java.util.*;
import javax.swing.table.*;
%%

%{
    
    /*
     * Returns the contens of the cel cellRef
     */
    
    private String getCellContent (String cellRef) throws ModelException {
        
        String column = "", row = "", cellcont = "";
        for (int i = 0; i<cellRef.length();i++) {
            if (Character.isLetter(cellRef.charAt(i)))
                column += cellRef.charAt(i);
            if (Character.isDigit(cellRef.charAt(i)))
                row += cellRef.charAt(i);
        }
        
        int col_num = dtm.findColumn(column.toUpperCase());
        int row_num;
        try {
            row_num = Integer.parseInt(row) - 1;
            cellcont = (String)dtm.getValueAt(row_num,col_num);
        }
        catch (Exception e) {
            throw new ModelException("Cell reference to non-existent cell in:");
        }
        if (cellcont == null || cellcont.equals(""))
            throw new ModelException("Cell reference to empty cell in:");
        if (dtmcol == col_num && dtmrow == row_num)
            throw new ModelException("Circular reference to cell in:");
        
        return cellcont;
        
    }
    
    /*
     * Constructor for SFLexer
     */
    
    public SFLexer(Reader in, DefaultTableModel model, int col, int row) {
        this.zzReader = in;
        dtm = model;
        dtmcol = col;
        dtmrow = row;
    }
    
%}

/*
 * Options & Declarations
 */

%class SFLexer
%public
%unicode
%implements SFParserTokens
%function nextToken
%int
%yylexthrow ModelException

%eofval{
    return 0;
%eofval}

%eofclose

%{
    public int token;
    public Object semVal;
    private DefaultTableModel dtm;
    private int dtmrow, dtmcol;
    public boolean celref = false;
    
%}

%%

/*
 * Lexical rules
 */

"#"[a-zA-Z]+[0-9]+		{ try { yypushStream(new StringReader(getCellContent(yytext().substring(1)))); } 
				  catch (Exception e) { throw new ModelException(e.getMessage()); } celref = true;} 
[Ff][1-9][0-9]*			{ semVal = yytext(); return token = FLOWVAR; }
[Ff][1-9][0-9]*"["[+-]"]"	{ semVal = yytext(); return token = FLOWVARTSO; }
([0-9]*"."[0-9]+)|[0-9]+ 	{ semVal = new SFFloat(yytext()); return token = CONST; }
"="				{ semVal = "E"; return token = OP; }
">="|">"			{ semVal = "G"; return token = OP; }
"<="|"<"			{ semVal = "L"; return token = OP; }
" "				{ /* ignore space */ }
"%".*		  		{ return token = COMMENT; }
"("				{ semVal = yytext(); return token = LPAR; }
")"				{ semVal = yytext(); return token = RPAR; }
"+"				{ semVal = yytext(); return token = ADD; }
"-"				{ semVal = yytext(); return token = NEG; }
"*"				{ semVal = yytext(); return token = MUL; }
"/"				{ semVal = yytext(); return token = DIV; }
<<EOF>>				{ if (yymoreStreams()) { yypopStream(); } else return ENDINPUT; }
"Int"[1-9][0-9]*		{ semVal = yytext(); return token = INTVAR; }
"Flt"[1-9][0-9]*		{ semVal = yytext(); return token = FLOATVAR; }
"Int"[1-9][0-9]*"["[+-]"]"	{ semVal = yytext(); return token = INTVARTSO; }
"Flt"[1-9][0-9]*"["[+-]"]"	{ semVal = yytext(); return token = FLOATVARTSO; }
.				{ throw new ModelException("Invalid character in"); }