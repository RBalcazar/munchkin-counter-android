package com.datarockets.mnchkn.activities.result;

import android.content.Context;

public class GameResultPresenterImpl implements GameResultPresenter, GameResultInteractorImpl.OnResultsLoaded {

    GameResultView gameResultView;
    GameResultInteractorImpl interactor;

    public GameResultPresenterImpl(GameResultView gameResultView, Context context) {
        this.gameResultView = gameResultView;
        this.interactor = new GameResultInteractorImpl(context);
    }

    @Override
    public void onCreate() {
        if (gameResultView != null) {
            interactor.loadGameResults(this);
        }
    }

    @Override
    public void onDestroy() {
        if (gameResultView != null) {
            interactor.clearSteps();
            gameResultView = null;
        }
    }

    @Override
    public void notifyChartDataPrepared() {
        if (gameResultView != null) {
            gameResultView.loadChartFragments();
        }
    }

}
