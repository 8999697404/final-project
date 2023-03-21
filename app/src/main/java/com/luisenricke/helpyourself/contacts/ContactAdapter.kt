package com.luisenricke.helpyourself.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.luisenricke.androidext.loadImageInternalStorage
import com.luisenricke.androidext.toastShort
import com.luisenricke.helpyourself.R
import com.luisenricke.helpyourself.database.AppDatabase
import com.luisenricke.helpyourself.database.dao.ContactDAO
import com.luisenricke.helpyourself.database.entity.Contact
import com.luisenricke.helpyourself.databinding.ItemContactBinding

class ContactAdapter(val context: Context, val clickListener: (Contact) -> Unit, val longClickListener: (Contact) -> Unit) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private var contacts: List<Contact> = arrayListOf()
    private val dao: ContactDAO = AppDatabase.getInstance(context).contactDAO()

    init {
        update()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int =
            contacts.size

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) =
            holder.bind(contacts[pos])

    inner class ViewHolder(private val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            with(binding) {
                lblId.text = contact.id.toString()
                lblName.text = contact.name
                lblRelationship.text = contact.relationship
                cardContact.isChecked = contact.isHighlighted

                if (contact.image.isNotEmpty()) {
                    val image = root.context.loadImageInternalStorage(contact.image)
                    imgProfile.setImageBitmap(image)
                } else {
                    imgProfile.setImageResource(R.drawable.ic_baseline_person_24)
                }

                cardContact.setOnClickListener {
                    clickListener(contact)
                }

                cardContact.setOnLongClickListener {
                    longClickListener(contact)

                    val highlightedList = dao.countHighlighted()

                    if (highlightedList >= 5) {
                        if (contact.isHighlighted) checkItem(contact, cardContact)
                        else context.toastShort(context.getString(R.string.contact_limit_highlighted))
                    } else {
                        checkItem(contact, cardContact)
                    }

                    true
                }
            }
        }
    }

    fun update() {
        contacts = dao.get().sortedBy { it.name }.sortedByDescending { it.isHighlighted }
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean =
            dao.count() == 0L

    private fun checkItem(contact: Contact, card: MaterialCardView) {
        val check = !contact.isHighlighted
        contact.isHighlighted = check
        card.isChecked = check
        dao.update(contact)
        update()
    }
}
