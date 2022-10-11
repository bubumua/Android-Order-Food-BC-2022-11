package com.example.Android_bigWork.Dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.example.Android_bigWork.R;
import com.example.Android_bigWork.Utils.DensityUtil;

public class MyDialog {
    CustomDialog dialog;
    View view; // dialog布局
    View animView; // 商品菜单布局
    View dismissView; // 半透明遮罩布局
    Context context;

    public MyDialog(Context context) {
        this.context = context;
        dialog = new CustomDialog(context, R.style.myDialog);
        // dialog的样式
        view = LayoutInflater.from(context).inflate(R.layout.dialog, null);
        animView = view.findViewById(R.id.v_anim);
        dialog.setContentView(view);

        dismissView = view.findViewById(R.id.v_dimiss);
        dismissView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setBeforeDismiss(new CustomDialog.IBeforeDismiss() {
            @Override
            public void onBeforeDismiss() {
                dismissAnim();
            }
        });


        // 设置dialog的位置
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);//背景透明，不然会有个白色的东东
//        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); //不使用窗口弹出动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度
        lp.height = WindowManager.LayoutParams.MATCH_PARENT; // 高度
        dialogWindow.setAttributes(lp);
        // 设置dialog为底部
        dialogWindow.setGravity(Gravity.BOTTOM);
    }

    public void show() {
        dialog.show();
        showAnim();
    }

    // 出现动画
    private void showAnim() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, DensityUtil.dp2px(context, 300), 0);
        animation.setDuration(300);
        animView.startAnimation(animation);
    }

    // 消失动画
    private void dismissAnim() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, DensityUtil.dp2px(context, 300));
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dialog.myDismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setDuration(300);
        animView.startAnimation(animation);
    }

}