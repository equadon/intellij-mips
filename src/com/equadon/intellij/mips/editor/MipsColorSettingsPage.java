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

import com.equadon.intellij.mips.icons.MipsIcons;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.util.containers.ContainerUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import javax.swing.*;

import static com.equadon.intellij.mips.editor.MipsSyntaxHighlighter.*;

public class MipsColorSettingsPage implements ColorSettingsPage {
  private static final AttributesDescriptor[] ATTRIBUTES = new AttributesDescriptor[] {
    new AttributesDescriptor("Illegal character", ERROR),
    new AttributesDescriptor("Comment", COMMENT),
    new AttributesDescriptor("String", STRING),
    new AttributesDescriptor("Number", NUMBER),
    new AttributesDescriptor("Register", REGISTER),
    new AttributesDescriptor("Operator", OPERATOR),
    new AttributesDescriptor("Directive", DIRECTIVE),
    new AttributesDescriptor("Label", LABEL),
  };

  private static Map<String, TextAttributesKey> ATTRIBUTES_KEY_MAP = ContainerUtil.newHashMap();

  static {
    ATTRIBUTES_KEY_MAP.put("d", DIRECTIVE);
    ATTRIBUTES_KEY_MAP.put("l", LABEL);
    ATTRIBUTES_KEY_MAP.put("o", OPERATOR);
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return MipsIcons.FILE;
  }

  @NotNull
  @Override
  public SyntaxHighlighter getHighlighter() {
    return new MipsSyntaxHighlighter();
  }

  @NotNull
  @Override
  public String getDemoText() {
    return "# this is a comment!\n" +
        ".data\n" +
        "myString: .asciiz \"Hello, world!\\n\"\n\n" +
        ".text\n\n" +
        "main\n" +
        "j exit\n\n" +
        "exit\n" +
        "  li $v0, 10\n" +
        "  syscall\n";
  }

  @Nullable
  @Override
  public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    return ATTRIBUTES_KEY_MAP;
  }

  @NotNull
  @Override
  public AttributesDescriptor[] getAttributeDescriptors() {
    return ATTRIBUTES;
  }

  @NotNull
  @Override
  public ColorDescriptor[] getColorDescriptors() {
    return ColorDescriptor.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "MIPS";
  }
}
