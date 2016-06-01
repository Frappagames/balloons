package com.frappagames.balloons.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.frappagames.balloons.Balloons;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(640, 400);
    }

    @Override
    public ApplicationListener getApplicationListener() {
        return new Balloons();
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new Balloons();
    }
}
