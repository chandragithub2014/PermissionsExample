package com.android.permissionsexample

interface PermissionListener {
  fun   shouldShowRationaleInfo()
  fun   isPermissionGranted(isGranted : Boolean)
}