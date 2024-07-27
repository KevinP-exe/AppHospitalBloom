package PacientesHelpers

import Kevin.Doris.hospitalbloom.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolderPacientes(view: View) : RecyclerView.ViewHolder(view) {
    val Nombre: TextView = view.findViewById(R.id.txtNombresCard)
    val Habitacion: TextView = view.findViewById(R.id.txtHabitacionCard)
}