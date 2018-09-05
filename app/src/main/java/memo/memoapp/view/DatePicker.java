package memo.memoapp.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

import memo.memoapp.R;

/**
 * Created by Angelo Faella
 */

public class DatePicker extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;

    // listener to execute when the date is set
    public void setListener(DatePickerDialog.OnDateSetListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        Calendar c = Calendar.getInstance();

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dpd=new DatePickerDialog(getActivity(), R.style.PickerTheme, listener,
                c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));

        //dpd.getDatePicker().setMaxDate(System.currentTimeMillis() - c.getTimeInMillis());

        dpd.setTitle(getString(R.string.set_date));
        return dpd;
    }
}
