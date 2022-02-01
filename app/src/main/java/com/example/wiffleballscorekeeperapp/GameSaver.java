package com.example.wiffleballscorekeeperapp;


import com.example.game.Game;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GameSaver
{
    public static String toJson(Game[] games)
    {
        Gson gson = new Gson();
        String gamesJson = gson.toJson(games);
        return gamesJson;
    }

    public static Game[] loadSavedGames(String gameJson)
    {
        Gson gson = new Gson();
        Type typeGame = new TypeToken<Game[]>() {}.getType();
        Game[] games = gson.fromJson(gameJson, typeGame);
        return games;
    }
}
