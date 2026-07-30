package org.neoedit.neoedit;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class Edit {
    public Edit(List<BlockEdit> inEdits) {
        edits = inEdits;
    }

    public void apply() {
        ArrayList<BlockPos> blockPositions = new ArrayList<>(edits.size());
        ArrayList<BlockState> blockStates = new ArrayList<>(edits.size());
        for (BlockEdit edit : edits) {
            blockPositions.add(edit.position);
            blockStates.add(edit.newState);
        }

        EditPayload editPayload = new EditPayload(blockPositions, blockStates);
        ClientPlayNetworking.send(editPayload);
    }

    public void undo() {

    }

    private List<BlockEdit> edits;
}
