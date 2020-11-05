package com.fedor_zavalnyj.testembedding

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import dev.flutter.pigeon.Pigeon
import io.flutter.embedding.android.DrawableSplashScreen
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.SplashScreen
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
        const val engineId = "my_engine_id"
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
            val flutterFragment: Fragment = FlutterFragment.createDefault()
            requireActivity().addFragment(flutterFragment)
        }

        /**      Предпрогрев flutter engine      */
        val flutterEngine = FlutterEngine(requireContext())
        flutterEngine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
        FlutterEngineCache
            .getInstance()
            .put(engineId, flutterEngine)


        /** канал для общения с модулем */
        val CHANNEL = "ru.test.embedding/hello"
        val methodChannel = MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        )
        methodChannel.setMethodCallHandler { call, result ->
            val messageText = "${call.method} ${call.arguments}"
            showToast(messageText)
            tvAnswer.text = messageText
        }


        /** PIGEON */
        Pigeon.Api.setup(flutterEngine.dartExecutor.binaryMessenger, this)
        /**  ******   */
        val flutterFragment: FlutterFragment =
            withCachedEngine(engineId)
                .shouldAttachEngineToActivity(false)
                .build()


        btnStartPreWarmedFlutterFragment.setOnClickListener {
            requireActivity().addFragment(flutterFragment)
            GlobalScope.launch(Dispatchers.Main) {
                delay(TimeUnit.SECONDS.toMillis(3))
                //Methods marked with @UiThread must be executed on the main thread.
                methodChannel.invokeMethod("hello", "from android")
            }
        }

        btnStartFlutterView.setOnClickListener {
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
