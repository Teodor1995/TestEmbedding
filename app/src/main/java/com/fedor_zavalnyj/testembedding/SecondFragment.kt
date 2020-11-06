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

    private lateinit var methodChannel: MethodChannel

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

        registerEnginesAndChannels()

        btnStartFlutterFragment.setOnClickListener {
            val flutterFragment: Fragment = createDefault()
            requireActivity().addFragment(flutterFragment)
        }

        btnStartPreWarmedFlutterFragmentWhite.setOnClickListener {
            val flutterFragment: FlutterFragment = getFlutterFragmentInstance(engineId_white)
            requireActivity().addFragment(flutterFragment)
            pushMessageToFlutterAfterLaunch()
        }

        btnStartPreWarmedFlutterFragmentBlue.setOnClickListener {
            val flutterFragment: FlutterFragment = getFlutterFragmentInstance(engineId_blue)
            requireActivity().addFragment(flutterFragment)
        }

        btnStartFlutterViewWhite.setOnClickListener {
            val flutterFragment: FlutterFragment = getFlutterFragmentInstance(engineId_white)
            addView(flutterFragment)
            pushMessageToFlutterAfterLaunch()
        }
    }

    private fun registerEnginesAndChannels() {
        /**      Предпрогрев flutter engines      */
        initFlutterEngine(engineId_blue, "/blue_screen")
        val engineWhiteScreen = initFlutterEngine(engineId_white, "/white_screen")

        /** канал для общения с модулем (для white screen)*/
        val CHANNEL = "ru.test.embedding/hello"
        methodChannel = MethodChannel(
            engineWhiteScreen.dartExecutor.binaryMessenger,
            CHANNEL
        )
        methodChannel.setMethodCallHandler { call, result ->
            val messageText = "${call.method} ${call.arguments}"
            showToast(messageText)
            tvAnswer.text = messageText
        }

        /** PIGEON */
        Pigeon.Api.setup(engineWhiteScreen.dartExecutor.binaryMessenger, this)
        /**  ******   */
    }

    private fun getFlutterFragmentInstance(engineId: String): FlutterFragment {
        return withCachedEngine(engineId)
            .shouldAttachEngineToActivity(false)
            .build()
    }

    private fun initFlutterEngine(engineId: String, initialRoute: String = ""): FlutterEngine {
        val engine = FlutterEngine(requireContext())
        if (initialRoute.isNotEmpty())
            engine.navigationChannel.setInitialRoute(initialRoute)
        engine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
        FlutterEngineCache
            .getInstance()
            .put(engineId, engine)
        return engine
    }

    /**Отправим команду из натива, например пришла команда от сервера и надо передать ее во флаттер*/
    private fun pushMessageToFlutterAfterLaunch() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(TimeUnit.SECONDS.toMillis(3))
            //Methods marked with @UiThread must be executed on the main thread.
            methodChannel.invokeMethod("hello", "from android")
        }
    }

    override fun notifyNative(bundle: Pigeon.Bundle) {
        showToast("flutter вернул ${bundle.count}")
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}
