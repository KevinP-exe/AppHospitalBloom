package PacientesHelpers

import Modelo.ClaseConexion
import Kevin.Doris.hospitalbloom.R
import android.app.TimePickerDialog
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import java.sql.Connection
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AdaptadorPacientes(private var Datos: List<tbPacientesInfo>) : RecyclerView.Adapter<ViewHolderPacientes>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPacientes {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_card_pacientes, parent, false)
        return ViewHolderPacientes(vista)
    }

    override fun getItemCount() = Datos.size

    fun actualizarListado(nuevaListaPacientes: List<tbPacientes>) {
        Datos = nuevaListaPacientes
        notifyDataSetChanged()
    }

    fun actualizarItem(
        UUID: String,
        NuevosNombres: String,
        NuevaHabitacion: String,
        NuevaEnfermedad: String,
        NuevoMedicamento: String,
        NuevaHora: String
    ) {
        val index = Datos.indexOfFirst { it.UUID_Paciente == UUID }
        if (index != -1) {
            Datos[index].Nombres = NuevosNombres
            Datos[index].Habitacion = NuevaHabitacion
            Datos[index].Enfermedad = NuevaEnfermedad
            Datos[index].Medicamento = NuevoMedicamento
            Datos[index].Hora_Aplicacion = NuevaHora
            notifyItemChanged(index)
        }
    }

    fun eliminarPaciente(NombresPaciente: String, position: Int) {
        val listaDePacientes = Datos.toMutableList()
        listaDePacientes.removeAt(position)
        CoroutineScope(Dispatchers.IO).launch {
            var objConexion: Connection? = null
            try {
                objConexion = ClaseConexion().cadenaConexion()
                val deletePaciente =
                    objConexion?.prepareStatement("DELETE FROM tbPacientes WHERE Nombres = ?")
                deletePaciente?.setString(1, NombresPaciente)
                deletePaciente?.executeUpdate()
                objConexion?.commit()
            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                objConexion?.close()
            }
        }
        Datos = listaDePacientes.toList()
        notifyItemRemoved(position)
    }

    fun actualizarPaciente(
        uuid: String,
        nombresNuevos: String,
        HabitacionNueva: String,
        EnfermedadNueva: String,
        MedicamentoNuevo: String,
        HoraNueva: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var objConexion: Connection? = null
            try {
                objConexion = ClaseConexion().cadenaConexion()
                val updatePaciente =
                    objConexion?.prepareStatement("UPDATE tbPacientes SET Nombres = ?, Habitacion = ?, Enfermedad = ?, Medicamento = ?, Hora_Aplicacion = ? WHERE UUID_Paciente = ?")
                updatePaciente?.setString(1, nombresNuevos)
                updatePaciente?.setString(2, HabitacionNueva)
                updatePaciente?.setString(3, EnfermedadNueva)
                updatePaciente?.setString(4, MedicamentoNuevo)
                updatePaciente?.setString(5, HoraNueva)
                updatePaciente?.setString(6, uuid)
                updatePaciente?.executeUpdate()
                objConexion?.commit()

                withContext(Dispatchers.Main) {
                    actualizarItem(
                        uuid,
                        nombresNuevos,
                        HabitacionNueva,
                        EnfermedadNueva,
                        MedicamentoNuevo,
                        HoraNueva
                    )
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                objConexion?.close()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolderPacientes, position: Int) {
        val item = Datos[position]
        holder.txtNombre.text = item.Nombres
        holder.txtHabitacion.text = item.Habitacion
        holder.txtEnfermedad.text = item.Enfermedad
        holder.txtMedicamento.text = item.Medicamento
        holder.txtHora.text = item.Hora_Aplicacion

        holder.imgEliminar.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Desea eliminar el Repuesto?")
            builder.setPositiveButton("Sí") { dialog, which ->
                eliminarPaciente(item.Nombres, position)
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        holder.imgEditar.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
            alertDialogBuilder.setTitle("Actualizar datos del repuesto")
            alertDialogBuilder.setMessage("Ingrese los nuevos datos del repuesto:")

            val layout = LinearLayout(holder.itemView.context)
            layout.orientation = LinearLayout.VERTICAL

            val nuevosNombresPacientes = EditText(holder.itemView.context)
            nuevosNombresPacientes.hint = "Nuevos Nombres:"
            nuevosNombresPacientes.inputType = InputType.TYPE_CLASS_TEXT
            layout.addView(nuevosNombresPacientes)

            val nuevaHabitacion = EditText(holder.itemView.context)
            nuevaHabitacion.hint = "Nueva Habitacion:"
            nuevaHabitacion.inputType = InputType.TYPE_CLASS_TEXT
            layout.addView(nuevaHabitacion)

            val nuevaEnfermedad = EditText(holder.itemView.context)
            nuevaEnfermedad.hint = "Nueva Enfermedad:"
            nuevaEnfermedad.inputType = InputType.TYPE_CLASS_TEXT
            layout.addView(nuevaEnfermedad)

            val nuevoMedicamento = EditText(holder.itemView.context)
            nuevoMedicamento.hint = "Nuevo Medicamento:"
            nuevoMedicamento.inputType = InputType.TYPE_CLASS_TEXT
            layout.addView(nuevoMedicamento)

            val nuevaHora = EditText(holder.itemView.context)
            nuevaHora.hint = "Nueva Hora:"
            nuevaHora.inputType = InputType.TYPE_CLASS_TEXT
            layout.addView(nuevaHora)

            nuevaHora.setOnClickListener {
                val cal = Calendar.getInstance()
                val hour = cal.get(Calendar.HOUR_OF_DAY)
                val minute = cal.get(Calendar.MINUTE)

                val timePickerDialog = TimePickerDialog(
                    holder.itemView.context,
                    { _, hourOfDay, minuteOfDay ->
                        if (hourOfDay in 6..22) {
                            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            cal.set(Calendar.MINUTE, minuteOfDay)
                            val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
                            val formattedTime = format.format(cal.time)
                            nuevaHora.setText(formattedTime)
                        } else {
                            Toast.makeText(
                                holder.itemView.context,
                                "Por favor, seleccione una hora entre 6 AM y 22 PM",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    hour,
                    minute,
                    false
                )

                alertDialogBuilder.setView(layout)

                alertDialogBuilder.setPositiveButton("Actualizar") { dialog, which ->
                    val nuevosNombres = nuevosNombresPacientes.text.toString()
                    val nuevaHabitacion = nuevaHabitacion.text.toString()
                    val nuevaEnfermedad = nuevaEnfermedad.text.toString()
                    val nuevoMedicamento = nuevoMedicamento.text.toString()
                    val nuevaHora = nuevaHora.text.toString()

                    if (nuevosNombres.isBlank() || nuevaHabitacion.isBlank() || nuevaEnfermedad.isBlank() || nuevoMedicamento.isBlank() || nuevaHora.isBlank()) {
                        Toast.makeText(
                            holder.itemView.context,
                            "Todos los campos son obligatorios",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        try {
                            actualizarPaciente(
                                item.UUID_Paciente,
                                nuevosNombres,
                                nuevaHabitacion,
                                nuevaEnfermedad,
                                nuevoMedicamento,
                                nuevaHora
                            )
                            actualizarItem(
                                item.UUID_Paciente,
                                nuevosNombres,
                                nuevaHabitacion,
                                nuevaEnfermedad,
                                nuevoMedicamento,
                                nuevaHora
                            )
                        } catch (e: NumberFormatException) {
                            Toast.makeText(holder.itemView.context, "", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                alertDialogBuilder.setNegativeButton("Cancelar") { dialog, which ->
                    dialog.dismiss()
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }
    }
}