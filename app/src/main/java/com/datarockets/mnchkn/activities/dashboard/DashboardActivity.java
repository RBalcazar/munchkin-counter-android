package com.datarockets.mnchkn.activities.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.datarockets.mnchkn.R;
import com.datarockets.mnchkn.activities.result.GameResultActivity;
import com.datarockets.mnchkn.activities.settings.SettingsActivity;
import com.datarockets.mnchkn.adapters.PlayerListAdapter;
import com.datarockets.mnchkn.fragments.dialogs.AddNewPlayerFragment;
import com.datarockets.mnchkn.fragments.players.PlayerFragment;
import com.datarockets.mnchkn.fragments.players.PlayerView;
import com.datarockets.mnchkn.models.Player;
import com.datarockets.mnchkn.store.PlayerDatabaseHelper;
import com.datarockets.mnchkn.utils.LogUtil;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements DashboardView,
        View.OnClickListener, AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener, AddNewPlayerFragment.AddNewPlayerDialogInterface,
        PlayerFragment.PlayerFragmentCallback{

    public static final String TAG = LogUtil.makeLogTag(DashboardActivity.class);

    DashboardPresenter presenter;
    Toolbar toolbar;
    ListView lvPlayerList;
    View lvPlayerListFooterView;
    PlayerListAdapter lvPlayerListAdapter;
    Button btnNextStep, btnAddNewPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new DashboardPresenterImpl(this, this);
        setContentView(R.layout.activity_dashboard);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnNextStep = (Button) findViewById(R.id.btn_next_step);
        btnNextStep.setOnClickListener(this);
        lvPlayerList = (ListView) findViewById(R.id.lv_player_list);
        lvPlayerListFooterView = getLayoutInflater().inflate(R.layout.player_add_list_item, null);
        lvPlayerList.addFooterView(lvPlayerListFooterView);
        btnAddNewPlayer = (Button) lvPlayerListFooterView.findViewById(R.id.btn_add_new_player);
        btnAddNewPlayer.setOnClickListener(this);
        lvPlayerList.setOnItemClickListener(this);
        lvPlayerList.setOnItemLongClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_finish_game:
                showConfirmFinishGameDialog();
                break;
            case R.id.item_settings:
                openSettingsActivity();
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finishGame() {
        Intent intent = new Intent(this, GameResultActivity.class);
        startActivity(intent);
    }

    @Override
    public void setItems(ArrayList<Player> players) {
        lvPlayerListAdapter = new PlayerListAdapter(this, players);
        lvPlayerList.setAdapter(lvPlayerListAdapter);
    }

    @Override
    public void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void showConfirmFinishGameDialog() {
        AlertDialog.Builder confirmFinishGameDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_finish_game_title)
                .setMessage(R.string.dialog_finish_game_message)
                .setPositiveButton(R.string.button_yes, (dialog, which) -> {
                    finishGame();
                })
                .setNegativeButton(R.string.button_no, (dialog, which) -> {
                    dialog.dismiss();
                });
        confirmFinishGameDialog.create().show();
    }

    @Override
    public void showAddNewPlayerDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddNewPlayerFragment addNewPlayerFragment = AddNewPlayerFragment.newInstance();
        addNewPlayerFragment.show(fragmentManager, TAG);
    }

    @Override
    public void updatePlayerData(int index, int levelScore, int levelStrength) {
        Player player = lvPlayerListAdapter.getItem(index);
        player.setLevelScore(levelScore);
        player.setStrengthScore(levelStrength);
        lvPlayerListAdapter.notifyDataSetChanged();
        presenter.updatePlayerListItem(index, levelScore, levelStrength);
    }

    @Override
    public void addPlayerToList(Player player) {
        lvPlayerListAdapter.add(player);
        lvPlayerListAdapter.notifyDataSetChanged();
    }

    @Override
    public void deletePlayerFromList(int position) {
        lvPlayerListAdapter.remove(lvPlayerListAdapter.getItem(position));
        lvPlayerListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_new_player:
                showAddNewPlayerDialog();
                break;
            case R.id.btn_next_step:
                if (lvPlayerList.getCheckedItemPosition() == lvPlayerList.getCount() - 2) {
                    lvPlayerList.setItemChecked(0, true);
                    lvPlayerList.setSelection(0);
                } else {
                    lvPlayerList.setItemChecked(lvPlayerList.getCheckedItemPosition() + 1, true);
                    lvPlayerList.setSelection(lvPlayerList.getCheckedItemPosition());
                }
                break;
        }
    }

    @Override
    public void onFinishEditDialog(String inputName) {
        presenter.addNewPlayer(inputName);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_player_delete_title)
                .setMessage(R.string.dialog_player_delete_message)
                .setPositiveButton(R.string.button_yes, (dialog, which) -> {
                    presenter.deletePlayerListItem(position, lvPlayerListAdapter.getItem(position).getId());
                })
                .setNegativeButton(R.string.button_no, (dialog, which) -> {
                    dialog.dismiss();
                })
                .create();
        alertDialog.show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        lvPlayerList.setItemChecked(position, true);
    }

    @Override
    public void onScoreChanged(Player player) {

    }

}