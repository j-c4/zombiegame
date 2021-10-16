package org.academiadecodigo.zombiegame.gameobjects;

import org.academiadecodigo.zombiegame.field.Position;
import org.academiadecodigo.zombiegame.field.Zones;
import org.academiadecodigo.zombiegame.gameobjects.player.Bullet;

public abstract class GameObjectsFactory {

    private static int zombieCounter;
    private static int wallCounter;

    public static Zombie makeZombies(Position playerPos) {

        //Select zone
        Zones zone = Zones.values()[zombieCounter];
        //change zone if centre
        if (zone == Zones.E) {
            zombieCounter++;
            zone = Zones.values()[zombieCounter];
        }
        //Add counter for next zone
        zombieCounter++;
        if (zombieCounter == Zones.values().length) {
            zombieCounter = 0;
        }

        //Create zombie random position in selected zone
        Position zombiePos = new Position(zone.getFirstCol(), zone.getLastCol(), zone.getFirstRow(), zone.getLastRow());

        return new Zombie(zombiePos, playerPos, zone);

    }

    public static Bullet makeBullets(Position playerPos) {

        Position bulletPos = new Position(playerPos.getCol(), playerPos.getRow());

        return new Bullet(bulletPos);

    }

    public static Position resetSpawnPos(Zones zone) {
        return new Position(zone.getFirstCol(), zone.getLastCol() - 1, zone.getFirstRow(), zone.getLastRow() - 1);
    }

    public static Wall makeWall() {

        //Select zone
        Zones zone = Zones.values()[wallCounter];
        //change zone if centre
        if (zone == Zones.E) {
            wallCounter++;
            zone = Zones.values()[wallCounter];
        }
        //Add counter for next zone
        wallCounter++;
        if (wallCounter == Zones.values().length) {
            wallCounter = 0;
        }

        //Create wall random position in selected zone
        Position wallPos = new Position(zone.getFirstCol(), zone.getLastCol(), zone.getFirstRow(), zone.getLastRow());

        return new Wall(wallPos, zone);

    }

    public static Position makeNewWallPos(Zones zone) {
        return new Position(zone.getFirstCol(), zone.getLastCol(), zone.getFirstRow(), zone.getLastRow());
    }

}
