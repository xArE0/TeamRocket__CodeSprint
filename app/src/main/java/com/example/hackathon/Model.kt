package com.example.hackathon

import android.content.Context
import org.json.JSONObject
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

fun loadEncoder(context: Context, fileName: String): Map<String, Int> {
    val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
    val jsonObject = JSONObject(json)
    val map = mutableMapOf<String, Int>()
    jsonObject.keys().forEach { key ->
        map[key] = jsonObject.getInt(key)
    }
    return map
}

fun loadModelFile(context: Context, modelFileName: String): MappedByteBuffer {
    val fileDescriptor = context.assets.openFd(modelFileName)
    val inputStream = fileDescriptor.createInputStream()
    val fileChannel = inputStream.channel
    val startOffset = fileDescriptor.startOffset
    val declaredLength = fileDescriptor.declaredLength
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
}

fun getTFLiteInterpreter(context: Context, modelFileName: String): Interpreter {
    val model = loadModelFile(context, modelFileName)
    return Interpreter(model)
}

fun predictDisease(
    context: Context,
    inputFeatures: Map<String, String>,
    encoderFiles: Map<String, String>,
    modelFileName: String
): Int {
    // Load encoders
    val encoders = encoderFiles.mapValues { loadEncoder(context, it.value) }
    // Prepare input array
    val inputArray = FloatArray(encoders.size)
    encoders.keys.forEachIndexed { idx, feature ->
        val value = inputFeatures[feature] ?: ""
        inputArray[idx] = encoders[feature]?.get(value)?.toFloat() ?: 0f
    }
    // Load model
    val interpreter = getTFLiteInterpreter(context, modelFileName)
    val output = Array(1) { FloatArray(1) }
    interpreter.run(arrayOf(inputArray), output)
    interpreter.close()
    return output[0][0].toInt() // or process as needed
}