package it.unibz.mobile.visualandruino;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.Parameter;
import it.unibz.mobile.visualandruino.utils.BrickCommunicator;
import it.unibz.mobile.visualandruino.utils.BrickHelper;
import it.unibz.mobile.visualandruino.utils.BrickPersister;
import it.unibz.mobile.visualandruino.utils.UiHelper;


public class MainActivity extends AppCompatActivity implements ItemParameterFragment.OnListFragmentInteractionListener {

    public ListFragment listFragment;
    public Fragment currentFragment;
    private int checkedItem = 0;


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

        if (id == R.id.action_settings) {
            showSettingsDialog(MainActivity.this);
        }


        if (id == R.id.load_sketch_button) {
            showPersistenceDialog(MainActivity.this, true);
        }

        if (id == R.id.save_sketch_button) {
            EditText edit = (EditText) listFragment.getMainView().findViewById(R.id.fileName);
            String fileName = edit.getText().toString();
            showPersistenceDialog(MainActivity.this, false);
        }
        if (id == R.id.run_sketch_button) {
            UiHelper.writeCommand("Executing sketch...");
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {

                        listFragment.executeBricks();
                        printCurrentVariables();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            thread.start();


        }

        if (id == R.id.debug_sketch_button) {

            UiHelper.writeCommand("Debugging sketch...");
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {

                        listFragment.debugBricks();
                        printCurrentVariables();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            thread.start();

        }

        return super.onOptionsItemSelected(item);
    }

    public void printDebugg(String message) {
        UiHelper.writeCommand(message);
    }

    public void printCurrentVariables() {

        UiHelper.writeCommand(Html.fromHtml(BrickHelper.getInstance().getCurrentVariablesFormatted()).toString());


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        ListFragment listF = ListFragment.newInstance();
        showListFragment(listF);

        boolean needToInitializeBluetooth = true;
        if (savedInstanceState != null) {
            ArrayList<Brick> backendBricks = BrickPersister.loadSketchFromJson(savedInstanceState.getString("bricks"));
            ArrayList<Pair<Long, Brick>> bricks = BrickPersister.translateBackendBricksToUiBricks(backendBricks);
            needToInitializeBluetooth = savedInstanceState.getBoolean("needToInitializeBT");


            if (bricks != null) {
                listF.setmItemArrayInital(bricks);
            }
        }

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_color)));
        BrickPersister.saveStandardSketch(getApplicationContext());

        final BrickCommunicator brickCommunicator = BrickCommunicator.getInstance();



        if (needToInitializeBluetooth) {
            brickCommunicator.initiateBluetooth(this);
        }



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

        }
    }

    public void showListProgFragment() {
        showFragment(listFragment);
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, "fragment");
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
        currentFragment = fragment;
    }

    public void showListFragment(Fragment fragment) {
        listFragment = (ListFragment) fragment;

        showFragment(listFragment);
    }

    public void onListFragmentInteraction(Parameter uri) {
        //you can leave it empty
    }




    public ArrayList<Pair<Long, Brick>> getCertainSketch(String fileName) {


        String standardJSON = BrickPersister.readJsonFile(getApplicationContext(), Constants.SKETCHES_FOLDER,
                fileName);
        ArrayList<Brick> brickList = BrickPersister.loadSketchFromJson(standardJSON);

        ArrayList<Pair<Long, Brick>> brickPairs = BrickPersister.translateBackendBricksToUiBricks(brickList);

        return brickPairs;
    }


    private void showPersistenceDialog(Context c, final boolean load) {

        String title;
        String positiveButtonText;


        final EditText taskEditText = new EditText(c);
        taskEditText.setText(Constants.EXAMPLE_SKETCH);
        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        if (load) {
            title = Constants.LOAD_TITLE;

            positiveButtonText = Constants.LOAD_BUTTON;
        } else {
            title = Constants.SAVE_TITLE;
            positiveButtonText = Constants.SAVE_BUTTON;
            builder.setMessage(Constants.SAVE_ENTER_FILE);
            builder.setView(taskEditText);
        }

        builder.setTitle(title);

        final String[] internalSketches = BrickPersister.getInternalSketches(getApplicationContext(), Constants.SKETCHES_FOLDER);

        checkedItem = 0;
        builder.setSingleChoiceItems(internalSketches, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkedItem = which;
            }
        });

        builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (load) {
                    if(internalSketches.length > 0) {
                        listFragment.setmItemArray(getCertainSketch(internalSketches[checkedItem]));
                    }

                } else {
                    String fileName = String.valueOf(taskEditText.getText());
                    ArrayList<Pair<Long, Brick>> brickList = listFragment.getmItemArray();

                    ArrayList<Brick> translatedBricks = BrickHelper.getInstance().translateUiBricksToBackendBricks(brickList);

                    BrickPersister.writeJsonToFile(getApplicationContext(), Constants.SKETCHES_FOLDER, fileName, BrickPersister.translateSketchToJson(translatedBricks));
                }

            }
        });
        builder.setNegativeButton(Constants.CANCEL_BUTTON, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void showSettingsDialog(final Activity a) {

        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        builder.setTitle(Constants.SETTINGS_TITLE);

        final EditText taskEditText = new EditText(a);
        taskEditText.setText(BrickCommunicator.getInstance().getBluetoothDeviceName());

        LinearLayout layout = new LinearLayout(a);
        layout.setOrientation(LinearLayout.VERTICAL);
        final TextView textView = new TextView(a);
        textView.setText("Bluetooth Device name");
        layout.addView(textView);
        layout.addView(taskEditText);

        builder.setView(layout);

        builder.setPositiveButton(Constants.RELOAD_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                BrickCommunicator.getInstance().disconnect();
                BrickCommunicator.getInstance().stop();
                BrickCommunicator.getInstance().initiateBluetooth(a, taskEditText.getText().toString());

            }
        });
        builder.setNegativeButton(Constants.CANCEL_BUTTON, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Make sure to call the super method so that the states of our views are saved
        super.onSaveInstanceState(outState);
        // Save our own state now
        ArrayList<Brick> bricks = BrickHelper.getInstance().translateUiBricksToBackendBricks(listFragment.getmItemArray());
        outState.putString("bricks", BrickPersister.translateSketchToJson(bricks));

        outState.putBoolean("needToInitializeBT", false);
    }




}