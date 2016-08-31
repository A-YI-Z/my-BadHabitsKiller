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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.curry.bhk.bhk.R;
import com.curry.bhk.bhk.activity.BaseActivity;
import com.curry.bhk.bhk.activity.LoginActivity;
import com.curry.bhk.bhk.bean.UserBean;
import com.curry.bhk.bhk.sqlite.UserdbOperator;
import com.curry.bhk.bhk.utils.ImageDisplayer;
import com.curry.bhk.bhk.utils.PublicStatic;
import com.curry.bhk.bhk.utils.SavePicture;
import com.curry.bhk.bhk.view.CircularImageView;
import com.curry.bhk.bhk.view.DeleteEditText;
import com.gc.materialdesign.views.ButtonRectangle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Curry on 2016/8/19.
 */
public class ProfiledFragment extends Fragment {

    private View mView;
    private DeleteEditText mUsernameEt;
    private EditText mOldPasswordEt;
    private EditText mNewPasswordEt;
    private EditText mConPasswordEt;
    private CircularImageView mProfiledHeadView;
    private ButtonRectangle saveProfiledBtn;
    private RelativeLayout mRelativeLayout;
    private ImageView mOpenPassword;

    private String mInputPassword = "";
    private String mNewPassword = "";
    private String mConfirmPassword = "";
    private String mProfiledHeadUrl = "";
    private boolean mIsVisible = true;

    private UserdbOperator userdbOperator;
    private SharedPreferences.Editor edit;

    private boolean usernameIsChanged = false;

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
        mOpenPassword.setOnClickListener(new ProfiledOnClick());
        saveProfiledBtn.setOnClickListener(new ProfiledOnClick());
        mUsernameEt.setOnClickListener(new ProfiledOnClick());
    }

    private void viewInit() {
        mUsernameEt = (DeleteEditText) mView.findViewById(R.id.profile_et_username);

        mProfiledHeadView = (CircularImageView) mView.findViewById(R.id.profile_img_head);
        saveProfiledBtn = (ButtonRectangle) mView.findViewById(R.id.profile_btn_finish);
        mOpenPassword = (ImageView) mView.findViewById(R.id.open_password);
        mRelativeLayout = (RelativeLayout) mView.findViewById(R.id.relativeLayout);

        mOldPasswordEt = (EditText) mView.findViewById(R.id.profile_et_old_password);
        mNewPasswordEt = (EditText) mView.findViewById(R.id.profile_et_new_password);
        mConPasswordEt = (EditText) mView.findViewById(R.id.profile_et_again_password);
    }

    private void dataInit() {
        String picture = BaseActivity.mHeadUrl;
        if (!picture.equals("")) {
            if (picture.equals("default")) {
                mProfiledHeadView.setImageResource(R.drawable.defult_img);
            } else {
                try {
//                    Bitmap bitmap = revitionImageSize(picture, 60);
//                    bitmap = CheckBitmapDegree.rotateBitmapByDegree(bitmap, CheckBitmapDegree.getBitmapDegree(picture));
//                    mProfiledHeadView.setImageBitmap(bitmap);
                    ImageDisplayer.getInstance(getActivity()).displayBmp(mProfiledHeadView, null, picture);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        mUsernameEt.setText(BaseActivity.mUsername);
        mProfiledHeadUrl = picture;

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
                case R.id.open_password:
                    if (mIsVisible) {
                        Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
                        LinearInterpolator linearInterpolator = new LinearInterpolator();
                        operatingAnim.setInterpolator(linearInterpolator);
                        operatingAnim.setFillAfter(true);
                        mOpenPassword.startAnimation(operatingAnim);

                        mRelativeLayout.setVisibility(View.VISIBLE);
                        mIsVisible = false;
                    } else {
                        Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.finsih_rotate);
                        LinearInterpolator linearInterpolator = new LinearInterpolator();
                        operatingAnim.setInterpolator(linearInterpolator);
                        operatingAnim.setFillAfter(true);
                        mOpenPassword.startAnimation(operatingAnim);

                        mRelativeLayout.setVisibility(View.GONE);
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
                edit = getActivity().getSharedPreferences(PublicStatic.SHAREDPREFERENCES_USER_BHK, 0).edit();

                userdbOperator = new UserdbOperator(getActivity());

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
        UserBean userBean = new UserBean();
        userBean.setEmail(BaseActivity.mEmail);
        userBean.setUsername(newUsername);
        userdbOperator.updateUser(userBean, 0);

        BaseActivity.mUsername = newUsername;

        edit.putString(PublicStatic.SHAREDPREFERENCES_USERNAME, newUsername);
        edit.commit();
    }

    private void passwordIsRight() {
        UserBean userBean = new UserBean();
        userBean.setEmail(BaseActivity.mEmail);

        mInputPassword = mOldPasswordEt.getText().toString();
        mConfirmPassword = mConPasswordEt.getText().toString();
        mNewPassword = mNewPasswordEt.getText().toString();

        if (mRelativeLayout.getVisibility() == View.VISIBLE) {
            if (mInputPassword.equals("") || mInputPassword == null
                    || mConfirmPassword.equals("") || mConfirmPassword == null
                    || mNewPassword.equals("") || mNewPassword == null) {

                Toast.makeText(getActivity(), "Please fill out completely.", Toast.LENGTH_LONG).show();
            } else {
                String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
                if (!mInputPassword.equals(BaseActivity.mPassword)) {
                    Log.e("curry", mInputPassword);
                    Log.e("curry", BaseActivity.mPassword);
                    Toast.makeText(getActivity(), "The original password is wrong.", Toast.LENGTH_LONG).show();
                } else if (!mNewPassword.matches(regex)) {
                    Toast.makeText(getActivity(), "The password must be made of letters and number more than eight.", Toast.LENGTH_LONG).show();
                } else if (mInputPassword.equals(mNewPassword)) {
                    Toast.makeText(getActivity(), "The new password is the same as the old password.", Toast.LENGTH_LONG).show();
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
        UserBean userBean = new UserBean();
        saveUserName();
        saveHeadUrl();

        userBean.setEmail(BaseActivity.mEmail);
        userBean.setPassword(mConfirmPassword);
        userdbOperator.updateUser(userBean, 1);

        BaseActivity.mPassword = mConfirmPassword;
        edit.putBoolean(PublicStatic.SHAREDPREFERENCES_CHECKBOX, false);
        edit.commit();

        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();

    }

    private void saveHeadUrl() {
        UserBean userBean = new UserBean();

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

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void sendBroadcast() {

        Intent intent = new Intent();

        intent.setAction("CHANGE");
        getActivity().sendBroadcast(intent);

    }


    private Bitmap revitionImageSize(String path, int size) throws IOException {
        // 取得图片
        InputStream temp = new ByteArrayInputStream(path.getBytes());
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
        options.inJustDecodeBounds = true;
        // 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
        BitmapFactory.decodeStream(temp, null, options);
        // 关闭流
        temp.close();

        // 生成压缩的图片
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            // 这一步是根据要设置的大小，使宽和高都能满足
            if ((options.outWidth >> i <= size)
                    && (options.outHeight >> i <= size)) {
                // 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
                temp = new ByteArrayInputStream(BaseActivity.mHeadUrl.getBytes());
                // 这个参数表示 新生成的图片为原始图片的几分之一。
                options.inSampleSize = (int) Math.pow(2.0D, i);
                // 这里之前设置为了true，所以要改为false，否则就创建不出图片
                options.inJustDecodeBounds = false;

                bitmap = BitmapFactory.decodeStream(temp, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;


    }
}
