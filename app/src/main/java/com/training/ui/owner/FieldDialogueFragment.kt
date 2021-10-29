package com.training.ui.owner

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.training.R
import com.training.model.FieldModel
import kotlinx.android.synthetic.main.fragment_field_dialogue.view.*


class FieldDialogueFragment(var passToFragment: (FieldModel) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireActivity())
        val view = layoutInflater.inflate(R.layout.fragment_field_dialogue, null)
        builder.setView(view).setTitle("Enter Field Data").setNegativeButton("cancel", DialogInterface.OnClickListener {
                dialogInterface, i ->

        }).setPositiveButton("Confirm", DialogInterface.OnClickListener {
                dialogInterface, i ->

            if(view.field_capacity.text.toString().length != 0 || view.field_price.text.toString().length != 0) {
                val game = view.sports_spinner.selectedItem.toString()
                val capacity = view.field_capacity.text.toString().toInt()
                val available = view.switchAvailable.isChecked
                val price = view.field_price.text.toString().toFloat()

                passToFragment(
                    FieldModel(
                        game,
                        capacity,
                        available,
                        price
                    )
                )
            }else{
                Toast.makeText(requireContext(), "Cannot have empty fields", Toast.LENGTH_SHORT).show()
            }
        })

        return builder.create()
    }

}