package it.unibz.mobile.visualandruino;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.Parameter;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;
import it.unibz.mobile.visualandruino.utils.BrickBuilder;
import it.unibz.mobile.visualandruino.utils.BrickPersister;


import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {


    ViewGroup _root;
    ListFragment listFragment;

    //private int _xDelta;
    //private int _yDelta;

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.load_sketch_button) {
            EditText edit = (EditText)listFragment.getMainView().findViewById(R.id.fileName);
            String fileName = edit.getText().toString();
            listFragment.setmItemArray(getCertainSketch(fileName));
        }

        if (id == R.id.save_sketch_button) {
            EditText edit = (EditText)listFragment.getMainView().findViewById(R.id.fileName);
            String fileName = edit.getText().toString();

            ArrayList<Brick> brickList = new ArrayList<>();

            for(Pair<Long, Brick> pair : listFragment.getmItemArray()) {
                brickList.add(pair.second);
            }

            BrickPersister.writeJsonToFile(getApplicationContext(), Constants.SKETCHES_FOLDER, fileName, BrickPersister.translateSketchToJson(brickList));

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        showFragment(ListFragment.newInstance());
/*
        _root = (ViewGroup)findViewById(R.id.container);

        ImageView  _view = (ImageView) findViewById(R.id.rectimage);
*/

        /*RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(400, 200);
        layoutParams.leftMargin = 50;
        layoutParams.topMargin = 50;
        layoutParams.bottomMargin = -250;
        layoutParams.rightMargin = -250;
        _view.setLayoutParams(layoutParams);*/

        //_view.setOnTouchListener(this);



        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_color)));
        BrickPersister.saveStandardSketch(getApplicationContext());

    }


    private void showFragment(Fragment fragment) {
        listFragment = (ListFragment) fragment;


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, "fragment").commit();
    }



    /*
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                break;
        }
        _root.invalidate();
        return true;
    }*/

    private ArrayList<Pair<Long, Brick>> getCertainSketch(String fileName) {
        ArrayList<Pair<Long, Brick>> brickPairs = new ArrayList<Pair<Long, Brick>>();

        String standardJSON = BrickPersister.readJsonFile(getApplicationContext(), Constants.SKETCHES_FOLDER,
                fileName);
        ArrayList<Brick> brickList = BrickPersister.loadSketchFromJson(standardJSON);


        for(Brick item : brickList) {
            brickPairs.add(new Pair<>((long) brickPairs.size(),item));
        }

        return brickPairs;
    }
}