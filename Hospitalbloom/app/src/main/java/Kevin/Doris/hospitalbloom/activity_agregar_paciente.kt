package Kevin.Doris.hospitalbloom

import Modelo.ClaseConexion
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class activity_agregar_paciente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_paciente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fun showTimePickerDialog(textView: EditText) {
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this@activity_agregar_paciente, // Use this@activity_agregar_paciente instead of requireContext()
                { _: TimePicker, hourOfDay: Int, minute: Int ->
                    if (hourOfDay in 7..15) {
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        cal.set(Calendar.MINUTE, minute)
                        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        val formattedTime = format.format(cal.time)
                        textView.setText(formattedTime)
                    } else {
                        Toast.makeText(
                            this@activity_agregar_paciente, // Use this@activity_agregar_paciente instead of requireContext()
                            "Por favor, seleccione una hora entre 8 AM y 3 PM",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                hour,
                minute,
                false
            )

            timePickerDialog.show()
        }

        val txtNombres = findViewById<EditText>(R.id.txt_nombres)
        val txtApellidos = findViewById<EditText>(R.id.txt_apellidos)
        val txtEdad = findViewById<EditText>(R.id.txt_edad)
        val txtFecha = findViewById<EditText>(R.id.txt_fecha_nacimiento)
        val txtEnfermedad = findViewById<EditText>(R.id.txt_enfermedad)
        val txtMedicamento = findViewById<EditText>(R.id.txt_medicamento)
        val txtHora = findViewById<EditText>(R.id.txt_hora_aplicacion)
        val txtHabitacion = findViewById<EditText>(R.id.txt_habitacion)
        val txtCama = findViewById<EditText>(R.id.txt_cama)
        val btnCrearPaciente = findViewById<Button>(R.id.btn_agregar)
        val imgFlecha = findViewById<ImageView>(R.id.imgFlecha)

        imgFlecha.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val intent = Intent(this@activity_agregar_paciente, activity_pacientes::class.java)
                startActivity(intent)
                finish()
            }
        }

        txtFecha.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            val fechaMinima = Calendar.getInstance()
            fechaMinima.set(anio, mes, dia + 1)

            val fechaMaxima = Calendar.getInstance()
            fechaMaxima.set(anio, mes, dia + 10)

            val datePickerDialog = DatePickerDialog(
                this@activity_agregar_paciente, // Use this@activity_agregar_paciente instead of requireContext()
                { view, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val fechaSeleccionada = "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                    txtFecha.setText(fechaSeleccionada)
                },
                anio, mes, dia
            )

            datePickerDialog.datePicker.minDate = fechaMinima.timeInMillis
            datePickerDialog.datePicker.maxDate = fechaMaxima.timeInMillis

            datePickerDialog.show()
        }

        txtHora.setOnClickListener {
            showTimePickerDialog(txtHora)
        }

        btnCrearPaciente.setOnClickListener {
            if (txtFecha.text.toString().isEmpty() || txtHora.text.toString().isEmpty()) {
                Toast.makeText(
                    this@activity_agregar_paciente, // Use this@activity_agregar_paciente instead of requireContext()
                    "Por favor, complete todos los campos.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val objConexion = ClaseConexion().cadenaConexion()

                        val addPaciente = objConexion?.prepareStatement("INSERT INTO tbPacientes (UUID_Paciente, Nombres, Apellidos, Edad, FechaNacimiento, Enfermedad, Medicamento, HoraAplicacion, Habitacion, Cama) Values(?,?,?,?,?,?,?,?,?,?)")!!
                        addPaciente.setString(1, UUID.randomUUID().toString())
                        addPaciente.setString(2, txtNombres.text.toString())
                        addPaciente.setString(3, txtApellidos.text.toString())
                        addPaciente.setString(4, txtEdad.text.toString())
                        addPaciente.setString(5, txtFecha.text.toString())
                        addPaciente.setString(6, txtEnfermedad.text.toString())
                        addPaciente.setString(7, txtMedicamento.text.toString())
                        addPaciente.setString(8, txtHora.text.toString())
                        addPaciente.setString(9, txtHabitacion.text.toString())
                        addPaciente.setString(10, txtCama.text.toString())

                        addPaciente.executeUpdate()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@activity_agregar_paciente, // Use this@activity_agregar_paciente instead of requireContext()
                                "Paciente agregado exitosamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@activity_agregar_paciente, // Use this@activity_agregar_paciente instead of requireContext()
                                "Error al agregar el paciente: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }
}

