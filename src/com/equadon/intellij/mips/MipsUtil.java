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

import com.equadon.intellij.mips.lang.MipsFileType;
import com.equadon.intellij.mips.lang.psi.MipsFile;
import com.equadon.intellij.mips.lang.psi.MipsNamedElement;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MipsUtil {
  /**
   * Find named elements that belong to the given project.
   * @param project Project that the elements belong to
   * @param cls Class type
   * @param <E> MipsNamedElement type
   * @return List of elements
   */
  public static <E extends MipsNamedElement> List<E> findNamedElements(Project project, Class<E> cls) {
    List<E> result = new ArrayList<>();
    Collection<VirtualFile> virtualFiles =
        FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, MipsFileType.INSTANCE, GlobalSearchScope.allScope(project));

    for (VirtualFile virtualFile : virtualFiles) {
      MipsFile file = (MipsFile) PsiManager.getInstance(project).findFile(virtualFile);
      if (file != null) {
        E[] items = PsiTreeUtil.getChildrenOfType(file, cls);

        if (items != null) {
          Collections.addAll(result, items);
        }
      }
    }

    return result;
  }

  /**
   * Find named elements that belong to the given project matching a key.
   * @param project Project that the elements belong to
   * @param key Key to match
   * @param cls Class type
   * @param <E> MipsNamedElement type
   * @return List of elements
   */
  public static <E extends MipsNamedElement> List<E> findNamedElements(Project project, String key, Class<E> cls) {
    List<E> result = null;
    Collection<VirtualFile> virtualFiles =
        FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, MipsFileType.INSTANCE, GlobalSearchScope.allScope(project));

    for (VirtualFile virtualFile : virtualFiles) {
      MipsFile file = (MipsFile) PsiManager.getInstance(project).findFile(virtualFile);
      if (file != null) {
        E[] items = PsiTreeUtil.getChildrenOfType(file, cls);

        if (items != null) {
          for (E item : items) {
            if (item.getName() != null && item.getName().equals(key)) {
              if (result == null) {
                result = new ArrayList<>();
              }

              result.add(item);
            }
          }
        }
      }
    }

    return result == null ? Collections.emptyList() : result;
  }
}
