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

import mars.MIPSprogram;
import mars.assembler.Token;
import mars.assembler.TokenList;
import mars.assembler.TokenTypes;
import mars.assembler.Tokenizer;

public class MipsTokenizer extends Tokenizer {
  public MipsTokenizer() {
    super();
  }

  public MipsTokenizer(MIPSprogram program) {
    super(program);
  }

  @Override
  protected void processCandidateToken(char[] token, char[] line, MIPSprogram program, int lineNum, String theLine, int linePos, int tokenPos, int tokenStartPos, TokenList tokenList) {
    // Consume delimiters before token
    int tokenStart = tokenStartPos - 1;

    if (tokenStart > 0) {
      List<Token> delimiterTokens = new ArrayList<>();

      for (int i = tokenStart - 1; i >= 0; i--) {
        String delimiter = new String(line, i, 1);
        TokenTypes type = TokenTypes.matchTokenType(delimiter);

        if (type.equals(TokenTypes.DELIMITER)) {
          delimiterTokens.add(new Token(type, delimiter, program, lineNum, i + 1));
        } else {
          break;
        }
      }

      for (int i = delimiterTokens.size() - 1; i >= 0; i--)
        tokenList.add(delimiterTokens.get(i));
    }

    super.processCandidateToken(token, line, program, lineNum, theLine, linePos, tokenPos, tokenStartPos, tokenList);

    // Consume delimiters after colon
    if (line[tokenStart] == ':') {
      boolean addColonTokens = false;
      List<Token> colonTokens = new ArrayList<>();

      for (int i = tokenStart + 1; i < line.length; i++) {
        String del = new String(line, i, 1);
        TokenTypes delType = TokenTypes.matchTokenType(del);

        if (delType.equals(TokenTypes.DELIMITER))
          colonTokens.add(new Token(delType, del, program, lineNum, i + 1));
        else if (delType.equals(TokenTypes.EOL)) {
          addColonTokens = true;
        } else
          break;
      }

      if (addColonTokens) {
        for (Token t : colonTokens)
          tokenList.add(t);
      }
    }
  }

  @Override
  protected void processDelimiterToken(char[] line, int linePos, MIPSprogram program, int lineNum, TokenList result) {
    String delimiter = new String(line, linePos, 1);
    TokenTypes type = TokenTypes.matchTokenType(delimiter);

    if (type.equals(TokenTypes.DELIMITER)) {
      result.add(new Token(type, delimiter, program, lineNum, linePos + 1));
    }
  }
}
