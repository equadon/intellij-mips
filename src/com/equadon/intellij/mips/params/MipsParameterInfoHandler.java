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

package com.equadon.intellij.mips.completion;

import com.equadon.intellij.mips.lang.psi.MipsDirectiveStatement;
import com.equadon.intellij.mips.lang.psi.MipsElement;
import com.equadon.intellij.mips.lang.psi.MipsInstruction;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.parameterInfo.CreateParameterInfoContext;
import com.intellij.lang.parameterInfo.ParameterInfoContext;
import com.intellij.lang.parameterInfo.ParameterInfoHandler;
import com.intellij.lang.parameterInfo.ParameterInfoUIContext;
import com.intellij.lang.parameterInfo.UpdateParameterInfoContext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

public class MipsParameterInfoHandler implements ParameterInfoHandler<MipsElement, MipsParameterInfoHandler.ParameterInfo> {
  private static final Logger LOG = Logger.getGlobal();

  @Override
  public boolean couldShowInLookup() {
    return true;
  }

  @Nullable
  @Override
  public Object[] getParametersForLookup(LookupElement element, ParameterInfoContext context) {
    return new Object[0];
  }

  @Nullable
  @Override
  public Object[] getParametersForDocumentation(ParameterInfo info, ParameterInfoContext context) {
    return new Object[0];
  }

  @Nullable
  @Override
  public MipsElement findElementForParameterInfo(@NotNull CreateParameterInfoContext context) {
    return null;
  }

  @Override
  public void showParameterInfo(@NotNull MipsElement element, @NotNull CreateParameterInfoContext context) {
    LOG.info("element: " + element + ", context: " + context);

    context.showHint(element, element.getTextRange().getStartOffset(), this);
  }

  @Nullable
  @Override
  public MipsElement findElementForUpdatingParameterInfo(@NotNull UpdateParameterInfoContext context) {
    return null;
  }

  @Override
  public void updateParameterInfo(@NotNull MipsElement element, @NotNull UpdateParameterInfoContext context) {
  }

  @Nullable
  @Override
  public String getParameterCloseChars() {
    return null;
  }

  @Override
  public boolean tracksParameterIndex() {
    return false;
  }

  @Override
  public void updateUI(ParameterInfo info, @NotNull ParameterInfoUIContext context) {
  }

  public static class ParameterInfo {
    public final MipsInstruction instruction;
    public final MipsDirectiveStatement directive;

    public final int currentIndex;

    private ParameterInfo(MipsInstruction instruction, int currentIndex) {
      this(instruction, null, currentIndex);
    }

    private ParameterInfo(MipsDirectiveStatement directive, int currentIndex) {
      this(null, directive, currentIndex);
    }

    private ParameterInfo(MipsInstruction instruction, MipsDirectiveStatement directive, int currentIndex) {
      this.instruction = instruction;
      this.directive = directive;
      this.currentIndex = currentIndex;
    }
  }
}
