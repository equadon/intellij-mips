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

import javax.swing.table.TableColumnModel;

import mars.mips.hardware.Coprocessor1;
import mars.mips.hardware.InvalidRegisterAccessException;

public class MipsCoproc1Table extends MipsRegisterTableBase {
  public MipsCoproc1Table() {
//    Coprocessor1.addRegistersObserver(this); // TODO: check why so many notifications are sent
  }

  @Override
  protected void updateHeader() {
    super.updateHeader();

    TableColumnModel model = getTableHeader().getColumnModel();
    model.getColumn(MipsRegisterTableColumns.Number.ordinal()).setHeaderValue("Float");
    model.getColumn(MipsRegisterTableColumns.Value.ordinal()).setHeaderValue("Double");
  }

  @Override
  protected Object getName(int row) {
    return Coprocessor1.getRegisters()[row].getName();
  }

  @Override
  protected Object getNumber(int row) {
    return getFormattedValue(Coprocessor1.getFloatFromRegister(row));
  }

  @Override
  protected Number getValue(int row) {
    try {
      return Coprocessor1.getDoubleFromRegisterPair(row);
    } catch (InvalidRegisterAccessException e) {
      return null;
    }
  }

  @Override
  public int getRowCount() {
    return Coprocessor1.getRegisters().length;
  }
}
