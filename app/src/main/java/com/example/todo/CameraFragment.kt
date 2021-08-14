package com.example.todo

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.todo.databinding.FragmentCameraBinding
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File
import java.lang.Exception
import java.util.concurrent.ExecutorService

class CameraFragment : Fragment() {

    var _binding:FragmentCameraBinding? = null
    val binding get() = _binding!!

    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCameraBinding.inflate(layoutInflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startCamera()

        binding.captureButton.setOnClickListener{
            takePhoto()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun takePhoto() {
        TODO("Not yet implemented")
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider:ProcessCameraProvider = cameraProviderFuture.get()

            //connect to out viewFinder on xml
            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(viewfinder.surfaceProvider) }

            //Default camera set to back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this,cameraSelector,preview
                )
            }catch (e:Exception){
                Log.e(TAG,"Binding Failed",e)
            }
        },ContextCompat.getMainExecutor(requireContext()))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    private fun getOutputDirectory(): File {
//        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
//            File(it,resources.getString(R.string.app_name)).apply {mkdirs()}
//        }
//        if (mediaDir!=null && mediaDir.exists()){
//            return mediaDir
//        }
//    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}