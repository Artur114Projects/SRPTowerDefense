package test

import java.nio.file.Paths

def recomp = Paths.get("C:\\Users\\Valense\\OneDrive\\Рабочий стол\\recomp")
def inPath = recomp.resolve("in")
def outPath = recomp.resolve("out")

def ruLang = inPath.resolve("ru_ru.lang").toFile()
def enLang = inPath.resolve("en_us.lang").toFile()
def out = outPath.resolve("out.lang").toFile()

def ruMap = initMap(ruLang)
def enMap = initMap(enLang)

println(ruMap)
println(enMap)

out.delete()
if (out.createNewFile()) {
    out.withPrintWriter {writer ->
        enMap.each {
            if (!ruMap.containsKey(it.key)) {
                writer.println(it.value)
            }
        }
    }
} else {
    throw new IllegalStateException()
}

Map<String, String> initMap(File file) {
    Map<String, String> map = new LinkedHashMap<>()
    file.newReader().eachLine {
        int i = it.indexOf("=");
        if (i != -1) map.put(it.substring(0, i), it)
        return null
    }
    return map
}