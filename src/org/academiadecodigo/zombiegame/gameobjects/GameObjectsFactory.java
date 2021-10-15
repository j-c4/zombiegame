package org.academiadecodigo.zombiegame.gameobjects;

import org.academiadecodigo.zombiegame.field.Position;
import org.academiadecodigo.zombiegame.field.Zones;
import org.academiadecodigo.zombiegame.player.Bullet;

public abstract class GameObjectsFactory {

    private static int zombieCounter;

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

    public static Position makeNewZombiePos(Zones zone) {
        return new Position(zone.getFirstCol(), zone.getLastCol(), zone.getFirstRow(), zone.getLastRow());
    }

}
