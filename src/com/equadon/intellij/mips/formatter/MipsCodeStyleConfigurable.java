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
import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.psi.codeStyle.CodeStyleSettings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MipsCodeStyleConfigurable extends CodeStyleAbstractConfigurable {
  public MipsCodeStyleConfigurable(@NotNull CodeStyleSettings settings, CodeStyleSettings cloneSettings) {
    super(settings, cloneSettings, "MIPS");
  }

  @Override
  protected CodeStyleAbstractPanel createPanel(CodeStyleSettings settings) {
    return new MipsCodeStyleMainPanel(getCurrentSettings(), settings);
  }

  @Nullable
  @Override
  public String getHelpTopic() {
    return null;
  }

  private static class MipsCodeStyleMainPanel extends TabbedLanguageCodeStylePanel {
    private MipsCodeStyleMainPanel(CodeStyleSettings currentSettings, CodeStyleSettings settings) {
      super(MipsLanguage.INSTANCE, currentSettings, settings);
    }
  }
}
