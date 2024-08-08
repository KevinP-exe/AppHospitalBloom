package Kevin.Doris.hospitalbloom

import Modelo.ClaseConexion
import PacientesHelpers.AdaptadorPacientes
import PacientesHelpers.tbPacientesInfo
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class activity_pacientes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pacientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }

        val Agregar = findViewById<ImageView>(R.id.imgAgragarPaciente)

        Agregar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val intent = Intent(this@activity_pacientes, activity_agregar_paciente::class.java)
                startActivity(intent)
                finish()
            }
        }

        fun obtenerDatosPacientes(): List<tbPacientesInfo> {
            val listadoPacientes = mutableListOf<tbPacientesInfo>()
            try {
                val objConexion = ClaseConexion().cadenaConexion()
                val statement = objConexion?.createStatement()
                val query =
                    "SELECT Nombres, Habitacion, Enfermedad, Medicamento, HoraAplicacion FROM tbPacientes"
                val resultSet = statement?.executeQuery(query)

                while (resultSet?.next() == true) {
                    val nombres = resultSet.getString("Nombres")
                    val habitacion = resultSet.getString("Habitacion")
                    val enfermedad = resultSet.getString("Enfermedad")
                    val medicamento = resultSet.getString("Medicamento")
                    val horaAplicacion = resultSet.getString("HoraAplicacion")

                    // Verificar que los valores no sean nulos
                    if (nombres != null && habitacion != null && enfermedad != null && medicamento != null && horaAplicacion != null) {
                        val paciente = tbPacientesInfo(
                            nombres,
                            habitacion,
                            enfermedad,
                            medicamento,
                            horaAplicacion
                        )
                        listadoPacientes.add(paciente)
                    } else {
                        Log.e("obtenerDatosPacientes", "Uno de los valores es nulo")
                    }
                }

                resultSet?.close()
                statement?.close()
                objConexion?.close()
            } catch (e: Exception) {
                Log.e("obtenerDatosPacientes", "Error fetching Pacientes data", e)
            }
            return listadoPacientes
        }


        val rcvRepuesto = findViewById<RecyclerView>(R.id.rcvPacientes)

        CoroutineScope(Dispatchers.IO).launch {
            val PacientesDB = obtenerDatosPacientes()
            withContext(Dispatchers.Main) {
                val adapter = AdaptadorPacientes(PacientesDB)
                rcvRepuesto.adapter = adapter
            }
        }
    }
}
