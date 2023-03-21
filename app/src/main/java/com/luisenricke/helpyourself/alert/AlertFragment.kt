package com.luisenricke.helpyourself.alert

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luisenricke.helpyourself.BaseFragment
import com.luisenricke.helpyourself.database.entity.Alert
import com.luisenricke.helpyourself.databinding.FragmentAlertBinding
import timber.log.Timber

class AlertFragment : BaseFragment() {

    private var _binding: FragmentAlertBinding? = null
    private val binding get() = _binding!!

    private lateinit var alertAdapter: AlertAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAlertBinding.inflate(inflater, container, false)

        alertAdapter = setAlertAdapter(binding.root.context)

        binding.apply {
            recyclerAlerts.apply {
                setHasFixedSize(true)
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (dy > 0) {
                            getActivityContext().setBottomNavigationViewVisibility(false)
                        } else if (dy < 0) {
                            getActivityContext().setBottomNavigationViewVisibility(true)
                        }
                    }
                })

                adapter = alertAdapter
            }

            return binding.root
        }

    }

    override fun onStart() {
        super.onStart()
        displayViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAlertAdapter(context: Context): AlertAdapter {
        val clickListener: (Alert) -> Unit = { item ->
            Timber.i("clickListener")
            val action = AlertFragmentDirections.actionAlertToAlertDetail()
            action.idAlert = item.id
            navController.navigate(action)
        }

        val longClickListener: (Alert) -> Unit = { item ->
            Timber.i("longClickListener")
        }

        return AlertAdapter(context, clickListener, longClickListener)
    }

    private fun displayViews() {
        val isAlertsEmpty = alertAdapter.isEmpty()

        if (isAlertsEmpty) {
            binding.apply {
                recyclerAlerts.visibility = View.GONE
                layoutEmpty.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                recyclerAlerts.visibility = View.VISIBLE
                layoutEmpty.visibility = View.GONE
            }

            alertAdapter.update()
        }
    }
}
