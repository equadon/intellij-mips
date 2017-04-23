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

import com.equadon.intellij.mips.run.debugger.MipsDebugConsoleState;
import com.equadon.intellij.mips.run.ui.MipsRunConfigurationEditor;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.LocatableConfigurationBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizerUtil;
import com.intellij.openapi.util.WriteExternalException;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MipsRunConfiguration extends LocatableConfigurationBase implements MipsRunConfigurationParams {
  private String mainFile;
  private Integer maxSteps;
  private String workingDirectory;
  private Boolean startMain;
  private Boolean extendedInstructions;

  protected MipsRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory) {
    super(project, factory, "com.equadon.intellij.mips.runconfig.MipsRunConfiguration");
  }

  @Nullable
  @Override
  public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws ExecutionException {
    boolean isDebugger = executor.getId().equals(DefaultDebugExecutor.EXECUTOR_ID);

    if (isDebugger) {
      return new MipsDebugConsoleState(this, getProject());
    } else {
      return new MipsConsoleState(this, getProject());
    }
  }

  @NotNull
  @Override
  public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    return new MipsRunConfigurationEditor(this);
  }

  @Override
  public void readExternal(Element element) throws InvalidDataException {
    super.readExternal(element);

    mainFile = JDOMExternalizerUtil.readField(element, "MIPS_MAIN_FILE");

    String steps = JDOMExternalizerUtil.readField(element, "MIPS_MAX_STEPS");
    maxSteps = (steps == null || steps.isEmpty()) ? -1 : Integer.parseInt(steps);

    String useMain = JDOMExternalizerUtil.readField(element, "MIPS_START_MAIN");
    startMain = useMain != null && Boolean.parseBoolean(useMain);

    String extended = JDOMExternalizerUtil.readField(element, "MIPS_EXTENDED_INSTRUCTIONS");
    extendedInstructions = extended != null && Boolean.parseBoolean(extended);
  }

  @Override
  public void writeExternal(Element element) throws WriteExternalException {
    super.writeExternal(element);

    String steps = maxSteps == null ? "-1" : maxSteps.toString();

    JDOMExternalizerUtil.writeField(element, "MIPS_MAIN_FILE", mainFile);
    JDOMExternalizerUtil.writeField(element, "MIPS_MAX_STEPS", steps);
    JDOMExternalizerUtil.writeField(element, "MIPS_START_MAIN", String.valueOf(startMain));
    JDOMExternalizerUtil.writeField(element, "MIPS_EXTENDED_INSTRUCTIONS", String.valueOf(extendedInstructions));
  }

  @Override
  public void checkConfiguration() throws RuntimeConfigurationException {
  }

  public static void copyParams(MipsRunConfigurationParams from, MipsRunConfigurationParams to) {
    to.setMainFile(from.getMainFile());
    to.setMaxSteps(from.getMaxSteps());
    to.setWorkingDirectory(from.getWorkingDirectory());
    to.setStartMain(from.getStartMain());
    to.setAllowExtendedInstructions(from.allowExtendedInstructions());
  }

  @Override
  public String getMainFile() {
    return mainFile;
  }

  @Override
  public void setMainFile(String filename) {
    mainFile = filename;
  }

  @Override
  public String getWorkingDirectory() {
    return workingDirectory;
  }

  @Override
  public void setWorkingDirectory(String dir) {
    workingDirectory = dir;
  }

  @Override
  public Integer getMaxSteps() {
    return maxSteps;
  }

  @Override
  public void setMaxSteps(Integer steps) {
    maxSteps = steps;
  }

  @Override
  public Boolean getStartMain() {
    return startMain;
  }

  @Override
  public void setStartMain(Boolean checked) {
    startMain = checked;
  }

  @Override
  public Boolean allowExtendedInstructions() {
    return extendedInstructions;
  }

  @Override
  public void setAllowExtendedInstructions(Boolean checked) {
    extendedInstructions = checked;
  }
}
