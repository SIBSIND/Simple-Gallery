package com.simplemobiletools.gallery.dialogs

import android.app.Activity
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.simplemobiletools.gallery.Config
import com.simplemobiletools.gallery.Constants
import com.simplemobiletools.gallery.R
import kotlinx.android.synthetic.main.dialog_change_sorting.view.*

class ChangeSortingDialog(val activity: Activity, val isDirectorySorting: Boolean, val listener: OnChangeSortingListener) : DialogInterface.OnClickListener {
    companion object {
        private var currSorting = 0

        lateinit var config: Config
        lateinit var view: View
    }

    init {
        config = Config.newInstance(activity)
        view = LayoutInflater.from(activity).inflate(R.layout.dialog_change_sorting, null)

        AlertDialog.Builder(activity)
                .setTitle(activity.resources.getString(R.string.sort_by))
                .setView(view)
                .setPositiveButton(R.string.ok, this)
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show()

        currSorting = if (isDirectorySorting) config.directorySorting else config.sorting
        setupSortRadio()
        setupOrderRadio()
    }

    private fun setupSortRadio() {
        val sortingRadio = view.dialog_radio_sorting
        var sortBtn = sortingRadio.dialog_radio_name

        if (currSorting and Constants.SORT_BY_DATE != 0) {
            sortBtn = sortingRadio.dialog_radio_date
        } else if (currSorting and Constants.SORT_BY_SIZE != 0) {
            sortBtn = sortingRadio.dialog_radio_size
        }
        sortBtn.isChecked = true
    }

    private fun setupOrderRadio() {
        val orderRadio = view.dialog_radio_order
        var orderBtn = orderRadio.dialog_radio_ascending

        if (currSorting and Constants.SORT_DESCENDING != 0) {
            orderBtn = orderRadio.dialog_radio_descending
        }
        orderBtn.isChecked = true
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        val sortingRadio = view.dialog_radio_sorting
        var sorting = when (sortingRadio.checkedRadioButtonId) {
            R.id.dialog_radio_name -> Constants.SORT_BY_NAME
            R.id.dialog_radio_date -> Constants.SORT_BY_DATE
            else -> Constants.SORT_BY_SIZE
        }

        if (view.dialog_radio_order.checkedRadioButtonId == R.id.dialog_radio_descending) {
            sorting = sorting or Constants.SORT_DESCENDING
        }

        if (isDirectorySorting) {
            if (config.directorySorting != sorting) {
                config.directorySorting = sorting
            }
        } else {
            if (config.sorting != sorting) {
                config.sorting = sorting
            }
        }
        listener.sortingChanged()
    }

    interface OnChangeSortingListener {
        fun sortingChanged()
    }
}