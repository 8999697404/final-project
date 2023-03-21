package com.luisenricke.helpyourself.alert

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luisenricke.androidext.loadImageInternalStorage
import com.luisenricke.helpyourself.R
import com.luisenricke.helpyourself.database.AppDatabase
import com.luisenricke.helpyourself.database.dao.AlertContactDAO
import com.luisenricke.helpyourself.database.dao.ContactDAO
import com.luisenricke.helpyourself.database.entity.AlertContact
import com.luisenricke.helpyourself.databinding.ItemAlertDetailsBinding

class AlertDetailAdapter(val context: Context, val idAlert: Long, val clickListener: (AlertContact) -> Unit, val longClickListener: (AlertContact) -> Unit) : RecyclerView.Adapter<AlertDetailAdapter.ViewHolder>() {

    private var alertContacts: List<AlertContact> = arrayListOf()
    private val alertContactDao: AlertContactDAO = AppDatabase.getInstance(context).alertContactDAO()
    private val contactDao: ContactDAO = AppDatabase.getInstance(context).contactDAO()

    init {
        update()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ItemAlertDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int =
            alertContacts.size

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) =
            holder.bind(alertContacts[pos])

    inner class ViewHolder(private val binding: ItemAlertDetailsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(alertContact: AlertContact) {
            with(binding) {
                lblId.text = alertContact.id.toString()
                lblMessage.text = alertContact.messageSent

                val contact = contactDao.get(alertContact.contactId)
                lblName.text = contact?.name!!

                if (contact.image.isNotEmpty()) {
                    val image = root.context.loadImageInternalStorage(contact.image)
                    imgProfile.setImageBitmap(image)
                } else {
                    imgProfile.setImageResource(R.drawable.ic_baseline_person_24)
                }

                cardContact.setOnClickListener {
                    clickListener(alertContact)
                }

                cardContact.setOnLongClickListener {
                    longClickListener(alertContact)

                    true
                }
            }
        }
    }

    fun update() {
        alertContacts = alertContactDao.getByAlert(idAlert)
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean =
            alertContactDao.getByAlert(idAlert).isEmpty()
}
