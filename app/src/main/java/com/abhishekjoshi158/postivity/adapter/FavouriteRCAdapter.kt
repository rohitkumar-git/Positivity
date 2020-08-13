package com.abhishekjoshi158.postivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abhishekjoshi158.postivity.MainActivity
import com.abhishekjoshi158.postivity.R
import com.abhishekjoshi158.postivity.datamodels.FavouriteData
import com.abhishekjoshi158.postivity.repository.GlideApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.zip.Inflater


class FavouriteRCAdapterViewHolder(
  v: View,
  val context: Context,
 val userClick: (documentId: String) -> Unit
) : RecyclerView.ViewHolder(v), View.OnClickListener {
  private var positivityImage: ImageView
  private var positivityText: TextView
  private var ll_download: LinearLayout
  private var ll_delete: LinearLayout
  private val storageReference: StorageReference
  private var documentId : String = ""
  init {
    storageReference = Firebase.storage.reference
    positivityImage = v.findViewById(R.id.iv_positivity)
    positivityText = v.findViewById(R.id.tv_positivity)
    ll_download = v.findViewById(R.id.ll_download)
    ll_delete = v.findViewById(R.id.ll_delete)

    ll_download.setOnClickListener(this)
    ll_delete.setOnClickListener(this)

    positivityImage.layoutParams.width = MainActivity.SCREEN_WIDTH
    val height = (4 * MainActivity.SCREEN_WIDTH) / 4
    positivityImage.layoutParams.height = height
    positivityImage.requestLayout()
  }

  fun bind(item: FavouriteData) {
    positivityText.text = item.positivity_text
    val path = "english/${item.image_url}.png"
    GlideApp.with(context).load(storageReference.child(path)).into(positivityImage)
    documentId = item.documentId
  }

  override fun onClick(v: View?) {
    when (v?.id) {
      R.id.ll_download -> {

      }
      R.id.ll_delete -> {
        userClick(documentId)
      }
    }
  }
}

class FavouriteRCAdapter(
  val favouriteItem: List<FavouriteData>,
  val userClick: (documentId: String) -> Unit
) : RecyclerView.Adapter<FavouriteRCAdapterViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteRCAdapterViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater.inflate(R.layout.favourite_item_layout, parent, false)
    return FavouriteRCAdapterViewHolder(view, parent.context,userClick)
  }

  override fun getItemCount(): Int {
    return favouriteItem.size
  }

  override fun onBindViewHolder(holder: FavouriteRCAdapterViewHolder, position: Int) {

    holder.bind(favouriteItem.get(position))

  }

}
