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

import java.io.PrintStream;

public class MipsConsoleOutputStream extends PrintStream {
  private final MipsSimulatorController controller;
  private final MipsConsoleInputStream inputStream;

  public MipsConsoleOutputStream(MipsSimulatorController controller, MipsConsoleInputStream inputStream) {
    super(System.out);
    this.controller = controller;
    this.inputStream = inputStream;
  }

  @Override
  public void print(String message) {
    inputStream.setMessage(message);
    controller.print(message);
  }
}
