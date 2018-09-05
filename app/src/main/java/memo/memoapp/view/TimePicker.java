package memo.memoapp.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

import memo.memoapp.R;

/**
 * Created by Angelo Faella
 */

public class TimePicker extends DialogFragment {


    private TimePickerDialog.OnTimeSetListener listener;

    public void setTimeListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog tpd=new TimePickerDialog(getActivity(), R.style.PickerTheme, listener,
                hour, minute, DateFormat.is24HourFormat(getActivity()));
        tpd.setTitle(getString(R.string.set_time));

        return tpd;
    }
}
