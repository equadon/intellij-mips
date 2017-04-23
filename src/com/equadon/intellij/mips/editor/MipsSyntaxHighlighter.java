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

package com.equadon.intellij.mips.editor;

import com.equadon.intellij.mips.lang.lexer.MipsLexerAdapter;
import com.equadon.intellij.mips.lang.psi.MipsElementTypes;
import com.equadon.intellij.mips.lang.psi.MipsTokenTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import org.jetbrains.annotations.NotNull;

public class MipsSyntaxHighlighter extends SyntaxHighlighterBase {
  public static final TextAttributesKey ERROR = TextAttributesKey.createTextAttributesKey("MIPS_ERROR", DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE);

  public static final TextAttributesKey COMMENT = TextAttributesKey.createTextAttributesKey("MIPS_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);

  public static final TextAttributesKey OPERATOR = TextAttributesKey.createTextAttributesKey("MIPS_OPERATOR", DefaultLanguageHighlighterColors.KEYWORD);
  public static final TextAttributesKey DIRECTIVE = TextAttributesKey.createTextAttributesKey("MIPS_DIRECTIVE", DefaultLanguageHighlighterColors.STATIC_FIELD);
  public static final TextAttributesKey LABEL = TextAttributesKey.createTextAttributesKey("MIPS_LABEL", DefaultLanguageHighlighterColors.IDENTIFIER);

  public static final TextAttributesKey STRING = TextAttributesKey.createTextAttributesKey("MIPS_STRING", DefaultLanguageHighlighterColors.STRING);
  public static final TextAttributesKey NUMBER = TextAttributesKey.createTextAttributesKey("MIPS_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
  public static final TextAttributesKey REGISTER = TextAttributesKey.createTextAttributesKey("MIPS_REGISTER", DefaultLanguageHighlighterColors.INSTANCE_FIELD);

  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return new MipsLexerAdapter();
  }

  @NotNull
  @Override
  public TextAttributesKey[] getTokenHighlights(IElementType type) {
    if (type == TokenType.BAD_CHARACTER)
      return pack(ERROR);
    if (MipsTokenTypes.COMMENTS.contains(type))
      return pack(COMMENT);
    if (type == MipsElementTypes.OPERATOR)
      return pack(OPERATOR);
    if (type == MipsElementTypes.DIRECTIVE)
      return pack(DIRECTIVE);
    if (type == MipsElementTypes.IDENTIFIER)
      return pack(LABEL);
    if (MipsTokenTypes.STRINGS.contains(type))
      return pack(STRING);
    if (MipsTokenTypes.NUMBERS.contains(type))
      return pack(NUMBER);
    if (MipsTokenTypes.REGISTERS.contains(type))
      return pack(REGISTER);

    return EMPTY;
  }
}
