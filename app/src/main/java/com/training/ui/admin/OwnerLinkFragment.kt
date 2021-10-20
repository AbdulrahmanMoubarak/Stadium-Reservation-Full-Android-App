package com.training.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.R
import com.training.model.StadiumModel
import com.training.model.UserModel
import com.training.states.SignInState
import com.training.ui.recyclerviews.OwnerLinkAdapter
import com.training.ui.recyclerviews.OwnersAdapter
import com.training.util.validation.ErrorFinder
import com.training.viewmodels.DataRetrieveViewModel
import com.training.viewmodels.EditViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_owner_link.*
import kotlinx.android.synthetic.main.fragment_users.*


@AndroidEntryPoint
class OwnerLinkFragment : Fragment() {

    private val viewModel: DataRetrieveViewModel by viewModels()
    private val editViewModel: EditViewModel by viewModels()

    val adapter = OwnerLinkAdapter()
    private var stadium: StadiumModel? = null

    lateinit var selectedUser: UserModel

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
        return inflater.inflate(R.layout.fragment_owner_link, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter.selectedUser.observe(this, {
            if(it != null) {
                selectedUser = it

                selectedUser.apply {
                    linked = true
                    stadium_key = stadium?.id
                }

                stadium?.let {
                    it.assigned = true
                    it.owner_id = selectedUser.getFirebaseFormat().email
                }

                editViewModel.updateUserData(selectedUser, false)
                stadium?.let {  editViewModel.updateStadiumData(it) }
            }
        })

        owner_link_recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setAdapter(adapter)
        }


        observeUserListLiveData()
        ObserveEditLiveData()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUsersUnlinked()
    }

    private fun observeUserListLiveData() {
        viewModel.retrieveState.observe(this, {data ->
            when(data::class){
                SignInState.Loading::class ->{
                    displayProgressbar(true)
                }

                SignInState.Success::class ->{
                    displayProgressbar(false)
                    val state  = data as SignInState.Success
                    adapter.setItem_List(state.data)
                    owner_link_recyclerView.adapter = adapter
                    txt_err_userList_link.visibility = View.GONE
                }

                SignInState.Error::class ->{
                    adapter.Item_List.clear()
                    owner_link_recyclerView.adapter = adapter
                    val state = data as SignInState.Error
                    displayProgressbar(false)
                    showErrorMsg(state.type)
                }
            }
        })
    }

    private fun ObserveEditLiveData(){
        editViewModel.updateState.observe(this, {data ->
            when(data::class){
                SignInState.Loading::class ->{
                    displayProgressbar(true)
                }

                SignInState.OperationSuccess::class ->{
                    displayProgressbar(false)
                    Log.d("Here", "ObserveEditLiveData: operation success")
                    Toast.makeText(requireContext(), "Successfully linked", Toast.LENGTH_SHORT).show()
                    val bundle = Bundle().apply {
                        putSerializable("stadium", stadium)
                    }
                    findNavController().navigate(R.id.action_ownerLinkFragment_to_stadiumViewFragment, bundle)
                }

                SignInState.Error::class ->{
                    adapter.Item_List.clear()
                    owner_recyclerView.adapter = adapter
                    val state = data as SignInState.Error
                    displayProgressbar(false)
                    showErrorMsg(state.type)
                }
            }
        })
    }

    private fun showErrorMsg(error: Int){
        val error_msg = ErrorFinder.getErrorMsg(error)
        txt_err_userList_link.text = error_msg
        txt_err_userList_link.visibility = View.VISIBLE
    }

    private fun displayProgressbar(isDisplayed:Boolean){
        progress_bar_userList_link.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }

}