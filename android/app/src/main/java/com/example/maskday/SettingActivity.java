package com.example.maskday;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout saveYearLayout, appInfoLayout;
    private ImageView backBtn;
    private Realm realm;
    private RealmResults<UserModel> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();

        saveYearLayout.setOnClickListener(this);
        appInfoLayout.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    private void init() {
        saveYearLayout = (LinearLayout) findViewById(R.id.input_year);
        appInfoLayout = (LinearLayout) findViewById(R.id.app_info);
        backBtn = (ImageView) findViewById(R.id.back_button);
    }

    @Override
    public void onClick(View view) {
        if (view == saveYearLayout) {
            showBirthDayPicker();
        } else if (view == backBtn) {
            finish();
        }
    }

    private void showBirthDayPicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        final Dialog birthDayDialog = new Dialog(this);
        birthDayDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        birthDayDialog.setContentView(R.layout.dialog_birthyear);

        Button okBtn = (Button) birthDayDialog.findViewById(R.id.ok_button);
        Button cancelBtn = (Button) birthDayDialog.findViewById(R.id.cancel_button);

        final NumberPicker picker = (NumberPicker) birthDayDialog.findViewById(R.id.number_picker);
        picker.setMinValue(year - 100);
        picker.setMaxValue(year);
        picker.setDescendantFocusability(NumberPicker.FOCUS_AFTER_DESCENDANTS);
        setDividerColor(picker, android.R.color.white);
        picker.setWrapSelectorWheel(false);
        picker.setValue(year - 20);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {

            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 출생년도 저장하기
                saveRealm(picker.getValue());
                Toast.makeText(SettingActivity.this, "저장되었습니다!", Toast.LENGTH_SHORT).show();
                birthDayDialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthDayDialog.dismiss();
            }
        });

        birthDayDialog.show();
    }

    /* Realm 초기설정 */
    private void getRealm(){
        realm = Realm.getDefaultInstance();
        userData = realm.where(UserModel.class).findAll();
    }

    /* Realm 에 저장하기 */
    private void saveRealm(int birthYear){
        realm = Realm.getDefaultInstance();
        UserModel userModel = new UserModel();

        realm.beginTransaction();
        userModel = realm.createObject(UserModel.class);
        userModel.setBirthYear(birthYear);
        realm.commitTransaction();
        Log.d("출생년도", birthYear + "");
    }


    /* dialog 구분선 커스텀 */
    private void setDividerColor(NumberPicker picker, int color) {
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


}
