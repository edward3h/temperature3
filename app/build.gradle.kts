plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    idea
    alias(libs.plugins.spotless)
    alias(libs.plugins.pkl)
    alias(libs.plugins.docker.application)
    alias(libs.plugins.jte)
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    annotationProcessor(libs.bundles.processors)
    implementation(libs.helidon.webserver)
    implementation(libs.avaje.inject)
    implementation(libs.avaje.http)
    implementation(libs.avaje.http.client)
    implementation(libs.avaje.jsonb)
//    implementation(libs.avaje.config)
    implementation(libs.slf4j)
    implementation(libs.liquibase)
    implementation(libs.bundles.config)
    runtimeOnly(libs.graalvm.sdk)
    runtimeOnly(libs.graalvm.truffle.api)
    runtimeOnly(libs.snakeyaml)
    runtimeOnly(libs.avaje.applog)
    runtimeOnly(libs.logevents)
    implementation(libs.bundles.jdbi)
    implementation(libs.mariadb)
    implementation(libs.jte.core)
    jteGenerate(libs.jte.models)
    testAnnotationProcessor(libs.avaje.inject.processor)
    testImplementation(libs.junit.api)
    testImplementation(libs.avaje.inject.test)
    testImplementation(libs.h2)
    testImplementation(libs.assertj)

    testRuntimeOnly(libs.junit.engine)
    testRuntimeOnly(libs.junit.platform)
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass = "org.ethelred.temperature3.Main"
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

spotless {
    java {
        targetExclude("build/**")
        importOrder()
        removeUnusedImports()
        palantirJavaFormat()
    }
    format("misc") {
        // define the files to apply `misc` to
        target("*.gradle", ".gitattributes", ".gitignore")

        // define the steps to apply to those files
        trimTrailingWhitespace()
        indentWithTabs() // or spaces. Takes an integer argument if you don't like 4
        endWithNewline()
    }

    kotlinGradle {
        target("*.gradle.kts") // default target for kotlinGradle
        ktlint() // or ktfmt() or prettier()
    }
}
val resources = layout.projectDirectory.dir("src/main/resources")

pkl {
    javaCodeGenerators {
        register("genJava") {
            generateGetters = true
            sourceModules.add(file(resources.file("ConfigurationTemplate.pkl")))
        }
    }
    evaluators {
        register("evalDev") {
            sourceModules.addAll(files("src/main/resources/dev.pkl"))
            transitiveModules.from(file("src/main/resources/ConfigurationTemplate.pkl"))
            outputFile = layout.buildDirectory.file("evaldev.pcf")
        }
    }
}

docker {
    javaApplication {
        baseImage = "eclipse-temurin:21-alpine"
    }
}

jte {
    generate()
    jteExtension("gg.jte.models.generator.ModelExtension")
    packageName = "org.ethelred.temperature3.templates"
}

tasks.named("genJava") {
    dependsOn(tasks.named("generateJte"))
}
