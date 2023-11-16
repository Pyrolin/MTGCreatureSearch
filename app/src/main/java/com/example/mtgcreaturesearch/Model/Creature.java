package com.example.mtgcreaturesearch.Model;

import org.jetbrains.annotations.Nullable;

public class Creature {

    private String name;
    private boolean favorited;
    @Nullable

    public Creature(String creatureName, boolean favorite){
        name = creatureName;
        favorited = favorite;
    }

    public void setFavorited(boolean favorite) {
        favorited = favorite;
    }

    public String getName() {
        return name;
    }

    public boolean getFavorited() {
        return favorited;
    }
}
