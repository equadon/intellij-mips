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

package com.equadon.intellij.mips.lang.psi;

import com.equadon.intellij.mips.MipsException;
import com.equadon.intellij.mips.lang.MipsLanguage;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import org.jetbrains.annotations.NotNull;

import mars.assembler.Token;
import mars.assembler.TokenTypes;

public class MipsElementType extends IElementType {
  public MipsElementType(@NotNull String debug) {
    super(debug, MipsLanguage.INSTANCE);
  }

  public static IElementType fromToken(@NotNull Token token) throws MipsException {
    TokenTypes type = token.getType();

    if (type.equals(TokenTypes.COLON))
      return MipsElementTypes.COLON;
    if (type.equals(TokenTypes.COMMENT))
      return MipsTokenTypes.COMMENT;
    if (type.equals(TokenTypes.DELIMITER))
      return TokenType.WHITE_SPACE;
    if (type.equals(TokenTypes.DIRECTIVE))
      return MipsElementTypes.DIRECTIVE;
//    if (type.equals(TokenTypes.EOL))
//      return MipsElementTypes.EOL;
    if (type.equals(TokenTypes.ERROR))
      return TokenType.BAD_CHARACTER;
    if (type.equals(TokenTypes.IDENTIFIER))
      return MipsElementTypes.IDENTIFIER;
    if (type.equals(TokenTypes.INTEGER_5))
      return MipsElementTypes.INTEGER_5;
    if (type.equals(TokenTypes.INTEGER_16))
      return MipsElementTypes.INTEGER_16;
    if (type.equals(TokenTypes.INTEGER_16U))
      return MipsElementTypes.INTEGER_16U;
    if (type.equals(TokenTypes.INTEGER_32))
      return MipsElementTypes.INTEGER_32;
    if (type.equals(TokenTypes.LEFT_PAREN))
      return MipsElementTypes.LPAREN;
    if (type.equals(TokenTypes.MINUS))
      return MipsElementTypes.MINUS;
    if (type.equals(TokenTypes.OPERATOR))
      return MipsElementTypes.OPERATOR;
    if (type.equals(TokenTypes.PLUS))
      return MipsElementTypes.PLUS;
    if (type.equals(TokenTypes.QUOTED_STRING))
      return MipsElementTypes.QUOTED_STRING;
    if (type.equals(TokenTypes.REAL_NUMBER))
      return MipsElementTypes.REAL_NUMBER;
    if (type.equals(TokenTypes.REGISTER_NAME))
      return MipsElementTypes.REGISTER_NAME;
    if (type.equals(TokenTypes.REGISTER_NUMBER))
      return MipsElementTypes.REGISTER_NUMBER;
    if (type.equals(TokenTypes.RIGHT_PAREN))
      return MipsElementTypes.RPAREN;

    throw new MipsException("Unknown token type: " + type);
  }
}
