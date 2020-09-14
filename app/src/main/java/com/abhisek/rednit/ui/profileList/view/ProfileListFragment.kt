package com.abhisek.rednit.ui.profileList.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.abhisek.rednit.R
import com.abhisek.rednit.databinding.ProfileListFragmentBinding
import com.abhisek.rednit.ui.payment.view.PaymentDialog
import com.abhisek.rednit.ui.profileList.adapter.ProfileListAdapter
import com.abhisek.rednit.viewmodel.ProfilesViewModel
import com.yuyakaido.android.cardstackview.*

class ProfileListFragment : Fragment(), CardStackListener {

    private val viewModel: ProfilesViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            activity,
            ProfilesViewModel.Factory(activity.application)
        ).get(ProfilesViewModel::class.java)
    }

    private lateinit var binding: ProfileListFragmentBinding

    private val manager by lazy { CardStackLayoutManager(context, this) }

    private val adapter by lazy { ProfileListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.profile_list_fragment, container, false)

        binding.profileViewModel = viewModel
        binding.lifecycleOwner = this

        initialize()

        viewModel.eventLikeButtonClicked.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                swipeRight()
            }
        })

        viewModel.eventSkipButtonClicked.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                swipeLeft()
            }
        })

        viewModel.eventUndoButtonClicked.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                undoSwipe()
            }
        })

        viewModel.profiles.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        viewModel.eventShowPaywall.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { show ->
                if (show) {
                    PaymentDialog().show(childFragmentManager, "payment")
                }
            }
        })


        return binding.root
    }

    private fun initialize() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = adapter
        binding.cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun swipeLeft() {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Left)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
        manager.setSwipeAnimationSetting(setting)
        binding.cardStackView.swipe()
    }

    private fun swipeRight() {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Right)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
        manager.setSwipeAnimationSetting(setting)
        binding.cardStackView.swipe()
    }

    private fun undoSwipe() {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Bottom)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(DecelerateInterpolator())
            .build()
        manager.setSwipeAnimationSetting(setting)
        binding.cardStackView.rewind()
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: ${manager.topPosition}")
    }

    override fun onCardSwiped(direction: Direction?) {
        Log.d("CardStackView", "onCardSwiped: ${manager.topPosition}")
        direction?.let {
            if (it == Direction.Right) {
                // liked
                viewModel.addLikedProfile(adapter.currentList.get(manager.topPosition - 1))
            } else {
                //skipped
                viewModel.addSkippedProfile(adapter.currentList.get(manager.topPosition - 1))
            }
        }
    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
        viewModel.undoLastActionProfile(adapter.currentList.get(manager.topPosition))
    }

    override fun onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View?, position: Int) {
        Log.d(
            "CardStackView",
            "onCardAppeared: ${manager.topPosition} gender: ${adapter.currentList.get(position)?.gender}"
        )
    }

    override fun onCardDisappeared(view: View?, position: Int) {
        Log.d("CardStackView", "onCardDisappeared: ${manager.topPosition}")
    }
}