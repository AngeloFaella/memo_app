package memo.memoapp.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import memo.memoapp.R;
import memo.memoapp.controller.AddNoteActivity;
import memo.memoapp.model.Category;
import memo.memoapp.model.Note;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Angelo Faella
 */

public class AddNoteView extends AbstractView {


    private static final String H1 = "h1";
    private static final String BOLD = "b";
    private static final String ITALIC = "i";

    private static final String MODE_REVEAL = "REVEAL";
    private static final String MODE_HIDE = "HIDE";

    private static final int MODE_NEW = 0;
    private static final int MODE_EDIT = 1;
    private int currentMode;
    private boolean hasChanged = false;

    private AddNoteActivity controller;
    private EditText title;
    private EditText content;
    private Note note;

    public AddNoteView(LayoutInflater inflater, AppCompatActivity controller) {
        super(inflater, controller);
        this.controller = (AddNoteActivity) controller;
        currentMode = MODE_NEW;
        init();
    }

    public AddNoteView(LayoutInflater inflater, AppCompatActivity controller, Note note) {
        super(inflater, controller);
        this.controller = (AddNoteActivity) controller;
        this.note = note;
        currentMode = MODE_EDIT;
        init();

        // fill note
        title.setText(note.getTitle());
        content.setText(note.getContent());
    }

    private void init(){
        root = inflater.inflate(R.layout.activity_add_note,null);

        // init Toolbar
        final Toolbar myToolbar=(Toolbar)root.findViewById(R.id.toolbar_new_note);
        controller.setSupportActionBar(myToolbar);
        controller.getSupportActionBar().setDisplayShowTitleEnabled(false);
        // activity title
        TextView name = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        name.setText(R.string.add_note);
        // on back pressed
        View back = myToolbar.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                safeExit();
            }
        });

        // title & content
        title = (EditText) root.findViewById(R.id.note_title);
        content = (EditText) root.findViewById(R.id.note_content);

        // speech recognition
        View speech = root.findViewById(R.id.shortcut_speech);
        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });

        // text formatting
        final View formatLayout = root.findViewById(R.id.text_shortcut);
        final View format = root.findViewById(R.id.shortcut_format);
        format.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] c = new int[2];
                format.getLocationOnScreen(c);
                // show html shortcuts
                circleAnimation(c[0],c[1], MODE_REVEAL,formatLayout);
            }
        });
        // add reminder
        View reminder = root.findViewById(R.id.shortcut_notification);
        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! title.getText().toString().isEmpty())
                    controller.goToNotificationView(title.getText().toString());
                else
                    showAlert(controller.getString(R.string.warning),
                            controller.getString(R.string.alert_note_title));
            }
        });

        // finish note
        View done = root.findViewById(R.id.shortcut_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terminate();
            }
        });

        //set up html shortcuts
        initHTMLshortcuts(formatLayout);
    }

    private void initHTMLshortcuts(final View formatLayout){
        // bold
        View bold = root.findViewById(R.id.shortcut_bold);
        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formatHTML(BOLD);
            }
        });

        // italic
        View italic = root.findViewById(R.id.shortcut_italic);
        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formatHTML(ITALIC);
            }
        });

        // heading
        final View heading = root.findViewById(R.id.shortcut_heading);
        heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formatHTML(H1);
            }
        });

        // done
        final View done = root.findViewById(R.id.shortcut_done_format);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] c = new int[2];
                heading.getLocationOnScreen(c);
                // hide html shortcuts
                circleAnimation(c[0],c[1], MODE_HIDE,formatLayout);
            }
        });
    }

    private boolean hasChanges() {
        return (currentMode != MODE_NEW &&
                !(note.getTitle().equals(title.getText().toString())
                        && note.getContent().equals(content.getText().toString())) )
                || hasChanged;
    }

    public void setChanged(boolean hasChanged){this.hasChanged = hasChanged;}

    private void terminate(){

        if(title.getText().toString().isEmpty()) {
            showAlert(controller.getString(R.string.warning),
                    controller.getString(R.string.alert_note_title));
            return;
        }

        // create note
        note = new Note(title.getText().toString(),new Date(),content.getText().toString(), Category.DEFAULT_CATEGORY);
        // save note
        if(controller.saveOrUpdateNote(note)) {
            // finish
            controller.finish();
        }else{
            // error, note already exists
            showAlert(controller.getString(R.string.error),
                    controller.getString(R.string.alert_note_already_exists));
        }
    }

    public void safeExit() {
        if(currentMode == MODE_NEW || hasChanges()){
            askConfirm();
        }else{
            controller.finish();
        }
    }

    private void askConfirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(controller);

        // message
        builder.setTitle(R.string.warning);
        if(currentMode == MODE_NEW) {
            builder.setMessage(R.string.alert_save_note);
        }else{
            builder.setMessage(R.string.alert_save_changes);
        }

        // on click listeners
        builder.setPositiveButton(R.string.answer_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                terminate();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controller.finish();
            }
        });

        builder.create().show();
    }

    private void circleAnimation(int cx, int cy, String mode, final View view){
        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(cx, cy);
        float initialRadius = 0f;

        //default mode is REVEAL
        int visibility = View.VISIBLE;
        if(mode.equals(MODE_HIDE)){
            initialRadius = finalRadius;
            finalRadius = 0;
            visibility = View.INVISIBLE;
        }

        // Check if the runtime version is at least Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // create the animator for this view (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, finalRadius);
            anim.setDuration(300);

            if(mode.equals(MODE_HIDE)) {
                // make the view invisible when the animation is done
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.INVISIBLE);
                    }
                });
            }else {
                // make the view visible and start the animation
                view.setVisibility(visibility);
            }
            anim.start();
        }else{
            view.setVisibility(visibility);
        }
    }

    private void formatHTML(String tag){
        // get selected text
        String selectedText = content.getText().toString()
                .substring(content.getSelectionStart(), content.getSelectionEnd());
        if(selectedText.trim().isEmpty()) return;

        // save string portions
        CharSequence contentHtml = content.getText();
        CharSequence start = contentHtml.subSequence(0,content.getSelectionStart());
        CharSequence end = "";
        Log.e(LOG_VIEW,"len: "+content.getText().length()+" e "+contentHtml.length());
        if(content.getSelectionEnd() < content.getText().length())
            end = contentHtml.subSequence(content.getSelectionEnd()+1,content.getText().length());


        // create html heading
        Spanned spannedHtml = Html.fromHtml("<"+tag+">"+selectedText+"</"+tag+">");
        CharSequence html = spannedHtml.subSequence(0,spannedHtml.length());  // remove '\n'
        CharSequence result = TextUtils.concat(start,html," ",end);
        contentHtml = new SpannableString(result);

        // build result string
        content.setText(contentHtml);
    }


    private void startVoiceInput() {
        // set options of intent
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, controller.getString(R.string.speech_note));

        try {
            /// start listening
            controller.startActivityForResult(intent, AddNoteActivity.REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            a.printStackTrace();
            Log.e("Memo",a.getMessage());
        }
    }

    public void setSpeechInputResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case AddNoteActivity.REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && data != null) {
                    // results of speech input
                    List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // String in position 0 is the most accurate
                    content.append(result.get(0));
                }
                break;
            }

        }
    }
}
