package com.datarockets.mnchkn.fragments.charts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.datarockets.mnchkn.R;
import com.datarockets.mnchkn.adapters.PlayerChartListAdapter;
import com.datarockets.mnchkn.models.Player;

import java.util.ArrayList;

import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartsFragment extends Fragment implements ChartsView {

    private static final String CHART_TYPE = "chart_type";

    View chartsView;
    LineChartView lineChartView;
    LineChartData lineChartData;
    ListView lvPlayerList;
    PlayerChartListAdapter lvPlayerListAdapter;
    ChartsPresenterImpl presenter;

    public static ChartsFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(CHART_TYPE, type);
        ChartsFragment fragment = new ChartsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        presenter = new ChartsPresenterImpl(this, getActivity());
        lineChartData = presenter.loadChartData(getArguments().getInt(CHART_TYPE));
        chartsView = inflater.inflate(R.layout.fragment_charts, container, false);
        return chartsView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvPlayerList = (ListView) view.findViewById(R.id.lv_player_list);
        lineChartView = (LineChartView) view.findViewById(R.id.line_chart_view);
        lineChartView.setLineChartData(lineChartData);
    }


    @Override
    public void showPlayersList(ArrayList<Player> players) {
        lvPlayerListAdapter = new PlayerChartListAdapter(getActivity(), players);
        lvPlayerList.setAdapter(lvPlayerListAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

}
