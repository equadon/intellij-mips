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

import com.equadon.intellij.mips.lang.MipsLanguage;
import com.equadon.intellij.mips.lang.psi.MipsFile;
import com.equadon.intellij.mips.lang.psi.MipsLabelDefinition;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.util.PsiTreeUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class MipsFileImpl extends PsiFileBase implements MipsFile {
  public MipsFileImpl(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, MipsLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return getViewProvider().getFileType();
  }

  @Override
  public Collection<MipsLabelDefinition> getLabelDefinitions() {
    return PsiTreeUtil.findChildrenOfType(this, MipsLabelDefinition.class);
  }
}
