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

import com.equadon.intellij.mips.run.MipsRunConfiguration;
import com.equadon.intellij.mips.run.controllers.MipsSimulatorController;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ExecutionConsole;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.breakpoints.XBreakpoint;
import com.intellij.xdebugger.breakpoints.XBreakpointHandler;
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider;
import com.intellij.xdebugger.frame.XSuspendContext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

public class MipsDebugProcess extends XDebugProcess {
  private final static Logger LOG = Logger.getLogger("MIPS");

  private MipsSimulatorController controller;
  private final ExecutionEnvironment env;

  private MipsLineBreakpointHandler lineBreakpointHandler;
  private MipsDebugConsoleState state;

  public MipsDebugProcess(@NotNull XDebugSession session, ExecutionEnvironment env) throws ExecutionException {
    super(session);

    this.env = env;
    this.controller = null;

    lineBreakpointHandler = new MipsLineBreakpointHandler(this);
    state = (MipsDebugConsoleState) getRunConfig().getState(env.getExecutor(), env);
  }

  public void setController(MipsSimulatorController controller) {
    this.controller = controller;
  }

  @Override
  public void sessionInitialized() {
    getSession().resume();
  }

  public MipsRunConfiguration getRunConfig() {
    return (MipsRunConfiguration) env.getRunProfile();
  }

  @Override
  public void startStepOver(@Nullable XSuspendContext context) {
    controller.step();
  }

  @NotNull
  @Override
  public XBreakpointHandler<?>[] getBreakpointHandlers() {
    return new XBreakpointHandler<?>[] { lineBreakpointHandler };
  }

  @NotNull
  @Override
  public ExecutionConsole createConsole() {
    return state.getConsole();
  }

  @Override
  public void resume(@Nullable XSuspendContext context) {
    LOG.info("DebugProcess.resume()");
    controller.resume();
  }

  @Override
  public void stop() {
    LOG.info("DebugProcess.stop()");
    controller.stop();
  }

  @NotNull
  @Override
  public XDebuggerEditorsProvider getEditorsProvider() {
    return new MipsDebuggerEditorsProvider();
  }

  public void addBreakpoint(XBreakpoint breakpoint) {
    LOG.info("DebugProcess.addBreakpoint()");
    controller.addBreakpoint(breakpoint);
  }

  public void removeBreakpoint(XBreakpoint breakpoint, boolean temporary) {
    LOG.info("DebugProcess.removeBreakpoint()");
  }
}
