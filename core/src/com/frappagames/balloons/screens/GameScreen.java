package com.frappagames.balloons.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.frappagames.balloons.Balloons;
import com.frappagames.balloons.Tools.Assets;
import com.frappagames.balloons.Tools.Balloon;
import com.frappagames.balloons.Tools.Settings;
import com.frappagames.balloons.Tools.abstractGameScreen;

import java.util.Iterator;

/**
 * Game screen class
 */
public class GameScreen extends abstractGameScreen {
    private static final int SPAWN_SPEED_MS = 1000;
    private static final int GAME_DURATION  = 90;
    private static final int PAUSE_DURATION = 5;
    final   Balloons       game;
    private Vector3        touchPoint;

    private Array<Balloon> balloons;
    private long           lastBalloonTime;
    private long           gameStartTime;
    private Integer        balloonBursted;
    private Integer        balloonSended;
    private Integer        combos;
    private Integer        score;
    private boolean        gameIsPlaying;
    private int            percent;
    private ImageButton    replayBtn;
    private ImageButton    exitBtn;
    private Image          etoile1;
    private Image          etoile2;
    private Image          etoile3;
    private Image          cocarde;
    private Table          scoreTable;
    private Label          lblScore;
    private Label          lblBestScore;
    private Label          lblBalloons;
    private Label          lblCombos;
    private boolean        isScoreSet;

    private ParticleEffectPool balloonsEffectPool;
    private Array<PooledEffect> effects;
    private ParticleEffect balloonEffect;


