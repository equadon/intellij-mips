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

package com.equadon.intellij.mips.lang.psi.mixins;

import com.equadon.intellij.mips.lang.psi.MipsElementTypes;
import com.equadon.intellij.mips.lang.psi.MipsNumberLiteral;
import com.equadon.intellij.mips.lang.psi.impl.MipsNamedElementImpl;
import com.intellij.lang.ASTNode;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MipsNumberRangeMixin extends MipsNamedElementImpl {
  public MipsNumberRangeMixin(@NotNull ASTNode node) {
    super(node);
  }

  public MipsNumberLiteral getRangeStart() {
    return findChildByType(MipsElementTypes.NUMBER_LITERAL);
  }

  public MipsNumberLiteral getRangeEnd() {
    List<MipsNumberLiteral> numbers = new ArrayList<>(findChildrenByType(MipsElementTypes.NUMBER_LITERAL));

    return (numbers.size() == 2) ? numbers.get(1) : null;
  }
}
