package it.unibz.mobile.visualandruino;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.woxthebox.draglistview.DragItem;
import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;

import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.Brick;


public class ListBricksBase extends Fragment {

    private ArrayList<Pair<Long, Brick>> mItemArray;
    private DragListView mDragListView;
    private ListSwipeHelper mSwipeHelper;
    private MySwipeRefreshLayout mRefreshLayout;
    private View mainView;

    ItemBrickAdapter listAdapter;


    public ListBricksBase()
    {
        mItemArray = new ArrayList<>();
    }
    public static ListBricksBase newInstance() {
        return new ListBricksBase();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fram_bricks_base, container, false);
        mRefreshLayout = (MySwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh_layout);
        mDragListView = (DragListView) mainView.findViewById(R.id.drag_list_view);
        mDragListView.getRecyclerView().setVerticalScrollBarEnabled(true);
        mRefreshLayout.setScrollingView(mDragListView.getRecyclerView());
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.app_color));
        setupListRecyclerView();

        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Visual-Andruino");
    }



    private void setupListRecyclerView() {

        mItemArray=((MainActivity) getContext()).getCertainSketch(Constants.STANDARD_SKETCH);

        mDragListView.setLayoutManager(new LinearLayoutManager(getContext()));

        listAdapter = new ItemBrickAdapter(getContext(), mItemArray, R.layout.list_item_parameters, R.id.item_layout, false,
                new RecyclerViewOnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        ((MainActivity) getContext()).listFragment.addBrick(mItemArray.get(position).second);
                        ((MainActivity) getContext()).showListProgFragment();

                    }
                });
        mDragListView.setAdapter(listAdapter, true);
        mDragListView.setCanDragHorizontally(false);
        mDragListView.setDragEnabled(false);
        mDragListView.setClickable(true );

        //mDragListView.setCustomDragItem(new MyDragItem(getContext(), R.layout.list_item_parameters));


    }


}
