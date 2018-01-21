package ru.taxiplanner.romananchugov.taxiplanner.pickers;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import ru.taxiplanner.romananchugov.taxiplanner.OrderItem;

/**
 * Created by romananchugov on 20.01.2018.
 */

@SuppressLint("ValidFragment")
public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private final static String TAG = "DatePickerDialogFragment";
    private OrderItem orderItem;
    private TextView textView;

    @SuppressLint("ValidFragment")
    public DatePickerDialogFragment(OrderItem orderItem, TextView textView){
        this.orderItem = orderItem;
        this.textView = textView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        //date that chosen
        orderItem.setDate(year, month + 1, day);
        textView.setText(orderItem.getDate());
    }
}
