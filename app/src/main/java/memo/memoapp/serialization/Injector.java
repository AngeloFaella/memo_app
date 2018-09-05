package memo.memoapp.serialization;

import android.content.Context;

/**
 * Created by Angelo Faella
 */

public class Injector {

    public static NoteDAO getNoteDAO(Context context){
        return LocalNoteDAO.getInstance(context);
    }
}