    public GameScreen(Balloons gameApp) {
        super(gameApp);

        this.game = gameApp;
        touchPoint = new Vector3();
        balloonBursted = 0;
        balloonSended = 0;
        combos = 0;
        percent = 0;
        gameStartTime = TimeUtils.nanoTime();
        gameIsPlaying = true;
        isScoreSet = false;
        balloons = new Array<Balloon>();

        effects = new Array<PooledEffect>();
        balloonEffect = new ParticleEffect();
        balloonEffect.load(Gdx.files.internal("explosion_white.fx"), Gdx.files.internal(""));
        balloonsEffectPool = new ParticleEffectPool(balloonEffect, 1, 10);

        if (Settings.musicEnabled) Assets.music.play();

        exitBtn = new ImageButton(Assets.home, Assets.homeOver);
        replayBtn = new ImageButton(Assets.replay, Assets.replayOver);
        etoile1 = new Image(Assets.smallStar);
        etoile1.setVisible(false);
        etoile2 = new Image(Assets.smallStar);
        etoile2.setVisible(false);
        etoile3 = new Image(Assets.bigStar);
        etoile3.setVisible(false);
        cocarde = new Image(Assets.cocarde);
        cocarde.setVisible(false);


        scoreTable = new Table();
        scoreTable.background(Assets.scoreBackground);
        scoreTable.setPosition((viewport.getWorldWidth() / 2) - 204, (viewport.getWorldHeight() / 2) - 235);
        scoreTable.setVisible(false);
        scoreTable.setSize(408, 516);
        stage.addActor(scoreTable);

        Table tableStars = new Table();
        tableStars.add(etoile1).pad(174, 76, 0, 0);
        tableStars.add(etoile3).pad(132, 6, 20, 6);
        tableStars.add(etoile2).pad(174, 0, 0, 88);
        scoreTable.add(tableStars).height(242).colspan(2).row();

        VerticalGroup vgScores = new VerticalGroup();
        lblScore = new Label("", Assets.souses45);
        lblBestScore = new Label("", Assets.souses20);
        lblScore.setAlignment(Align.center);
        lblBestScore.setAlignment(Align.center);
        vgScores.addActor(lblScore);
        vgScores.addActor(lblBestScore);

        Table tableScore = new Table();
        tableScore.add().width(100);
        tableScore.add(vgScores).width(208);
        tableScore.add(cocarde).pad(23, 0, 0, 46).left().bottom();
        scoreTable.add(tableScore).height(95).colspan(2).row();

        lblBalloons = new Label("", Assets.souses22);
        lblBalloons.setAlignment(Align.center);
        scoreTable.add(lblBalloons).pad(24, 227, 13, 56).colspan(2).row();

        lblCombos = new Label("", Assets.souses22);
        lblCombos.setAlignment(Align.center);
        scoreTable.add(lblCombos).pad(28, 227, 41, 56).colspan(2).row();

        scoreTable.add(exitBtn).pad(0, 52, 0, 52);
        scoreTable.add(replayBtn).pad(0, 52, 0, 52);


        exitBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MenuScreen(game));
            }
        });
        replayBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new GameScreen(game));
            }
        });

        spawnBalloon();
    }

    private void spawnBalloon() {
        int x = MathUtils.random(0, Balloons.width - Balloon.BALLOON_WIDTH);
        balloons.add(new Balloon(x));
        lastBalloonTime = TimeUtils.nanoTime();
        balloonSended++;
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        update();
        draw(delta);
    }

    public void update() {
        // Check if game time is over (more than 1m30
        if (gameIsPlaying && TimeUtils.nanoTime() - gameStartTime > GAME_DURATION * 1000000000L) {
            gameIsPlaying = false;
        }

        // Reduce balloons spawning delay to add difficulty over time
        double acceleration = Math.round((TimeUtils.nanoTime() - gameStartTime) / 130000000);

        // check if we need to create a new raindrop
        if (gameIsPlaying) {
            if (TimeUtils.nanoTime() - lastBalloonTime > (SPAWN_SPEED_MS - acceleration) * 1000000)
                spawnBalloon();
        }

        // If there is at least one balloon, move it.
        if (balloons.size > 0) {
            Iterator<Balloon> iter            = balloons.iterator();
            int               balloonsTouched = 0;
            while (iter.hasNext()) {
                Balloon balloon = iter.next();

                // Is balloon touched ?
                if (Gdx.input.justTouched()) {
                    viewport.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
                    if (balloon.contains(touchPoint.x, touchPoint.y)) {
                        if (Settings.soundEnabled) Assets.playSound(Assets.clickSound);
                        iter.remove();
                        balloonBursted++;
                        balloonsTouched++;

                        // Add effect
                        PooledEffect effect = balloonsEffectPool.obtain();
                        effect.setPosition(touchPoint.x, touchPoint.y);
                        effect.getEmitters().get(0).getTint().setColors(balloon.getColor());
                        effects.add(effect);

                        continue;
                    }
                }
                balloon.y += balloon.speed * Gdx.graphics.getDeltaTime();
                if (balloon.y > Balloons.height) iter.remove();
            }

            // Is it a combo
            if (balloonsTouched > 1) combos++;

        } else {
            // If game is over, show score
            if (!gameIsPlaying) {
                percent = Math.round(((float) balloonBursted / (float) balloonSended) * 100);

                // Redirect to score after a little time (1m35 so game end + 5s)
                if (TimeUtils.nanoTime() - gameStartTime > (GAME_DURATION + PAUSE_DURATION) * 1000000000L) {
                    if (Settings.musicEnabled) Assets.music.stop();

                    if (!isScoreSet) {
                        if (percent == 100) {
                            score = balloonBursted * 5;
                        } else if (percent >= 75) {
                            score = balloonBursted * 3;
                        } else if (percent >= 50) {
                            score = balloonBursted * 2;
                        } else {
                            score = balloonBursted;
                        }

                        score += combos * 50;

                        if (score > Settings.getBestScore()) {
                            Settings.setBestScore(score);

                            lblBestScore.setText("Nouveau Record");
                            cocarde.setVisible(true);
                        } else {
                            lblBestScore.setText("Record : " + Settings.getBestScore().toString());
                        }
                        isScoreSet = true;
                    }

                    lblScore.setText(score.toString());
                    lblBalloons.setText(balloonBursted.toString() + "/" + balloonSended.toString());
                    lblCombos.setText(combos.toString());

                    // First star
                    if (percent >= 50) {
                        etoile1.setVisible(true);
                    }

                    // Second star
                    if (percent >= 75) {
                        etoile2.setVisible(true);
                    }

                    // Third star
                    if (percent == 100) {
                        etoile3.setVisible(true);
                    }

                    scoreTable.setVisible(true);
                }
            }

        }
    }

    public void draw(float delta) {
        game.batch.begin();

        for (Balloon balloon : balloons) {
            game.batch.draw(balloon.texture, balloon.x, balloon.y);
        }

        for (int i = effects.size - 1; i >= 0; i--) {
            PooledEffect effect = effects.get(i);
            effect.draw(game.batch, delta);
            if (effect.isComplete()) {
                effect.free();
                effects.removeIndex(i);
            }
        }

        game.batch.end();
    }
}
