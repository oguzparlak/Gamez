package com.oguzparlak.ramotioncardslider.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.oguzparlak.ramotioncardslider.*
import com.oguzparlak.ramotioncardslider.helper.FeaturedStreamsQuery
import com.oguzparlak.ramotioncardslider.helper.FeaturedStreamsQueryBuilder
import com.oguzparlak.ramotioncardslider.helper.StreamQuery
import com.oguzparlak.ramotioncardslider.helper.StreamQueryBuilder
import com.oguzparlak.ramotioncardslider.model.Stream
import com.ramotion.cardslider.CardSliderLayoutManager
import com.ramotion.cardslider.CardSnapHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.stream_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class StreamFragment : Fragment() {

    companion object {
        private const val TAG = "StreamFragment"
    }

    private lateinit var mStreamList: ArrayList<Stream>

    private lateinit var mAdapter: StreamAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: ")
        return LayoutInflater.from(context).inflate(R.layout.stream_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mStreamList = ArrayList()

        // Request TEST
        val volleyClient = VolleyClient.instance
        volleyClient.prepareWithContext(activity!!)

        // Build a Stream Url
        val url = FeaturedStreamsQueryBuilder().getQuery(FeaturedStreamsQuery())
        volleyClient.addToRequestQueue(url)

        mRecyclerView.layoutManager = CardLayoutManager()
        CardSnapHelper().attachToRecyclerView(mRecyclerView)

    }

    /**
     * Do De-Serialization here
     * Parse Streams
     * TODO Fix Here, This block should work with any type of response
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageReceived(data: Any) {
        val parser = JsonParser()
        val root = parser.parse(data.toString())
        val streamsArray = root.asJsonObject["featured"].asJsonArray
        for (streamObject in streamsArray) {
            val temp = streamObject.asJsonObject["stream"].asJsonObject
            val stream = Gson().fromJson(temp, Stream::class.java)
            Log.d(TAG, "onMessageReceived: stream: $stream")
            mStreamList.add(stream)
        }
        mAdapter = StreamAdapter()
        mRecyclerView.adapter = mAdapter
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    /**
     * Customized CardLayoutManager
     */
    inner class CardLayoutManager: CardSliderLayoutManager(16, 640, 32F)

    /**
     * Simple Adapter class of the RecyclerView
     */
    inner class StreamAdapter : RecyclerView.Adapter<StreamAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val rootView = LayoutInflater.from(context).inflate(R.layout.card_layout, parent, false)
            return ViewHolder(rootView)
        }

        override fun getItemCount(): Int {
            return mStreamList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // bind stream to view
            holder.bind(mStreamList[position])
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private var imageView: ImageView = itemView.findViewById(R.id.mCardImageView)

            fun bind(stream: Stream) {
                // Picasso
                Picasso.get().load(stream.preview.large).into(imageView)
            }

        }
    }
}
