@file:DependsOn("com.fasterxml.jackson.core:jackson-databind:2.15.2")

import java.net.URL
import java.io.File
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import java.util.Properties

val PROJECT_ROOT = File(requireNotNull(System.getProperty("user.dir"))).canonicalFile

fun loadLocalProperty(key: String): String {
    val props = Properties()
    File(PROJECT_ROOT, "local.properties").inputStream().use {
        props.load(it)
    }
    return props.getProperty(key)
        ?: error("$key not found in local.properties")
}

val OUTPUT_FILE = File(
    PROJECT_ROOT,
    "presentation/src/main/assets/regions.json"
)

// 데이터 모델
data class Region(
    val code: String,
    val name: String,
    val sgg: String,
    val umd: String,
    val ri: String
) {
    fun isSido(): Boolean =
        umd == "000" && ri == "00" && !name.contains(" ")

    fun isSigungu(): Boolean =
        sgg != "000" && umd == "000" && ri == "00"
}

// 메인 로직
println("=== region generator start ===")

val mapper = ObjectMapper()
val allItems = mutableListOf<Region>()
val SERVICE_KEY = loadLocalProperty("REGION_API_KEY")
val PAGE_SIZE = 1000
var page = 1

while (true) {
    val url =
        "https://apis.data.go.kr/1741000/StanReginCd/getStanReginCdList" +
                "?serviceKey=$SERVICE_KEY" +
                "&type=json" +
                "&pageNo=$page" +
                "&numOfRows=$PAGE_SIZE"

    val json = URL(url).readText()
    val root = mapper.readTree(json)

    val rowNode = root.path("StanReginCd")
        .firstOrNull { it.has("row") }
        ?.path("row")

    if (rowNode == null || !rowNode.isArray || rowNode.isEmpty) break

    rowNode.forEach {
        allItems.add(
            Region(
                code = it.path("region_cd").asText(),
                name = it.path("locatadd_nm").asText(),
                sgg = it.path("sgg_cd").asText(),
                umd = it.path("umd_cd").asText(),
                ri = it.path("ri_cd").asText()
            )
        )
    }

    println("page=$page, count=${rowNode.size()}")
    if (rowNode.size() < PAGE_SIZE) break
    page++
}

println("total received = ${allItems.size}")

// 결과 구조 생성
val result = mutableMapOf<String, MutableList<String>>()
val sidoMap = mutableMapOf<String, String>()

// 1. 시도 수집
allItems.filter { it.isSido() }.forEach {
    result[it.name] = mutableListOf()
    sidoMap[it.code.substring(0, 2)] = it.name
}

// 2. 시·군 정리
val sigunguBySido = mutableMapOf<String, MutableSet<String>>()

allItems.filter { it.isSigungu() }.forEach {
    val sidoName = sidoMap[it.code.substring(0, 2)] ?: return@forEach
    val short = it.name.removePrefix("$sidoName ")

    val finalName =
        if (short.contains("시 ") && short.endsWith("구"))
            short.substringBefore(" ")
        else
            short

    sigunguBySido
        .getOrPut(sidoName) { mutableSetOf() }
        .add(finalName)
}

// 3. 결과 반영
sigunguBySido.forEach { (sido, list) ->
    result[sido] = list.sorted().toMutableList()
}

// JSON 출력
val outputMapper = ObjectMapper().apply {
    enable(SerializationFeature.INDENT_OUTPUT)
}

OUTPUT_FILE.parentFile?.mkdirs()
OUTPUT_FILE.writeText(outputMapper.writeValueAsString(result))

println("written to: ${OUTPUT_FILE.absolutePath}")
println("=== region generator end ===")