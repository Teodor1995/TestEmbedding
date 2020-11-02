package com.fedor_zavalnyj.testembedding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.first_fragment.*


class FirstFragment : Fragment() {

    companion object {
        const val engineId = "my_engine_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.first_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnStartFlutterActivity.setOnClickListener {
            startActivity(
                FlutterActivity.createDefaultIntent(requireContext())
            )
        }

        /**Предпрогрев flutter engine*/
        val flutterEngine = FlutterEngine(requireContext());
        flutterEngine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
        FlutterEngineCache
            .getInstance()
            .put(engineId, flutterEngine)
        /**    ******             */

        btnStartPreWarmedFlutterActivity.setOnClickListener {
            startActivity(
                FlutterActivity
                    .withCachedEngine(engineId)
                    .build(requireContext())
            )
        }

        btnNext.setOnClickListener {
            (requireActivity() as MainActivity).addFragment(SecondFragment())
        }
    }

}
