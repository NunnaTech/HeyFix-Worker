package com.pandadevs.heyfix_worker.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pandadevs.heyfix_worker.R
import com.pandadevs.heyfix_worker.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    lateinit var binding:ActivityAboutBinding
    lateinit var adapter: AdapterMember

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tbApp.setNavigationOnClickListener { finish() }

        val list:List<Member> = listOf(
            Member(name = "Alexis Loya García", role = "Desarrollador Backend", image = R.drawable.img_alexis),
            Member(name = "Luis Enrique Álvarez Ortiz", role = "Desarrollador Móvil", image = R.drawable.img_luis),
            Member(name = "Jair David Vasquez Martinez", role = "Desarrollador Backend", image = R.drawable.img_jair),
            Member(name = "Jackeline Aguas Calderon", role = "Desarrolladora Móvil", image = R.drawable.img_jacke),
            Member(name = "Raúl Genaro Adame Najera", role = "Desarrollador Móvil", image = R.drawable.img_raul),
            Member(name = "Hector Saldaña Espinoza", role = "Desarrollador Móvil", image = R.drawable.img_hector),
        )

        adapter = AdapterMember(list, this)
        binding.rvMembers.layoutManager = LinearLayoutManager(this)
        binding.rvMembers.adapter = adapter
    }
}