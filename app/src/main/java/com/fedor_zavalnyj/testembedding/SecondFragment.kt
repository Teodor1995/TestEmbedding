package com.fedor_zavalnyj.testembedding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import kotlinx.android.synthetic.main.second_fragment.*


class SecondFragment : Fragment() {

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
            (requireActivity() as MainActivity).addFragment(flutterFragment)
        }

        /**      Предпрогрев flutter engine      */
        val flutterEngine = FlutterEngine(requireContext());
        flutterEngine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
        FlutterEngineCache
            .getInstance()
            .put(engineId, flutterEngine)
        /**          ******             */

        btnStartPreWarmedFlutterFragment.setOnClickListener {
            val flutterFragment: FlutterFragment =
                FlutterFragment.withCachedEngine(engineId).build()
            (requireActivity() as MainActivity).addFragment(flutterFragment)
        }
    }

}
