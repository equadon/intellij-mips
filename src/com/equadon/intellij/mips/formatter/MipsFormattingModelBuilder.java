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

package com.equadon.intellij.mips.formatter;

import com.equadon.intellij.mips.lang.MipsLanguage;
import com.equadon.intellij.mips.lang.psi.MipsElementTypes;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.tree.TokenSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MipsFormattingModelBuilder implements FormattingModelBuilder {
  @NotNull
  @Override
  public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
    CommonCodeStyleSettings commonSettings = settings.getCommonSettings(MipsLanguage.INSTANCE);
    MipsCodeStyleSettings mipsSettings = settings.getCustomSettings(MipsCodeStyleSettings.class);
    SpacingBuilder spacingBuilder = createSpaceBuilder(commonSettings, mipsSettings);
    MipsFormattingBlock block = new MipsFormattingBlock(element.getNode(), null, null, spacingBuilder, commonSettings, mipsSettings);
    return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(), block, settings);
  }

  private static SpacingBuilder createSpaceBuilder(CommonCodeStyleSettings common, MipsCodeStyleSettings mips) {
    TokenSet additiveOperators = TokenSet.create(MipsElementTypes.PLUS, MipsElementTypes.MINUS);

    return new SpacingBuilder(common.getRootSettings(), MipsLanguage.INSTANCE)
        .before(MipsElementTypes.COLON).spaceIf(common.SPACE_BEFORE_COLON)
        .before(MipsElementTypes.COMMA).spaceIf(common.SPACE_BEFORE_COMMA)
        .after(MipsElementTypes.COMMA).spaceIf(common.SPACE_AFTER_COMMA)
        .around(additiveOperators).spaceIf(common.SPACE_AROUND_ADDITIVE_OPERATORS)

        .beforeInside(MipsElementTypes.REGISTER_LITERAL, MipsElementTypes.INSTRUCTION_ARG).spaceIf(common.SPACE_AFTER_COMMA)

        .after(MipsElementTypes.LPAREN).spaceIf(common.SPACE_WITHIN_PARENTHESES)
        .before(MipsElementTypes.RPAREN).spaceIf(common.SPACE_WITHIN_PARENTHESES)
        .after(MipsElementTypes.RPAREN).none()

        .after(MipsElementTypes.REGISTER_OFFSET).spaceIf(mips.SPACE_AFTER_REGISTER_OFFSET)
        ;
  }

  @Nullable
  @Override
  public TextRange getRangeAffectingIndent(PsiFile file, int i, ASTNode node) {
    return null;
  }
}
