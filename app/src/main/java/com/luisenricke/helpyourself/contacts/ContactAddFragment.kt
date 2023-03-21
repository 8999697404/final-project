package com.luisenricke.helpyourself.contacts

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.luisenricke.androidext.saveImageInternalStorage
import com.luisenricke.androidext.toastShort
import com.luisenricke.helpyourself.BaseFragment
import com.luisenricke.helpyourself.Constraint
import com.luisenricke.helpyourself.R
import com.luisenricke.helpyourself.database.entity.Contact
import com.luisenricke.helpyourself.databinding.FragmentContactAddBinding
import com.luisenricke.helpyourself.service.SendSMS
import com.luisenricke.kotlinext.formatPhone
import com.luisenricke.kotlinext.removeWhiteSpaces
import timber.log.Timber
import com.luisenricke.helpyourself.contacts.ContactUtils as utils

class ContactAddFragment : BaseFragment() {

    private var _binding: FragmentContactAddBinding? = null
    private val binding get() = _binding!!

    private var image: Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getActivityContext().setBottomNavigationViewVisibility(false)
        _binding = FragmentContactAddBinding.inflate(inflater, container, false)

        binding.apply {
            val context = root.context

            // region Toolbar
            getActivityContext().setSupportActionBar(toolbar)
            setupActionBar(getActivityContext().supportActionBar, getString(R.string.contact_add))
            // endregion Toolbar

            // region BindViews
            // Image
            imgProfile.setOnClickListener {
                imageOptionsSimple(context, imgProfile)
            }

            //Name
            txtName.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) utils.hasEmptyField(context, txtLayoutName, txtName.text!!)
            }

            // Phone
            txtPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
            txtPhone.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    utils.hasEmptyField(context, txtLayoutPhone, txtPhone.text!!)
                    utils.hasValidPhoneField(context, txtLayoutPhone, txtPhone.text!!)
                }
            }

            // Relationship
            val adapter = ArrayAdapter(context, R.layout.item_relationship, context.resources.getStringArray(R.array.contact_relationship_list))

            (txtLayoutRelationship.editText as? AutoCompleteTextView)?.setAdapter(adapter)

            txtRelationship.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) getActivityContext().keyboard.hide(v)
                else utils.hasEmptyField(context, txtLayoutRelationship, txtRelationship.text!!)
            }

            // Message
            val smsMaxLength = SendSMS.getInstance(context).getMaxLength()
            txtLayoutMessage.counterMaxLength = smsMaxLength

            // Switch
            val highlightedList = database.contactDAO().countHighlighted()
            if (highlightedList >= 5) {
                swtHighlighted.visibility = View.GONE
            }

            // Buttons
            btnCancel.setOnClickListener {
                navController.popBackStack()
            }

            btnAccept.setOnClickListener {
                val isEmptyName = utils.hasEmptyField(context, txtLayoutName, txtName.text!!)
                val isEmptyPhone = utils.hasEmptyField(context, txtLayoutPhone, txtPhone.text!!)
                val isEmptyRelationShip = utils.hasEmptyField(context, txtLayoutRelationship, txtRelationship.text!!)
                val isPhoneValid = utils.hasValidPhoneField(context, txtLayoutPhone, txtPhone.text!!)
                val isPhoneAlreadyExist = utils.hasPhoneAlreadyExist(context, database.contactDAO(), null, txtLayoutPhone, txtPhone.text!!)
                val isMessageExceeded = utils.hasMessageExceeded(context, smsMaxLength, txtLayoutMessage, txtMessage.text!!)

                if (!isEmptyName && !isEmptyPhone && !isEmptyRelationShip && !isPhoneValid && !isPhoneAlreadyExist && !isMessageExceeded) {
                    val formatPhone = txtPhone.text.toString().removeWhiteSpaces().formatPhone("")
                    val name = txtName.text.toString()

                    val insertContact = Contact(
                            name = name,
                            phone = formatPhone,
                            isHighlighted = swtHighlighted.isChecked,
                            relationship = txtRelationship.text.toString(),
                            message = txtMessage.text.toString()
                    )

                    val insertedId = database.contactDAO().insert(insertContact)

                    if (imgProfile.tag == context.getString(R.string.photo_image_tag_custom)) {
                        insertContact.id = insertedId
                        val imageSrc = "${insertedId}_${formatPhone}_image"
                        val isSavedImage = context.saveImageInternalStorage(image, imageSrc)
                        insertContact.image = if (isSavedImage) imageSrc else ""
                        database.contactDAO().update(insertContact)
                    }

                    Timber.v("Contact: ${database.contactDAO().get(insertedId)}")
                    toastShort(context.getString(R.string.contact_added_successfully))
                    navController.popBackStack()
                }
            }
            // endregion BindViews
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            Constraint.INTENT_READ_CONTACTS_CODE -> {
                val contact = utils.getContact(binding.root.context, data)
                Timber.v("Imported contact: \n $contact")

                binding.txtName.setText(contact.name)
                utils.hasEmptyField(binding.root.context, binding.txtLayoutName, binding.txtName.text!!)

                binding.txtPhone.setText(contact.phone)
                utils.hasEmptyField(binding.root.context, binding.txtLayoutPhone, binding.txtPhone.text!!)
                utils.hasValidPhoneField(binding.root.context, binding.txtLayoutPhone, binding.txtPhone.text!!)
            }
            Constraint.INTENT_IMAGE_FROM_GALLERY -> {
                image = getImage(binding.root.context, data) ?: return
                Timber.v("The image was set with ${image?.width}x${image?.height}")

                binding.imgProfile.setImageBitmap(image)
                binding.imgProfile.tag = binding.root.context.getString(R.string.photo_image_tag_custom)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_contact_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home        -> {
                navController.popBackStack()
                true
            }
            R.id.menu_contact_import -> {
                utils.requestContact(this)
                true
            }
            else                     -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getActivityContext().setBottomNavigationViewVisibility(true)
        _binding = null
    }
}
