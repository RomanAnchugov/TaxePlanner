package ru.taxiplanner.romananchugov.taxiplanner.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import ru.taxiplanner.romananchugov.taxiplanner.R;

/**
 * Created by romananchugov on 28.01.2018.
 */

public class NumberOfSeatsDialogFragment extends DialogFragment {
    public static final String EXTRA_NUMBER_OF_SEATS_TAG = "NumberOfSeats";
    private static final String TAG = "NumberOfSeatsDialog";

    private TextView textView;
    private SeekBar seekBar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = getActivity().getLayoutInflater().inflate(R.layout.new_order_number_of_seats, null);

        seekBar = (SeekBar) v.findViewById(R.id.new_order_number_of_seats_seek_bar);
        textView = (TextView) v.findViewById(R.id.new_order_number_of_seats_text_view);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView.setText(i + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Bundle bundle = getArguments();
        if(!bundle.getString(EXTRA_NUMBER_OF_SEATS_TAG).equals("not written")){
            textView.setText(bundle.getString(EXTRA_NUMBER_OF_SEATS_TAG));
            seekBar.setProgress(Integer.parseInt(bundle.getString(EXTRA_NUMBER_OF_SEATS_TAG)));
        }else{
            textView.setText("1");
            seekBar.setProgress(1);
        }

        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_NUMBER_OF_SEATS_TAG, seekBar.getProgress() + "");
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });


        builder.setView(v);
        builder.setTitle("Number of seats");

        return builder.create();
    }
}
