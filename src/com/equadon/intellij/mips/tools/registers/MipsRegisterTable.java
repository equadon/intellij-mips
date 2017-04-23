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

import mars.mips.hardware.RegisterFile;

public class MipsRegisterTable extends MipsRegisterTableBase {
  private static int PC_REGISTER = 32;
  private static int HI_REGISTER = 33;

  public MipsRegisterTable() {
//    RegisterFile.addRegistersObserver(this); // TODO: check why so many notifications are sent
  }

  private static int LO_REGISTER = 34;

  @Override
  protected Object getName(int row) {
    if (row == PC_REGISTER)
      return RegisterFile.getProgramCounterRegister().getName();
    else if (row == HI_REGISTER)
      return "hi";
    else if (row == LO_REGISTER)
      return "lo";

    return RegisterFile.getRegisters()[row].getName();
  }

  @Override
  protected Object getNumber(int row) {
    if (row > 31)
      return "";

    return RegisterFile.getRegisters()[row].getNumber();
  }

  @Override
  protected Number getValue(int row) {
    if (row == PC_REGISTER)
      return RegisterFile.getProgramCounterRegister().getValue();

    return RegisterFile.getValue(row);
  }

  @Override
  public int getRowCount() {
    return RegisterFile.getRegisters().length + 3;
  }
}
