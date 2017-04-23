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

package com.equadon.intellij.mips.stubs.types;

import com.equadon.intellij.mips.lang.MipsLanguage;
import com.equadon.intellij.mips.stubs.MipsFileStub;
import com.intellij.psi.tree.IStubFileElementType;

public class MipsFileElementType extends IStubFileElementType<MipsFileStub> {
  public static final IStubFileElementType INSTANCE = new MipsFileElementType();
  private MipsFileElementType() {
    super("MIPS_FILE", MipsLanguage.INSTANCE);
  }
}
