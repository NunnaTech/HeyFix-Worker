package com.pandadevs.heyfix_worker.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.GeoPoint
import com.pandadevs.heyfix_worker.R
import com.pandadevs.heyfix_worker.data.model.ChatModel
import com.pandadevs.heyfix_worker.databinding.ActivityRequestServiceBinding
import com.pandadevs.heyfix_worker.provider.LocationLiveDataProvider
import com.pandadevs.heyfix_worker.provider.UserLastProvider
import com.pandadevs.heyfix_worker.utils.SharedPreferenceManager
import com.pandadevs.heyfix_worker.viewmodel.HiredServiceViewModel
import kotlinx.coroutines.launch

class RequestServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRequestServiceBinding
    private var idServideHired: String = ""
    private var chatModel: ChatModel = ChatModel("", "", "")
    private val hiredServiceViewModel: HiredServiceViewModel by viewModels()

    companion object {
        const val ID_SERVICE_HIRED = "ID_SERVICE_HIRED"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initObservers()
    }

    private fun initView() {
        idServideHired = intent.getStringExtra(ID_SERVICE_HIRED) ?: ""
        binding.tbApp.setNavigationOnClickListener { finish() }
        lifecycleScope.launch {
            hiredServiceViewModel.getDataHiredService(idServideHired)
            hiredServiceViewModel.isThereACurrentService(idServideHired)
        }
    }

    private fun initObservers() {
        hiredServiceViewModel.isThereACurrentService.observe(this) {
            if (it != null) {
                chatModel.id = it.id
                chatModel.client_name = it.client_name
                binding.tvAddress.text = it.address
                binding.tvName.text = it.client_name
                binding.btnGoogleMaps.isEnabled = true
                binding.btnArrived.isEnabled = true
                binding.btnFinish.isEnabled = true
                binding.btnCancel.isEnabled = true
                binding.btnChat.isEnabled = true
                binding.btnGoogleMaps.setOnClickListener { _ -> openGoogleMaps(it.worker_ubication, it.client_ubication) }
                binding.btnChat.setOnClickListener { goToChatActivity() }
                binding.btnArrived.setOnClickListener { confirmArrived() }
                binding.btnCancel.setOnClickListener { confirmCancel() }
                binding.btnFinish.setOnClickListener { confirmFinished() }
                if (it.canceled && !it.completed) callBackServiceCanceled()
            }
        }

        hiredServiceViewModel.dataClient.observe(this) {
            chatModel.client_picture = it.picture
            binding.btnCall.isEnabled = true
            Glide.with(this).load(it.picture).into(binding.civPicture)
            binding.btnCall.setOnClickListener { _ -> openCall(it.phone_number) }
        }

        LocationLiveDataProvider(this).observe(this){
            if(chatModel.id.isNotEmpty()){
                UserLastProvider.setLastCurrentPositionOnHiredService(chatModel.id, GeoPoint(it.latitude, it.longitude))
            }
        }
    }

    private fun goToChatActivity() {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra(ChatActivity.CHAT_MODEL, chatModel)
        startActivity(intent)
    }

    private fun callBackServiceCanceled() {
        MaterialAlertDialogBuilder(this, R.style.MyThemeOverlay_MaterialAlertDialog_Canceled)
            .setTitle("Servicio cancelado")
            .setIcon(R.drawable.ilus_cancel)
            .setCancelable(false)
            .setMessage("El servicio ha sido cancelado.")
            .setPositiveButton("Aceptar") { _, _ -> goToHomeActivity() }
            .show()
    }

    private fun goToHomeActivity() {
        val intent = Intent(this, MainActivity::class.java)
        finishAffinity()
        startActivity(intent)
    }

    private fun openCall(phone_number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${phone_number}")
        startActivity(intent)
    }

    private fun openGoogleMaps(worker: GeoPoint, client: GeoPoint) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(
                "http://maps.google.com/maps?saddr=${
                    worker.latitude.toString().substring(0, 10)
                },${
                    worker.longitude.toString().substring(0, 10)
                }&daddr=${client.latitude},${client.longitude}"
            )
        )
        startActivity(intent)
    }

    private fun confirmCancel() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Cancelar servicio")
            .setMessage("¿Estás seguro de cancelar el servicio?")
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Si, cancelar") { _, _ -> onCancelService() }
            .show()
    }

    private fun confirmFinished() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Finalizar servicio")
            .setMessage("¿Has completado correctamente el servicio?")
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Si, lo he hecho") { _, _ -> onCompletedService() }
            .show()
    }

    private fun confirmArrived() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Confirmación de llegada")
            .setMessage("¿Has llegado a la ubicación?")
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Sí, he llegado") { _, _ -> onArrivedService() }
            .show()
    }


    private fun onCancelService() {
        lifecycleScope.launch {
            hiredServiceViewModel.cancelService(
                idServideHired,
                hiredServiceViewModel.isThereACurrentService.value!!.client_id,
                hiredServiceViewModel.isThereACurrentService.value!!.worker_id,
            )
        }
    }

    private fun onArrivedService() {
        lifecycleScope.launch {
            hiredServiceViewModel.arrivedService(idServideHired)
        }
    }

    private fun onCompletedService() {
        lifecycleScope.launch {
            hiredServiceViewModel.completedService(
                idServideHired,
                hiredServiceViewModel.isThereACurrentService.value!!.client_id,
                hiredServiceViewModel.isThereACurrentService.value!!.worker_id,
            )
        }

        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
        )
            .setTitle("Servicio finalizado")
            .setIcon(R.drawable.ilus_complete)
            .setCancelable(false)
            .setMessage("¡Muy bien! El servicio ha sido finalizado con éxito, bien hecho!.")
            .setPositiveButton("Aceptar") { _, _ -> goToHomeActivity() }
            .show()
    }
}
