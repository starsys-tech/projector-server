plugins {
  kotlin("jvm")
  `maven-publish`
}

publishToSpace()

version = project(":projector-server").version

kotlin {
  jvmToolchain {
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
  }
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}

dependencies {
  api(project(":projector-awt-common"))
}
