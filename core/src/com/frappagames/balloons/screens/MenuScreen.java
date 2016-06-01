package com.frappagames.balloons.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.frappagames.balloons.Balloons;
import com.frappagames.balloons.Tools.Assets;
import com.frappagames.balloons.Tools.Settings;
import com.frappagames.balloons.Tools.abstractGameScreen;

/**
 * Main menu class for the game
 *
 * Created by jmoreau on 14/08/15.
 */
public class MenuScreen extends abstractGameScreen {
    public MenuScreen(final Balloons gameApp) {
        super(gameApp);

        ImageButton playBtn  = new ImageButton(Assets.play, Assets.playOver);
        ImageButton soundBtn = new ImageButton(Assets.soundOn, Assets.soundOff, Assets.soundOff);
        ImageButton musicBtn = new ImageButton(Assets.musicOn, Assets.musicOff, Assets.musicOff);
        ImageButton exitBtn  = new ImageButton(Assets.exit, Assets.exitOver);

        soundBtn.pad(0, 30, 30, 10);
        if (!Settings.soundEnabled) soundBtn.setChecked(true);

        musicBtn.pad(0, 10, 30, 0);
        if (!Settings.musicEnabled) musicBtn.setChecked(true);

        exitBtn.pad(0, 0, 30, 30);


        HorizontalGroup hg = new HorizontalGroup();
        hg.addActor(soundBtn);
        hg.addActor(musicBtn);

        table.add(playBtn).colspan(2).expand().row();
        table.add(hg).bottom().left();
        table.add(exitBtn).bottom().right();

        playBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new GameScreen(gameApp));
            }
        });
        soundBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Settings.toggleSound();
                Assets.playSound(Assets.clickSound);
            }
        });
        musicBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Settings.toggleMusic();
                Assets.playSound(Assets.clickSound);
            }
        });
        exitBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Assets.playSound(Assets.clickSound);
                Gdx.app.exit();
            }
        });
    }
}
