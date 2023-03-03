package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.FragmentRegistrationBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.RegistrationViewModel

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private val viewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        val photoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    ImagePicker.RESULT_ERROR -> {
                        Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                            .show()
                    }
                    else -> {
                        val uri = it.data?.data ?: return@registerForActivityResult
                        viewModel.addPhoto(uri, uri.toFile())
                    }
                }
            }

        viewModel.media.observe(viewLifecycleOwner) { media ->
            if (media == null) {
                binding.registrationContainer.isGone = true
                return@observe
            }
            binding.registrationContainer.isVisible = true
            binding.previewAvatar.setImageURI(media.uri)
        }


        binding.registrationButton.setOnClickListener {
            val login = binding.loginReg.text.toString()
            val password = binding.passwordReg.text.toString()
            val repeatPassword = binding.repeatPasswordReg.text.toString()
            val name = binding.nameReg.text.toString()

            if (password == repeatPassword) {
                viewModel.register(login, password, name)
                AndroidUtils.hideKeyboard(requireView())
            } else {
                Snackbar.make(
                    binding.root,
                    R.string.invalid_password,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.successfulEntry) {
                findNavController().navigateUp()
            }

            if (state.isBlank) {
                Snackbar.make(
                    binding.root,
                    R.string.empty_field,
                    Snackbar.LENGTH_LONG
                ).show()
            }

            if (state.error) {
                Snackbar.make(
                    binding.root,
                    R.string.error_loading,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        binding.avatarButton.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(2048)
                .createIntent(photoLauncher::launch)
        }


        binding.clearButton.setOnClickListener {
            viewModel.clearPhoto()
        }
//todo
        viewModel.registrationSuccess.observe(viewLifecycleOwner) { it ->
            AppAuth.geiInstance().setAuth(it.id, it.token)
//            findNavController().navigateUp()
            findNavController().navigateUp()
            Snackbar.make(binding.root, R.string.successful_registration, Snackbar.LENGTH_LONG)
                .show()
        }
//todo
        return binding.root
    }
}