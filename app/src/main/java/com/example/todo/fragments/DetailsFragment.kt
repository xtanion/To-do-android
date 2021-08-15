package com.example.todo.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.todo.R
import com.example.todo.TodoViewModel
import com.example.todo.data.TodoEntity
import com.example.todo.databinding.FragmentDetailsBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailsFragment : Fragment() {

    var _binding: FragmentDetailsBinding? = null
    val binding get() = _binding!!
    private val mViewModel: TodoViewModel by activityViewModels()
    val args:DetailsFragmentArgs by navArgs()
    var bmp: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(layoutInflater,container,false)
        val view =  binding.root

        return view
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val entity = args.dataEntity!!
        bmp = entity.bitmap

        activity?.findViewById<BottomAppBar>(R.id.bottom_appbar)?.visibility = View.GONE
        activity?.findViewById<FloatingActionButton>(R.id.floatingActionButtonMain)?.visibility = View.GONE

        binding.detailedItemTitle.setText( args.dataEntity?.title.toString())
        binding.detailedDescription.setText(args.dataEntity?.description)
        binding.datetimeDetails.text = "Last Updated: ${entity.dateTime}"

        if(args.dataEntity?.important == true){
            binding.importantSign.imageTintList = (ColorStateList.valueOf(ContextCompat.getColor(
                requireContext(), R.color.red
            )))
        }
        if(args.dataEntity?.completed==true){
            binding.checkbox.isChecked = true
        }

        //Picks Image From Ext. Storage
        val imageAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri->
                binding.image1.setImageURI(uri)
                val stream = activity?.contentResolver?.openInputStream(uri)
                bmp = BitmapFactory.decodeStream(stream)
            }
        )

        binding.galleryIcon.setOnClickListener {
            if (bmp==null) {
                requestPermission()
                imageAction.launch("image/*")
                //bmp = binding.image1.drawable.toBitmap(250, 250)
                binding.imageCard.visibility = View.VISIBLE
                //binding.image1.setImageBitmap(bmp)
            }else{
                Toast.makeText(context,"Image already exist, Remove to add new one",Toast.LENGTH_SHORT).show()
            }
        }

        if (bmp!=null){
            Log.d("BitmapDetails",bmp.toString())
            binding.apply {
                image1.setImageBitmap(bmp)
                image1.invalidate()
                imageCard.visibility = View.VISIBLE
            }
        }

        //Picks Image From Camera
        val getImageFromCamera = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                if (it.data!=null){
                    val bundle = it.data!!.extras
                    val bitmap:Bitmap = bundle?.get("data") as Bitmap
                    binding.image1.setImageBitmap(bitmap)
                }
            }
        )

        binding.cameraIcon.setOnClickListener {
            if(bmp==null) {
                requestPermission()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                getImageFromCamera.launch(intent)
            }else{
                Toast.makeText(context,"Image Already Exists",Toast.LENGTH_SHORT).show()
            }
        }

        binding.updateTickButton.setOnClickListener {
            val title:String = binding.detailedItemTitle.text.toString()
            val description:String = binding.detailedDescription.text.toString()
            val importance:Boolean = args.dataEntity!!.important
            val check: Boolean = binding.checkbox.isChecked
            //Add more later on (links,images,importance,etc)

            val updatedUnit:TodoEntity = TodoEntity(entity.id,title,description,importance,check,entity.groupName,entity.dateTime,entity.nestedTodo,bmp)
            mViewModel.updateTodo(updatedUnit)
            //Toast.makeText(context,updatedUnit.toString(),Toast.LENGTH_SHORT).show()
            Log.d("BMP",updatedUnit.toString())
            val action = DetailsFragmentDirections.actionDetailsFragmentToMydayFragment()
            Navigation.findNavController(view).navigate(action)
        }

        binding.navigateBack.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

    }

    private fun hasStorageReadPermission() = ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
    private fun hasStorageWritePermission() = ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
    private fun hasCameraPermission() = ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED
    private fun requestPermission(){
        val permissionList = mutableListOf<String>()
        if(!hasStorageReadPermission()){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!hasStorageWritePermission()){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!hasCameraPermission()){
            permissionList.add(Manifest.permission.CAMERA)
        }

        if (permissionList.isNotEmpty()){
            ActivityCompat.requestPermissions(requireActivity(),permissionList.toTypedArray(),0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==0 && grantResults.isNotEmpty()){
            grantResults.forEach {
                if (it==PackageManager.PERMISSION_GRANTED){
                    Log.d("PermissionsRequest","Permission Granted")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}