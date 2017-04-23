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

import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.ArrayList;

import mars.ErrorList;
import mars.ErrorMessage;
import mars.MIPSprogram;
import mars.ProcessingException;
import mars.mips.hardware.RegisterFile;

/**
 * Use MARS to assemble and simulate the application.
 */
public class MarsRunner extends Thread {
  public final ProcessHandler processHandler;
  private final ConsoleView console;
  private final MipsRunConfiguration cfg;

  private final MipsConsoleInputStream inputStream;

  public MarsRunner(ProcessHandler processHandler, ConsoleView console, MipsRunConfiguration cfg) {
    this.processHandler = processHandler;
    this.console = console;
    this.cfg = cfg;

    inputStream = new MipsConsoleInputStream(this, console, cfg.getProject());
  }

  private void initialize() {
    // Ugly hack to intercept System.out and System.in
    System.setOut(new ConsolePrintStream(inputStream));
    System.setIn(inputStream);
  }

  @Override
  public void run() {
    // Redirect System.out.print to our console
    initialize();

    MIPSprogram program = new MIPSprogram();

    try {
      assemble(program);

      RegisterFile.initializeProgramCounter(cfg.getStartMain());

      simulate(program);
    } catch (ProcessingException e) {
      printErrorList(e.errors());
    }

    interrupt();
  }

  private void assemble(MIPSprogram program) throws ProcessingException {
    ArrayList<MIPSprogram> programs = new ArrayList<>();
    programs.add(program);

    program.readSource(cfg.getMainFile());
    program.tokenize();

    ErrorList warnings = program.assemble(programs, cfg.allowExtendedInstructions());

    if (warnings.warningsOccurred())
      printErrorList(warnings);
  }

  private void simulate(MIPSprogram program) throws ProcessingException {
    printlnSystem("MARS :: simulation started...\n");
    program.simulate(cfg.getMaxSteps());
    printlnSystem("\n\nMARS :: simulation finished!\n");
  }

  @Override
  public void interrupt() {
    inputStream.resetEof();
    processHandler.destroyProcess();
  }

  private void println(String message) {
    print(message + "\n");
  }

  private void printlnSystem(String message) {
    printSystem(message + "\n");
  }

  private void print(String message) {
    print(message, ConsoleViewContentType.NORMAL_OUTPUT);
  }

  private void printSystem(String message) {
    print(message, ConsoleViewContentType.SYSTEM_OUTPUT);
  }

  private void printErrorList(ErrorList errors) {
    for (Object o : errors.getErrorMessages()) {
      ErrorMessage msg = (ErrorMessage) o;
      printlnError(msg);
    }
  }

  private void printlnError(ErrorMessage error) {
    if (error == null) return;

    Project project = cfg.getProject();
    VirtualFile file = LocalFileSystem.getInstance().findFileByPath(error.getFilename());
    int line = error.getLine() - 1;
    int column = error.getPosition() - 1;

    if (file == null) return;

    printError("Error: ");
    printError("(");
    console.printHyperlink(file.getPresentableName() + ":" + error.getLine(), new OpenFileHyperlinkInfo(project, file, line, column));
    printError("): " + error.getMessage() + "\n");
  }

  private void printError(String message) {
    print("MARS Error: ", ConsoleViewContentType.ERROR_OUTPUT);
  }

  private void print(String message, ConsoleViewContentType type) {
    console.print(message, type);
  }

  private class ConsolePrintStream extends PrintStream {
    @NotNull
    private final MipsConsoleInputStream inputStream;

    public ConsolePrintStream(@NotNull MipsConsoleInputStream inputStream) {
      super(System.out);
      this.inputStream = inputStream;
    }

    @Override
    public void print(String s) {
      inputStream.setMessage(s);
      MarsRunner.this.print(s);
    }
  }
}
