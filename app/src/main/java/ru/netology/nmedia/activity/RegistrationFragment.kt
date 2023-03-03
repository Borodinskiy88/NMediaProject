package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.FragmentRegistrationBinding
import ru.netology.nmedia.viewmodel.RegistrationViewModel

class RegistrationFragment : Fragment() {

    lateinit var binding: FragmentRegistrationBinding
    private val viewModel by viewModels<RegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        val photoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    ImagePicker.RESULT_ERROR -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.photo_error),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    else -> {
                        val uri = it.data?.data ?: return@registerForActivityResult
                        viewModel.addPhoto(uri, uri.toFile())
                    }
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.successfulEntry) {
                Snackbar.make(binding.root, R.string.pass_check, Snackbar.LENGTH_LONG)
                    .show()
            }
            if (state.failedEntry) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        binding.clearButton.setOnClickListener {
            viewModel.clearPhoto()
        }

        binding.avatarButton.setOnClickListener {
            MaterialAlertDialogBuilder(this.requireContext())
                .setTitle(resources.getString(R.string.choose))
                .setNegativeButton(R.string.gallery) { _, _ ->
                    ImagePicker.with(this)
                        .galleryOnly()
                        .crop()
                        .compress(192)
                        .createIntent(photoLauncher::launch)
                }
                .setPositiveButton(R.string.photo) { _, _ ->
                    ImagePicker.with(this)
                        .cameraOnly()
                        .crop()
                        .compress(192)
                        .createIntent(photoLauncher::launch)
                }
                .show()
        }


        viewModel.media.observe(viewLifecycleOwner) { mediaAvatar ->
            if (mediaAvatar == null) {
                binding.registrationContainer.isGone = true
                return@observe
            }
            binding.registrationContainer.isVisible = true
            binding.previewAvatar.setImageURI(mediaAvatar.uri)
        }

        binding.registrationButton.setOnClickListener {
            if (binding.loginReg.text.isNullOrEmpty()
                || binding.passwordReg.text.isNullOrEmpty()
                || binding.repeatPasswordReg.text.isNullOrEmpty()
                || binding.nameReg.text.isNullOrEmpty()
            ) {
                Snackbar.make(binding.root, R.string.registration_button, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                viewModel.register(
                    binding.loginReg.text.toString(),
                    binding.passwordReg.text.toString(),
                    binding.repeatPasswordReg.text.toString(),
                    binding.nameReg.text.toString()
                )
            }
        }

        viewModel.registrationSuccess.observe(viewLifecycleOwner) {
            AppAuth.geiInstance().setAuth(it.id, it.token)
            findNavController().navigateUp()
            findNavController().navigateUp()
            Snackbar.make(binding.root, R.string.successful_registration, Snackbar.LENGTH_LONG)
                .show()
        }

        return binding.root
    }
}