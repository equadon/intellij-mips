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

package com.equadon.intellij.mips.lang.psi.mixins;

import com.equadon.intellij.mips.lang.psi.MipsElementTypes;
import com.equadon.intellij.mips.lang.psi.MipsNumberLiteral;
import com.equadon.intellij.mips.lang.psi.impl.MipsNamedElementImpl;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MipsDirectiveMixin extends MipsNamedElementImpl {
  public MipsDirectiveMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    PsiElement child = getFirstChild();

    if (child.getNode().getElementType() == MipsElementTypes.DIRECTIVE)
      return child;

    return null;
  }

  public List<MipsNumberLiteral> getNumbers() {
    return findChildrenByType(MipsElementTypes.NUMBER_LITERAL);
  }

  public String getIdentifier() {
    PsiElement id = findChildByType(MipsElementTypes.IDENTIFIER);

    if (id != null)
      return id.getText();

    return null;
  }

  public String getString() {
    PsiElement string = findChildByType(MipsElementTypes.STRING_LITERAL);

    if (string != null)
      return string.getText();

    return null;
  }
}
