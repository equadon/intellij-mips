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

package com.equadon.intellij.mips.actions;

import com.equadon.intellij.mips.icons.MipsIcons;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDirectory;

import org.jetbrains.annotations.Nullable;

public class CreateMipsFileAction extends CreateFileFromTemplateAction implements DumbAware {
  private static final String NEW_MIPS_FILE = "New MIPS Application";

  public CreateMipsFileAction() {
    super(NEW_MIPS_FILE, "", MipsIcons.FILE);
  }

  @Override
  protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
    builder
        .setTitle(NEW_MIPS_FILE)
        .addKind("Application", MipsIcons.FILE, "MIPS Application")
        .setValidator(new InputValidatorEx() {
          @Nullable
          @Override
          public String getErrorText(String s) {
            return !StringUtil.isEmpty(s) && FileUtil.sanitizeFileName(s, false).equals(s) ? null :
                "'" + s + "'" + " is not a valid MIPS application name";
          }

          @Override
          public boolean checkInput(String s) {
            return getErrorText(s) == null;
          }

          @Override
          public boolean canClose(String s) {
            return getErrorText(s) == null;
          }
        });
  }

  @Override
  protected String getActionName(PsiDirectory psiDirectory, String s, String s1) {
    return NEW_MIPS_FILE;
  }
}
