package com.frappagames.balloons.Tools;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Balloon object class
 *
 * Define a balloon, its speed, position and texture
 * 
 * Created by Miridan on 31/10/15.
 */
public class Balloon extends Rectangle {
    public TextureRegion texture;
    public int speed;
    public final static int BALLOON_SPEED = 200;
    public final static int BALLOON_WIDTH = 128;
    public final static int BALLOON_HEIGHT = 166;
    public float[] colors = new float[3];

    public Balloon(int x) {
        speed   = MathUtils.round(BALLOON_SPEED * MathUtils.random(70, 130) / 100);
        y       = -BALLOON_HEIGHT;
        width   = BALLOON_WIDTH;
        height  = BALLOON_HEIGHT;
        int rand = (int) (Math.random() * 14);
        this.colors = findColor(rand);
        texture = Assets.balloonsTextures.get(rand);
        this.setPosition(x, y);
    }

    public float[] getColor() {
        return this.colors;
    }

    private float[] findColor(int index)
    {
        float[] colors = new float[3];
        switch (index) {
            case 1: // Blue
                colors[0] = 0.102f;
                colors[1] = 0.482f;
                colors[2] = 0.953f;
                break;
            case 2: // Gold
                colors[0] = 0.984f;
                colors[1] = 0.847f;
                colors[2] = 0.204f;
                break;
            case 3: // Gray
                colors[0] = 0.671f;
                colors[1] = 0.671f;
                colors[2] = 0.671f;
                break;
            case 4: // Green
                colors[0] = 0.69f;
                colors[1] = 0.906f;
                colors[2] = 0.282f;
                break;
            case 5: // Green 2 (Dark)
                colors[0] = 0.302f;
                colors[1] = 0.533f;
                colors[2] = 0.204f;
                break;
            case 6: // Maroon
                colors[0] = 0.608f;
                colors[1] = 0.353f;
                colors[2] = 0.216f;
                break;
            case 7: // Orange
                colors[0] = 1.0f;
                colors[1] = 0.518f;
                colors[2] = 0.227f;
                break;
            case 8: // Pink
                colors[0] = 1.0f;
                colors[1] = 0.576f;
                colors[2] = 0.8f;
                break;
            case 9: // Pink 2 (Dark)
                colors[0] = 1.0f;
                colors[1] = 0.227f;
                colors[2] = 0.557f;
                break;
            case 10: // Purple
                colors[0] = 0.808f;
                colors[1] = 0.227f;
                colors[2] = 1.0f;
                break;
            case 11: // Red
                colors[0] = 1.0f;
                colors[1] = 0.157f;
                colors[2] = 0.129f;
                break;
            case 12: // Turkoise
                colors[0] = 0.227f;
                colors[1] = 0.906f;
                colors[2] = 1.0f;
                break;
            case 13: // White
                colors[0] = 0.969f;
                colors[1] = 0.969f;
                colors[2] = 0.969f;
                break;
            default : // Black
                colors[0] = 0.082f;
                colors[1] = 0.082f;
                colors[2] = 0.082f;
        }

        return colors;
    }
}
