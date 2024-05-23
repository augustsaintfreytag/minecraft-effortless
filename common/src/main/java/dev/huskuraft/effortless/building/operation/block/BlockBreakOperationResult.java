package dev.huskuraft.effortless.building.operation.block;

import java.util.Collections;
import java.util.List;

import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.OperationSummaryType;
import dev.huskuraft.effortless.building.operation.empty.EmptyOperation;

public class BlockBreakOperationResult extends BlockOperationResult {

    public BlockBreakOperationResult(
            BlockBreakOperation operation,
            Type result,
            List<ItemStack> inputs,
            List<ItemStack> outputs
    ) {
        super(operation, result, inputs, outputs);
    }

    @Override
    public Operation getReverseOperation() {
        if (result().fail()) {
            return new EmptyOperation();
        }

        return new BlockPlaceOperation(
                operation.getWorld(),
                operation.getPlayer(),
                operation.getContext(),
                operation.getStorage(),
                operation.getInteraction(),
                operation.getBlockState(),
                operation.getEntityState()
        );
    }

    @Override
    public List<ItemStack> getSummary(OperationSummaryType type) {
        return switch (type) {
            case BLOCKS_DESTROYED -> switch (result) {
                case SUCCESS, SUCCESS_PARTIAL, CONSUME -> outputs;
                default -> List.of();
            };
            case BLOCKS_NOT_BREAKABLE -> switch (result) {
                case FAIL_PLAYER_CANNOT_INTERACT, FAIL_PLAYER_CANNOT_BREAK, FAIL_WORLD_BORDER, FAIL_WORLD_HEIGHT -> outputs;
                default -> List.of();
            };
            case BLOCKS_TOOLS_INSUFFICIENT -> switch (result) {
                case FAIL_ITEM_INSUFFICIENT, FAIL_TOOL_INSUFFICIENT -> outputs;
                default -> Collections.emptyList();
            };
            case BLOCKS_BLACKLISTED -> switch (result) {
                case FAIL_CONFIG_BLACKLISTED -> outputs;
                default -> List.of();
            };
            case BLOCKS_NO_PERMISSION -> switch (result) {
                case FAIL_CONFIG_BREAK_PERMISSION -> outputs;
                default -> List.of();
            };
            default -> List.of();
        };
    }

}
