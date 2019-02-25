package com.example.placesdemo.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.placesdemo.R
import com.example.placesdemo.responce.PlacesResponce
import com.example.placesdemo.responce.Result
import io.realm.RealmResults
import java.util.ArrayList


class ResultListAdapter(context: Context, resultList: ArrayList<Result>) :
    RecyclerView.Adapter<ResultListAdapter.ViewHolder>(), View.OnClickListener {

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var viewDataBinding: ViewDataBinding
    val mContext = context
    val mResultList = resultList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultListAdapter.ViewHolder {
        // val layoutInflater = LayoutInflater.from(mContext)
        val layoutInflater = LayoutInflater.from(mContext)
        viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_card, parent, false)
        return ResultListAdapter.ViewHolder(viewDataBinding.root)

    }

    override fun getItemCount(): Int {

        return mResultList.size

    }

    override fun onBindViewHolder(holder: ResultListAdapter.ViewHolder, position: Int) {


        val item = mResultList!![position]
        holder.bindItem(item, position)


    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = DataBindingUtil.bind<ViewDataBinding>(itemView)
        val mPropertyImage = itemView.findViewById<ImageView>(R.id.image_Property)
        val mTitleName = itemView.findViewById<TextView>(R.id.text_Title)
        val mAddress = itemView.findViewById<TextView>(R.id.address)
        val mRootCV = itemView.findViewById<CardView>(R.id.list_item_holder_cv)
        fun bindItem(item: Result, position: Int) {

            if (item.icon!! != null)
                GlideManager.loadImage(item.icon!!, mPropertyImage)

            mTitleName.text = item.name
            mAddress.text = item.vicinity
            // mPhoneNumber.text = item.

        }


    }
}