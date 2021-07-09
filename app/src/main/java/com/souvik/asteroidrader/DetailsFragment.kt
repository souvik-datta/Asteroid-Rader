package com.souvik.asteroidrader

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.databinding.DataBindingUtil
import com.souvik.asteroidrader.databinding.FragmentDashboardBinding
import com.souvik.asteroidrader.databinding.FragmentDetailsBinding
import com.souvik.asteroidrader.model.ApiResponse

class DetailsFragment : Fragment() {
    private lateinit var details: ApiResponse
    private lateinit var binding: FragmentDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        details = DetailsFragmentArgs.fromBundle(requireArguments()).details
        Log.d("TAG", "onCreate: $details")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.data = details
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvHazardData.text =
            if (details.isPotentiallyHazard) {
                binding.ivAsteroid.setImageDrawable(requireActivity().getDrawable(R.drawable.dangerous_asteroid))
                "Potentially Hazard"
            }
            else {
                binding.ivAsteroid.setImageDrawable(requireActivity().getDrawable(R.drawable.good_asteroid))
                "Not Hazardous"
            }
    }
}
