package com.game.core.factories;

import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.scene.blocks.Block;
import com.game.core.scene.blocks.BreakableBlock;
import com.game.core.scene.blocks.SolidBlock;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.config.SceneConfig;

/**
 * A factory class responsible for creating {@link Block} instances.
 * The blocks can be breakable or solid, and are configured based on
 * the provided scene configuration and corresponding tile graphics.
 */
public class BlockFactory {

    /**
     * Creates a {@link Block} instance based on the provided configuration
     * and tile. This method initializes the block's hitbox and determines
     * whether the block is breakable or solid.
     *
     * @param blockConfig the configuration data for the block, including its hitbox and properties
     * @param tile        the {@link Tile} that represents the graphical design of the block
     * @return a new instance of {@link BreakableBlock} or {@link SolidBlock}, depending on the configuration
     */
    public static Block createFromConfig(SceneConfig.MappingBlockConfig blockConfig, Tile tile) {
        RectangleBounds hitbox = BoundsFactory.createForBlock(blockConfig.getHitbox());

        Block block;
        if (blockConfig.isBreakable()) block = new BreakableBlock(tile, hitbox);
        else block = new SolidBlock(tile, hitbox);

        return block;
    }
}
