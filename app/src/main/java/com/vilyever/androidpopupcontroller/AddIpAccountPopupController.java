package com.vilyever.androidpopupcontroller;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vilyever.popupcontroller.popup.PopupController;

/**
 * AddIpAccountPopupController
 * ESchoolbag <com.ftet.resourceplaycontrol>
 * Created by su on 2016/5/20.
 * Feature: 多媒体PC-编辑（添加、修改、删除）连接账号界面-添加连接账号弹窗
 */
public class AddIpAccountPopupController extends PopupController {

    /*班级名称编辑框*/
    private EditText mClassNameEt;

    /*IP地址编辑框*/
    private EditText mIpAddressEt;

    /*端口号编辑框*/
    private EditText mPortNumberEt;

    /*保存按钮*/
    private Button mSaveBtn;

    /*保存按钮点击回调接口*/
    OnAddClickListener onAddClickListener;


    public AddIpAccountPopupController(Context context) {
        super(context, R.layout.layout_controller_popup_add_ip_account);
    }

    @Override
    protected void onViewFirstAppeared() {
        super.onViewFirstAppeared();

        initListener();

    }

    /**
     * 初始化监听
     */
    private void initListener() {
        getSaveBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String className = getClassNameEt().getText().toString().trim();
                String ip = getIpAddressEt().getText().toString().trim();
                String port = getPortNumberEt().getText().toString().trim();

                getOnAddClickListener().onAddClick(className, ip, port);
            }
        });

        this.setOnPopupDismissListener(new OnPopupDismissListener() {
            @Override
            public void onPopupWindowDismiss(PopupController controller) {
                /*关闭弹窗，重置清空编辑框内容*/
                getClassNameEt().setText("");
                getIpAddressEt().setText("");
                getPortNumberEt().setText("");
            }
        });

    }

    /**
     * 班级名称getter
     *
     * @return
     */
    public EditText getClassNameEt() {
        if (mClassNameEt == null) {
            mClassNameEt = (EditText) getView().findViewById(R.id.et_class_name_add_ip_account);
        }
        return mClassNameEt;
    }

    /*Ip地址编辑框getter*/
    public EditText getIpAddressEt() {
        if (mIpAddressEt == null) {
            mIpAddressEt = (EditText) getView().findViewById(R.id.et_ip_address_add_ip_account);
        }
        return mIpAddressEt;
    }

    /*端口号编辑框getter*/
    public EditText getPortNumberEt() {
        if (mPortNumberEt == null) {
            mPortNumberEt = (EditText) getView().findViewById(R.id.et_port_number_add_ip_account);
        }
        return mPortNumberEt;
    }

    /*保存按钮getter*/
    public Button getSaveBtn() {
        if (mSaveBtn == null) {
            mSaveBtn = (Button) getView().findViewById(R.id.btn_add_add_ip_account);
        }
        return mSaveBtn;
    }

    /*保存按钮点击回调接口getter*/
    public OnAddClickListener getOnAddClickListener() {
        if (onAddClickListener == null
                ) {
            onAddClickListener = new OnAddClickListener() {
                @Override
                public void onAddClick(String className, String ip, String port) {

                }
            };
        }
        return onAddClickListener;
    }

    /*保存按钮点击回调接口setter*/
    public void setOnAddClickListener(OnAddClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    /*保存按钮点击回调接口*/
    public interface OnAddClickListener {
        void onAddClick(String className, String ip, String port);
    }


}
