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

import com.intellij.ui.table.JBTable;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import mars.Globals;
import mars.Settings;
import mars.mips.hardware.Register;
import mars.venus.NumberDisplayBaseChooser;

public abstract class MipsRegisterTableBase extends JBTable implements Observer {
  private boolean hexValues;

  public MipsRegisterTableBase() {
    hexValues = Globals.getSettings().getBooleanSetting(Settings.DISPLAY_VALUES_IN_HEX);

    Globals.getSettings().addObserver(this);

    setModel(new AbstractTableModel() {
      @Override
      public int getRowCount() {
        return MipsRegisterTableBase.this.getRowCount();
      }

      @Override
      public int getColumnCount() {
        return 3;
      }

      @Override
      public Object getValueAt(int row, int col) {
        MipsRegisterTableColumns c = MipsRegisterTableColumns.values()[col];

        switch (c) {
          case Name:
            return getName(row);
          case Number:
            return getNumber(row);
          case Value:
            return getFormattedValue(getValue(row));
          default:
            return "";
        }
      }

      @Override
      public boolean isCellEditable(int row, int col) {
        return col == MipsRegisterTableColumns.Value.ordinal();
      }

      @Override
      public Class<?> getColumnClass(int col) {
        return String.class;
      }
    });

    updateHeader();
  }

  protected abstract Object getName(int row);
  protected abstract Object getNumber(int row);
  protected abstract Number getValue(int row);

  protected Object getFormattedValue(Number number) {
    if (number == null) return null;

    int base = hexValues ? NumberDisplayBaseChooser.HEXADECIMAL : NumberDisplayBaseChooser.DECIMAL;

    if (number instanceof Float)
      return NumberDisplayBaseChooser.formatNumber(number.floatValue(), base);

    if (number instanceof Double)
      return NumberDisplayBaseChooser.formatNumber(number.doubleValue(), base);

    return NumberDisplayBaseChooser.formatNumber(number.intValue(), base);
  }

  @Override
  public abstract int getRowCount();

  @Override
  public void update(Observable observable, Object o) {
    System.out.println("DEBUG :: notification :: obs=" + observable + ", o=" + o);
    if (observable instanceof Settings) {
      Settings settings = (Settings) observable;
      hexValues = settings.getBooleanSetting(Settings.DISPLAY_VALUES_IN_HEX);

      updateRows();
    } else if (observable instanceof Register) {
      Register reg = (Register) observable;

      updateRow(reg.getNumber());
    }
  }

  public void updateRows() {
    ((AbstractTableModel) getModel()).fireTableDataChanged();
  }

  public void updateRow(int row) {
    ((AbstractTableModel) getModel()).fireTableRowsUpdated(row, row);
  }

  protected void updateHeader() {
    TableColumnModel header = getTableHeader().getColumnModel();
    for (MipsRegisterTableColumns col : MipsRegisterTableColumns.values()) {
      header.getColumn(col.ordinal()).setHeaderValue(col.toString());
    }
  }
}
