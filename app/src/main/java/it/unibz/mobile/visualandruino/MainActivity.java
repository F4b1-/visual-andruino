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


import android.support.v7.app.AppCompatActivity;




public class MainActivity extends AppCompatActivity {


    ViewGroup _root;
    //private int _xDelta;
    //private int _yDelta;



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

    }


    private void showFragment(Fragment fragment) {
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
}