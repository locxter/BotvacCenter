package com.github.locxter.botvaccenter.gui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.locxter.botvaccenter.gui.components.InfoDialog
import com.github.locxter.botvaccenter.gui.components.Label
import com.github.locxter.botvaccenter.gui.components.MapVisualization
import com.github.locxter.botvaccenter.gui.components.Navigation
import com.github.locxter.botvaccenter.gui.components.Title
import com.github.locxter.botvaccenter.lib.BotvacController
import com.github.locxter.botvaccenter.lib.SettingsController
import com.github.locxter.botvaccenter.model.Botvac
import com.github.locxter.botvaccenter.model.EStatus
import com.github.locxter.botvaccenter.model.Map
import com.github.locxter.botvaccenter.model.Point
import com.github.locxter.botvaccenter.model.Scan
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class MappingScreen(
    val botvacController: BotvacController
) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val fileSaver = rememberFileSaverLauncher { }
        val status by remember { mutableStateOf(botvacController.status) }
        var mappingEnabled by remember { mutableStateOf(false) }
        var botvac by remember { mutableStateOf(botvacController.botvac.getDeepCopy()) }
        var showLoadingPopup by remember { mutableStateOf(false) }
        var showErrorPopup by remember { mutableStateOf(false) }
        /*
        LifecycleEffect(onStarted = {
            botvacController.botvac.map.points.clear()
            botvacController.botvac.map.points.addAll(
                arrayOf(
                    Point(0, 2984),
                    Point(-107, 2972),
                    Point(-216, 2990),
                    Point(-316, 2911),
                    Point(-402, 2764),
                    Point(-487, 2669),
                    Point(-567, 2576),
                    Point(-562, 2163),
                    Point(-644, 2155),
                    Point(-717, 2115),
                    Point(-804, 2118),
                    Point(-909, 2157),
                    Point(-1337, 1966),
                    Point(-1324, 1871),
                    Point(-1201, 1195),
                    Point(-1211, 1119),
                    Point(-1208, 1034),
                    Point(-1191, 943),
                    Point(-1183, 865),
                    Point(-1171, 790),
                    Point(-1160, 720),
                    Point(-1151, 655),
                    Point(-1138, 565),
                    Point(-1130, 483),
                    Point(-1204, 469),
                    Point(-996, 330),
                    Point(-988, 267),
                    Point(-977, 188),
                    Point(-961, 112),
                    Point(-939, 39),
                    Point(-697, -129),
                    Point(-685, -189),
                    Point(-1628, -380),
                    Point(-1080, -322),
                    Point(-1573, -665),
                    Point(-1564, -756),
                    Point(-675, -452),
                    Point(-1500, -1223),
                    Point(-1484, -1295),
                    Point(-1345, -1221),
                    Point(-1094, -1044),
                    Point(-1042, -1134),
                    Point(-958, -1120),
                    Point(-478, -683),
                    Point(-758, -1175),
                    Point(-524, -899),
                    Point(-460, -889),
                    Point(-393, -934),
                    Point(-490, -1304),
                    Point(-414, -1294),
                    Point(-342, -1284),
                    Point(-267, -1251),
                    Point(-123, -1263),
                    Point(-60, -1239),
                    Point(0, -1052),
                    Point(66, -1038),
                    Point(132, -1028),
                    Point(196, -1016),
                    Point(265, -1083),
                    Point(345, -1041),
                    Point(424, -1045),
                    Point(492, -1147),
                    Point(449, -937),
                    Point(437, -849),
                    Point(426, -773),
                    Point(418, -712),
                    Point(412, -640),
                    Point(483, -629),
                    Point(460, -569),
                    Point(634, -606),
                    Point(697, -599),
                    Point(765, -589),
                    Point(843, -579),
                    Point(905, -554),
                    Point(2239, -863),
                    Point(2420, -832),
                    Point(2528, -817),
                    Point(2577, -735),
                    Point(2558, -636),
                    Point(2541, -541),
                    Point(2524, -447),.getDeepCopy()
                    Point(2516, -357),
                    Point(2487, -266),
                    Point(2470, -179),
                    Point(2472, -93),
                    Point(2466, -6),
                    Point(2453, 79),
                    Point(2429, 424),
                    Point(2408, 508),
                    Point(2382, 591),
                    Point(2371, 678),
                    Point(2355, 765),
                    Point(2345, 855),
                    Point(2327, 943),
                    Point(2313, 1036),
                    Point(2294, 1127),
                    Point(2268, 1217),
                    Point(2158, 1256),
                    Point(2057, 1243),
                    Point(1959, 1229),
                    Point(1876, 1221),
                    Point(1851, 1303),
                    Point(1835, 1393),
                    Point(1819, 1489),
                    Point(1825, 1610),
                    Point(1807, 1715),
                    Point(1792, 1829),
                    Point(1744, 2140),
                    Point(1729, 2287),
                    Point(895, 2662),
                    Point(829, 2798),
                    Point(807, 2919),
                    Point(784, 3052),
                    Point(676, 3089),
                    Point(557, 3068),
                    Point(442, 3051),
                    Point(327, 3022),
                    Point(217, 3009),
                    Point(108, 2994),
                    Point(-473, 2588),
                    Point(-1303, 1769),
                    Point(817, 3184),
                    Point(2334, -851),
                    Point(1628, 2148),
                )
            )
        })
        */
        /*
        LifecycleEffect(onDisposed = {
            botvacController.botvac.scan = Scan()
            botvacController.botvac.map = Map()
            botvacController.botvac.location = Point()
            botvacController.botvac.angle = 0.0
            botvacController.botvac.oldScan = Scan()
            botvacController.botvac.oldLocation = Point()
            botvacController.botvac.oldAngle = 0.0
            botvac = botvacController.botvac.getDeepCopy()
        })
        */
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Navigation(
                onClick = { navigator.pop() },
                modifier = Modifier.padding(bottom = 10.dp),
                isBack = true
            ) {
                Title("Mapping")
            }
            MapVisualization(
                botvac = botvac,
                onClick = {
                    println(it)
                    if (mappingEnabled) {
                        println("Enabled: $it")
                    }
                },
                modifier = Modifier.weight(1f).fillMaxSize().padding(bottom = 10.dp)
            )
            Button(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                onClick = {
                    mappingEnabled = !mappingEnabled
                    if (mappingEnabled) {
                        /*botvacController.botvac.scan = Scan()
                        botvacController.botvac.map = Map()
                        botvacController.botvac.location = Point()
                        botvacController.botvac.angle = 0.0
                        botvacController.botvac.oldScan = Scan()
                        botvacController.botvac.oldLocation = Point()
                        botvacController.botvac.oldAngle = 0.0*/
                        //botvacController.botvac = Botvac()
                        //botvac = botvacController.botvac
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                botvacController.updateLidar()
                                //botvac = botvacController.botvac.copy()
                                //botvacController.botvac.map.points.clear()
                                //botvacController.botvac.map.points.addAll(points)
                                botvac = botvacController.botvac.getDeepCopy()
                                println(botvac.map.points.size)
                            } catch (exception: Exception) {
                                showErrorPopup = true
                            }

                        }
                    }
                },
                enabled = status == EStatus.CONNECTED
            ) {
                if (!mappingEnabled) {
                    Label("Enable mapping")
                } else {
                    Label("Disable mapping")
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        var data = "X:, Y:\n"
                        for (point in botvac.map.points) {
                            data += "${point.x}, ${point.y}\n"
                        }
                        fileSaver.launch(
                            bytes = data.encodeToByteArray(),
                            baseName = "map",
                            extension = "csv",
                        )
                    }
                },
                enabled = botvac.map.points.isNotEmpty()
            ) {
                Label("Save map")
            }
            InfoDialog(showLoadingPopup, "Loading...") { showLoadingPopup = false }
            InfoDialog(showErrorPopup, "Failed to communicate with robot") {
                showErrorPopup = false
            }
        }
    }

    companion object {
        fun Preview(): Screen {
            return MappingScreen(BotvacController())
        }
    }
}
