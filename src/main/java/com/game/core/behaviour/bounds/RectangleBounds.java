package com.game.core.behaviour.bounds;


public class RectangleBounds extends Bounds {
    private float height;
    private float width;

    public RectangleBounds(float width, float height) {
        this.setHeight(height);
        this.setWidth(width);
    }

    @Override
    public boolean intersects(Bounds other) {
        float x = this.getX();
        float y = this.getY();

        if (other instanceof RectangleBounds rect) {
            return Math.abs(x - rect.getX()) < (this.getWidth() + rect.getWidth()) / 2 &&
                    Math.abs(y - rect.getY()) < (this.getHeight() + rect.getHeight()) / 2;
        } else if (other instanceof CircleBounds circle) {
            float closestX = Math.max(x - this.getWidth() / 2, Math.min(circle.getX(), x + this.getWidth() / 2));
            float closestY = Math.max(y - this.getHeight() / 2, Math.min(circle.getY(), y + this.getHeight() / 2));
            return Math.sqrt(Math.pow(circle.getX() - closestX, 2) + Math.pow(circle.getY() - closestY, 2)) <= circle.getRadius();
        }

        return false;
    }

    @Override
    public boolean contains(float x, float y) {
        return false;
    }

    public float getHeight() {
        return this.height;
    }

    private void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return this.width;
    }

    private void setWidth(float width) {
        this.width = width;
    }
}
