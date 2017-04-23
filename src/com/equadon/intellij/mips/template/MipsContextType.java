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

package com.equadon.intellij.mips.template;

import com.equadon.intellij.mips.lang.MipsLanguage;
import com.equadon.intellij.mips.lang.psi.MipsFile;
import com.intellij.codeInsight.template.EverywhereContextType;
import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiUtilCore;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MipsContextType extends TemplateContextType {
  protected MipsContextType(@NotNull String id, @NotNull String presentableName, @Nullable Class<? extends TemplateContextType> baseContextType) {
    super(id, presentableName, baseContextType);
  }

  @Override
  public boolean isInContext(@NotNull PsiFile file, int offset) {
    if (!PsiUtilCore.getLanguageAtOffset(file, offset).isKindOf(MipsLanguage.INSTANCE)) return false;
    PsiElement element = file.findElementAt(offset);
    if (element instanceof PsiWhiteSpace) {
      return false;
    }
    return element != null && isInContext(element);
  }

  protected abstract boolean isInContext(PsiElement element);

  protected static class Generic extends MipsContextType {
    protected Generic() {
      super("MIPS_CODE", "MIPS", EverywhereContextType.class);
    }

    @Override
    protected boolean isInContext(PsiElement element) {
      return true;
    }
  }

  protected static class Declaration extends MipsContextType {
    protected Declaration() {
      super("MIPS_DECLARATION", "MIPS", Generic.class);
    }

    @Override
    protected boolean isInContext(PsiElement element) {
      return element != null && element.getParent() instanceof MipsFile;
    }
  }
}
