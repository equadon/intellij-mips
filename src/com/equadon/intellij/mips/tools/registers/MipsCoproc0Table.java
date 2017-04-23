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

package com.equadon.intellij.mips.tools.registers;

import mars.mips.hardware.Coprocessor0;

public class MipsCoproc0Table extends MipsRegisterTableBase {
  public MipsCoproc0Table() {
//    Coprocessor0.addRegistersObserver(this); // TODO: check why so many notifications are sent
  }

  @Override
  protected Object getName(int row) {
    return Coprocessor0.getRegisters()[row].getName();
  }

  @Override
  protected Object getNumber(int row) {
    return Coprocessor0.getRegisters()[row].getNumber();
  }

  @Override
  protected Number getValue(int row) {
    return Coprocessor0.getRegisters()[row].getValue();
  }

  @Override
  public int getRowCount() {
    return Coprocessor0.getRegisters().length;
  }
}
