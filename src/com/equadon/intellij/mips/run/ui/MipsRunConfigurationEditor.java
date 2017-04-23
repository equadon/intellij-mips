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

package com.equadon.intellij.mips.run.ui;

import com.equadon.intellij.mips.lang.MipsFileType;
import com.equadon.intellij.mips.run.MipsRunConfiguration;
import com.equadon.intellij.mips.run.MipsRunConfigurationParams;
import com.intellij.ide.util.BrowseFilesListener;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MipsRunConfigurationEditor extends SettingsEditor<MipsRunConfiguration> implements MipsRunConfigurationParams {
  private final MipsRunConfiguration cfg;

  private JPanel rootPanel;
  private JCheckBox useMainCheck;
  private JSpinner maxStepsSpinner;
  private TextFieldWithBrowseButton mainFileField;
  private TextFieldWithBrowseButton workingDirField;
  private JCheckBox extendedInstructionsCheck;

  public MipsRunConfigurationEditor(MipsRunConfiguration cfg) {
    this.cfg = cfg;

    mainFileField.addBrowseFolderListener("MIPS Main File", "Choose the MIPS file containing the main function.", cfg.getProject(), getChooseMainFileDescriptor());
    workingDirField.addBrowseFolderListener("Working Directory", "Choose working directory.", cfg.getProject(), BrowseFilesListener.SINGLE_DIRECTORY_DESCRIPTOR);
  }

  private FileChooserDescriptor getChooseMainFileDescriptor() {
    FileChooserDescriptor chooseMainFile = new FileChooserDescriptor(
        true,
        false,
        false,
        false,
        false,
        false
    );
    chooseMainFile.withTreeRootVisible(true);
    chooseMainFile.withFileFilter(virtualFile -> virtualFile.getFileType() instanceof MipsFileType);
    chooseMainFile.setRoots(cfg.getProject().getBaseDir());

    return chooseMainFile;
  }

  @Override
  protected void resetEditorFrom(@NotNull MipsRunConfiguration runConfiguration) {
    MipsRunConfiguration.copyParams(runConfiguration, this);
  }

  @Override
  protected void applyEditorTo(@NotNull MipsRunConfiguration runConfiguration) throws ConfigurationException {
    MipsRunConfiguration.copyParams(this, runConfiguration);
  }

  @NotNull
  @Override
  protected JComponent createEditor() {
    return rootPanel;
  }

  @Override
  public String getMainFile() {
    return mainFileField.getText();
  }

  @Override
  public void setMainFile(String filename) {
    mainFileField.setText(filename);
  }

  @Override
  public String getWorkingDirectory() {
    return workingDirField.getText();
  }

  @Override
  public void setWorkingDirectory(String dir) {
    workingDirField.setText(dir);
  }

  @Override
  public Integer getMaxSteps() {
    return (Integer) maxStepsSpinner.getValue();
  }

  @Override
  public void setMaxSteps(Integer steps) {
    maxStepsSpinner.setValue(steps);
  }

  @Override
  public Boolean getStartMain() {
    return useMainCheck.isSelected();
  }

  @Override
  public void setStartMain(Boolean checked) {
    useMainCheck.setSelected(checked);
  }

  @Override
  public Boolean allowExtendedInstructions() {
    return extendedInstructionsCheck.isSelected();
  }

  @Override
  public void setAllowExtendedInstructions(Boolean checked) {
    extendedInstructionsCheck.setSelected(checked);
  }

  private void createUIComponents() {
    SpinnerNumberModel maxStepsModel = new SpinnerNumberModel();
    maxStepsModel.setMinimum(-1);
    maxStepsModel.setValue(-1);

    maxStepsSpinner = new JSpinner(maxStepsModel);
  }
}
