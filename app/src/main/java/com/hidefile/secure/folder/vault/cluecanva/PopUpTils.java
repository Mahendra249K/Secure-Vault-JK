package com.hidefile.secure.folder.vault.cluecanva;

import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hidefile.secure.folder.vault.R;

public class PopUpTils {

    public static void showDialogWithIcon(Context mContext, String title, String description, String positiveBtnText, String negativeBtnText, Boolean isCheckTrashCan) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialog);
        View dialogView = View.inflate(mContext, R.layout.pop_sure_dlt, null);
        AppCompatTextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        AppCompatTextView tvDescription = dialogView.findViewById(R.id.tvDescription);
        AppCompatButton btnPositive = dialogView.findViewById(R.id.btnPositive);
        AppCompatButton btnNegative = dialogView.findViewById(R.id.btnNegative);
        tvDialogTitle.setText(title);
        tvDescription.setText(description);
        if (isCheckTrashCan) {

        } else {

        }
        btnPositive.setText(positiveBtnText);
        btnNegative.setText(negativeBtnText);

        btnPositive.setOnClickListener(view -> {

        });
        btnNegative.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

    }

}
