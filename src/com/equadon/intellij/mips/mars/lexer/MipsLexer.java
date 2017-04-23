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

import com.equadon.intellij.mips.MipsException;
import com.equadon.intellij.mips.lang.psi.MipsElementType;
import com.equadon.intellij.mips.mars.old.MipsProgram;
import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import mars.ErrorList;
import mars.ErrorMessage;
import mars.ProcessingException;
import mars.assembler.SourceLine;
import mars.assembler.Token;
import mars.assembler.TokenList;

public class MipsLexer extends LexerBase {
  private CharSequence buffer;
  private int tokenStart;
  private int tokenEnd;
  private int bufferEnd;
  private IElementType tokenType;

  private Deque<Token> tokens;
  private List<Integer> lineIndexes;
  private boolean eof;

  private int prevIndex = 0;

  @Override
  public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
    this.buffer = buffer;
    this.tokenStart = startOffset;
    this.tokenEnd = startOffset;
    this.bufferEnd = endOffset;
    this.tokenType = null;

//    System.out.println("LEXER: start('" + buffer.toString().replace("\n", "\\n") + "', " + startOffset + ".." + endOffset + ")");

    try {
      tokenize();

      advance();
    } catch (IOException e) {
      System.out.println("Error: Failed to tokenize buffer: " + buffer);
      e.printStackTrace();
    }
  }

  /**
   * Generate tokens using MARS tokenizer.
   */
  private void tokenize() throws IOException {
    // Read lines for lexer
    tokens = new ArrayDeque<>();
    lineIndexes = new ArrayList<>();

    MipsProgram program = new MipsProgram();
    ArrayList<SourceLine> sourceLines = new ArrayList<>();

    int lineNumber = 0;

    int offset = 0;
    for (String line : MipsProgram.splitLines(buffer.toString())) {
      lineIndexes.add(offset);
      sourceLines.add(new SourceLine(line, program, ++lineNumber));

      offset += line.length();
    }

    program.setSourceLineList(sourceLines);

    // Tokenize
    try {
      program.tokenize();

      ArrayList tokenLists = program.getTokenList();
      for (int i = 0; i < tokenLists.size(); i++) {
        TokenList tokenList = (TokenList) tokenLists.get(i);
        for (int j = 0; j < tokenList.size(); j++) {
          tokens.addLast(tokenList.get(j));
        }
      }
    } catch (ProcessingException e) {
      ErrorList errors = program.getTokenizer().getErrors();
      ArrayList messages = errors.getErrorMessages();
      for (int i = 0; i < messages.size(); i++) {
        ErrorMessage msg = (ErrorMessage) messages.get(i);
        System.out.print(msg.isWarning() ? "MARS WARN :: " : "MARS ERR  :: ");
        System.out.print("  * " + msg.getMessage() + "\n");
      }
    }
  }

  @Override
  public int getState() {
    return 0;
  }

  @Nullable
  @Override
  public IElementType getTokenType() {
    if (tokenType == null && !eof)
      advance();
    return tokenType;
  }

  @Override
  public int getTokenStart() {
    return tokenStart;
  }

  @Override
  public int getTokenEnd() {
    return tokenEnd;
  }

  @Override
  public void advance() {
    if (tokenStart >= bufferEnd || tokens.isEmpty()) {
//      System.out.println("LEXER: end of file");

      tokenType = null;
      tokenStart = bufferEnd;
      tokenEnd = bufferEnd;
      prevIndex = bufferEnd;
      eof = true;
      return;
    }

    Token token = tokens.pop();
    try {
      tokenType = MipsElementType.fromToken(token);

      tokenStart = lineIndexes.get(token.getOriginalSourceLine() - 1) + token.getStartPos() - 1;
      String value = token.getValue();
      tokenEnd = tokenStart + value.length();

//      System.out.println("LEXER: Token: " + tokenType + " (index=" + (tokenStart == tokenEnd ? tokenStart : tokenStart + ".." + tokenEnd) + "): \"" + buffer.subSequence(tokenStart, tokenEnd).toString().replace("\n", "\\n").replace("\t", "\\t") + "\"");

      if (prevIndex != tokenStart) {
        System.out.println("WARNING: gap found at " + prevIndex);
        throw new RuntimeException("gap at " + prevIndex + " to " + tokenStart);
      }

      prevIndex = tokenEnd;
    } catch (MipsException e) {
      e.printStackTrace();
    }
  }

  @NotNull
  @Override
  public CharSequence getBufferSequence() {
    return buffer;
  }

  @Override
  public int getBufferEnd() {
    return bufferEnd;
  }
}
