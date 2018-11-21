package it.unibz.mobile.visualandruino;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woxthebox.draglistview.DragItem;
import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;

import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.ArduinoCommandBrick;
import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.Parameter;
import it.unibz.mobile.visualandruino.utils.BrickCommunicator;
import it.unibz.mobile.visualandruino.utils.BrickExecutor;

public class ListFragment extends Fragment {

    private ArrayList<Pair<Long, Brick>> mItemArray;
    private DragListView mDragListView;
    private ListSwipeHelper mSwipeHelper;
    private MySwipeRefreshLayout mRefreshLayout;
    private View mainView;
    BrickExecutor brickExecutor;
    ItemBrickAdapter listAdapter;

    public static ListFragment newInstance() {
        return new ListFragment();
    }


    public ArrayList<Brick> getBricksArray() {
        ArrayList<Brick> arrBricks= new ArrayList<Brick>();
        for (int i=0; i< mItemArray.size(); i++)
        {
            arrBricks.add(mItemArray.get(i).second);
        }
        return arrBricks;
    }
    public Brick getBrick(int i)
    {
        ArrayList<Parameter> arr=new ArrayList<Parameter>();
        Parameter val=new Parameter("Output",String.valueOf((i%2)));
        Parameter valPin=new Parameter("PinNumber","22");
        arr.add(val );
        arr.add(valPin );

        String name="ON";
        if(i%2==0)
        {
            name="OFF";
        }

        return new ArduinoCommandBrick(name, i%2 , arr, 3);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.firstlist_layout, container, false);
        mRefreshLayout = (MySwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh_layout);
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

        mItemArray = new ArrayList<>();

        for (int i = 0; i < 3; i++) {


            mItemArray.add( new Pair<>((long) i,getBrick(i)));
        }

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
                }
            }
        });

        setupListRecyclerView();

        /*
        mSmoothBluetooth = new SmoothBluetooth(getActivity().getApplicationContext());
        mSmoothBluetooth.setListener(mListener);
        mSmoothBluetooth.tryConnection();*/
        final BrickCommunicator brickCommunicator = BrickCommunicator.getInstance();
        brickCommunicator.initiateBluetooth(this);


/*
        final Button buttonHigh = mainView.findViewById(R.id.digitalHigh);
        buttonHigh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText edit = (EditText)mainView.findViewById(R.id.pinNumber);
                String pinNumber = edit.getText().toString();


                brickExecutor.executeBlocks(getBricksArray(), pinNumber);


                //String testCommand = "3 " + pinNumber + " 1;";

                // Code here executes on main thread after user presses button
                //mSmoothBluetooth.send(testCommand, false);
                //brickCommunicator.sendCommand(testCommand);
            }
        });


        final Button buttonAnalogRead = mainView.findViewById(R.id.analogReadButton);
        buttonAnalogRead.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String testCommand = "-1 41;";

                // Code here executes on main thread after user presses button
                //mSmoothBluetooth.send(testCommand, false);
                brickCommunicator.sendCommand(testCommand);
            }
        });*/

        FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mItemArray.add( new Pair<>((long) mItemArray.size(),getBrick(mItemArray.size())));
                listAdapter.notifyDataSetChanged();

            }
        });

        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Visual-Andruino");
    }



    private void setupListRecyclerView() {
        mDragListView.setLayoutManager(new LinearLayoutManager(getContext()));
        brickExecutor= new BrickExecutor();


         listAdapter = new ItemBrickAdapter(getContext(), mItemArray, R.layout.list_item_parameters, R.id.image, false,

                new RecyclerViewOnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        /*EditText edit = (EditText)mainView.findViewById(R.id.pinNumber);
                        String pinNumber = edit.getText().toString();
                        //Toast.makeText(view.getContext(), "Start - position: " + mItemArray.get(position).second.getName(), Toast.LENGTH_SHORT).show();*/

                        brickExecutor.executeBrick(mItemArray.get(position).second, mItemArray.get(position).second.getParameters().get(0).getValue());
                    }
                });
        mDragListView.setAdapter(listAdapter, true);
        mDragListView.setCanDragHorizontally(false);
        mDragListView.setCustomDragItem(new MyDragItem(getContext(), R.layout.list_item_parameters));




    }

    public void updateReturnView(String answer) {
        /*TextView resultView = getView().findViewById(R.id.resultView);
        resultView.setText(answer);*/
    }


    private static class MyDragItem extends DragItem {

        MyDragItem(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            CharSequence text = ((TextView) clickedView.findViewById(R.id.text)).getText();
            ((TextView) dragView.findViewById(R.id.text)).setText(text);
            dragView.findViewById(R.id.item_layout).setBackgroundColor(dragView.getResources().getColor(R.color.list_item_background));
        }
    }
}
