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
import com.equadon.intellij.mips.lang.psi.MipsRegisterLiteral;
import com.equadon.intellij.mips.lang.psi.impl.MipsNamedElementImpl;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;

public class MipsInstructionArgMixin extends MipsNamedElementImpl {
  public MipsInstructionArgMixin(@NotNull ASTNode node) {
    super(node);
  }

  public boolean isRegister() {
    return getFirstChild() instanceof MipsRegisterLiteral;
  }

  public boolean isNumber() {
    return getFirstChild() instanceof MipsNumberLiteral;
  }

  public boolean isIdentifier() {
    PsiElement child = getFirstChild();

    return (child != null && child.getNode().getElementType() == MipsElementTypes.IDENTIFIER);
  }
}
