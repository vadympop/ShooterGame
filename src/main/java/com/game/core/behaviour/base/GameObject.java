package com.game.core.behaviour.base;

import com.game.core.behaviour.interfaces.Positionable;

/**
 * Represents the base class for all game objects in the system.
 * The {@code GameObject} class extends {@code PositionWrapper} and implements
 * the {@link com.game.core.behaviour.interfaces.Positionable} interface,
 * providing positional behavior to all derived objects.
 *
 * <p>This abstract class is intended to be extended by specific game objects
 * to inherit position-handling functionality and provide game-related behaviors.</p>
 *
 * @see com.game.core.behaviour.base.PositionWrapper
 * @see com.game.core.behaviour.interfaces.Positionable
 */
public abstract class GameObject extends PositionWrapper implements Positionable {}
