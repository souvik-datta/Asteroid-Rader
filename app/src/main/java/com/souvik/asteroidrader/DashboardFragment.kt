package com.souvik.asteroidrader

import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.souvik.asteroidrader.adapter.RvAdapter
import com.souvik.asteroidrader.databinding.FragmentDashboardBinding
import com.souvik.asteroidrader.model.ApiResponse
import com.souvik.asteroidrader.utils.DownloadDataWorker
import java.util.concurrent.TimeUnit

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private val TAG = DashboardFragment::class.java.simpleName
    private var adapter: RvAdapter? = null
    private var list = ArrayList<ApiResponse>()
    private lateinit var workManager: WorkManager
    private lateinit var sync: PeriodicWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpObserver()
    }

    private fun initView() {
        sync = PeriodicWorkRequestBuilder<DownloadDataWorker>(1, TimeUnit.DAYS)
            .setConstraints(Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresCharging(true)
                .build()
            )
            .build()

        workManager = WorkManager
            .getInstance(requireActivity().applicationContext)

        workManager.enqueueUniquePeriodicWork(
            "sync", ExistingPeriodicWorkPolicy.KEEP,
            sync
        )

        adapter = RvAdapter(list, requireContext(), object : RvAdapter.onClickView {
            override fun onClick(position: Int) {
                val direction =
                    DashboardFragmentDirections.actionDashboardFragmentToDetailsFragment(list[position])
                findNavController().navigate(direction)
            }
        })
        binding.rvShowElements.adapter = adapter
    }

    private fun setUpObserver() {
        viewModel.responseList.observe(viewLifecycleOwner, Observer {
            binding.pbLoader.visibility = View.GONE
            Log.d(TAG, "setUpObserver: $it")
            list.addAll(it)
            Log.d(TAG, "setUpObserver: $list")
            adapter?.notifyDataSetChanged()
        })

        viewModel.imageUrl.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "setUpObserver: $it")
            Glide.with(binding.ivOffDay.context)
                .load(it.split("|")[0])
                .error(resources.getDrawable(R.drawable.ic_image_error))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.pbImage.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.pbImage.visibility = View.GONE
                        return false
                    }

                })
                .into(binding.ivOffDay)
            Log.d(TAG, "setUpObserver: ${it.split("|")[1]}")
            binding.ivOffDay.contentDescription = "Image of the Day: ${it.split("|")[1]}"
        })

        workManager.getWorkInfoByIdLiveData(sync.id).observe(viewLifecycleOwner, Observer {
            if (it.state == WorkInfo.State.ENQUEUED) {
                viewModel.loadDataFromDb()
            }
        })
    }
}