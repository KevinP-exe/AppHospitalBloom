package PacientesHelpers

import Kevin.Doris.hospitalbloom.R
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class ViewHolderPacientes(view: View): RecyclerView.ViewHolder(view) {
    val txtNombre: TextView = view.findViewById(R.id.txtNombreCard)
    val txtHabitacion: TextView = view.findViewById(R.id.txtHabitacionCard)
    val txtEnfermedad: TextView = view.findViewById(R.id.txtEnfermedadCard)
    val txtMedicamento: TextView = view.findViewById(R.id.txtMedicamentoCard)
    val txtHora: TextView = view.findViewById(R.id.txtHoraCard)
    val imgEliminar: ImageView = view.findViewById(R.id.imgEliminar)
    val imgEditar: ImageView = view.findViewById(R.id.imgEditar)
}