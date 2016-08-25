package com.curry.bhk.bhk.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.curry.bhk.bhk.bean.UserBean;
import com.curry.bhk.bhk.sqlite.UserdbOperator;
import com.curry.bhk.bhk.utils.CheckBitmapDegree;
import com.curry.bhk.bhk.utils.SavePicture;
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
    private CircleImageView mProfiledHeadView;
    private ButtonRectangle saveProfiledBtn;

    private String mSqlPassword = "";
    private String mInputPassword = "";
    private String mNewPassword = "";
    private String mConfirmPassword = "";
    private String mProfiledHeadUrl = "";

    private UserdbOperator userdbOperator;
    private UserBean userBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mView = inflater.inflate(R.layout.profiled_fragment, null);

        viewInit();

        dataInit();

        profiledOnClick();

        return mView;
    }

    private void viewInit() {
        mUsernameEt = (DeleteEditText) mView.findViewById(R.id.profile_et_username);
        mOldPasswordEt = (EditText) mView.findViewById(R.id.profile_et_old_password);
        mNewPasswordEt = (EditText) mView.findViewById(R.id.profile_et_new_password);
        mConPasswordEt = (EditText) mView.findViewById(R.id.profile_et_again_password);
        mProfiledHeadView = (CircleImageView) mView.findViewById(R.id.profile_img_head);
        saveProfiledBtn = (ButtonRectangle) mView.findViewById(R.id.profile_btn_finish);

    }

    private void dataInit() {

        if (!BaseActivity.mHeadUrl.equals("")) {
            if (BaseActivity.mHeadUrl.equals("default")) {
                mProfiledHeadView.setImageResource(R.drawable.defult_img);
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(BaseActivity.mHeadUrl);
                bitmap = CheckBitmapDegree.rotateBitmapByDegree(bitmap, CheckBitmapDegree.getBitmapDegree(BaseActivity.mHeadUrl));
                mProfiledHeadView.setImageBitmap(bitmap);
            }
        }

        mUsernameEt.setText(BaseActivity.mUsername);
    }

    private void profiledOnClick() {
        mProfiledHeadView.setOnClickListener(new ProfiledOnClick());
        saveProfiledBtn.setOnClickListener(new ProfiledOnClick());
    }

    private class ProfiledOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.profile_btn_finish:

                    saveDialog();
                    break;
                case R.id.profile_img_head:
                    choose_head_img();
                    break;
                default:
                    break;
            }
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
        userdbOperator = new UserdbOperator(getActivity());
        userBean = new UserBean();
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

    /**
     * choose head image
     */
    private void choose_head_img() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose head-image");
        String items[] = {"Photo album", "Take a photo"};
        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intentPick = new Intent(Intent.ACTION_PICK, null);
                                intentPick.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intentPick, 0);
                                break;
                            case 1:
                                Intent intentCapture = new Intent("android.media.action.IMAGE_CAPTURE");
                                startActivityForResult(intentCapture, 1);

                                break;
                            default:
                                break;
                        }
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mProfiledHeadUrl = new SavePicture(getActivity()).pictureResult(requestCode, resultCode, data, mProfiledHeadView);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
