package com.pandadevs.heyfix_worker.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.GeoPoint
import com.pandadevs.heyfix_worker.R
import com.pandadevs.heyfix_worker.databinding.ActivityRequestServiceBinding

class RequestServiceActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    lateinit var binding: ActivityRequestServiceBinding
    lateinit var destination: GeoPoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }


    private fun initView() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        binding.tbApp.setNavigationOnClickListener { finish() }
        binding.btnChat.setOnClickListener { startActivity(Intent(this, ChatActivity::class.java)) }
        destination = GeoPoint(37.422740, -122.139956)
        binding.btnArrived.setOnClickListener { confirmArrived() }
        binding.btnCancel.setOnClickListener { confirmCancel() }
        binding.btnFinish.setOnClickListener { confirmFinished() }
        binding.btnGoogleMaps.setOnClickListener { openGoogleMaps() }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }


    private fun openGoogleMaps() {
        val iam = GeoPoint(18.8480376, -99.2302448)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?saddr=${iam.latitude},${iam.longitude}&daddr=${destination.latitude},${destination.longitude}")
        )
        startActivity(intent)
    }

    private fun confirmCancel() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Cancelar servicio")
            .setMessage("¿Estás seguro de cancelar el servicio?")
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Si, cancelar") { dialog, _ -> }
            .show()
    }

    private fun confirmFinished() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Finalizar servicio")
            .setMessage("¿Has completado correctamente el servicio?")
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Si, lo he hecho") { dialog, _ -> }
            .show()
    }

    private fun confirmArrived() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Confirmación de llegada")
            .setMessage("¿Has llegado a la ubicación?")
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Sí, he llegado") { _, _ -> }
            .show()
    }
}
