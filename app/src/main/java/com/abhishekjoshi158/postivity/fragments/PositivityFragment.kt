package com.abhishekjoshi158.postivity.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhishekjoshi158.postivity.R
import com.abhishekjoshi158.postivity.adapter.PositivityRCAdapter
import com.abhishekjoshi158.postivity.datamodels.PositivityData
import com.abhishekjoshi158.postivity.utilities.LIKE
import com.abhishekjoshi158.postivity.utilities.SAVE
import com.abhishekjoshi158.postivity.utilities.SHARE
import com.abhishekjoshi158.postivity.utilities.getURI
import com.abhishekjoshi158.postivity.viewmodels.HomeScreenViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


class PositivityFragment : Fragment() {
  private val TAG = PositivityFragment::class.java.simpleName
  private lateinit var adapter: PositivityRCAdapter
  private val storageReference : StorageReference = Firebase.storage.reference
  private var viewModel: HomeScreenViewModel? = null
  private var items: MutableList<PositivityData> = mutableListOf()
  private var likes : MutableMap<String,Boolean> = mutableMapOf()
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val view = inflater.inflate(R.layout.fragment_postivity, container, false)
    val rc_view_positivity = view.findViewById<RecyclerView>(R.id.rc_view_positivity)

    rc_view_positivity.layoutManager = LinearLayoutManager(requireContext())
    viewModel = ViewModelProvider(requireActivity()).get(HomeScreenViewModel::class.java)
    adapter = PositivityRCAdapter(items,likes, ::userClickEvent)
    rc_view_positivity.adapter = adapter
    viewModel?.positivity?.observe(viewLifecycleOwner, Observer<List<PositivityData>> {
      items.clear()
      items.addAll(it)
      adapter.notifyDataSetChanged()
    })
    viewModel?.myLike?.observe(viewLifecycleOwner, Observer<Map<String, Boolean>> { myLikeList ->
      likes.clear()
      likes.putAll(myLikeList)
      adapter.notifyDataSetChanged()
      Log.d(TAG,"mylikes ")
      myLikeList.forEach { value ->

        Log.d(TAG,"mylikes ${value.key} ${value.value}")
       }

    })
    return view
  }

  fun userClickEvent(type: String, documentId: String) {
    when (type) {
      LIKE -> {
        viewModel?.updateLike(documentId)
      }
      SHARE -> {
      Toast.makeText(requireContext(),"Share ",Toast.LENGTH_SHORT).show()
        var islandRef = storageReference.child("english/1.png")

        val ONE_MEGABYTE: Long = 1024 * 1024
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {bytes ->
          val mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
          val contentUri = getURI(requireContext(),mBitmap)
          if (contentUri != null ) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri,requireActivity().getContentResolver().getType(contentUri))
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            startActivity(Intent.createChooser(shareIntent, "Choose an app"))
          }
        }.addOnFailureListener {
          // Handle any errors
        }

      }
      SAVE -> {

      }
    }
  }

}
