package com.vilyever.androidpopupcontroller;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vilyever.popupcontroller.ViewController;
import com.vilyever.popupcontroller.hud.HudController;
import com.vilyever.popupcontroller.popup.PopupController;
import com.vilyever.unitconversion.DimenConverter;

public class MainActivity extends AppCompatActivity {
    final MainActivity self = this;
    
    private PopupController popupController;
    private PopupController popupController2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();


//                self.popupController.popupFromView(self.findViewById(R.id.titleLabel), PopupDirection.Up, true);

//                self.popupController.popupInView(self.getWindow().getDecorView(), PopupDirection.Center);

//                if (!self.popupController.isAppeared()) {
//                    self.popupController.attachToParent((ViewGroup) findViewById(R.id.layout));
//                }
//                else {
//                    self.popupController.detachFromParent();
//                }

//                new SimpleAlertController()
//                        .setTitle("Title标题")
//                        .setMessage("Message 信息")
//                        .setPositiveButtonTitle("确定")
//                        .setNegativeButtonTitle("取消")
//                        .show(view);
//                        .show(self);
//                        .show();

//                self.draggingContainerController.addDraggingChild(self.popupController.getView());



                getHudController().show();
            }
        });

//        new DraggingManager(fab) {
//            @Override
//            protected void onDraggingBegin(View draggingView) {
////                super.onDraggingBegin(draggingView);
//                draggingView.animate().scaleX(2.0f).scaleY(2.0f).start();
//
//            }
//
//            @Override
//            protected void onDraggingEnd(View draggingView) {
//                animateBackToOriginalCoordinateBeforeEndDragging();
//            }
//        };

        self.popupController = new PopupController(self, R.layout.test_view);
        self.popupController.setPopupBackgroundColor(Color.WHITE);
        self.popupController.setEdgePadding(8, 8, 8, 8);
        self.popupController.setEdgeRoundedRadius(20);
        self.popupController.setPopupShadowRadius(5);
        self.popupController.setDirectionArrowHeight(50);
//        self.popupController.setAttachAnimationPerformer(new FadeInAnimationPerformer().setAnimationDirection(AnimationDirection.Left));
//        self.popupController.setDetachAnimationPerformer(new FadeOutAnimationPerformer().setAnimationDirection(AnimationDirection.Right));

//        self.popupController.attachToParent((ViewGroup) findViewById(R.id.layout));

//        draggingContainerController = new DraggingContainerController(this);
//        draggingContainerController.getView().setBackgroundColor(Color.WHITE);
//        draggingContainerController.attachToActivity(this);


//        self.popupController2 = new PopupController(self, R.layout.test_view);
//        self.popupController2.getView().setBackgroundColor(Color.GREEN);
//        draggingContainerController.addDraggingChild(self.popupController2.getView(), new DraggingChildOptions().setAutoAttachNearestEdge(true).setAutoAttachNearestEdgeAnimated(true).setAutoAttachNearestEdgesSide(DraggingChildOptions.EdgeLeft));

    }

    private TextView titleLabel;
    public TextView getTitleLabel() {
        if (this.titleLabel == null) {
            this.titleLabel = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            int margin = DimenConverter.dpToPixel(8);
            layoutParams.setMargins(margin, margin, margin, margin);
            this.titleLabel.setLayoutParams(layoutParams);
            this.titleLabel.setGravity(Gravity.CENTER);
            this.titleLabel.setTypeface(Typeface.DEFAULT_BOLD);
            this.titleLabel.setTextSize(20);
            this.titleLabel.setTextColor(Color.WHITE);
            titleLabel.setText("aabbcc\nccddee");
        }
        return this.titleLabel;
    }

    private HudController hudController;
    protected HudController getHudController() {
        if (this.hudController == null) {
            this.hudController = new HudController();
            hudController.setLeftButtonTitle("ok").dismissOnLeftButton();
            hudController.setRightButtonTitle("cancel").dismissOnRightButton();
            hudController.setCenterButtonTitle("haha");
            hudController.setCustomView(getEditController().getView());
        }
        return this.hudController;
    }

    private ViewController editController;
    protected ViewController getEditController() {
        if (this.editController == null) {
            this.editController = new ViewController(this, R.layout.test_edit_layout);
        }
        return this.editController;
    }
}
