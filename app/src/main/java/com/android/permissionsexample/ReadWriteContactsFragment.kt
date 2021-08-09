package com.android.permissionsexample

import android.Manifest
import android.content.ContentResolver
import android.content.DialogInterface
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_read_write_contacts.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReadWriteContactsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReadWriteContactsFragment : Fragment() , PermissionListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var permissionHelper: PermissionHelper
    private var contactList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read_write_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionHelper =  PermissionHelper(this, this)
        permissionHelper.checkForMultiplePermissions(
            arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
            )
        )
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReadWriteContactsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReadWriteContactsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun shouldShowRationaleInfo() {
      println("shouldShowRationaleInfo...................")
        val dialogBuilder = AlertDialog.Builder(requireContext())

        // set message of alert dialog
        dialogBuilder.setMessage("Read/Write Contacts permission is Required")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("OK") { dialog, id ->
              permissionHelper.launchPermissionDialogForMultiplePermissions(
                  arrayOf(
                      Manifest.permission.READ_CONTACTS,
                      Manifest.permission.WRITE_CONTACTS
                  )
              )
                dialog.cancel()
            }
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("AlertDialogExample")
        // show alert dialog
        alert.show()

    }

    override fun isPermissionGranted(isGranted: Boolean) {
        if(isGranted){
            Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
            readContacts()
        }
    }

    private fun readContacts(){
        val contentResolver: ContentResolver =  requireContext().contentResolver
        val  cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()){
                do {     contactList.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                }while (cursor.moveToNext());
            }
        }

        if(contactList.size > 0 ){
            info.text = contactList.toString()
        }

    }
}