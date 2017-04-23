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

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import static com.equadon.intellij.mips.lang.psi.MipsElementTypes.*;

public class MipsTokenTypes {
  public static final IElementType COMMENT = new MipsElementType("COMMENT");

  public static final IElementType TAB = new MipsElementType("TAB");

  /** Token sets */
  public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE, TAB);
  public static final TokenSet COMMENTS = TokenSet.create(COMMENT);
  public static final TokenSet STRINGS = TokenSet.create(LQUOTE, QUOTED_STRING, RQUOTE);

  public static final TokenSet NUMBERS = TokenSet.create(INTEGER_5, INTEGER_16, INTEGER_16U, REAL_NUMBER);
  public static final TokenSet REGISTERS = TokenSet.create(REGISTER_NAME, REGISTER_NUMBER);
}
