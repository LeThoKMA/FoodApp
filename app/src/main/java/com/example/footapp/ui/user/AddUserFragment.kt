package com.example.footapp.ui.user

import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.base.BaseFragment
import com.example.footapp.databinding.ActivityAddUserBinding

class AddUserFragment :
    BaseFragment<ActivityAddUserBinding, AddUserViewModel>() {

    private var isStaffPicking = true

    override fun getContentLayout(): Int {
        return R.layout.activity_add_user
    }

    override fun initView() {
    }

    override fun initListener() {
        binding?.apply {
            rgRole.setOnCheckedChangeListener { _, checkedId ->
                // Xử lý sự kiện theo RadioButton đã chọn
                when (checkedId) {
                    R.id.rbStaff -> {
                        isStaffPicking = true
                    }

                    R.id.rbShip -> {
                        isStaffPicking = false
                    }
                }
            }

            tvRegister.setOnClickListener {
                viewModel.register(
                    phone = this.edtPhone.text.toString(),
                    name = this.edtName.text.toString(),
                    account = this.edtAccount.text.toString(),
                    pass = this.edtPasswd.text.toString(),
                    passRepeat = this.edtConfirmPasswd.text.toString(),
                    isStaff = isStaffPicking
                )
            }
            imvBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun observerLiveData() {
        viewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }


    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(),
        )[AddUserViewModel::class.java]
    }
}
