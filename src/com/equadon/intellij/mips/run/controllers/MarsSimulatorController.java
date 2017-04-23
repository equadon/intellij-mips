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
import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.breakpoints.XBreakpoint;

import java.util.ArrayList;

import mars.ErrorList;
import mars.ErrorMessage;
import mars.Globals;
import mars.MIPSprogram;
import mars.ProcessingException;
import mars.mips.hardware.RegisterFile;

public class MarsSimulatorController extends MipsSimulatorController {
  private boolean paused;
  private boolean finished;

  public MarsSimulatorController(MipsRunConfiguration cfg, ConsoleView console, ProcessHandler processHandler) {
    super(cfg, console, processHandler);

    initialize();
  }

  public MarsSimulatorController(MipsRunConfiguration cfg, ConsoleView console, XDebugProcess process, XDebugSession session) {
    super(cfg, console, process, session);

    initialize();
  }

  private void initialize() {
    paused = false;
    finished = false;

    try {
      assemble();
    } catch (ProcessingException e) {
      printlnError("Failed to assemble file: " + cfg.getMainFile());
      e.printStackTrace();
    }
  }

  @Override
  public boolean isPaused() {
    return paused;
  }

  @Override
  public boolean isFinished() {
    return finished;
  }

  @Override
  public void resume() {
    paused = false;

    try {
      finished = Globals.program.simulateFromPC(getMarsBreakpoints(), cfg.getMaxSteps(), null);
    } catch (ProcessingException e) {
      printlnError("Failed to simulate file: " + cfg.getMainFile());
      e.printStackTrace();
    } finally {
      if (finished) {
        stop();
      }
    }
  }

  @Override
  public void stop() {
    if (isDebugger()) {
    } else {
      processHandler.destroyProcess();
    }
  }

  @Override
  public void pause() {
    paused = true;
  }

  @Override
  public void step() {
  }

  @Override
  public void addBreakpoint(XBreakpoint breakpoint) {
  }

  @Override
  public void removeBreakpoint(XBreakpoint breakpoint) {
  }

  private int[] getMarsBreakpoints() {
    return new int[0];
  }

  private void assemble() throws ProcessingException {
    RegisterFile.resetRegisters();
    RegisterFile.initializeProgramCounter(cfg.getStartMain());

    ArrayList<MIPSprogram> programs = new ArrayList<>();
    programs.add(Globals.program);

    Globals.program.readSource(cfg.getMainFile());
    Globals.program.tokenize();

    ErrorList warnings = Globals.program.assemble(programs, cfg.allowExtendedInstructions());

    if (warnings.warningsOccurred())
      printMarsErrorList(warnings);
  }

  public void printMarsErrorList(ErrorList errors) {
    for (Object o : errors.getErrorMessages()) {
      ErrorMessage msg = (ErrorMessage) o;
      printMarsErrorMessage(msg);
    }
  }

  public void printMarsErrorMessage(ErrorMessage error) {
    if (error == null) return;

    Project project = cfg.getProject();
    VirtualFile file = LocalFileSystem.getInstance().findFileByPath(error.getFilename());
    int line = error.getLine() - 1;
    int column = error.getPosition() - 1;

    if (file == null) return;

    printError("Error: ");
    printError("(");
    console.printHyperlink(file.getPresentableName() + ":" + error.getLine(), new OpenFileHyperlinkInfo(project, file, line, column));
    printlnError("): " + error.getMessage());
  }
}
