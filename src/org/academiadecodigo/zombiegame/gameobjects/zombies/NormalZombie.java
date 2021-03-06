package org.academiadecodigo.zombiegame.gameobjects.zombies;

import org.academiadecodigo.simplegraphics.graphics.Rectangle;
import org.academiadecodigo.zombiegame.Sound;
import org.academiadecodigo.zombiegame.field.Background;
import org.academiadecodigo.zombiegame.field.Direction;
import org.academiadecodigo.zombiegame.field.Position;
import org.academiadecodigo.zombiegame.field.Zones;

public class NormalZombie extends Zombie{
    private String picturePath = "assets/zombie right.png";
    private Sound zombieKilledSound = new Sound("assets/zombieKilledSound.wav");

    private int hitPoints = 2;

    private int health = 2;

    public NormalZombie(Position pos, Position playerPos, Zones spawnZone) {
        super(pos, playerPos, spawnZone);

        newPicture(pos, picturePath);
    }

    @Override
    public void newPicture(Position pos, String picturePath) {

        super.newPicture(pos, picturePath);

        //used to readapt colliding position size [magic numbers]

        firstCol = pos.getCol() + 6;
        lastCol = pos.getCol() + posSizeX - 5;
        firstRow = pos.getRow() + 10;
        lastRow = pos.getRow() + posSizeY - 2;

        posSizeX = lastCol - firstCol;
        posSizeY = lastRow - firstRow;

        /*
        //test size
        int x = firstCol * Background.getCellSize() + Background.getPadding();
        int y = firstRow * Background.getCellSize() + Background.getPadding();
        int width = lastCol * Background.getCellSize() - x;
        int height =  lastRow * Background.getCellSize() - y;

        Rectangle testRectangle = new Rectangle(x, y, width, height);
        testRectangle.draw();
        //

         */

        if (lastCol > Background.getCols()) {
            pos.setCol(Background.getCols() - picture.getWidth() - 15);

            newPicture(pos, picturePath);
        }

        if (lastRow > Background.getRows()) {
            pos.setRow(Background.getRows() - picture.getHeight() - 15);

            newPicture(pos, picturePath);
        }
    }

    @Override
    public void moveZombie() {

        picToUseLeftRight = null;
        picToUseUpDown = null;

        int targetCol = playerPos.getCol();
        int targetRow = playerPos.getRow();

        if (targetCol > pos.getCol() && !forbiddenRight) {
            super.moveObject(Direction.RIGHT);
            picToUseLeftRight = Direction.RIGHT;
        }

        if (targetCol < pos.getCol() && !forbiddenLeft) {
            super.moveObject(Direction.LEFT);
            picToUseLeftRight = Direction.LEFT;
        }

        if (targetRow < pos.getRow() && !forbiddenUp) {
            super.moveObject(Direction.UP);
            picToUseUpDown = Direction.UP;

        }

        if (targetRow > pos.getRow() && !forbiddenDown) {
            super.moveObject(Direction.DOWN);
            picToUseUpDown = Direction.DOWN;

        }

        if (picToUseUpDown == Direction.UP) {

            if (picToUseLeftRight == Direction.LEFT) {
                picture.load("assets/zombie up left.png");
                return;
            }
            if (picToUseLeftRight == Direction.RIGHT) {
                picture.load("assets/zombie up right.png");
                return;
            }

            picture.load("assets/zombie up.png");
            return;
        }

        if (picToUseUpDown == Direction.DOWN) {

            if (picToUseLeftRight == Direction.LEFT) {
                picture.load("assets/zombie down left.png");
                return;
            }

            if (picToUseLeftRight == Direction.RIGHT) {
                picture.load("assets/zombie down right.png");
                return;
            }

            picture.load("assets/zombie down.png");
            return;
        }

        if (picToUseLeftRight == Direction.LEFT) {
            picture.load("assets/zombie left.png");
            return;
        }
        if (picToUseLeftRight == Direction.RIGHT) {
            picture.load("assets/zombie right.png");
        }
    }

    @Override
    public String getPicturePath() {
        return picturePath;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getHitPoints() {
        return hitPoints;
    }

    @Override
    public void hit() {
        health -= 1;
        if (health == 0) {
            this.picture.delete();
            firstRow = 1;
            lastRow = 1;
            firstCol = 1;
            lastCol = 1;
            this.picture = null;
            zombieKilledSound.play(true);
        }
    }
}
