/*
 * Copyright 2017 Niklas Persson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.equadon.intellij.mips.mars.lexer;

import java.util.ArrayList;
import java.util.List;

import mars.ErrorList;
import mars.ProcessingException;
import mars.assembler.SourceLine;
import mars.assembler.SymbolTable;
import mars.assembler.Token;
import mars.assembler.TokenList;
import mars.assembler.TokenTypes;

public class MipsProgram extends mars.MIPSprogram {
  public void readLines(String text) {
    ArrayList<SourceLine> lines = new ArrayList<>();

    int lineNumber = 1;

    for (String line : splitLines(text)) {
      lines.add(new SourceLine(line, this, lineNumber++));
    }

    setSourceLineList(lines);
  }

  @Override
  public void tokenize() throws ProcessingException {
    this.tokenizer = new MipsTokenizer();
    this.tokenList = tokenizer.tokenize(this);
    this.localSymbolTable = new SymbolTable(null); // prepare for assembly
  }

  public ErrorList assemble(boolean extendedAssemblerEnabled) throws ProcessingException {
    stripDelimitersAndEOL();

    ArrayList<MipsProgram> programs = new ArrayList<>();
    programs.add(this);

    return super.assemble(programs, extendedAssemblerEnabled);
  }

  @Override
  public ErrorList assemble(ArrayList programsToAssemble, boolean extendedAssemblerEnabled) throws ProcessingException {
    stripDelimitersAndEOL();

    return super.assemble(programsToAssemble, extendedAssemblerEnabled);
  }

  @Override
  public ErrorList assemble(ArrayList programsToAssemble, boolean extendedAssemblerEnabled, boolean warningsAreErrors) throws ProcessingException {
    stripDelimitersAndEOL();

    return super.assemble(programsToAssemble, extendedAssemblerEnabled, warningsAreErrors);
  }

  /**
   * Remove DELIMITER and EOL tokens from token lists.
   */
  private void stripDelimitersAndEOL() {
    ArrayList tokenLists = getTokenList();

    for (int i = 0; i < tokenLists.size(); i++) {
      TokenList list = (TokenList) tokenLists.get(i);

      for (int j = 0; j < list.size(); j++) {
        Token token = list.get(j);

        if (token.getType().equals(TokenTypes.DELIMITER) || token.getType().equals(TokenTypes.EOL)) {
          list.remove(j);
        }
      }

      if (list.isEmpty()) {
        tokenLists.remove(i);
      }
    }
  }

  public static List<String> splitLines(String buffer) {
    List<String> lines = new ArrayList<>();

    int lineStart = 0;
    boolean inString = false;

    for (int i = 0; i < buffer.length(); i++) {
      if (buffer.charAt(i) == '"' && (i > 0 && buffer.charAt(i - 1) != '\\')) {
        inString = !inString;
      }

      if (!inString) {
        if (buffer.charAt(i) == '\n') {
          lines.add(buffer.substring(lineStart, i + 1));
          lineStart = i + 1;
        }
      }
    }

    lines.add(buffer.substring(lineStart));

    return lines;
  }
}
