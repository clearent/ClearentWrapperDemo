package com.example.clearentwrapperdemo.feature

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.clearent.idtech.android.wrapper.ClearentWrapper
import com.clearent.idtech.android.wrapper.ui.util.checkPermissionsToRequest
import com.example.clearentwrapperdemo.R
import com.example.clearentwrapperdemo.data.ClearentDemoDataSource
import com.example.clearentwrapperdemo.data.DataStatus
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val pairingLoadingStatusTitle = "Pairing status"
    }

    private val clearentWrapper = ClearentWrapper.getInstance()

    private val viewModel by viewModels<MainViewModel>()

    private val multiplePermissionsContract = ActivityResultContracts.RequestMultiplePermissions()
    private val multiplePermissionsLauncher =
        registerForActivityResult(multiplePermissionsContract) {
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askPermissions()

        setupSdkListener()
        setupViewModel()

        navigateToHomeScreen()
    }

    private fun askPermissions() {
        multiplePermissionsLauncher.launch(checkPermissionsToRequest(context = applicationContext))
    }

    private fun setupSdkListener() {
        clearentWrapper.setListener(ClearentDemoDataSource)
    }

    private fun setupViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataFlow.collect { state ->
                    handleStateChanged(state)
                }
            }
        }
    }

    private fun handleStateChanged(state: DataStatus) {

        val screen = when (state) {
            is DataStatus.ReadersList -> PairingFragment(
                state,
                {
                    viewModel.pairReader(it)
                    handleStateChanged(
                        DataStatus.LoadingStatus(
                            pairingLoadingStatusTitle,
                            "Pairing ongoing"
                        )
                    )
                },
                {
                    viewModel.stopSearching()
                    navigateToHomeScreen()
                }
            )
            is DataStatus.LoadingStatus -> LoadingFragment(state)
            is DataStatus.ResultMessage -> ResultFragment(state) {
                navigateToHomeScreen()
            }
        }

        navigateToScreen(screen)
    }

    private fun navigateToScreen(screen: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, screen)
        }
    }

    private fun navigateToHomeScreen() {
        navigateToScreen(
            HomeFragment(
                {
                    handleStateChanged(DataStatus.ReadersList(listOf()))
                    viewModel.searchReaders()
                },
                { viewModel.startTransaction(it) }
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        clearentWrapper.removeListener()
    }
}