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

package com.equadon.intellij.mips.completion;

import com.equadon.intellij.mips.icons.MipsIcons;
import com.equadon.intellij.mips.lang.psi.MipsFile;
import com.equadon.intellij.mips.lang.psi.MipsLabelDefinition;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.util.ProcessingContext;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mars.Globals;
import mars.assembler.Directives;
import mars.mips.instructions.Instruction;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class MipsCompletionContributor extends CompletionContributor {
  private static final Pattern INSTRUCTION_ID = Pattern.compile("^([^\\d]+)\\d{3} $");

  public MipsCompletionContributor() {
    // Completion for directives and instructions
    extend(CompletionType.BASIC, psiElement().withParent(MipsFile.class), new CompletionProvider<CompletionParameters>() {
      @Override
      protected void addCompletions(@NotNull CompletionParameters params, ProcessingContext context, @NotNull CompletionResultSet result) {
        suggestDirectives(result);
        suggestInstructions(result);
      }
    });

    // Completion for labels
    extend(CompletionType.BASIC, psiElement(), new CompletionProvider<CompletionParameters>() {
      @Override
      protected void addCompletions(@NotNull CompletionParameters params, ProcessingContext context, @NotNull CompletionResultSet result) {
        suggestLabels(params, result);
      }
    });
  }

  /**
   * Add all label definitions in current file.
   * TODO: Optimize using index.
   * TODO: Add tail text as documentation if there is a comment just above/below the label definition.
   * @param params Completion parameters to get the PsiFile
   * @param result Result set completions will be added to
   */
  private void suggestLabels(CompletionParameters params, CompletionResultSet result) {
    if (params.getOriginalFile() instanceof MipsFile) {
      MipsFile file = (MipsFile) params.getOriginalFile();

      for (MipsLabelDefinition label : file.getLabelDefinitions()) {
        if (label.getName() != null) {
          result.addElement(LookupElementBuilder.create(label.getName())
              .withTypeText("Label")
              .withBoldness(true)
              .withIcon(MipsIcons.LABEL)
          );
        }
      }
    }
  }

  /**
   * Add all directives to completions.
   * Note: A space will not be added at the end since some directives won't need arguments.
   * @param result Result set with completions
   */
  private void suggestDirectives(CompletionResultSet result) {
    ArrayList directives = Directives.getDirectiveList();
    for (Object o : directives) {
      Directives directive = (Directives) o;
      result.addElement(LookupElementBuilder.create(directive.getName())
          .withTailText(" " + directive.getDescription(), true)
          .withTypeText("Directive", false)
          .withInsertHandler(new MipsInsertHandler())
          .withBoldness(true)
          .withIcon(MipsIcons.DIRECTIVE)
      );
    }
  }

  /**
   * Add all instructions to completions. TODO: Respect setting wether to use extended instruction
   * set or not.
   * @param result Result set with completions
   */
  private void suggestInstructions(CompletionResultSet result) {
    int n = 1;
    for (Object i : Globals.instructionSet.getInstructionList()) {
      Instruction instruction = (Instruction) i;
      String name = String.format("%s%03d ", instruction.getName(), n++);

      result.addElement(LookupElementBuilder.create(name)
          .withTailText(" " + instruction.getDescription(), true)
          .withTypeText("Instruction")
          .withInsertHandler(new MipsInsertHandler())
          .withBoldness(true)
          .withPresentableText(instruction.getExampleFormat())
          .withIcon(MipsIcons.INSTRUCTION)
      );
    }
  }

  private class MipsInsertHandler implements InsertHandler<LookupElement> {
    @Override
    public void handleInsert(InsertionContext context, LookupElement element) {
      Editor editor = context.getEditor();
      Document document = editor.getDocument();

      int start = context.getStartOffset();
      int tail = context.getTailOffset();

      String text = document.getText().substring(start, context.getTailOffset());

      if (text.startsWith(".")) {
        document.replaceString(start, tail, text.substring(1));
      } else {
        Matcher m = INSTRUCTION_ID.matcher(text);

        if (m.matches()) {
          document.replaceString(tail - 4, tail - 1, "");
        }
      }
    }
  }
}
