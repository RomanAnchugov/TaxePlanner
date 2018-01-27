package ru.taxiplanner.romananchugov.taxiplanner.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ru.taxiplanner.romananchugov.taxiplanner.R;

/**
 * Created by romananchugov on 24.01.2018.
 */

public class DescriptionDialogFragment extends DialogFragment {
    public static final String EXTRA_DESCRIPTION_TAG = "Description";

    private EditText descriptionEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.new_order_description, null);
        Bundle bundle = getArguments();

        //GET EXISTING DESCRIPTION
        descriptionEditText = (EditText) v.findViewById(R.id.new_order_description_edit_text);
        if(!bundle.getString(EXTRA_DESCRIPTION_TAG).equals("not written")) {
            descriptionEditText.setText(bundle.getString(EXTRA_DESCRIPTION_TAG));
        }else{
            descriptionEditText.setText("");
        }

        builder.setView(v);
        builder.setTitle("Description");
        //OK CLICK
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //SETTING NEW DESCRIPTION
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DESCRIPTION_TAG, descriptionEditText.getText().toString());
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        });//OK CLICK

        //CANCEL CLICK
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });//CANCEL CLICK

        return builder.create();
    }

}
