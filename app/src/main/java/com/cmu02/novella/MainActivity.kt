package com.cmu02.novella

import ai.pleos.playground.vehicle.Vehicle
import ai.pleos.playground.vehicle.api.model.area.DoorArea
import ai.pleos.playground.vehicle.constant.area.DoorValue
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var vehicle: Vehicle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text = TextView(this).apply { textSize = 32f }
        setContentView(text)

        vehicle = Vehicle(this)
        vehicle.initialize()

        vehicle.getDoor().getCurrentDoorPosition(
            area = DoorArea(DoorValue.ROW_1_LEFT),
            onComplete = { pos: Int? ->
                Log.d(TAG, "door position: $pos")
                // 콜백 스레드가 문서에 명시되지 않아 UI 스레드로 넘긴다
                runOnUiThread { text.text = "운전석 도어 위치: $pos" }
            },
            onFailed = { e: Exception ->
                // shortcut: 2.3.2 AAR에서 VehicleException이 난독화되어 is 검사 불가, errorCode 분기는 SDK 수정 후
                Log.e(TAG, "getCurrentDoorPosition failed", e)
                runOnUiThread { text.text = "조회 실패: ${e.message}" }
            }
        )
    }

    override fun onDestroy() {
        vehicle.release()
        super.onDestroy()
    }

    private companion object {
        const val TAG = "Novella"
    }
}
