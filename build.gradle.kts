plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("org.asciidoctor.jvm.convert") version "3.3.2"

}

group = "jp.falsystack"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val asciidoctorExt: Configuration by configurations.creating // 2. configuration 추가

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // rest docs
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")

    runtimeOnly("com.h2database:h2")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// sinppetsDir 추가
val snippetsDir by extra {
    file("build/generated-snippets")
}

tasks {

    // test Task snippetsDir 추가
    test {
        outputs.dir(snippetsDir)
        useJUnitPlatform()
    }

    // asciidoctor Task 추가
    asciidoctor {
        inputs.dir(snippetsDir)
        configurations("asciidoctorExt")
        dependsOn(test)
    }

    // bootJar Settings
    bootJar {
        dependsOn(asciidoctor)
        from("build/docs/asciidoc") {
            into("static/docs")
        }
    }

    // 생성된 asciidoc index.html 을 src/man/resources/static/docs 에 복사
    register<Copy>("copyAsciidoctor") {
        dependsOn(asciidoctor)
        from(file("$layout.buildDirectory/docs/asciidoc"))
        into(file("src/main/resources/static/docs"))
    }

    // 빌드가 실행될 때 copyAsciidoctor 이 실행되도록 설정
    build {
        dependsOn("copyAsciidoctor")
    }
}
