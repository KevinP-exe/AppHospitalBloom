package Kevin.Doris.hospitalbloom

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val paciente = findViewById<Button>(R.id.btnPacientes)
        val medicamento = findViewById<Button>(R.id.btnMedicamentos)
        val receta = findViewById<Button>(R.id.btnRecetas)

        paciente.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val intent = Intent(this@Menu, activity_pacientes::class.java)
                startActivity(intent)
                finish()
            }
        }
    medicamento.setOnClickListener {
        GlobalScope.launch(Dispatchers.IO) {
            val intent = Intent(this@Menu, AgregarMedicamento::class.java)
            startActivity(intent)
            finish()
        }
    }
        receta.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val intent = Intent(this@Menu, AgregarReceta::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}