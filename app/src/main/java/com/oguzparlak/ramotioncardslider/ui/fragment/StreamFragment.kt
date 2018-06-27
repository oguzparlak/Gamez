package com.oguzparlak.ramotioncardslider.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.oguzparlak.ramotioncardslider.R
import com.oguzparlak.ramotioncardslider.helper.responsehandler.FeaturedStreamsResponseHandler
import com.oguzparlak.ramotioncardslider.helper.responsehandler.JsonResponseHandler
import com.oguzparlak.ramotioncardslider.helper.responsehandler.StreamResponseHandler
import com.oguzparlak.ramotioncardslider.hide
import com.oguzparlak.ramotioncardslider.model.Stream
import com.ramotion.cardslider.CardSliderLayoutManager
import com.ramotion.cardslider.CardSnapHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.stream_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class StreamFragment : Fragment() {

    enum class StreamType {
        FeaturedStreamType, AllStreams;
    }

    private fun getResponseHandler(type: StreamType, root: JsonElement): JsonResponseHandler<Stream>? {
        return when (type) {
            StreamType.FeaturedStreamType -> FeaturedStreamsResponseHandler(root)
            StreamType.AllStreams -> StreamResponseHandler(root)
        }
    }

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

    private lateinit var mStreamType: StreamType

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
     * Do De-Serialization here
     * Parse Streams
     * TODO Fix Here, This block should work with any type of response
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageReceived(data: Any) {
        val parser = JsonParser()
        val root = parser.parse(data.toString())
        mFragmentProgressBar.hide()
        mStreamType = arguments?.getSerializable(STREAM_TYPE_KEY) as StreamType
        val responseHandler = getResponseHandler(mStreamType, root)
        mStreamList = responseHandler?.handle() as ArrayList<Stream>
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
    inner class CardLayoutManager: CardSliderLayoutManager(32, 640, 32F)

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

            private var mStreamerTextView: TextView = itemView.findViewById(R.id.mStreamerTextView)

            private var mGameTextView: TextView = itemView.findViewById(R.id.mGameTextView)

            fun bind(stream: Stream) {
                // Picasso
                Picasso.get().load(stream.preview.large).into(imageView)
                // Streamer
                mStreamerTextView.text = stream.channel.displayName
                // Game
                mGameTextView.text = stream.game
            }

        }
    }
}
