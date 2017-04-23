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

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;
import com.yourkit.util.ArrayUtil;

import org.jetbrains.annotations.Nullable;

public class MipsLiveTemplateProvider implements DefaultLiveTemplatesProvider {
  private static final String[] TEMPLATE_FILES = { "liveTemplates/mips" };

  @Override
  public String[] getDefaultLiveTemplateFiles() {
    return TEMPLATE_FILES;
  }

  @Nullable
  @Override
  public String[] getHiddenLiveTemplateFiles() {
    return ArrayUtil.EMPTY_STRING_ARRAY;
  }
}
