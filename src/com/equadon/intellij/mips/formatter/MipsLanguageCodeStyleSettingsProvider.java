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
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MipsLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {
  private static final String MIPS = "MIPS";

  @NotNull
  @Override
  public Language getLanguage() {
    return MipsLanguage.INSTANCE;
  }

  @Nullable
  @Override
  public CommonCodeStyleSettings getDefaultCommonSettings() {
    CommonCodeStyleSettings common = new CommonCodeStyleSettings(getLanguage());
    common.SPACE_BEFORE_COLON = false;
    common.SPACE_WITHIN_PARENTHESES = false;
    common.SPACE_AROUND_ADDITIVE_OPERATORS = false;

    CommonCodeStyleSettings.IndentOptions indent = common.initIndentOptions();
    indent.USE_TAB_CHARACTER = false;

    return common;
  }

  @Override
  public void customizeSettings(@NotNull CodeStyleSettingsCustomizable consumer, @NotNull SettingsType settingsType) {
    if (settingsType == SettingsType.SPACING_SETTINGS) {
      consumer.showStandardOptions(
          "SPACE_BEFORE_COLON",
          "SPACE_BEFORE_COMMA",
          "SPACE_AFTER_COMMA",
          "SPACE_WITHIN_PARENTHESES",
          "SPACE_AROUND_ADDITIVE_OPERATORS"
      );

      consumer.showCustomOption(MipsCodeStyleSettings.class, "SPACE_AFTER_REGISTER_OFFSET", "After register offset", MIPS);
    } else if (settingsType == SettingsType.INDENT_SETTINGS) {
      consumer.showStandardOptions(
          "USE_TAB_CHARACTER"
      );
    } else if (settingsType == SettingsType.BLANK_LINES_SETTINGS) {
    } else if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
    }
  }

  @Override
  public String getCodeSample(@NotNull SettingsType settingsType) {
    return DEFAULT_CODE_SAMPLE;
  }

  private static final String DEFAULT_CODE_SAMPLE =
      "# This is a comment\n" +
          ".data\n" +
          "  integer: .word 10\n" +
          "  myString: .asciiz \"Hello, world!\\n\"\n" +
          "\n" +
          ".text\n" +
          "\n" +
          "\n" +
          "main:\n" +
          "  lw $t1, integer+5($t0)\n" +
          "  j exit\n" +
          "\n" +
          "exit:\n" +
          "  li $v0, 4\n" +
          "  la $a0, myString\n" +
          "  syscall\n" +
          "\n" +
          "\n" +
          "\n" +
          "# exit application\n" +
          "  li $v0, 10\n" +
          "  syscall\n";
}
