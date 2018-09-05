package memo.memoapp.view;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import memo.memoapp.R;

/**
 * Created by Angelo Faella
 */

public class SplashScreenView extends AbstractView {

    public SplashScreenView(LayoutInflater inflater, AppCompatActivity activity) {
        super(inflater,activity);
        init();
    }

    private void init() {
        root = inflater.inflate(R.layout.activity_splash_screen,null);

        // Hide the status bar.
        View decorView = controller.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
