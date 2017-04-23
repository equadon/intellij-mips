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
import com.equadon.intellij.mips.run.debugger.MipsSuspendContext;
import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.breakpoints.XBreakpoint;
import com.intellij.xdebugger.frame.XSuspendContext;

import org.apache.commons.lang.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import mars.ErrorList;
import mars.ErrorMessage;
import mars.Globals;
import mars.MIPSprogram;
import mars.ProcessingException;
import mars.ProgramStatement;
import mars.mips.hardware.RegisterFile;
import mars.simulator.Simulator;
import mars.simulator.SimulatorNotice;

public class MarsSimulatorController extends MipsSimulatorController implements Observer {
  private final static Logger LOG = Logger.getLogger("MIPS");

  private boolean paused;
  private boolean finished;

  private Map<Integer, Integer> lineToPC; // source line, program counter
  private Map<Integer, Integer> pcToLine; // program counter, source line
  private Map<Integer, XBreakpoint> lineToBreakpoint;

  public MarsSimulatorController(MipsRunConfiguration cfg, ConsoleView console, ProcessHandler processHandler) {
    super(cfg, console, processHandler);

    initialize();
  }

  public MarsSimulatorController(MipsRunConfiguration cfg, ConsoleView console, XDebugProcess process, XDebugSession session) {
    super(cfg, console, process, session);

    initialize();
  }

  private void initialize() {
    Simulator.getInstance().addObserver(this);

    lineToPC = new HashMap<>();
    pcToLine = new HashMap<>();
    lineToBreakpoint = new HashMap<>();

    paused = false;
    finished = false;

    try {
      assemble();

      if (isDebugger())
        updateLineToPC();
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
    LOG.info("controller.resume(debug=" + isDebugger() + ")");

    paused = false;

    try {
      finished = Globals.program.simulateFromPC(getMarsBreakpoints(), cfg.getMaxSteps(), null);
    } catch (ProcessingException e) {
      printlnError("Failed to simulate file: " + cfg.getMainFile());
      e.printStackTrace();
    } finally {
      if (finished) {
        stop();
      } else {
        breakpointReached();
      }
    }
  }

  private void breakpointReached() {
    LOG.info("controller.breakpointReached()");

    ArrayList statements = Globals.program.getParsedList();

    if (RegisterFile.getProgramCounter() > ((ProgramStatement) statements.get(statements.size() - 1)).getAddress()) {
      // PC is after last instruction, stop execution
      stop();
    }

    XBreakpoint breakpoint = findBreakpointByPC(RegisterFile.getProgramCounter());

    if (breakpoint != null) {
      XSuspendContext suspendContext = new MipsSuspendContext(debugSession.getProject(), this, breakpoint, RegisterFile.getProgramCounter());

      debugSession.breakpointReached(breakpoint, "Breakpoint PC=" + RegisterFile.getProgramCounter(), suspendContext);
    }
  }

  private void updateLineToPC() {
    ArrayList statements = Globals.program.getParsedList();

    for (int i = 0; i < Globals.program.getParsedList().size(); i++) {
      ProgramStatement s = (ProgramStatement) statements.get(i);
      lineToPC.put(s.getSourceLine() - 1, s.getAddress());
      pcToLine.put(s.getAddress(), s.getSourceLine() - 1);
    }
  }

  private XBreakpoint findBreakpointByPC(int programCounter) {
    int line = pcToLine.get(programCounter);

    return lineToBreakpoint.get(line);
  }

  @Override
  public void stop() {
    super.stop();

    LOG.info("controller.stop()");
  }

  @Override
  public void pause() {
    LOG.info("controller.pause()");

    paused = true;
  }

  @Override
  public void step() {
    LOG.info("controller.step()");

    stop();

    throw new NotImplementedException("Stepping is not implemented yet!");
  }

  @Override
  public void addBreakpoint(XBreakpoint breakpoint) {
    super.addBreakpoint(breakpoint);
    lineToBreakpoint.put(breakpoint.getSourcePosition().getLine(), breakpoint);
  }

  @Override
  public void removeBreakpoint(XBreakpoint breakpoint) {
    super.removeBreakpoint(breakpoint);
  }

  private int[] getMarsBreakpoints() {
    ArrayList statements = Globals.program.getParsedList();
    int[] breaks = new int[breakpoints.size()];

    int breakIndex = 0;
    for (XBreakpoint b : breakpoints) {
      int line = b.getSourcePosition().getLine();
      for (int i = 0; i < statements.size(); i++) {
        ProgramStatement s = (ProgramStatement) statements.get(i);
        if (line == s.getSourceLine() - 1) {
          breaks[breakIndex++] = s.getAddress();
        }
      }
    }

    return breaks;
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

  @Override
  public void update(Observable observable, Object o) {
    LOG.info("obs=" + observable + ", o=" + o);

    if (o instanceof SimulatorNotice && ((SimulatorNotice) o).getAction() == SimulatorNotice.SIMULATOR_STOP) {
    }
  }
}
