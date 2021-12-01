plugins {
  kotlin("jvm")
  `maven-publish`
}

publishToSpace()

version = project(":projector-server").version

val projectorClientVersion: String by project
val projectorClientGroup: String by project

dependencies {
  implementation("$projectorClientGroup:projector-util-logging:$projectorClientVersion")
}
