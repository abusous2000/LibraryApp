package com.sample.libraryapplication.view


import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.sample.libraryapplication.R
import com.sample.libraryapplication.view.BookListActivity.DataModel


class DrawerItemCustomAdapter(mContext: Context, var layoutResourceId: Int, data: List<DataModel>) :
      ArrayAdapter<DataModel>(mContext, layoutResourceId, data) {
    var mContext: Context
    var data: List<DataModel>

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var listItem: View = convertView!!
        val inflater = (mContext as Activity).layoutInflater
        var listItem = inflater.inflate(layoutResourceId, parent, false)
        val imageViewIcon: ImageView = listItem.findViewById(R.id.imageViewIcon) as ImageView
        val textViewName = listItem.findViewById(R.id.textViewName) as TextView
        val (icon, name) = data[position]

        imageViewIcon.setImageResource(icon)
        textViewName.text = name

        return listItem
    }

    init {
        this.mContext = mContext
        this.data = data
    }
}
