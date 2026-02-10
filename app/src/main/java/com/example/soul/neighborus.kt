//package com.example.soul
//import android.util.Log
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.launch
//import java.text.Normalizer
//import java.text.Normalizer.normalize
//import java.util.Locale
//import kotlin.math.pow
//
//
//import kotlin.math.*
//
//
//enum class Mode{
//    INDOOR,OUTDOOR
//}
//enum class Confidence {
//    HIGH, MEDIUM, REJECT
//}
//
//fun getOutdoorConfidence(accuracy: Float): Confidence = when {
//    accuracy <= 10f -> Confidence.HIGH
//    accuracy <= 18f -> Confidence.MEDIUM
//    else -> Confidence.REJECT
//}
//
//
//
//fun giveTieKeys(
//    userLat: Double,
//    userLng: Double,
//    confidence: Confidence,
//    accuracy: Float
//) : List<String> {
//    val precision=4
//// find the tile key
//    val centerTile = buildCenterKey(userLat,userLng,4)
//
//    // center locaiton
//    val (centerLat, centerLng) = tileCenter(centerTile, 4)
//
//// pass center location and user locaiton and find the distance between them
//    val distance = distanceMeters(
//        centerLat, centerLng,
//        userLat, userLng
//    )
//
//    val direction= findDirection(centerLat,centerLng, userLat, userLng)
//
//    val scope = decideTileScope(accuracy, distance)
//
//    val tiles = when (scope) {
//        TileScope.CENTER ->
//            listOf(centerTile)
//
//        TileScope.DIRECTIONAL ->
//            listOf(centerTile) +
//                    getDirectionalNeighbours(centerTile,precision, direction)
//
//        TileScope.DIRECTIONAL_OF_DIRECTIONAL ->
//            listOf(centerTile) +
//                    getDirectionalNeighbours(centerTile, precision, direction) +
//                    getDirectionalNeighboursOfNeighbours(centerTile, direction, precision)
//
//
//
//        TileScope.REJECT ->
//            emptyList()
//    }
//
//    if(confidence == Confidence.REJECT){
//        return  emptyList()
//
//    }
//
//
//    return tiles
//
//}
//
//fun getDirectionalNeighbours(
//    centerTile: String,
//    precision: Int,
//    direction: Direction
//): List<String> {
//
//    // Parse "lat_lng"
//    val parts = centerTile.split("_")
//    require(parts.size == 2) { "Invalid tileKey: $centerTile" }
//
//    val lat = parts[0].toDouble()
//    val lng = parts[1].toDouble()
//
//    // Tile size in degrees (e.g. 0.0001 for precision=4)
//    val step = 1.0 / Math.pow(10.0, precision.toDouble())
//
//    fun fmt(v: Double) = "%.${precision}f".format(v)
//
//    // Offsets: (dLat, dLng)
//    val offsets = when (direction) {
//        Direction.N  -> listOf(1 to 0, 1 to -1, 1 to 1)
//        Direction.E  -> listOf(0 to 1, 1 to 1, -1 to 1)
//        Direction.S  -> listOf(-1 to 0, -1 to -1, -1 to 1)
//        Direction.W  -> listOf(0 to -1, 1 to -1, -1 to -1)
//
//        Direction.NE -> listOf(1 to 0, 0 to 1, 1 to 1)
//        Direction.NW -> listOf(1 to 0, 0 to -1, 1 to -1)
//        Direction.SE -> listOf(-1 to 0, 0 to 1, -1 to 1)
//        Direction.SW -> listOf(-1 to 0, 0 to -1, -1 to -1)
//
//        Direction.CENTER -> emptyList()
//    }
//
//    return offsets.map { (dLat, dLng) ->
//        val nLat = lat + dLat * step
//        val nLng = lng + dLng * step
//        "${fmt(nLat)}_${fmt(nLng)}"
//    }
//}
//
//fun buildCenterKey(
//    lat: Double,
//    lng: Double,
//    precision: Int
//): String {
//    Log.e("location22"," buildCenterKey ->  $lat and $lng ")
//
//    val factor = 10.0.pow(precision)
//
//    val tLat = floor(lat * factor) / factor
//    val tLng = floor(lng * factor) / factor
//
//    fun fmt(v: Double) = "%.${precision}f".format(v)
//
//    return "${fmt(tLat)}_${fmt(tLng)}"
//}
//
//
//fun tileCenter(tileKey: String, precision: Int): Pair<Double, Double> {
//    val (latStr, lngStr) = tileKey.split("_")
//    val lat = latStr.toDouble()
//    val lng = lngStr.toDouble()
//
//    val step = 1.0 / 10.0.pow(precision.toDouble())
//
//    return Pair(
//        lat + step / 2,
//        lng + step / 2
//    )
//}
//
//
//
//fun distanceMeters(
//    lat1: Double, lng1: Double,
//    lat2: Double, lng2: Double
//): Double {
//
//    val R = 6371000.0 // Earth radius in meters
//
//    val dLat = Math.toRadians(lat2 - lat1)
//    val dLng = Math.toRadians(lng2 - lng1)
//
//    val a = sin(dLat / 2) * sin(dLat / 2) +
//            cos(Math.toRadians(lat1)) *
//            cos(Math.toRadians(lat2)) *
//            sin(dLng / 2) * sin(dLng / 2)
//
//    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
//
//    return R * c
//}
//
//enum class Direction {
//    N, NE, E, SE, S, SW, W, NW, CENTER
//}
//
//fun findDirection(
//    centerLat: Double,
//    centerLng: Double,
//    userLat: Double,
//    userLng: Double
//): Direction {
//
//    val dLat = userLat - centerLat   // vertical move
//    val dLng = userLng - centerLng   // horizontal move
//
//    val eps = 1e-7 // tiny threshold to avoid floating noise
//
//    if (kotlin.math.abs(dLat) < eps && kotlin.math.abs(dLng) < eps) {
//        return Direction.CENTER
//    }
//
//    return when {
//        dLat > 0 && dLng > 0 -> Direction.NE
//        dLat > 0 && dLng < 0 -> Direction.NW
//        dLat < 0 && dLng > 0 -> Direction.SE
//        dLat < 0 && dLng < 0 -> Direction.SW
//        dLat > 0 -> Direction.N
//        dLat < 0 -> Direction.S
//        dLng > 0 -> Direction.E
//        else -> Direction.W
//    }
//}
//
//
//
//enum class TileScope {
//    CENTER,
//    DIRECTIONAL,
//    DIRECTIONAL_OF_DIRECTIONAL,
//    // FULL_5x5, after 20
//    REJECT
//}
//
//
//// use karo esme confidence ko
//fun decideTileScope(
//    accuracy: Float,
//    distanceFromCenter: Double
//): TileScope {
//
//    val reach = accuracy + distanceFromCenter
//
//    val halfTile = 5.5        // meters
//    val halfDiagonal = 7.8   // meters
//    val twoTiles = 18.0      // meters
//
//    return when {
//        reach <= halfTile ->
//            TileScope.CENTER
//
//        reach <= halfDiagonal ->
//            TileScope.DIRECTIONAL
//
//        reach <= twoTiles ->
//            TileScope.DIRECTIONAL_OF_DIRECTIONAL
//
////        reach <= 25 ->
////            TileScope.FULL_5x5
//
//        else ->
//            TileScope.REJECT
//    }
//
//    /*
//    halfTile      = 5.5m
//halfDiagonal = 7.8m
//twoTiles     = 18m
//full5x5Max   = 25m
//     */
//
//}
//fun getDirectionalNeighboursOfNeighbours(
//    centerTile: String,
//    direction: Direction,
//    precision: Int
//): List<String> {
//
//    // Parse "lat_lng"
//    val parts = centerTile.split("_")
//    require(parts.size == 2) { "Invalid tileKey: $centerTile" }
//
//    val lat = parts[0].toDouble()
//    val lng = parts[1].toDouble()
//
//    val step = 1.0 / Math.pow(10.0, precision.toDouble())
//    fun fmt(v: Double) = "%.${precision}f".format(v)
//
//    // 2-step offsets (directional ring)
//    val offsets = when (direction) {
//        Direction.N  -> listOf(2 to 0, 2 to -1, 2 to 1)
//        Direction.E  -> listOf(0 to 2, 1 to 2, -1 to 2)
//        Direction.S  -> listOf(-2 to 0, -2 to -1, -2 to 1)
//        Direction.W  -> listOf(0 to -2, 1 to -2, -1 to -2)
//
//        Direction.NE -> listOf(2 to 0, 0 to 2, 1 to 1, 2 to 1, 1 to 2)
//        Direction.NW -> listOf(2 to 0, 0 to -2, 1 to -1, 2 to -1, 1 to -2)
//        Direction.SE -> listOf(-2 to 0, 0 to 2, -1 to 1, -2 to 1, -1 to 2)
//        Direction.SW -> listOf(-2 to 0, 0 to -2, -1 to -1, -2 to -1, -1 to -2)
//
//        Direction.CENTER -> emptyList()
//    }
//
//    return offsets.map { (dLat, dLng) ->
//        val nLat = lat + dLat * step
//        val nLng = lng + dLng * step
//        "${fmt(nLat)}_${fmt(nLng)}"
//    }.distinct()
//}
//
//
//
////
////
////
////
////
////
////
////fun getNeighbourTileKeys(
////    centerTile: String,
////    precision: Int
////): List<String> {
////
////    fun Double.format(digit: Int): String {
////        return "%.${digit}f".format(this)
////    }
////
////    // "lat_lng" → lat, lng
////    val parts = centerTile.split("_")
////    if (parts.size != 2) return emptyList()
////
////    val lat = parts[0].trim().toDouble()
////    val lng = parts[1].trim().toDouble()
////
////    //10.0.pow(4) = 10000
////    //step = 1 / 10000 = 0.0001
////    // val step=0.0001, no make it dynamic
////    val step = 1.0 / 10.0.pow(precision)
////
////    val tiles = mutableListOf<String>()
//////Tum center se chal nahi rahe
//////Tum square ke “name” bana rahe ho
////    for (dLat in -1..1) {
////        for (dLng in -1..1) {
////            val nLat = lat + (dLat * step)
////            val nLng = lng + (dLng * step)
////
////            tiles.add(
////                "${nLat.format(precision)}_${nLng.format(precision)}"
////            )
////        }
////    }
////
////    return tiles
////}
////
//////
////
//suspend fun hashCreate(
//    tileKeys: List<String>,
//    title: String,
//    onError: suspend (String) -> Unit,
//    mode: Mode
//): List<String> {
//
//    return tileKeys.mapNotNull {
//        makeHash(mode = mode,it, title, onError = onError)
//    }
//}
//
//
//
//
//
//suspend fun makeHash(
//    mode: Mode,
//    tileKey: String?,
//    title: String?,
//    floor: String?="",
//    onError:  suspend (String) -> Unit
//): String? {
//
//
//
//
//
//    if (tileKey.isNullOrBlank()) {//null.isNullOrEmpty() // true  👉 Null + empty 👉 Lekin "   " ko miss kar deta hai ❌
//        onError("location required")
//        return null
//    }
//
//    if (title.isNullOrBlank()) {
//        onError("Title required")
//        return null
//    }
//
//    if(mode== Mode.INDOOR && floor.isNullOrBlank()){
//        onError("floor required")
//        return null
//    }
//
//    // 🔹 Helper function
//    fun normalize3(input: String): String {
//        return Normalizer.normalize(input, Normalizer.Form.NFD)//é → e + accent mark
//            .lowercase(Locale.US)
//            .trim()
//            .replace(Regex("\\s+"), "_")// " " -> _ and "  " -> __ and \\s+ so "  " -> _
//            .replace(Regex("[^a-z0-9-_]"), "")//Jo bhi character a-z, 0-9 ya underscore nahi hai → use delete kar do
//            .replace(Regex("_+"), "_")
//            .trim('_')
//
//    }
//
//
//    val normTile = normalize3(tileKey)
//
//    val normTitle = normalize3(title)
//
//    val normFloor = floor?.let { normalize3(it) }
//
//
//    if (normTile.isBlank()) {
//        onError(
//            if (mode == Mode.OUTDOOR)
//                "Unable to detect location. Please try again."
//            else
//                "Please select a valid building"
//        )
//        return null
//    }
//
//    if (normTitle.isBlank()) {
//        onError("Please enter a valid complaint title")
//        return null
//    }
//
//    if (mode == Mode.INDOOR && normFloor.isNullOrBlank()) {
//        onError("Please select a valid floor")
//        return null
//    }
//
//    return if (mode == Mode.INDOOR) {
//        "${normTile}_${normFloor}_$normTitle"
//    } else {
//        "${normTile}_$normTitle"
//    }
//}
////
////
////
////fun makeHash(tileKey: String, title2: String,floor: String=""): String {
//////     "é" → "e"
//////"ñ" → "n"
////    fun x(title:String):String {
////
////        normalize(title, Normalizer.Form.NFD)
////            .lowercase(Locale.US)              // covert in small
////            .trim()                            // remove spaces
////            .replace(
////                Regex("\\s+"),
////                "_"
////            )      // spaces - > convert into ->>  _ // example -> "street   light" → "street_light"
////            .replace(Regex("[^a-z0-9_]"), "")
////
////    }// remove special charaters
//////    “Jo bhi character a–z, 0–9 ya _ ke alawa ho,
//////usko empty string se replace kar do”
//////
//////⸻
//////
//////🔹 "" = Empty String
//////
//////"" ka matlab:
//////
//////Kuch bhi nahi
//////Matlab jo character match hua, wo hata diya jaata hai
////
////    return if(floor.isNotEmpty()){
////        val normFloor = normalize(floor)
////        "${tileKey}_${normFloor}_${normalizedTitle}"
////    }
////    else{
////        "${tileKey}_$normalizedTitle"
////
////    }
////}
////
////
////
////
//enum class Decision {
//    ALLOW_NEW,
//
//    REJECT
//}
////
////
////
////
////fun handleFetchTileKeys(
////    candidates: List<FirstAppFireStoreDataClass>,
////    newLat: Double,
////    newLng: Double,
////    newConfidence: Confidence
////): Decision {
////
////    if (candidates.isEmpty()) {
////        return Decision.ALLOW_NEW
////    }
////
////    val radius = when (newConfidence) {
////        Confidence.HIGH -> 20.0
////        Confidence.MEDIUM -> 30.0
////        Confidence.LOW -> 50.0
////        Confidence.REJECT -> 30.0
////    }
////
////    // 1️⃣ Distance filter
////    val nearby = candidates.mapNotNull { old ->
////        val lat = old.latitude ?: return@mapNotNull null
////        val lng = old.longitude ?: return@mapNotNull null
////
////        val d = distanceMeters(newLat, newLng, lat, lng)
////        old to d
////    }.filter { (_, d) -> d <= radius }
////
////    if (nearby.isEmpty()) {
////        return Decision.ALLOW_NEW
////    }
////
////    // 2️⃣ Best match = closest
////    val (best, _) = nearby.minByOrNull { it.second }!!
////
////    val bestConfidence = try {
////        Confidence.valueOf(best.confidence.toString())
////    } catch (e: Exception) {
////        Confidence.LOW
////    }
////
////    // 3️⃣ Decision rules
////    return when {
////        bestConfidence == Confidence.HIGH &&
////                newConfidence == Confidence.HIGH ->
////            Decision.REJECT_DUPLICATE
////
////        bestConfidence == Confidence.HIGH ->
////            Decision.MERGE_WITH_EXISTING
////
////        else ->
////            Decision.ALLOW_NEW
////    }
////}
////
////fun distanceMeters(
////    lat1: Double, lng1: Double,
////    lat2: Double, lng2: Double
////): Double {
////
////    val R = 6371000.0 // Earth radius in meters
////
////    val dLat = Math.toRadians(lat2 - lat1)
////    val dLng = Math.toRadians(lng2 - lng1)
////
////    val a = sin(dLat / 2).pow(2.0) +
////            cos(Math.toRadians(lat1)) *
////            cos(Math.toRadians(lat2)) *
////            sin(dLng / 2).pow(2.0)
////
////    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
////
////    return R * c
////}
//fun decideComplaintAction(
//    candidates: List<FirstAppFireStoreDataClass>,
//    newLat: Double,
//    newLng: Double,
//    newConfidence: Confidence
//): Decision {
//
//    // 1️⃣ Reject bad GPS upfront
//    if (newConfidence == Confidence.REJECT) {
//        return Decision.REJECT
//    }
//
//    // 2️⃣ No old complaints → allow
//    if (candidates.isEmpty()) {
//        return Decision.ALLOW_NEW
//    }
//
//    // 3️⃣ Radius based on confidence
//    val radius = when (newConfidence) {
//        Confidence.HIGH -> 10.0
//        Confidence.MEDIUM -> 18.0
//        Confidence.REJECT -> return Decision.REJECT
//    }
//
//    // 4️⃣ Distance filter
//    val nearby = candidates.mapNotNull { old ->
//        val lat = old.latitude ?: return@mapNotNull null
//        val lng = old.longitude ?: return@mapNotNull null
//
//        val d = distanceMeters(newLat, newLng, lat, lng)
//        old to d
//    }.filter { it.second <= radius }
//
//    // 5️⃣ If anything nearby → reject as duplicate
//    return if (nearby.isNotEmpty()) {
//        Decision.REJECT
//    } else {
//        Decision.ALLOW_NEW
//    }
//}