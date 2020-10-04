rootProject.name = "psychics-abilities"

include(
    "abilities"
)

File("abilities").listFiles()?.filter { it.isDirectory && it.name != "build" }?.forEach { file ->
    include(":abilities:${file.name}")
}