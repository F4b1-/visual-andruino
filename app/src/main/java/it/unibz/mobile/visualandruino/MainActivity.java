package it.unibz.mobile.visualandruino;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;
import it.unibz.mobile.visualandruino.dummy.DummyContent;
import it.unibz.mobile.visualandruino.models.Parameter;


import android.support.v7.app.AppCompatActivity;




public class MainActivity extends AppCompatActivity implements ItemParameterFragment.OnListFragmentInteractionListener {


    ViewGroup _root;
    //private int _xDelta;
    //private int _yDelta;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        showFragment(ListFragment.newInstance());



        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_color)));

    }


    public void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, "fragment").commit();
    }

    public void onListFragmentInteraction(Parameter uri){
        //you can leave it empty
    }



}