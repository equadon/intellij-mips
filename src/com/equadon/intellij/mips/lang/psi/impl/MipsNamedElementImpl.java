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

package com.equadon.intellij.mips.lang.psi.impl;

import com.equadon.intellij.mips.lang.psi.MipsNamedElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MipsNamedElementImpl extends MipsElementImpl implements MipsNamedElement {
  public MipsNamedElementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String getName() {
    PsiElement element = getNameIdentifier();
    if (element != null)
      return getValue(element);

    return null;
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    return getFirstChild();
  }

  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    return null;
  }

  public static String getValue(PsiElement element) {
    return element.getText();
  }
}
