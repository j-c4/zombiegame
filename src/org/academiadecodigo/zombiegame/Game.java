package org.academiadecodigo.zombiegame;

import org.academiadecodigo.simplegraphics.graphics.Rectangle;
import org.academiadecodigo.zombiegame.field.*;
import org.academiadecodigo.zombiegame.gameobjects.CollisionDetector;
import org.academiadecodigo.zombiegame.gameobjects.GameObjectsFactory;
import org.academiadecodigo.zombiegame.gameobjects.walls.Wall;
import org.academiadecodigo.zombiegame.gameobjects.zombies.Zombie;
import org.academiadecodigo.zombiegame.gameobjects.player.Bullet;
import org.academiadecodigo.zombiegame.gameobjects.player.Player;

public class Game {

    private static int zombies_nr = 10;
    private int wallNr = 13;

    private Zombie[] zombieHoard;
    private Bullet[] bulletsShot;
    private Wall[] walls;
    private Player player;
    private GameOver gameOver;
    private Sound backgroundMusic = new Sound("assets/backgroundMusic.wav");
    private HealthBar hpBar;
    private Menu menu;
    private Sound playerDyingSound = new Sound("assets/playerDyingSound.wav");
    private Score scoreBoard;
    private Round round;
    private int roundNumber = 1;
    private int zombiesKilled = 0;
    private int gameWon = 0;

    private Background background;

    private CollisionDetector collisionDetector;

    public Game() {
        background = new Background();

        if (roundNumber == 1) {
            player = new Player();
            player.newPicture(player.getPos(), "assets/playerright.png");
        }

        scoreBoard = new Score();
        round = new Round();
    }

    public void init() throws InterruptedException {

        if (zombies_nr == 2) { //só para o início
            scoreBoard.drawScore();
        }

        Rectangle roundPic = new Rectangle();
        roundPic.draw();

        for (int i = 0; i < 1000; i ++) {
            if (i == 1000 - 1) {
                roundPic.delete();
            }
        }


        round.setRound(roundNumber);

        Thread.sleep(1000);

        round.deleteRound();



        walls = new Wall[wallNr];

        zombieHoard = new Zombie[zombies_nr];

        bulletsShot = player.getBullets();

        for (int z = 0; z < zombies_nr; z++) {
            zombieHoard[z] = GameObjectsFactory.makeZombie(player.getPos());
        }

        for (int w = 0; w < wallNr; w++) {
            walls[w] = GameObjectsFactory.makeWall();
        }

        hpBar = new HealthBar();
        collisionDetector = new CollisionDetector(zombieHoard, player, bulletsShot, walls, hpBar);

        player.setCollisionDetector(collisionDetector);

        //check zombies overlap
        for (Zombie z : zombieHoard) {
            collisionDetector.checkOverlap(z);
        }

    }

    public void start() throws InterruptedException {
        player.setPlayerReady();
        backgroundMusic.play(true);

        while (gameWon != 1) {
            Thread.sleep(17);

            if (player.getHealth() <= 0) {

                playerDyingSound.play(true);
                GameOver gameOver = new GameOver();
                background = null;
                menu = null;
                scoreBoard = null;

                for (Wall w : walls) {
                    w.remove();
                }
                for (Bullet b : bulletsShot) {
                    if (b != null) {
                        b.getPos().setCol(0);
                        b.getPos().setRow(0);
                        b.destroyBullet();
                    }
                }
                for(Zombie z : zombieHoard) {
                    if (z != null) {
                        z.getPos().setRow(0);
                        z.getPos().setCol(0);
                        z.remove();
                    }
                }

                player.remove();

                zombieHoard = null;
                bulletsShot = null;
                collisionDetector = null;
                backgroundMusic.stop();

                menu = new Menu();
            }

            for (int i = 0; i < 4; i++) { //speed
                moveAllBullets();
            }

            for (int i = 0; i < 2; i++) { //speed
                player.move();
            }

            moveAllZombies();

        }

    }

    public void newRound() throws InterruptedException {

        background = null;
        player.resetLife();
        for (Wall w : walls) {
            w.remove();
        }
        for (Bullet b : bulletsShot) {
            if (b != null) {
                b.destroyBullet();
            }
        }
        zombieHoard = null;
        bulletsShot = null;
        collisionDetector = null;
        player.newPicture(GameObjectsFactory.resetSpawn(Zones.E), "assets/playerright.png");
        zombies_nr++;

        round.setRound(roundNumber);

        Thread.sleep(1000);

        round.deleteRound();

        init();
    }

    public void moveAllBullets() {
        for (Bullet b : bulletsShot) {
            if (b != null && !b.isImpacted()) {
                collisionDetector.checkBulletCollision(b);
                b.moveBullet();
            }
        }
    }

    public void moveAllZombies() throws InterruptedException {
        int zombiesDead = 0;
        for (Zombie z : zombieHoard) {

            if (z.getHealth() == 0) {
                zombiesDead++;
                continue;
            }

            collisionDetector.checkZombieCollision(z);

            z.moveZombie();

        }
        if (zombiesDead != zombiesKilled) {
            zombiesKilled += (zombiesDead - zombiesKilled);
            gameWon = scoreBoard.setScore(zombiesKilled);
        }
        if (zombiesDead == zombies_nr) {
            zombiesKilled = 0;
            scoreBoard.setZero();
            roundNumber++;

            newRound();
        }
    }
}
