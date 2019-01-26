package it.unibz.mobile.visualandruino;


import android.os.Bundle;
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

import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;

import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.enums.BrickStatus;
import it.unibz.mobile.visualandruino.utils.BrickExecutor;
import it.unibz.mobile.visualandruino.utils.BrickHelper;
import it.unibz.mobile.visualandruino.utils.BrickIncorrectOrderException;

public class ListFragment extends Fragment {

    private ArrayList<Pair<Long, Brick>> mItemArray;
    private DragListView mDragListView;
    private BrickSwipeRefreshLayout mRefreshLayout;
    private View mainView;
    BrickExecutor brickExecutor;
    ItemBrickAdapter listAdapter;
    ProgressBar progressBarRun;

    public ListFragment()
    {
        mItemArray = new ArrayList<>();
    }
    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.frame_prog_layout, container, false);
        mRefreshLayout = (BrickSwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh_layout);
        mDragListView = (DragListView) mainView.findViewById(R.id.drag_list_view);
        mDragListView.getRecyclerView().setVerticalScrollBarEnabled(true);
        mDragListView.setDragListListener(new DragListView.DragListListenerAdapter() {
            @Override
            public void onItemDragStarted(int position) {
                mRefreshLayout.setEnabled(false);
                //Toast.makeText(mDragListView.getContext(), "Start - position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                mRefreshLayout.setEnabled(true);
                if (fromPosition != toPosition) {
                    //Toast.makeText(mDragListView.getContext(), "End - position: " + toPosition, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRefreshLayout.setScrollingView(mDragListView.getRecyclerView());
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.app_color));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        mDragListView.setSwipeListener(new ListSwipeHelper.OnSwipeListenerAdapter() {
            @Override
            public void onItemSwipeStarted(ListSwipeItem item) {
                mRefreshLayout.setEnabled(false);
            }

            @Override
            public void onItemSwipeEnded(ListSwipeItem item, ListSwipeItem.SwipeDirection swipedDirection) {
                mRefreshLayout.setEnabled(true);

                // Swipe to delete on left
                if (swipedDirection == ListSwipeItem.SwipeDirection.LEFT) {
                    Pair<Long, String> adapterItem = (Pair<Long, String>) item.getTag();
                    int pos = mDragListView.getAdapter().getPositionForItem(adapterItem);
                    mDragListView.getAdapter().removeItem(pos);
                }else
                {
                    Pair<Long, String> adapterItem = (Pair<Long, String>) item.getTag();
                    int pos = mDragListView.getAdapter().getPositionForItem(adapterItem);


                    brickExecutor.executeBrick(mItemArray.get(pos).second, ListFragment.this, true);
                }
            }
        });




        setupListRecyclerView();




        FloatingActionButton fabLayout = (FloatingActionButton) mainView.findViewById(R.id.addLayout);
        fabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) view.getContext()).showFragment(ListBricksBase.newInstance());
            }
        });




        progressBarRun = (ProgressBar) mainView.findViewById(R.id.progress_bar_run);


        EditText edit = (EditText)mainView.findViewById(R.id.fileName);
        edit.setText(Constants.STANDARD_SKETCH);


        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Visual-Andruino");
    }

    public void addBrick(Brick brick)
    {
        mItemArray.add( new Pair<>((long) mItemArray.size(),brick));
        mDragListView.getAdapter().notifyDataSetChanged();
    }

    private void setupListRecyclerView() {
        mDragListView.setLayoutManager(new LinearLayoutManager(getContext()));
        brickExecutor= new BrickExecutor();
         listAdapter = new ItemBrickAdapter(getContext(), mItemArray, R.layout.list_item_parameters, R.id.item_layout, true,
                new RecyclerViewOnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                       // brickExecutor.executeBrick(mItemArray.get(position).second);
                    }
                });
        mDragListView.setAdapter(listAdapter, true);
        mDragListView.setCanDragHorizontally(false);


    }

    public void printDebbug(String message)
    {
        ((MainActivity) this.getContext()).printDebugg(message);
    }
    public void executeBricks()
    {
        try {
            brickExecutor.executeBlocks(BrickHelper.getInstance().translateUiBricksToBackendBricks((ArrayList<Pair<Long, Brick>>) mItemArray.clone()), this, false);
        }
        catch (BrickIncorrectOrderException exp)
        {
            this.printDebbug(exp.getMessage());
        }


    }

    public void debugBricks()
    {
        brickExecutor.executeBlocks(BrickHelper.getInstance().translateUiBricksToBackendBricks((ArrayList<Pair<Long, Brick>>) mItemArray.clone()), this, true);

    }



    public void printCurrentVariables() {
        ((TextView)mainView.findViewById(R.id.varView)).setText(Html.fromHtml(BrickHelper.getInstance().getCurrentVariablesFormatted()));

    }


    public void setmItemArray(ArrayList<Pair<Long, Brick>> mItemArray) {

        this.mItemArray.clear();
        this.mItemArray.addAll(mItemArray);
        mDragListView.getAdapter().notifyDataSetChanged();
    }

    public void setmItemArrayInital(ArrayList<Pair<Long, Brick>> mItemArray) {

        this.mItemArray = mItemArray;
    }

    public void setBrickStatus(long brickUI, BrickStatus brickStatus) {

        for(Pair<Long, Brick> pair : this.getmItemArray()) {
            if(pair.first == brickUI) {
                pair.second.setBrickStatus(brickStatus);
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        mDragListView.getAdapter().notifyDataSetChanged();

                    }
                });

            }
        }


    }

    public ArrayList<Pair<Long, Brick>> getmItemArray() {
        return mItemArray;
    }

    public View getMainView() {
        return mainView;
    }





}
