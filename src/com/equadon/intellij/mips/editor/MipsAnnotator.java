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

import com.equadon.intellij.mips.lang.psi.MipsLabelDefinition;
import com.equadon.intellij.mips.lang.psi.MipsVisitor;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;

public class MipsAnnotator implements Annotator, DumbAware {
  @Override
  public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
    element.accept(new MipsVisitor() {
      @Override
      public void visitLabelDefinition(@NotNull MipsLabelDefinition o) {
        // Highlight main label
        if (o.getName().equals("main")) {
          setHighlighting(o.getLabelIdentifier(), holder, MipsSyntaxHighlighter.MAIN_LABEL);
        }
      }
    });
  }

  private static void setHighlighting(@NotNull PsiElement element, @NotNull AnnotationHolder holder, @NotNull TextAttributesKey key) {
    holder.createInfoAnnotation(element, null).setEnforcedTextAttributes(TextAttributes.ERASE_MARKER);
    holder.createInfoAnnotation(element, null).setEnforcedTextAttributes(EditorColorsManager.getInstance().getGlobalScheme().getAttributes(key));
  }
}
