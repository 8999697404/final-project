package com.luisenricke.helpyourself.contacts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luisenricke.helpyourself.BaseFragment
import com.luisenricke.helpyourself.database.entity.Contact
import com.luisenricke.helpyourself.databinding.FragmentContactBinding
import timber.log.Timber

class ContactFragment : BaseFragment() {

    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactAdapter: ContactAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentContactBinding.inflate(inflater, container, false)

        contactAdapter = setContactAdapter(binding.root.context)

        binding.apply {

            btnAddContact.setOnClickListener {
                val action = ContactFragmentDirections.actionContactToContactAdd()
                navController.navigate(action)
            }

            btnAddContactEmpty.setOnClickListener {
                val action = ContactFragmentDirections.actionContactToContactAdd()
                navController.navigate(action)
            }

            recyclerContacts.apply {
                setHasFixedSize(true)
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (dy > 0 && btnAddContact.visibility == View.VISIBLE) {
                            btnAddContact.hide()
                            getActivityContext().setBottomNavigationViewVisibility(false)
                        } else if (dy < 0 && btnAddContact.visibility != View.VISIBLE) {
                            btnAddContact.show()
                            getActivityContext().setBottomNavigationViewVisibility(true)
                        }
                    }
                })

                adapter = contactAdapter
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        displayViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setContactAdapter(context: Context): ContactAdapter {
        val clickListener: (Contact) -> Unit = { item ->
            val action = ContactFragmentDirections.actionContactToContactDetail()
            action.idContact = item.id
            navController.navigate(action)
        }

        val longClickListener: (Contact) -> Unit = { item ->
            Timber.i("longClicked")
        }

        return ContactAdapter(context, clickListener, longClickListener)
    }

    private fun displayViews() {
        val isEmptyContacts = contactAdapter.isEmpty()

        if (isEmptyContacts) {
            binding.apply {
                recyclerContacts.visibility = View.GONE
                btnAddContact.visibility = View.GONE
                layoutEmpty.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                recyclerContacts.visibility = View.VISIBLE
                btnAddContact.visibility = View.VISIBLE
                layoutEmpty.visibility = View.GONE
            }

            contactAdapter.update()
        }
    }
}
