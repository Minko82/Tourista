plugins {
	java
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
	id("io.freefair.lombok") version "8.6"
}

group = "com.supercoolproject"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")  // Need for sec:authorize tag to work in html template
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-logging")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("com.h2database:h2")

	implementation("net.datafaker:datafaker:2.1.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testImplementation("io.cucumber:cucumber-java:7.14.0")
	testImplementation("io.cucumber:cucumber-junit:7.14.0")
	testImplementation("io.cucumber:cucumber-spring:7.14.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

configurations {
	create("cucumberRuntime").apply {
		extendsFrom(configurations.testImplementation.get())
	}
}

tasks.register("cucumberTest") {
	dependsOn("assemble", "testClasses")
	doLast {
		javaexec {
			mainClass = "io.cucumber.core.cli.Main"
			classpath = configurations.getByName("cucumberRuntime") + sourceSets.main.get().output + sourceSets.test.get().output
			args = listOf(
					"--plugin", "pretty",
					"--plugin", "html:target/cucumber-report.html",
					"--glue", "com.supercoolproject.tourista.cucumber.glue",
					"src/test/resources/features"
			)
		}
	}
}

