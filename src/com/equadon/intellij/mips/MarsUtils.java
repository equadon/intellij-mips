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

package com.equadon.intellij.mips;

import com.equadon.intellij.mips.lang.psi.MipsElementType;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import mars.assembler.Token;
import mars.assembler.TokenTypes;

public class MarsUtils {
  /**
   * Helper method for the lexer to get the correct element type.
   * @param text token string to find
   * @return element type
   */
  public static IElementType getTokenType(CharSequence text) {
    TokenTypes type = TokenTypes.matchTokenType(text.toString());

    try {
      return MipsElementType.fromToken(new Token(type, null, null, 0, 0));
    } catch (MipsException e) {
      System.out.println("WARNING: Unknown token type found during lexing: " + type);
      return TokenType.BAD_CHARACTER;
    }
  }
}
