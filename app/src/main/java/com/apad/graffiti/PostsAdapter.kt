package com.apad.graffiti.com.apad.graffiti

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.apad.graffiti.R
import com.squareup.picasso.Picasso
import java.io.InputStream
import java.net.URL


//most of this file is code you copy and paste every time you want a scrolling list
//ask me if you have questions for it


public class PostsAdapter(private var activity : Activity, private var content : ArrayList<Array<String>>) : BaseAdapter()
{
    private class ViewHolder(row : View?)
    {
        var txtTitle : TextView? = null;
        var txtCate : TextView? = null;
        var txtUser: TextView? = null;
        var txtDate_Posted : TextView? = null;
        var txtTag : TextView? = null;
        var txtLocation : TextView? = null;
        var imgImage : ImageView? = null;
        var txtText : TextView? = null;
        init {
            this.txtTitle = row?.findViewById(R.id.txtTitle);
            this.txtCate = row?.findViewById(R.id.txtCate);
            this.txtUser = row?.findViewById(R.id.txtUser);
            this.txtDate_Posted = row?.findViewById(R.id.txtDate_Posted);
            this.txtTag = row?.findViewById(R.id.txtTag);
            this.txtLocation = row?.findViewById(R.id.txtLocation);
            this.imgImage = row?.findViewById(R.id.imgImage);
            this.txtText = row?.findViewById(R.id.txtText);
        }
    }

    private inner class ImageLoader(internal var bmImage: ImageView?) : AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg urls: String?): Bitmap? {
            val urldisplay = urls[0]
            var mIcon11: Bitmap? = null
            try {
                val url = java.net.URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(url)
            } catch (e: Exception) {

                e.printStackTrace()
            }

            return mIcon11
        }

        override fun onPostExecute(result: Bitmap?) {
            bmImage?.setImageBitmap(result)
        }
    }

    public override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val viewHolder : ViewHolder
        if(convertView == null)
        {
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.content_card, null);
            viewHolder =
                ViewHolder(view)
            view.tag = viewHolder
        }
        else
        {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val text = content[position][0];
        val location = content[position][1];
        val src = content[position][2];
        val title = content[position][3];
        val date_posted = content[position][4];
        val cate = content[position][5];
        val tag = content[position][6];
        val user = content[position][7];


        viewHolder.txtText?.text = text  // do i do this for src/art/image
        viewHolder.txtLocation?.text = "Location:" + location + "\n"
        viewHolder.txtTitle?.text = title
        viewHolder.txtDate_Posted?.text = date_posted
        viewHolder.txtCate?.text = "Category:" + cate
        viewHolder.txtUser?.text = "User:" + user
        viewHolder.txtTag?.text = "Tag:" + tag

        val id = activity.getResources().getIdentifier(src, "drawable", activity.getPackageName())
        Picasso.with(activity).load(src).into(viewHolder.imgImage);
        return view as View
    }

    public override fun getCount(): Int
    {
        return content.size;
    }

    public override fun getItem(position: Int): Array<String>
    {
        return content[position]
    }

    public override fun getItemId(position: Int): Long
    {
        return position.toLong()
    }

}