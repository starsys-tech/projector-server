plugins {
  kotlin("jvm")
  `maven-publish`
}

publishToSpace()

version = project(":projector-server").version

kotlin {
  jvmToolchain {
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
  }
}

dependencies {
  api(project(":projector-awt-common"))
}
