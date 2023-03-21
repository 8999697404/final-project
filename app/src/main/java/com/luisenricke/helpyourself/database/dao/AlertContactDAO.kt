package com.luisenricke.helpyourself.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.luisenricke.helpyourself.database.entity.Alert
import com.luisenricke.helpyourself.database.entity.AlertContact
import com.luisenricke.helpyourself.database.entity.AlertContact.SCHEMA
import com.luisenricke.helpyourself.database.entity.Contact
import com.luisenricke.room.dao.Base
import com.luisenricke.room.dao.Delete
import com.luisenricke.room.dao.PrimaryKey
import com.luisenricke.room.dao.Update

@Suppress("unused")
@Dao
abstract class AlertContactDAO : Base<AlertContact>, Update<AlertContact>, Delete<AlertContact>, PrimaryKey<AlertContact> {

    @Query("SELECT COUNT(*) FROM ${SCHEMA.TABLE}")
    abstract override fun count(): Long

    @Query("SELECT * FROM ${SCHEMA.TABLE}")
    abstract override fun get(): List<AlertContact>

    @Query("DELETE FROM ${SCHEMA.TABLE}")
    abstract override fun drop()

    @Query("SELECT * FROM ${SCHEMA.TABLE} WHERE id = :id")
    abstract override fun get(id: Long): AlertContact?

    @Query("SELECT * FROM ${SCHEMA.TABLE} WHERE id IN(:ids)")
    abstract override fun get(ids: LongArray): List<AlertContact>

    @Query("DELETE FROM ${SCHEMA.TABLE} WHERE id = :id")
    abstract override fun delete(id: Long): Int

    @Query("DELETE FROM ${SCHEMA.TABLE} WHERE id IN(:ids)")
    abstract override fun deletes(ids: LongArray): Int

    @Query("SELECT * FROM ${SCHEMA.TABLE} WHERE alert_id = :idAlert")
    abstract fun getByAlert(idAlert: Long): List<AlertContact>

    @Query(
            """
            SELECT * FROM ${Alert.SCHEMA.TABLE} AS LEFT_ 
            INNER JOIN ${SCHEMA.TABLE} AS RIGHT_ 
            ON LEFT_.${Alert.SCHEMA.ID} = RIGHT_.${SCHEMA.ALERT_ID} 
            WHERE RIGHT_.${SCHEMA.CONTACT_ID} = :idContact 
        """
    )
    abstract fun getAlerts(idContact: Long): List<Alert>

    @Query(
            """
            SELECT * FROM ${Contact.SCHEMA.TABLE} AS RIGHT_
            INNER JOIN ${SCHEMA.TABLE} AS LEFT_
            ON RIGHT_.${Contact.SCHEMA.ID} = LEFT_.${SCHEMA.CONTACT_ID}
            WHERE LEFT_.${SCHEMA.ALERT_ID} = :idAlert
        """
    )
    abstract fun getContacts(idAlert: Long): List<Contact>
}
