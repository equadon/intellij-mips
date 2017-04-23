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

package com.equadon.intellij.mips.formatter;

import com.equadon.intellij.mips.lang.psi.MipsTokenTypes;
import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MipsFormattingBlock extends AbstractBlock {
  private final SpacingBuilder spacingBuilder;
  private final CommonCodeStyleSettings commonSettings;
  private final MipsCodeStyleSettings mipsSettings;

  public MipsFormattingBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment, SpacingBuilder spacingBuilder, CommonCodeStyleSettings commonSettings, MipsCodeStyleSettings mipsSettings) {
    super(node, wrap, alignment);

    this.spacingBuilder = spacingBuilder;
    this.commonSettings = commonSettings;
    this.mipsSettings = mipsSettings;
  }

  @Override
  protected List<Block> buildChildren() {
    final List<Block> blocks = new ArrayList<>();

    for (ASTNode child = myNode.getFirstChildNode(); child != null; child = child.getTreeNext()) {
      if (!shouldCreateBlockFor(child)) continue;
      blocks.add(createChildBlock(myNode, child));
    }
    return Collections.unmodifiableList(blocks);
  }

  private boolean shouldCreateBlockFor(ASTNode node) {
    IElementType type = node.getElementType();

    return getTextRange().getLength() != 0 &&  !MipsTokenTypes.WHITE_SPACES.contains(type);
  }

  private Block createChildBlock(ASTNode parent, ASTNode child) {
    return new MipsFormattingBlock(child, null, Alignment.createAlignment(), spacingBuilder, commonSettings, mipsSettings);
  }

  @Override
  public Indent getIndent() {
    return Indent.getNoneIndent();
  }

  @Nullable
  @Override
  public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    return spacingBuilder.getSpacing(this, child1, child2);
  }

  @Override
  public boolean isLeaf() {
    return myNode.getFirstChildNode() == null;
  }
}
