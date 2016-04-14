package com.datarockets.mnchkn.fragments.charts;

import android.content.Context;

import com.datarockets.mnchkn.models.Player;
import com.datarockets.mnchkn.store.GameServiceImpl;
import com.datarockets.mnchkn.store.PlayerServiceImpl;

import java.util.ArrayList;

import lecho.lib.hellocharts.model.LineChartData;

public class ChartsInteractorImpl implements ChartsInteractor {

    private GameServiceImpl gameService;
    private PlayerServiceImpl playerService;

    public ChartsInteractorImpl(Context context) {
        gameService = GameServiceImpl.getInstance(context.getApplicationContext());
        playerService = PlayerServiceImpl.getInstance(context.getApplicationContext());
    }

    @Override
    public LineChartData loadLineChartData(int type) {
        return gameService.createScoresChartData(type, gameService.getScoresChartData());
    }

    @Override
    public void loadPlayers(OnChartLoadedListener listener) {
        listener.showPlayers(playerService.getPlayersList());
    }


}
