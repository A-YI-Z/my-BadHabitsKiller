package com.curry.bhk.bhk.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.activity.BaseActivity;
import com.curry.bhk.bhk.activity.LoginActivity;
import com.curry.bhk.bhk.bean.UserBean;
import com.curry.bhk.bhk.sqlite.UserdbOperator;
import com.curry.bhk.bhk.utils.CheckBitmapDegree;
import com.curry.bhk.bhk.utils.PublicStatic;
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
    private TextView mChangePasswordTv;
    private RelativeLayout mRelativeLayout;

    private String mSqlPassword = "";
    private String mInputPassword = "";
    private String mNewPassword = "";
    private String mConfirmPassword = "";
    private String mProfiledHeadUrl = "";
    private boolean mIsVisible = true;

    private UserdbOperator userdbOperator;
    private UserBean userBean;
    private SharedPreferences.Editor edit;

    private boolean usernameIsChanged = false;
    private boolean passwordIsChanged = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.profiled_fragment, null);
        viewInit();
        dataInit();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mProfiledHeadView.setOnClickListener(new ProfiledOnClick());
        mChangePasswordTv.setOnClickListener(new ProfiledOnClick());
        saveProfiledBtn.setOnClickListener(new ProfiledOnClick());
        mUsernameEt.setOnClickListener(new ProfiledOnClick());
    }

    private void viewInit() {
        mUsernameEt = (DeleteEditText) mView.findViewById(R.id.profile_et_username);
        mOldPasswordEt = (EditText) mView.findViewById(R.id.profile_et_old_password);
        mNewPasswordEt = (EditText) mView.findViewById(R.id.profile_et_new_password);
        mConPasswordEt = (EditText) mView.findViewById(R.id.profile_et_again_password);
        mProfiledHeadView = (CircleImageView) mView.findViewById(R.id.profile_img_head);
        saveProfiledBtn = (ButtonRectangle) mView.findViewById(R.id.profile_btn_finish);
        mChangePasswordTv = (TextView) mView.findViewById(R.id.profile_tv);
        mRelativeLayout = (RelativeLayout) mView.findViewById(R.id.relativeLayout);
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
        mProfiledHeadUrl = BaseActivity.mHeadUrl;

        edit = getActivity().getSharedPreferences(PublicStatic.SHAREDPREFERENCES_USER_BHK, 0).edit();

        userdbOperator = new UserdbOperator(getActivity());
        userBean = new UserBean();
        userBean.setEmail(BaseActivity.mEmail);
    }

    private class ProfiledOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.profile_btn_finish:
                    if (!userNameIsChanged() && mProfiledHeadUrl.equals(BaseActivity.mHeadUrl)
                            && mRelativeLayout.getVisibility() == View.INVISIBLE) {
                        Toast.makeText(getActivity(), "There is no change!", Toast.LENGTH_SHORT).show();
                        mUsernameEt.setText(BaseActivity.mUsername);
                        mUsernameEt.setClearIconVisible(false);
                        mUsernameEt.setCursorVisible(false);
                    } else {
                        passwordIsRight();
                    }
                    break;
                case R.id.profile_img_head:
                    choose_head_img();
                    break;
                case R.id.profile_tv:
                    if (mIsVisible) {
                        mRelativeLayout.setVisibility(View.VISIBLE);
                        mIsVisible = false;
                    } else {
                        mRelativeLayout.setVisibility(View.INVISIBLE);
                        mIsVisible = true;
                    }

                    break;
                case R.id.profile_et_username:
                    mUsernameEt.setClearIconVisible(true);
                    mUsernameEt.setCursorVisible(true);
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

                if (mRelativeLayout.getVisibility() == View.VISIBLE) {
                    updatePassword();
                } else {
                    saveUserName();
                    saveHeadUrl();
                }
                sendBroadcast();
            }
        });
        builder.create().show();
        
        mUsernameEt.setClearIconVisible(false);
        mUsernameEt.setCursorVisible(false);
    }

    private boolean userNameIsChanged() {
        String nowUsername = mUsernameEt.getText().toString();
        if (nowUsername.equals(BaseActivity.mUsername) || nowUsername.equals("")) {
            usernameIsChanged = false;
        } else {
            usernameIsChanged = true;
        }
        return usernameIsChanged;
    }

    /**
     * username is successful and  then  check the password
     */
    private void saveUserName() {
        String newUsername = mUsernameEt.getText().toString();

        userBean.setUsername(newUsername);
        userdbOperator.updateUser(userBean, 0);

        BaseActivity.mUsername = newUsername;

        edit.putString(PublicStatic.SHAREDPREFERENCES_USERNAME, newUsername);
        edit.commit();
    }

    private void passwordIsRight() {
        mSqlPassword = userdbOperator.qureyPassword(userBean);

        mInputPassword = mNewPasswordEt.getText().toString();
        mConfirmPassword = mConPasswordEt.getText().toString();
        mNewPassword = mNewPasswordEt.getText().toString();

        if (mRelativeLayout.getVisibility() == View.VISIBLE) {
            if (mInputPassword.equals("") || mInputPassword == null
                    || mConfirmPassword.equals("") || mConfirmPassword == null
                    || mNewPassword.equals("") || mNewPassword == null) {
//                passwordIsChanged = false;

                Toast.makeText(getActivity(), "Please fill out completely.", Toast.LENGTH_LONG).show();
            } else {
                String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
                if (!mInputPassword.equals(mSqlPassword)) {
                    Toast.makeText(getActivity(), "The original password is wrong.", Toast.LENGTH_LONG).show();
                } else if (!mNewPassword.matches(regex)) {
                    Toast.makeText(getActivity(), "The password type is wrong.", Toast.LENGTH_LONG).show();
                } else if (!mConfirmPassword.equals(mNewPassword)) {
                    Toast.makeText(getActivity(), "Confirm password is wrong!", Toast.LENGTH_SHORT).show();
                } else {
                    saveDialog();
                }
            }
        } else {
            saveDialog();
        }

    }

    private void updatePassword() {

        saveUserName();

        saveHeadUrl();

        userBean.setEmail(BaseActivity.mEmail);
        userBean.setPassword(mConfirmPassword);
        userdbOperator.updateUser(userBean, 1);

        edit.putBoolean(PublicStatic.SHAREDPREFERENCES_CHECKBOX, false);
        edit.commit();

        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
//        } else {
//            Toast.makeText(getActivity(), "Confirm password is wrong!", Toast.LENGTH_SHORT).show();
//        }
    }

    private void saveHeadUrl() {

        BaseActivity.mHeadUrl = mProfiledHeadUrl;

        userBean.setPic_url(mProfiledHeadUrl);

        userdbOperator.updateUser(userBean, 2);

    }

    /**
     * choose head image
     */
    private void choose_head_img() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose a picture .");
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

        if (!mProfiledHeadUrl.equals(BaseActivity.mHeadUrl) && !mProfiledHeadUrl.equals("")) {
//            saveProfiledBtn.setOnClickListener(new ProfiledOnClick());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void sendBroadcast() {

        Intent intent = new Intent();

        intent.setAction("CHANGE");
        getActivity().sendBroadcast(intent);

    }
}
