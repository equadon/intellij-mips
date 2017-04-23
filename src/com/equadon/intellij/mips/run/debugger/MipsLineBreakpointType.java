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

package com.equadon.intellij.mips.run.debugger;

import com.equadon.intellij.mips.lang.MipsFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.breakpoints.XBreakpointProperties;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.intellij.xdebugger.breakpoints.XLineBreakpointTypeBase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MipsLineBreakpointType extends XLineBreakpointTypeBase {
  protected MipsLineBreakpointType() {
    super("mips-line", "MIPS Line Breakpoint", new MipsDebuggerEditorsProvider());
  }

  @Override
  public String getDisplayText(XLineBreakpoint<XBreakpointProperties> breakpoint) {
    XSourcePosition sourcePosition = breakpoint.getSourcePosition();

    assert sourcePosition != null;
    return "Line " + String.valueOf(sourcePosition.getLine()) +
        " in file " + sourcePosition.getFile().getName();
  }

  @Override
  public boolean canPutAt(@NotNull VirtualFile file, int line, @NotNull Project project) {
    return file.getFileType() == MipsFileType.INSTANCE;
  }

  @Nullable
  @Override
  public XBreakpointProperties createBreakpointProperties(@NotNull VirtualFile virtualFile, int line) {
    return null;
  }
}
