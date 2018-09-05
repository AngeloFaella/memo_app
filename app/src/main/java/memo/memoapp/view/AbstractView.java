package memo.memoapp.view;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Angelo Faella
 */

public abstract class AbstractView {

    public static final String LOG_VIEW = "Memo View";

    protected View root;
    protected AppCompatActivity controller;
    protected LayoutInflater inflater;

    public AbstractView(LayoutInflater inflater, AppCompatActivity controller) {
        this.inflater = inflater;
        this.controller = controller;
    }

    public View getRoot(){
        return root;
    }

    void showAlert(String title, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(controller);

        builder.setTitle(title);

        builder.setMessage(msg);

        builder.setPositiveButton("Ok",null);

        builder.create().show();
    }
}
