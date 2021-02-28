import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
}
group = "me.parasiticcapacitance"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.github.ajalt.clikt:clikt:3.1.0")
    implementation("org.jsoup:jsoup:1.11.3")
    testImplementation(kotlin("test-junit"))
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}