package com.game.core.factories;

import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.scene.blocks.Block;
import com.game.core.scene.blocks.BreakableBlock;
import com.game.core.scene.blocks.SolidBlock;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.config.SceneConfig;

public class BlockFactory {
    public static Block createFromConfig(SceneConfig.MappingBlockConfig blockConfig, Tile tile) {
        RectangleBounds hitbox = BoundsFactory.createForBlock(blockConfig.getHitbox());

        Block block;
        if (blockConfig.isBreakable()) block = new BreakableBlock(tile, hitbox);
        else block = new SolidBlock(tile, hitbox);

        return block;
    }
}
