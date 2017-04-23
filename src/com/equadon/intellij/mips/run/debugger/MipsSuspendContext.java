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

import com.intellij.openapi.project.Project;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.breakpoints.XBreakpoint;
import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XSuspendContext;

import org.jetbrains.annotations.Nullable;

public class MipsSuspendContext extends XSuspendContext {
  private final Project project;
  private final MipsDebuggerController controller;
  private final XBreakpoint breakpoint;
  private final XSourcePosition position;
  private final int programCounter;

  private XExecutionStack executionStack;

  public MipsSuspendContext(Project project, MipsDebuggerController controller, XBreakpoint breakpoint, int programCounter) {
    this.project = project;
    this.controller = controller;
    this.breakpoint = breakpoint;
    this.programCounter = programCounter;
    this.position = breakpoint.getSourcePosition();
    this.executionStack = initExecutionStack();
  }

  @Nullable
  @Override
  public XExecutionStack getActiveExecutionStack() {
    return initExecutionStack();
  }

  private XExecutionStack initExecutionStack() {
    MipsStackFrame frame = new MipsStackFrame(project, controller, position, 0);

    return new MipsExecutionStack(project, controller, "Main sub routine", frame);
  }
}
