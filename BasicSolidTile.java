package ca.vanzeben.game.level.tiles;

import ca.vanzeben.game.gfx.SpriteSheet;

public class BasicSolidTile extends BasicTile {

    public BasicSolidTile(SpriteSheet sheet, int x, int y, int levelColour) {
        super(sheet, x, y, levelColour);
        this.solid = true;
    }

}
