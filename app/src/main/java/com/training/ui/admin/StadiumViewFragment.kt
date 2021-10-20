package com.training.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.training.R
import com.training.model.StadiumModel
import com.training.states.SignInState
import com.training.util.constants.AccessPrivilege
import com.training.util.validation.ErrorFinder
import com.training.viewmodels.DataRetrieveViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_stadium_view.*
import kotlinx.android.synthetic.main.fragment_user_view.*

@AndroidEntryPoint
class StadiumViewFragment : Fragment() {
    private var stadium: StadiumModel? = null

    private val viewModel: DataRetrieveViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            stadium = it.getSerializable("stadium") as StadiumModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stadium_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()

        stadium_view_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        stadium_view_link.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("stadium", stadium)
            }
            findNavController().navigate(R.id.action_stadiumViewFragment_to_ownerLinkFragment, bundle)
        }

        if (stadium != null) {
            stadium?.let { stadium_view_name.text = it.name }
            stadium?.let { stadium_view_location.text = it.location_str }
            var hasUser = false
            stadium?.let { hasUser = it.assigned }
            if (hasUser) {
                user_exist_card.visibility = View.VISIBLE
                user_notExist_card.visibility = View.GONE
                stadium?.let { viewModel.getUserByEmail(it.owner_id) }
            }
        } else {
            stadium_view_data.visibility = View.GONE
            stadium_view_error.visibility = View.VISIBLE
        }
    }



    private fun observeLiveData() {
        viewModel.user_retrieveState.observe(this, { data ->
            when (data::class) {
                SignInState.Success::class -> {
                    val state = data as SignInState.Success
                    stadium_owner_name.text = state.data.first_name + " " + state.data.last_name
                    stadium_owner_phone.text = state.data.phone
                }

                SignInState.Error::class -> {
                    Log.d("Here", "subscribeLiveData: Error")
                    val state = data as SignInState.Error
                    stadium_owner_name.text = ErrorFinder.getErrorMsg(state.type)
                    stadium_owner_phone.text = ""
                }
            }
        })
    }
}