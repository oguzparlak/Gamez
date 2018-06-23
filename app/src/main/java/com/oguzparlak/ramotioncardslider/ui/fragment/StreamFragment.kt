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

    private var expanded = false

    private lateinit var mStreamList: ArrayList<Stream>

    private lateinit var mAdapter: StreamAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: ")
        return LayoutInflater.from(context).inflate(R.layout.stream_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: ")

        mStreamList = ArrayList()

        // Request TEST
        val volleyClient = VolleyClient.instance
        volleyClient.prepareWithContext(activity!!)
        volleyClient.addToRequestQueue("https://api.twitch.tv/kraken/streams/?stream_type=all&limit=100")

        mRecyclerView.layoutManager = CardLayoutManager()
        CardSnapHelper().attachToRecyclerView(mRecyclerView)

        // Lottie Test
        mLottieAnimationView.setAnimation(R.raw.active)

        mSearchImageView.setOnClickListener {
            if (!expanded) {
                // Make the Search bar taller with an animation
                val resizeAnimation = ResizeAnimation(mSearchView, activity!!.getFrameWidth())
                resizeAnimation.duration = 400
                mSearchView.startAnimation(resizeAnimation)
                // mSearchStreamEditText.visibility = View.VISIBLE
                // SearchView Animation
                mSearchStreamEditText.applyFadeInAnimation(context!!)
                mSectionTextView.applyFadeOutAnimation(context!!)
                mSearchImageView.setImageResource(R.drawable.ic_close_white_24dp)
                // Show keyboard
                context!!.showSoftKeyboard(mSearchStreamEditText)
                mSearchStreamEditText.requestFocus()
                // mBottomNavigationView.applyFadeOutAnimation(context!!, true)
                expanded = true
            } else {
                val resizeAnimation = ResizeAnimation(mSearchView, 120)
                resizeAnimation.duration = 400
                // mSearchStreamEditText.visibility = View.INVISIBLE
                mSearchView.startAnimation(resizeAnimation)
                mSearchStreamEditText.applyFadeOutAnimation(context!!, true)
                mSectionTextView.applyFadeInAnimation(context!!)
                mSearchImageView.setImageResource(R.drawable.ic_search_white_24dp)
                // mBottomNavigationView.applyFadeInAnimation(context!!)
                context!!.hideKeyboardFrom(mSearchStreamEditText)
                expanded = false
            }
        }

    }

    /**
     * Do De-Serialization here
     * Parse Streams
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageReceived(data: Any) {
        val parser = JsonParser()
        val root = parser.parse(data.toString())
        val streamsArray = root.asJsonObject["streams"].asJsonArray
        for (streamObject in streamsArray) {
            val stream = Gson().fromJson(streamObject, Stream::class.java)
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
    inner class CardLayoutManager: CardSliderLayoutManager(128, 480, 64F)

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
                // Viewer Count
                mViewerCountTextView.text = stream.viewerCount.toK()
                // Game
                mPlayingTextView.text = getString(R.string.playing, stream.game)
                // Display Name
                mDisplayNameTextView.text = stream.channel.displayName
                // Stream Description
                mStreamDescriptionTextView.text = stream.channel.status
                // Live Status
                if (stream.streamType == "live")
                    mLottieAnimationView.setAnimation(R.raw.active)
                else
                    mLottieAnimationView.setAnimation(R.raw.passive)
                if (!mLottieAnimationView.isAnimating) {
                    mLottieAnimationView.playAnimation()
                }

            }

        }
    }
}
