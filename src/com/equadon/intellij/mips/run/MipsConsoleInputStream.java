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

import com.equadon.intellij.mips.icons.MipsIcons;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public class MipsConsoleInputStream extends InputStream {
  private final MarsRunner runner;
  private final ConsoleView console;
  private final Project project;

  private String message = null;
  private String buffer = null;

  private boolean eof;

  public MipsConsoleInputStream(MarsRunner runner, ConsoleView console, Project project) {
    this.runner = runner;
    this.console = console;
    this.project = project;

    this.message = "";

    eof = false;
  }

  public void setMessage(String message) {
    this.message += message;
  }

  @Override
  public int read(@NotNull byte[] bytes, int offset, int length) throws IOException {
    if (eof) return -1;

    readInput();

    for (int i = offset; i < buffer.length(); i++)
      bytes[i] = (byte) buffer.charAt(i);

    eof = true;

    return buffer.length();
  }

  private void readInput() {
    ApplicationManager.getApplication().invokeAndWait(() -> {
      buffer = Messages.showInputDialog(project, message, "MIPS Input Dialog", MipsIcons.FILE);
    });
  }

  @Override
  public int read() throws IOException {
    return -1;
  }

  public void resetEof() {
    eof = false;
  }
}
