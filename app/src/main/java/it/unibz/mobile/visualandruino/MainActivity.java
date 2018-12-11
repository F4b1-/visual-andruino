package it.unibz.mobile.visualandruino;



import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import java.util.ArrayList;
import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.Parameter;
import it.unibz.mobile.visualandruino.utils.BrickPersister;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity implements ItemParameterFragment.OnListFragmentInteractionListener {


    ViewGroup _root;
    ListFragment listFragment;
    Fragment currentFragment;



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
            showPersistenceDialog(MainActivity.this, true);
        }

        if (id == R.id.save_sketch_button) {
            EditText edit = (EditText)listFragment.getMainView().findViewById(R.id.fileName);
            String fileName = edit.getText().toString();
            showPersistenceDialog(MainActivity.this, false);
        }
        if(id== R.id.run_sketch_button){

            listFragment.executeBricks();
        }

        if(id== R.id.debug_sketch_button){

            listFragment.executeNextBrick();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        ListFragment listF =ListFragment.newInstance();
        showListFragment(listF);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_color)));
        BrickPersister.saveStandardSketch(getApplicationContext());


    }

    @Override
    public void onBackPressed() {


        // note: you can also use 'getSupportFragmentManager()'
        FragmentManager mgr = getSupportFragmentManager();
        if (mgr.getBackStackEntryCount() == 0) {
            // No backstack to pop, so calling super
            super.onBackPressed();
        } else {
            mgr.popBackStack();
            //mgr.popBackStackImmediate(currentFragment.getClass().getName(), 0);
        }
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, "fragment");
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
        currentFragment=fragment;
    }

    public void showListFragment(Fragment fragment) {
        listFragment = (ListFragment) fragment;

        showFragment(fragment);
    }

    public void onListFragmentInteraction(Parameter uri){
        //you can leave it empty
    }

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

    private void showPersistenceDialog(Context c, final boolean load) {
        String title;
        String message;
        String positiveButtonText;

        if(load) {
            title = Constants.LOAD_TITLE;
            message = Constants.LOAD_ENTER_FILE;
            positiveButtonText = Constants.LOAD_BUTTON;
        } else {
            title = Constants.SAVE_TITLE;
            message = Constants.SAVE_ENTER_FILE;
            positiveButtonText = Constants.SAVE_BUTTON;
        }

        final EditText taskEditText = new EditText(c);
        taskEditText.setText(Constants.STANDARD_SKETCH);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(title)
                .setMessage(message)
                .setView(taskEditText)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fileName = String.valueOf(taskEditText.getText());
                        if(load) {
                            listFragment.setmItemArray(getCertainSketch(fileName));
                        } else {
                            ArrayList<Brick> brickList = new ArrayList<>();

                            for(Pair<Long, Brick> pair : listFragment.getmItemArray()) {
                                brickList.add(pair.second);
                            }

                            BrickPersister.writeJsonToFile(getApplicationContext(), Constants.SKETCHES_FOLDER, fileName, BrickPersister.translateSketchToJson(brickList));
                        }

                    }
                })
                .setNegativeButton(Constants.CANCEL_BUTTON, null)
                .create();
        dialog.show();
    }

}