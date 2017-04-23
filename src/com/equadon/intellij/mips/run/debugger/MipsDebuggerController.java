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
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.breakpoints.XBreakpoint;
import com.intellij.xdebugger.frame.XSuspendContext;

import org.apache.commons.lang.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import mars.ErrorList;
import mars.MIPSprogram;
import mars.ProcessingException;
import mars.ProgramStatement;
import mars.mips.hardware.RegisterFile;
import mars.simulator.Simulator;
import mars.simulator.SimulatorNotice;

public class MipsDebuggerController implements Observer {
  private final MipsDebugProcess debugProcess;
  private final XDebugSession session;
  private final MIPSprogram program;

  private final List<XBreakpoint> breakpoints;

  private final Map<Integer, Integer> lineToPC; // source line, program counter
  private final Map<Integer, Integer> pcToLine; // program counter, source line
  private final Map<Integer, XBreakpoint> lineToBreakpoint;

  private boolean started;

  public MipsDebuggerController(MipsDebugProcess debugProcess, XDebugSession session) {
    this.debugProcess = debugProcess;
    this.session = session;
    this.breakpoints = new ArrayList<>();

    lineToPC = new HashMap<>();
    pcToLine = new HashMap<>();
    lineToBreakpoint = new HashMap<>();

    started = false;

    program = new MIPSprogram();
    Simulator.getInstance().addObserver(this);

    try {
      assemble(program);
      updateLineToPC();
    } catch (ProcessingException e) {
      e.printStackTrace();
    }
  }

  private void updateLineToPC() {
    ArrayList statements = program.getParsedList();

    for (int i = 0; i < program.getParsedList().size(); i++) {
      ProgramStatement s = (ProgramStatement) statements.get(i);
      lineToPC.put(s.getSourceLine() - 1, s.getAddress());
      pcToLine.put(s.getAddress(), s.getSourceLine() - 1);
    }
  }

  private int[] getBreakPoints() {
    ArrayList statements = program.getParsedList();
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

  private void assemble(MIPSprogram program) throws ProcessingException {
    ArrayList<MIPSprogram> programs = new ArrayList<>();
    programs.add(program);

    MipsRunConfiguration cfg = debugProcess.getRunConfig();

    program.readSource(cfg.getMainFile());
    program.tokenize();

    ErrorList warnings = program.assemble(programs, cfg.allowExtendedInstructions());
  }

  public boolean hasStarted() {
    return started;
  }

  public void resume() {
    System.out.println("DebugController.resume()");

    try {
      boolean finished;

      if (hasStarted())
        finished = program.simulateFromPC(getBreakPoints(), -1, null);
      else
        finished = program.simulate(getBreakPoints());

      if (finished) {
        stop();
      } else {
        breakpointReached();
      }
    } catch (ProcessingException e) {
      e.printStackTrace();
    }
  }

  private void breakpointReached() {
    ArrayList statements = program.getParsedList();

    if (RegisterFile.getProgramCounter() > ((ProgramStatement) statements.get(statements.size() - 1)).getAddress()) {
      // PC is after last instruction, stop execution
      stop();
    }

    started = true;

    System.out.println("DebugController.breakpointReached(): PC=" + RegisterFile.getProgramCounter());

    XBreakpoint breakpoint = findBreakpointByPC(RegisterFile.getProgramCounter());

    if (breakpoint != null) {
      XSuspendContext suspendContext = new MipsSuspendContext(session.getProject(), this, breakpoint, RegisterFile.getProgramCounter());

      session.breakpointReached(breakpoint, "Breakpoint PC=" + RegisterFile.getProgramCounter(), suspendContext);
    }
  }

  private XBreakpoint findBreakpointByPC(int programCounter) {
    int line = pcToLine.get(programCounter);

    return lineToBreakpoint.get(line);
  }

  public void stop() {
    System.out.println("DebugController.stop()");
    debugProcess.getProcessHandler().destroyProcess();
  }

  public void addBreakpoint(XBreakpoint breakpoint) {
    System.out.println("DebugController.addBreakpoint()");
    breakpoints.add(breakpoint);
    lineToBreakpoint.put(breakpoint.getSourcePosition().getLine(), breakpoint);
  }

  @Override
  public void update(Observable observable, Object o) {
    System.out.println("obs=" + observable + ", o=" + o);
    if (o instanceof SimulatorNotice && ((SimulatorNotice) o).getAction() == SimulatorNotice.SIMULATOR_STOP) {
    }
  }

  public void step() {
    stop();

    throw new NotImplementedException("Stepping is not implemented yet!");
  }
}
