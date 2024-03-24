plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    idea
    id("com.diffplug.spotless") version "6.25.0"
    id("org.pkl-lang") version "0.25.2"
    id("com.bmuschko.docker-java-application") version "9.4.0"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    annotationProcessor(libs.avaje.inject.processor)
    annotationProcessor(libs.avaje.http.processor)
    annotationProcessor(libs.avaje.http.client.processor)
    annotationProcessor(libs.avaje.jsonb.processor)
    annotationProcessor(libs.jdbi.processor)
    implementation(libs.helidon.webserver)
    implementation(libs.avaje.inject)
    implementation(libs.avaje.http)
    implementation(libs.avaje.http.client)
    implementation(libs.avaje.jsonb)
//    implementation(libs.avaje.config)
    implementation(libs.slf4j)
    implementation(libs.liquibase)
    implementation("org.pkl-lang:pkl-codegen-java:0.25.2")
    implementation("org.pkl-lang:pkl-config-java:0.25.2")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")
    runtimeOnly(libs.snakeyaml)
    runtimeOnly(libs.logevents)
    implementation(libs.bundles.jdbi)
    implementation(libs.mariadb)
    testAnnotationProcessor(libs.avaje.inject.processor)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.avaje.inject.test)
    testImplementation("com.h2database:h2:2.2.224")
    testImplementation("org.assertj:assertj-core:3.25.1")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.1")
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