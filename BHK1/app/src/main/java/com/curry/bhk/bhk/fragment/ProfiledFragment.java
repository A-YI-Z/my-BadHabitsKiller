package com.curry.bhk.bhk.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.activity.BaseActivity;
import com.curry.bhk.bhk.activity.LoginActivity;
import com.curry.bhk.bhk.activity.RegistActivity;
import com.curry.bhk.bhk.bean.UserBean;
import com.curry.bhk.bhk.sqlite.UserdbOperator;
import com.curry.bhk.bhk.view.CircleImageView;
import com.curry.bhk.bhk.view.DeleteEditText;
import com.gc.materialdesign.views.ButtonRectangle;

/**
 * Created by Curry on 2016/8/19.
 */
public class ProfiledFragment extends Fragment {

    private View mView;
    private DeleteEditText mUsernameEt;
    private EditText mOldPasswordEt;
    private EditText mNewPasswordEt;
    private EditText mConPasswordEt;
    private CircleImageView mProfiledHead;
    private ButtonRectangle saveProfiledBtn;

    private String mSqlPassword = "";
    private String mInputPassword = "";
    private String mNewPassword = "";
    private String mConfirmPassword = "";

    private UserdbOperator userdbOperator;
    private UserBean userBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mView = inflater.inflate(R.layout.profiled_fragment, null);

        viewInit();

        dataInit();

        return mView;
    }

    private void viewInit() {
        mUsernameEt = (DeleteEditText) mView.findViewById(R.id.profile_et_username);
        mOldPasswordEt = (EditText) mView.findViewById(R.id.profile_et_old_password);
        mNewPasswordEt = (EditText) mView.findViewById(R.id.profile_et_new_password);
        mConPasswordEt = (EditText) mView.findViewById(R.id.profile_et_again_password);
        mProfiledHead = (CircleImageView) mView.findViewById(R.id.profile_img_head);
        saveProfiledBtn = (ButtonRectangle) mView.findViewById(R.id.profile_btn_finish);

        mProfiledHead.setOnClickListener(new ProfiledOnClick());
        saveProfiledBtn.setOnClickListener(new ProfiledOnClick());
    }

    private void dataInit() {
        userdbOperator = new UserdbOperator(getActivity());
        userBean = new UserBean();
        userBean.setUsername(BaseActivity.mUsername);
        String strPictureUrl = userdbOperator.qureyPicture(userBean);
        if (!strPictureUrl.equals("")) {
            if (strPictureUrl.equals("default")) {
                mProfiledHead.setImageResource(R.drawable.defult_img);
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(strPictureUrl);
                bitmap = RegistActivity.rotateBitmapByDegree(bitmap, RegistActivity.getBitmapDegree(strPictureUrl));
                mProfiledHead.setImageBitmap(bitmap);
            }
        }
//        List<UserBean> userbean_list = userdbOperator.queryUser(2, userBean);
//        if (!userbean_list.isEmpty()) {
//            if (userbean_list.get(0).getPic_url().equals("default")) {
//                mProfiledHead.setImageResource(R.drawable.defult_img);
//            } else {
//                Bitmap bitmap = BitmapFactory.decodeFile(userbean_list.get(0).getPic_url());
//                bitmap = RegistActivity.rotateBitmapByDegree(bitmap, RegistActivity.getBitmapDegree(userbean_list.get(0).getPic_url()));
//                mProfiledHead.setImageBitmap(bitmap);
//            }
//        }

        mUsernameEt.setText(BaseActivity.mUsername);
    }

    private class ProfiledOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.profile_btn_finish:

                    saveDialog();
                    break;
                case R.id.profile_img_head:

                    break;
                default:
                    break;
            }
        }

        private void saveDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Your info is changed, would you like to save ?");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    usernameChange();

                }
            });

            builder.create().show();
        }

        /**
         * username is successful and  then  check the password
         */
        private void usernameChange() {
            String newUsername = mUsernameEt.getText().toString();
            if (!BaseActivity.mUsername.equals(newUsername) && !newUsername.equals("")) {

                userBean.setEmail(BaseActivity.mEmail);
                userBean.setUsername(newUsername);
                userdbOperator.updateUser(userBean);

                BaseActivity.mUsername = newUsername;

                passwordChange();
            }
        }

        private void passwordChange() {

            mSqlPassword = userdbOperator.qureyPassword(userBean);
            mInputPassword = mNewPasswordEt.getText().toString();
            mConfirmPassword = mConPasswordEt.getText().toString();
            mNewPassword = mNewPasswordEt.getText().toString();

            mOldPasswordEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (mInputPassword.equals(mSqlPassword)) {
                        mOldPasswordEt.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.right), null);
                    } else {
                        mOldPasswordEt.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.error), null);
                    }
                }
            });

            String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
            if (!mNewPassword.matches(regex)) {
                new BaseActivity().toastSomething(getActivity(), "The password is wrong.");
            } else if (mConfirmPassword.equals(mNewPassword)) {

                userBean.setEmail(BaseActivity.mEmail);
                userBean.setPassword(mConfirmPassword);
                userdbOperator.updateUser(userBean);

                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            } else {
                new BaseActivity().toastSomething(getActivity(), "Confirm password is wrong!");
            }
        }
    }
}