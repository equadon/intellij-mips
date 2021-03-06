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

import com.equadon.intellij.mips.run.controllers.MarsSimulatorController;
import com.equadon.intellij.mips.run.controllers.MipsSimulatorController;
import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.NopProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MipsConsoleState implements RunProfileState {
  protected final MipsRunConfiguration cfg;
  protected final ExecutionEnvironment env;
  protected final Project project;
  protected final ConsoleView console;

  protected MipsSimulatorController controller;

  public MipsConsoleState(MipsRunConfiguration cfg, ExecutionEnvironment env, Project project) {
    this.cfg = cfg;
    this.env = env;
    this.project = project;

    TextConsoleBuilder builder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
    this.console = builder.getConsole();
  }

  public ConsoleView getConsole() {
    return console;
  }

  @Nullable
  @Override
  public ExecutionResult execute(Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
    ProcessHandler processHandler = new NopProcessHandler();

    controller = new MarsSimulatorController(cfg, console, processHandler);
    controller.resume();

    return new DefaultExecutionResult(console, processHandler);
  }
}
