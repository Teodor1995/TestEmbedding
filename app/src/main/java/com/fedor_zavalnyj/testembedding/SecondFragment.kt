package com.fedor_zavalnyj.testembedding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import dev.flutter.pigeon.Pigeon
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import kotlinx.android.synthetic.main.second_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class SecondFragment : FlutterFragment(), Pigeon.Api {

    companion object {
        const val engineId_white = "my_engine_id_white"
        const val engineId_blue = "my_engine_id_blue"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.second_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnStartFlutterFragment.setOnClickListener {
            val flutterFragment: Fragment = createDefault()
            requireActivity().addFragment(flutterFragment)
        }

        /**      Предпрогрев flutter engines      */
        val flutterEngineForWhiteScreen = FlutterEngine(requireContext())
        flutterEngineForWhiteScreen.navigationChannel.setInitialRoute("/white_screen");
        flutterEngineForWhiteScreen.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
        FlutterEngineCache
            .getInstance()
            .put(engineId_white, flutterEngineForWhiteScreen)

        val flutterEngineForBlueScreen = FlutterEngine(requireContext())
        flutterEngineForBlueScreen.navigationChannel.setInitialRoute("/blue_screen");
        flutterEngineForBlueScreen.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
        FlutterEngineCache
            .getInstance()
            .put(engineId_blue, flutterEngineForBlueScreen)


        /** канал для общения с модулем (для white screen)*/
        val CHANNEL = "ru.test.embedding/hello"
        val methodChannel = MethodChannel(
            flutterEngineForWhiteScreen.dartExecutor.binaryMessenger,
            CHANNEL
        )
        methodChannel.setMethodCallHandler { call, result ->
            val messageText = "${call.method} ${call.arguments}"
            showToast(messageText)
            tvAnswer.text = messageText
        }


        /** PIGEON */
        Pigeon.Api.setup(flutterEngineForWhiteScreen.dartExecutor.binaryMessenger, this)
        /**  ******   */

        btnStartPreWarmedFlutterFragmentWhite.setOnClickListener {
            val flutterFragment: FlutterFragment =
                withCachedEngine(engineId_white)
                    .shouldAttachEngineToActivity(false)
                    .build()
            requireActivity().addFragment(flutterFragment)
            GlobalScope.launch(Dispatchers.Main) {
                delay(TimeUnit.SECONDS.toMillis(3))
                //Methods marked with @UiThread must be executed on the main thread.
                methodChannel.invokeMethod("hello", "from android")
            }
        }

        btnStartPreWarmedFlutterFragmentBlue.setOnClickListener {
            val flutterFragment: FlutterFragment =
                withCachedEngine(engineId_blue)
                    .shouldAttachEngineToActivity(false)
                    .build()
            requireActivity().addFragment(flutterFragment)
        }

        btnStartFlutterViewWhite.setOnClickListener {
            val flutterFragment: FlutterFragment =
                withCachedEngine(engineId_white)
                    .shouldAttachEngineToActivity(false)
                    .build()
            addView(flutterFragment)
            GlobalScope.launch(Dispatchers.Main) {
                delay(TimeUnit.SECONDS.toMillis(3))
                //Methods marked with @UiThread must be executed on the main thread.
                methodChannel.invokeMethod("hello", "from android")
            }
        }
    }

    override fun notifyNative(bundle: Pigeon.Bundle) {
        showToast("flutter вернул ${bundle.count}")
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


}
