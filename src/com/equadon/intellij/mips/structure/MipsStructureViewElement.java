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

package com.equadon.intellij.mips.structure;

import com.equadon.intellij.mips.lang.psi.MipsFile;
import com.equadon.intellij.mips.lang.psi.MipsLabelDefinition;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class MipsStructureViewElement implements StructureViewTreeElement, ItemPresentation {
  private final PsiElement element;

  public MipsStructureViewElement(PsiElement element) {
    this.element = element;
  }

  @Override
  public Object getValue() {
    return element;
  }

  @NotNull
  @Override
  public ItemPresentation getPresentation() {
    return this;
  }

  @NotNull
  @Override
  public TreeElement[] getChildren() {
    if (element instanceof MipsFile) {
      MipsLabelDefinition[] labels = PsiTreeUtil.getChildrenOfType(element, MipsLabelDefinition.class);

      if (labels == null) return EMPTY_ARRAY;

      List<TreeElement> treeElements = new ArrayList<>(1);

      for (MipsLabelDefinition label : labels) {
        treeElements.add(new MipsStructureViewElement(label));
      }

      return treeElements.toArray(new TreeElement[treeElements.size()]);
    } else {
      return EMPTY_ARRAY;
    }
  }

  @Override
  public void navigate(boolean requestFocus) {
    if (element instanceof NavigationItem) {
      ((NavigationItem) element).navigate(requestFocus);
    }
  }

  @Override
  public boolean canNavigate() {
    return element instanceof NavigationItem &&
        ((NavigationItem) element).canNavigate();
  }

  @Override
  public boolean canNavigateToSource() {
    return element instanceof NavigationItem &&
        ((NavigationItem) element).canNavigateToSource();
  }

  @Nullable
  @Override
  public String getPresentableText() {
    if (element instanceof MipsFile)
      return ((MipsFile) element).getName();
    if (element instanceof MipsLabelDefinition)
      return ((MipsLabelDefinition) element).getName();

    throw new AssertionError(element.getClass().getName());
  }

  @Nullable
  @Override
  public String getLocationString() {
    if (element instanceof MipsFile)
      return ((MipsFile) element).getFileType().getDescription();
    if (element instanceof MipsLabelDefinition)
      return "label";

    return null;
  }

  @Nullable
  @Override
  public Icon getIcon(boolean unused) {
    return element.getIcon(0);
  }
}
