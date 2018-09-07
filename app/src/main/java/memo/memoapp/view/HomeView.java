package memo.memoapp.view;

import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import memo.memoapp.R;
import memo.memoapp.controller.HomeActivity;
import memo.memoapp.controller.Service;
import memo.memoapp.model.Note;

/**
 * Created by Angelo Faella
 */

public class HomeView extends AbstractView implements Observer, ItemOnTouchListener,
                                           RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private HomeActivity controller;
    private List<Note> notes;

    private int previousSearch = 0;
    private List<Note> displayedNotes;
    private NoteAdapter adapter;


    public HomeView(LayoutInflater inflater, AppCompatActivity controller, List<Note> notes) {
        super(inflater, controller);
        this.controller = (HomeActivity) controller;
        this.notes = notes;
        displayedNotes = new ArrayList<>(notes);
        init();
    }

    private void init(){
        root = inflater.inflate(R.layout.activity_home,null);

        //init Toolbar & menu
        final Toolbar myToolbar=(Toolbar)root.findViewById(R.id.toolbar_home);
        controller.setSupportActionBar(myToolbar);
        controller.getSupportActionBar().setDisplayShowTitleEnabled(false);
        /*controller.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        controller.getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_menu);*/

        //new note
        FloatingActionButton fab = (FloatingActionButton)root.findViewById(R.id.fab_newNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to AddNote activity
                controller.goToNewNote();
            }
        });

        //display notes
        RecyclerView notesView = (RecyclerView) root.findViewById(R.id.recyclerView_notes);
        adapter = new NoteAdapter(displayedNotes,this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(controller, LinearLayoutManager.VERTICAL, false);
        notesView.setLayoutManager(mLayoutManager);
        notesView.setAdapter(adapter);

        // adding item touch helper
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT
                                                                                       | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(notesView);

        //search function
        search(adapter);
    }

    /**
    *   Real time search for the notes
    * */
    private void search(final NoteAdapter adapter){

        EditText searchValue = (EditText) root.findViewById(R.id.search_input);
        searchValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();
                if(input.length() > previousSearch){
                    filterList(input);
                }else{
                    increaseList(input);
                }
                previousSearch = input.length();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterList(String input){
        input = input.toLowerCase();

        // remove non matching strings
        for(int i=displayedNotes.size()-1; i >= 0 ; i--){
            Note n = displayedNotes.get(i);
            if(! n.getTitle().toLowerCase().contains(input)){
                displayedNotes.remove(i);
                Log.e(Service.LOG_TAG,"filterList: removed "+n);
            }
        }
    }

    private void resetList(){
        displayedNotes.clear();
        displayedNotes.addAll(notes);
        Log.e(Service.LOG_TAG,"Reset list");
    }

    private void increaseList(String input){
        // reset
        if(input.length() == 0){
            resetList();
            return;
        }

        // add matching strings
        for(Note n : notes){
            if(n.getTitle().contains(input)){
                if(! displayedNotes.contains(n)){
                    displayedNotes.add(n);
                    Log.e(Service.LOG_TAG,"IncreaseList(): added "+n);
                }
            }
        }
    }


    /**
     * called when recycler view is swiped
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NoteAdapter.NoteViewHolder) {
            // get the removed item name to display it in snack bar
            String name = displayedNotes.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final Note deletedItem = displayedNotes.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler
            displayedNotes.remove(deletedItem);
            adapter.notifyItemRemoved(deletedIndex);

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(root.findViewById(R.id.home_coordinator), "\""+name+"\" eliminato", Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.cancel, new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    displayedNotes.add(deletedIndex,deletedItem);
                    adapter.notifyItemInserted(deletedIndex);
                }
            });

            // if not restored, remove item permanently
            snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    if(! displayedNotes.contains(deletedItem)){
                        controller.removeNote(deletedItem);
                        Log.e(Service.LOG_TAG,"Removed permanently");
                    }
                }
            });
            snackbar.show();
        }
    }


    /**
     * called when the note list is modified
     */
    @Override
    public void update(Observable o, Object arg) {
        resetList();
        adapter.notifyDataSetChanged();
        Log.e(Service.LOG_TAG,"Home update()");
    }

    @Override
    public void onTouch(int pos) {
        controller.viewNote(displayedNotes.get(pos));
    }

}
