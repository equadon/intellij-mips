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

package com.equadon.intellij.mips.run;

import com.equadon.intellij.mips.lang.psi.MipsFile;
import com.equadon.intellij.mips.lang.psi.MipsLabelDefinition;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Iterator;

import static com.intellij.execution.configurations.ConfigurationType.CONFIGURATION_TYPE_EP;

public class MipsRunConfigurationProducer extends RunConfigurationProducer<MipsRunConfiguration> {
  protected MipsRunConfigurationProducer() {
    super(Extensions.findExtension(CONFIGURATION_TYPE_EP, MipsRunConfigurationType.class));
  }

  @Override
  protected boolean setupConfigurationFromContext(MipsRunConfiguration cfg, ConfigurationContext context, Ref<PsiElement> ref) {
    PsiElement psiElement = ref.get();
    if (psiElement == null || !psiElement.isValid()) {
      return false;
    }

    PsiFile file = psiElement.getContainingFile();

    if (!(file instanceof MipsFile)) {
      return false;
    }

    String mainFile = file.getVirtualFile().getCanonicalPath();

    cfg.setMainFile(mainFile);
    cfg.setAllowExtendedInstructions(true);
    cfg.setStartMain(false);

    cfg.setName(file.getVirtualFile().getName() + ":main");

    for (Iterator<MipsLabelDefinition> it = PsiTreeUtil.childIterator(file, MipsLabelDefinition.class); it.hasNext(); ) {
      MipsLabelDefinition label = it.next();

      if (label.getName() != null && label.getName().equals("main")) {
        cfg.setStartMain(true);
        break;
      }
    }

    return true;
  }

  @Override
  public boolean isConfigurationFromContext(MipsRunConfiguration cfg, ConfigurationContext context) {
    PsiElement psiElement = context.getPsiLocation();
    if (psiElement == null || !psiElement.isValid()) {
      return false;
    }

    PsiFile file = psiElement.getContainingFile();
    if (!(file instanceof MipsFile)) {
      return false;
    }

    String cfgName = file.getVirtualFile().getName() + ":main";

    return cfg.getName().equals(cfgName);
  }
}
