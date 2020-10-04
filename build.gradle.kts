tasks.forEach { it.enabled = false }

gradle.buildFinished { buildDir.deleteRecursively() }