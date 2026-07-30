package org.neoedit.neoedit;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEdit
{
    public BlockEdit(BlockPos inPosition, BlockState inState)
    {
        position = inPosition;
        newState = inState;
        oldState = Minecraft.getInstance().level.getBlockState(position);
    }

    public BlockPos position;
    public BlockState oldState;
    public BlockState newState;
}
