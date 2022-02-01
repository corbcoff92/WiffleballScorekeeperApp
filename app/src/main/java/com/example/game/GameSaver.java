package com.example.game;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

class GameSaver
{
    static String toJson(Game game)
    {
        Gson gson = new Gson();
        String gameJson = gson.toJson(game);
        return gameJson;
    }

    static Game fromJson(String gameJson)
    {
        Gson gson = new Gson();
        Type typeGame = new TypeToken<Game>() {}.getType();
        Game game = gson.fromJson(gameJson, typeGame);
        return game;
    }
}
