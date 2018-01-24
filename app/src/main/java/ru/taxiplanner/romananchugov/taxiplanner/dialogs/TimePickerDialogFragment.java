package ru.taxiplanner.romananchugov.taxiplanner.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import ru.taxiplanner.romananchugov.taxiplanner.OrderItem;

/**
 * Created by romananchugov on 21.01.2018.
 */

@SuppressLint("ValidFragment")
public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "TimePickerDialogFragment";
    private OrderItem orderItem;
    private TextView textView;

    public TimePickerDialogFragment(OrderItem orderItem, TextView textView){
        this.orderItem = orderItem;
        this.textView = textView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int minutes, int hours) {
        orderItem.setTime(hours, minutes);
        textView.setText(orderItem.getTime());
    }
}
