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

package com.equadon.intellij.mips.lang;

import com.equadon.intellij.mips.icons.MipsIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MipsFileType extends LanguageFileType {
  public static final MipsFileType INSTANCE = new MipsFileType();

  public static final String DEFAULT_EXTENSION = "s";

  private MipsFileType() {
    super(MipsLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public String getName() {
    return MipsLanguage.NAME;
  }

  @NotNull
  @Override
  public String getDescription() {
    return "MIPS File";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return DEFAULT_EXTENSION;
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return MipsIcons.FILE;
  }
}
