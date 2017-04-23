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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.table.JBTable;

import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

import javax.swing.*;

import mars.Globals;
import mars.Settings;

public class MipsRegistersToolWindowFactory implements ToolWindowFactory {
  private ToolWindow toolWindow;

  private JPanel registersPanel;
  private JPanel coproc0Panel;
  private JPanel coproc1Panel;
  private JBTable registersTable;
  private JBTable coproc1Table;
  private JBTable coproc0Table;
  private JCheckBox showValuesInHexCheckBox;
  private JPanel settingsPanel;

  public MipsRegistersToolWindowFactory() {
    showValuesInHexCheckBox.setSelected(Globals.getSettings().getBooleanSetting(Settings.DISPLAY_VALUES_IN_HEX));

    showValuesInHexCheckBox.addActionListener(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Globals.getSettings().setBooleanSetting(Settings.DISPLAY_VALUES_IN_HEX, showValuesInHexCheckBox.isSelected());
      }
    });
  }

  @Override
  public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
    this.toolWindow = toolWindow;

    ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

    Content registers = contentFactory.createContent(registersPanel, "Registers", true);
    Content coproc1 = contentFactory.createContent(coproc1Panel, "Coproc 1", true);
    Content coproc0 = contentFactory.createContent(coproc0Panel, "Coproc 0", true);
    Content settings = contentFactory.createContent(settingsPanel, "Settings", true);

    toolWindow.getContentManager().addContent(registers);
    toolWindow.getContentManager().addContent(coproc1);
    toolWindow.getContentManager().addContent(coproc0);
    toolWindow.getContentManager().addContent(settings);
  }

  private void createUIComponents() {
    registersTable = new MipsRegisterTable();
    coproc1Table = new MipsCoproc1Table();
    coproc0Table = new MipsCoproc0Table();
  }
}
