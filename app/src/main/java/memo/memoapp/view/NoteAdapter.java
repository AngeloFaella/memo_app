package memo.memoapp.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import memo.memoapp.R;
import memo.memoapp.model.Note;

/**
 * Created by Angelo Faella
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    private List<Note> notes;
    private ItemOnTouchListener onClickListener;

    public NoteAdapter(List<Note> notes, ItemOnTouchListener onClickListener) {
        this.notes = notes;
        this.onClickListener = onClickListener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new NoteViewHolder(item);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = notes.get(position);

        // On Click Listener
        final int pos = position;
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onTouch(pos);
            }
        });

        // Attributes
        holder.title.setText(note.getTitle());
        holder.date.setText(note.getDateFormat());
        // bell
        if(note.hasReminder()) holder.bell.setVisibility(View.VISIBLE);
        else  holder.bell.setVisibility(View.INVISIBLE);

        // pin?
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    static class NoteViewHolder extends RecyclerView.ViewHolder{

        View item;
        View foreground;
        TextView title, date;
        ImageView pin,bell;

         NoteViewHolder(View itemView) {
            super(itemView);

            item = itemView;
            foreground = item.findViewById(R.id.foreground_layout);
            title = (TextView) item.findViewById(R.id.title);
            date = (TextView) item.findViewById(R.id.date);
            pin = (ImageView) item.findViewById(R.id.pin);
            bell = (ImageView) item.findViewById(R.id.bell_icon);
        }
    }
}
