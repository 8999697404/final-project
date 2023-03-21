package com.luisenricke.helpyourself.alert

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luisenricke.helpyourself.R
import com.luisenricke.helpyourself.database.AppDatabase
import com.luisenricke.helpyourself.database.dao.AlertDAO
import com.luisenricke.helpyourself.database.entity.Alert
import com.luisenricke.helpyourself.databinding.ItemAlertBinding
import com.luisenricke.kotlinext.formatDateTimeExtended
import com.luisenricke.kotlinext.roundDecimals

class AlertAdapter(val context: Context, val clickListener: (Alert) -> Unit, val longClickListener: (Alert) -> Unit) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {

    private var alerts: List<Alert> = arrayListOf()
    private val dao: AlertDAO = AppDatabase.getInstance(context).alertDAO()

    init {
        update()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ItemAlertBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int =
            alerts.size

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) =
            holder.bind(alerts[pos])

    inner class ViewHolder(private val binding: ItemAlertBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(alert: Alert) {
            with(binding) {
                lblId.text = alert.id.toString()
                lblDate.text = alert.timestamp.time.formatDateTimeExtended()

                val latitude: Double = alert.latitude.roundDecimals(10).toDouble()
                val longitude: Double = alert.longitude.roundDecimals(10).toDouble()

                lblLocation.text = if (latitude != 0.0 && longitude != 0.0) {
                    "$latitude, $longitude"
                } else {
                    context.getString(R.string.alert_detail_location_not_available)
                }

                cardContact.setOnClickListener {
                    clickListener(alert)
                }

                cardContact.setOnLongClickListener {
                    longClickListener(alert)

                    true
                }
            }
        }
    }

    fun update() {
        alerts = dao.get().sortedByDescending { it.timestamp }
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean =
            dao.count() == 0L
}
