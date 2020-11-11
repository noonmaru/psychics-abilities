plugins {
    kotlin("jvm") version "1.4.10"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    `maven-publish`
}

if (version == "unspecified") version = parent!!.version

allprojects {
    repositories {
        mavenCentral()
    }
}

val relocate = (findProperty("relocate") as? String)?.toBoolean() ?: true

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        maven(url = "https://papermc.io/repo/repository/maven-public/")
        maven(url = "https://jitpack.io/")
        mavenLocal()
    }

    dependencies {
        compileOnly(kotlin("stdlib-jdk8"))
        compileOnly(kotlin("reflect"))
        compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
        compileOnly("com.destroystokyo.paper:paper-api:1.16.3-R0.1-SNAPSHOT")
        compileOnly("com.github.noonmaru:tap:3.2.5")
        compileOnly("com.github.noonmaru:psychics:0.9.3")

//        testImplementation("junit:junit:4.13")
//        testImplementation("org.mockito:mockito-core:3.3.3")
//        testImplementation("org.powermock:powermock-module-junit4:2.0.7")
//        testImplementation("org.powermock:powermock-api-mockito2:2.0.7")
//        testImplementation("org.slf4j:slf4j-api:1.7.25")
//        testImplementation("org.apache.logging.log4j:log4j-core:2.8.2")
//        testImplementation("org.apache.logging.log4j:log4j-slf4j-impl:2.8.2")
//        testImplementation("org.spigotmc:spigot:1.16.3-R0.1-SNAPSHOT")
    }

    tasks {
        compileJava {
            options.encoding = "UTF-8"
        }
        javadoc {
            options.encoding = "UTF-8"
        }
        compileKotlin {
            kotlinOptions.jvmTarget = "1.8"
        }
        compileTestKotlin {
            kotlinOptions.jvmTarget = "1.8"
        }
        // process yml
        processResources {
            filesMatching("*.yml") {
                expand(project.properties)
            }
        }
        shadowJar {
            archiveBaseName.set("${project.group}.${project.name}")
            archiveClassifier.set("")
            archiveVersion.set("")

            if (relocate) {
                relocate("com.github.noonmaru.tap", "com.github.noonmaru.psychics.tap")
            }
        }
        create<Copy>("copyShadowJarToParent") {
            from(shadowJar)
            into { File(parent!!.buildDir, "libs") }
        }
        create<Copy>("copyShadowJarToDocker") {
            from(shadowJar)
            var dest = File(rootDir, ".docker/plugins/Psychics/abilities")
            if (File(dest, shadowJar.get().archiveFileName.get()).exists()) dest = File(dest, "update")
            into(dest)
            doLast { println("${shadowJar.get().archiveFileName.get()} copied to ${dest.path}") }
        }
        assemble {
            dependsOn(named("copyShadowJarToParent"))
        }
    }
}

// :abilities project cancel
tasks.filter { it.name != "clean" }.forEach { it.enabled = false }
