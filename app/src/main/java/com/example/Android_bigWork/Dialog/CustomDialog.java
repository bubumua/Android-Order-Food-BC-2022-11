package com.example.Android_bigWork.Dialog;

import android.app.Dialog;
import android.content.Context;

public class CustomDialog extends Dialog {
    IBeforeDismiss iBeforeDismiss;

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void dismiss(){
        iBeforeDismiss.onBeforeDismiss();
    }

    // 真正让dialog消失
    public void myDismiss() {
        super.dismiss();// dialog消失
    }

    // dismiss前执行
    interface IBeforeDismiss {
        void onBeforeDismiss();
    }

    public void setBeforeDismiss(IBeforeDismiss iBeforeDismiss) {
        this.iBeforeDismiss = iBeforeDismiss;
    }
}