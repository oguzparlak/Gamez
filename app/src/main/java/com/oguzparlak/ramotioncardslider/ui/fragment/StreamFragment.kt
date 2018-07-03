package com.oguzparlak.ramotioncardslider.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.oguzparlak.ramotioncardslider.R
import com.oguzparlak.ramotioncardslider.getResponseHandler
import com.oguzparlak.ramotioncardslider.helper.interfaces.Error
import com.oguzparlak.ramotioncardslider.helper.interfaces.OnStreamClickListener
import com.oguzparlak.ramotioncardslider.helper.responsehandler.FeaturedStreamsResponseHandler
import com.oguzparlak.ramotioncardslider.helper.responsehandler.JsonResponseHandler
import com.oguzparlak.ramotioncardslider.helper.responsehandler.StreamResponseHandler
import com.oguzparlak.ramotioncardslider.hide
import com.oguzparlak.ramotioncardslider.model.Stream
import com.oguzparlak.ramotioncardslider.model.StreamType
import com.oguzparlak.ramotioncardslider.ui.activity.PlayerActivity
import com.ramotion.cardslider.CardSliderLayoutManager
import com.ramotion.cardslider.CardSnapHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.stream_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class StreamFragment : Fragment(), OnStreamClickListener {

    companion object {
        private const val TAG = "StreamFragment"

        private const val TITLE_KEY = "title_key"

        private const val STREAM_TYPE_KEY = "stream_type_key"

        fun newInstance(title: String, streamType: StreamType): StreamFragment {
            val self = StreamFragment()
            val bundle = Bundle()
            bundle.putString(TITLE_KEY, title)
            bundle.putSerializable(STREAM_TYPE_KEY, streamType)
            self.arguments = bundle
            return self
        }
    }

    private lateinit var mStreamList: ArrayList<Stream>

    private lateinit var mAdapter: StreamAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.stream_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mStreamList = arrayListOf()

        mRecyclerView.layoutManager = CardLayoutManager()
        CardSnapHelper().attachToRecyclerView(mRecyclerView)

        mStreamTitleTextView.text = arguments?.getString(TITLE_KEY)

    }

    /**
     * Fired when a Stream is selected from the List
     */
    override fun onStreamClicked(stream: Stream) {
        // Prepare a PlayerActivity Intent
        val channel = stream.channel.displayName
        val intent = Intent(context!!, PlayerActivity::class.java)
        intent.putExtra("channel_extra", channel)
        startActivity(intent)
    }

    /**
     * Do De-Serialization here
     * Parse Streams
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageReceived(streams: List<Stream>) {
        mFragmentProgressBar.hide()
        mStreamList = streams as ArrayList<Stream>
        mAdapter = StreamAdapter()
        mAdapter.assignOnClickListener(this)
        mRecyclerView.adapter = mAdapter
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onError(error: Error) {
        // TODO Implement it later
        Log.d(TAG, "onError: ${error.message}")
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
    inner class CardLayoutManager: CardSliderLayoutManager(32, 640, 32F)

    /**
     * Simple Adapter class of the RecyclerView
     */
    inner class StreamAdapter : RecyclerView.Adapter<StreamAdapter.ViewHolder>() {

        private var mStreamClickListener: OnStreamClickListener? = null

        fun assignOnClickListener(listener: OnStreamClickListener) {
            mStreamClickListener = listener
        }

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

            private var mStreamerTextView: TextView = itemView.findViewById(R.id.mStreamerTextView)

            private var mGameTextView: TextView = itemView.findViewById(R.id.mGameTextView)

            fun bind(stream: Stream) {
                // Picasso
                Picasso.get().load(stream.preview.large).into(imageView)
                // Streamer
                mStreamerTextView.text = stream.channel.displayName
                // Game
                mGameTextView.text = stream.game
                // Set OnStreamListener
                itemView.setOnClickListener {
                    mStreamClickListener?.onStreamClicked(stream)
                }
            }

        }
    }
}
