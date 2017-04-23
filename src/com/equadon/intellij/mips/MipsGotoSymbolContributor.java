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

package com.equadon.intellij.mips;

import com.equadon.intellij.mips.lang.psi.MipsLabelDefinition;
import com.equadon.intellij.mips.lang.psi.impl.MipsLabelDefinitionImpl;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MipsGotoSymbolContributor implements ChooseByNameContributor {
  @NotNull
  @Override
  public String[] getNames(Project project, boolean includeNonProjectItems) {
    List<MipsLabelDefinition> labels = MipsUtil.findNamedElements(project, MipsLabelDefinition.class);
    List<String> names = new ArrayList<>(labels.size());
    for (MipsLabelDefinition label : labels) {
      if (label.getName() != null && !label.getName().isEmpty()) {
        names.add(label.getName());
      }
    }

    return names.toArray(new String[names.size()]);
  }

  @NotNull
  @Override
  public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
    List<MipsLabelDefinitionImpl> labels = MipsUtil.findNamedElements(project, name, MipsLabelDefinitionImpl.class);

    return labels.toArray(new NavigationItem[labels.size()]);
  }
}
