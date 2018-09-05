package memo.memoapp.controller;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import memo.memoapp.R;
import memo.memoapp.serialization.Injector;
import memo.memoapp.view.SplashScreenView;

public class SplashScreenActivity extends AppCompatActivity {

    private SplashScreenView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create view
        view = new SplashScreenView(LayoutInflater.from(this),this);
        setContentView(view.getRoot());
    }

    @Override
    public void onResume(){
        super.onResume();

        // retrieve notes
        Injector.getNoteDAO(this).retrieveAllNotes();

        new CountDownTimer(1500,500){   //1,5 sec of splash screen
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(Service.LOG_TAG,"Countdown : "+millisUntilFinished);
            }
            @Override
            public void onFinish() {
                // start Home activity
                startActivity(new Intent(SplashScreenActivity.this,HomeActivity.class));
                view = null;
                finish();
            }
        }.start();
    }
}
