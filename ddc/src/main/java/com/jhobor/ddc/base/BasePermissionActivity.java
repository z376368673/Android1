package com.jhobor.ddc.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class BasePermissionActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int NEEDED_PERMINSSIONS = 9;
    String[] perms;
    String message;
    AuthorizionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void doRequest(String[] perms, String message, AuthorizionCallback callback) {
        this.perms = perms;
        this.message = message;
        this.callback = callback;
        requestNeededPerminssions();
    }

    @AfterPermissionGranted(NEEDED_PERMINSSIONS)
    private void requestNeededPerminssions() {
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            callback.onGranted();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, message,
                    NEEDED_PERMINSSIONS, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    protected interface AuthorizionCallback {
        void onGranted();
    }
}
