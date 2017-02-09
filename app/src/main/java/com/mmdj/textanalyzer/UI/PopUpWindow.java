package com.mmdj.textanalyzer.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mmdj.textanalyzer.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class PopUpWindow {
    private  Context context;
    private  Activity activity;
    private  String message;


    public PopUpWindow(Context context, Activity activity, String message) {
        this.context = context;
        this.activity = activity;
        this.message = message;
    }

    public Context getContext() {
        return context;
    }

    public PopUpWindow setContext(Context context) {
        this.context = context;
        return this;
    }

    public Activity getActivity() {
        return activity;
    }

    public PopUpWindow setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public PopUpWindow setMessage(String message) {
        this.message = message;
        return this;
    }

    public void doPopUpWindow() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_help, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        //Shadow:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            popupWindow.setElevation(10f);
        }
        // popupWindow.setTouchable(true);
        // popupWindow.setFocusable(true);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        TextView txtVwPopupText = (TextView) popupView.findViewById(R.id.txtVw_popup_text);
        txtVwPopupText.setText(message);

        Button btnDismiss = (Button) popupView.findViewById(R.id.btn_popup_dismiss);
        btnDismiss.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        //popupWindow.showAsDropDown(findViewById(id), -30, -30);

    }
}
