package com.longtraidep.imagegallery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;

public class CreateAlbumDialog extends AppCompatDialogFragment {
    private EditText mPassword;
    private EditText mName;
    private CreateAlbumDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_album_dialog, null);

        builder.setView(view).setTitle("Create album").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = mName.getText().toString();
                String password = mPassword.getText().toString();
                mListener.applyNamePassword(name, password);
            }
        });

        mName = (EditText) view.findViewById(R.id.enter_name);
        mPassword = (EditText) view.findViewById(R.id.enter_password);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateAlbumDialogListener) context;
    }

    public interface CreateAlbumDialogListener
    {
        void applyNamePassword(String name, String password);
    }
}
