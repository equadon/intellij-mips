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

import com.equadon.intellij.mips.run.controllers.MipsSimulatorController;
import com.intellij.openapi.project.Project;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.frame.XStackFrame;

import org.jetbrains.annotations.Nullable;

public class MipsStackFrame extends XStackFrame {
  private final Project project;
  private final MipsSimulatorController controller;
  private final XSourcePosition position;
  private final int index;

  public MipsStackFrame(Project project, MipsSimulatorController controller, XSourcePosition position, int index) {
    this.project = project;
    this.controller = controller;
    this.position = position;
    this.index = index;
  }

  @Nullable
  @Override
  public XSourcePosition getSourcePosition() {
    return position;
  }
}
