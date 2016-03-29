package com.datarockets.mnchkn.activities.dashboard;

import com.datarockets.mnchkn.models.Player;

import java.util.ArrayList;
import java.util.List;

public interface DashboardView {
    void finishGame();
    void setItems(ArrayList<Player> players);
    void openSettingsActivity();
    void showConfirmFinishGameDialog();
    void showAddNewPlayerDialog();
    void updatePlayerData(Player player, int position);
    void addPlayerToList(Player player);
    void deletePlayerFromList(int index);
    void showStartContinueDialog();
}
