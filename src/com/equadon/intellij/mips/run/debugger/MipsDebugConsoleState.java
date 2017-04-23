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

import com.equadon.intellij.mips.run.MipsConsoleState;
import com.equadon.intellij.mips.run.MipsRunConfiguration;
import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MipsDebugConsoleState extends MipsConsoleState {
  public MipsDebugConsoleState(MipsRunConfiguration cfg, ExecutionEnvironment env, Project project) {
    super(cfg, env, project);
  }

  @Nullable
  @Override
  public ExecutionResult execute(Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
    if (!(runner instanceof MipsDebugRunner)) throw new ExecutionException("Invalid runner: " + runner + ", must be of type MipsDebugRunner");

    return new DefaultExecutionResult(console, ((MipsDebugRunner) runner).getDebugProcess().getProcessHandler());
  }

  public ConsoleView getConsole() {
    return console;
  }
}
