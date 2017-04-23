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
import com.equadon.intellij.mips.lang.psi.MipsInstructionArg;
import com.equadon.intellij.mips.lang.psi.MipsNamedElement;
import com.equadon.intellij.mips.lang.psi.impl.MipsInstructionArgImpl;
import com.equadon.intellij.mips.lang.psi.impl.MipsNamedElementImpl;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MipsInstructionMixin extends MipsNamedElementImpl implements MipsNamedElement {
  public MipsInstructionMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    return getOperator();
  }

  /**
   * Get the operator element.
   * @return operator element
   */
  public PsiElement getOperator() {
    PsiElement child = getFirstChild();

    if (child != null && child.getNode().getElementType() == MipsElementTypes.OPERATOR)
      return child;

    return null;
  }

  /**
   * Get a list of the instruction arguments.
   * @return list of arguments
   */
  public List<MipsInstructionArg> getArguments() {
    List<MipsInstructionArg> args = new ArrayList<>();
    for (PsiElement child : getChildren())
      if (child instanceof MipsInstructionArgImpl)
        args.add((MipsInstructionArg) child);

    return args;
  }

  /**
   * Check if this instruction has any arguments.
   * @return true if instruction has arguments
   */
  public boolean hasArguments() {
    for (PsiElement child : getChildren())
      if (child instanceof MipsInstructionArgImpl)
        return true;

    return false;
  }
}
