import com.android.build.gradle.internal.dsl.LintOptions
import io.gitlab.arturbosch.detekt.Detekt
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

buildscript {
    val kotlin_version by extra("1.5.0")
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.1")
        classpath(kotlin("gradle-plugin", "1.5.0"))
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt") version "1.9.1"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

subprojects {
    apply<KtlintPlugin>()

    afterEvaluate {
        if (plugins.hasPlugin("android") || plugins.hasPlugin("android-library")) {
            if (extensions.findByName("LintOptions") == true) {
                extensions.getByType<LintOptions>().apply {
                    isAbortOnError = false
                    isCheckAllWarnings = true
                }
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.register("detektCheck", Detekt::class) {
    parallel = true
    setSource(files(projectDir))
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")
    config.setFrom(files("$rootDir/detekt.yml"))
    buildUponDefaultConfig = false
}
