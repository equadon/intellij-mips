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

package com.equadon.intellij.mips.run.controllers;

import com.equadon.intellij.mips.run.MipsRunConfiguration;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.breakpoints.XBreakpoint;

/**
 * Simulator controllers should implement this interface to handle simulation and debugging.
 */
public abstract class MipsSimulatorController {
  protected final boolean debugger;
  protected final MipsRunConfiguration cfg;
  protected final ConsoleView console;
  protected final ProcessHandler processHandler;
  protected final XDebugProcess debugProcess;
  protected final XDebugSession debugSession;

  protected MipsConsoleInputStream inputStream;

  public MipsSimulatorController(MipsRunConfiguration cfg, ConsoleView console, ProcessHandler processHandler) {
    this(false, cfg, console, processHandler, null, null);
  }

  public MipsSimulatorController(MipsRunConfiguration cfg, ConsoleView console, XDebugProcess process, XDebugSession session) {
    this(true, cfg, console, null, process, session);
  }

  public MipsSimulatorController(boolean debugger,
                                 MipsRunConfiguration cfg,
                                 ConsoleView console,
                                 ProcessHandler processHandler,
                                 XDebugProcess debugProcess,
                                 XDebugSession debugSession) {
    this.debugger = debugger;
    this.cfg = cfg;
    this.console = console;
    this.processHandler = processHandler;
    this.debugProcess = debugProcess;
    this.debugSession = debugSession;

    inputStream = new MipsConsoleInputStream(cfg.getProject());

    // Intercept System.out and System.in to handle output and input.
    System.setOut(new MipsConsoleOutputStream(this, inputStream));
    System.setIn(inputStream);
  }

  public boolean isDebugger() {
    return debugger;
  }

  /**
   * Check if simulator has paused (at a breakpoint)
   * @return true if paused
   */
  public abstract boolean isPaused();

  /**
   * Check if simulator is finished (i.e. no more instructions to execute)
   * @return true if finished
   */
  public abstract boolean isFinished();

  /**
   * Resume simulation.
   */
  public abstract void resume();

  /**
   * Pause simulation.
   */
  public abstract void pause();

  /**
   * Stop simulation.
   */
  public abstract void stop();

  /**
   * Step one instruction.
   */
  public abstract void step();

  /**
   * Add new breakpoint.
   * @param breakpoint breakpoint to add
   */
  public abstract void addBreakpoint(XBreakpoint breakpoint);

  /**
   * Remove breakpoint.
   * @param breakpoint breakpoint to remove
   */
  public abstract void removeBreakpoint(XBreakpoint breakpoint);

  public void println(String message) {
    print(message + "\n");
  }

  public void print(String message) {
    print(message, ConsoleViewContentType.NORMAL_OUTPUT);
  }

  public void printlnSystem(String message) {
    printSystem(message + "\n");
  }

  public void printSystem(String message) {
    print(message, ConsoleViewContentType.SYSTEM_OUTPUT);
  }

  public void printlnError(String message) {
    printError(message + "\n");
  }

  public void printError(String message) {
    print(message, ConsoleViewContentType.ERROR_OUTPUT);
  }

  public void print(String message, ConsoleViewContentType type) {
    console.print(message, type);
  }
}
