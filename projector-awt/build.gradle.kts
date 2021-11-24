/*
 * Copyright (c) 2019-2021, JetBrains s.r.o. and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation. JetBrains designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact JetBrains, Na Hrebenech II 1718/10, Prague, 14000, Czech Republic
 * if you need additional information or have any questions.
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  `maven-publish`
}

publishToSpace()

fun createSourceSetForJavaVersion(version: Int, prefix: String = "jdk") = sourceSets.create("$prefix$version") {

  val launcher = javaToolchains.launcherFor {
    languageVersion.set(JavaLanguageVersion.of(version))
  }

  (tasks[getCompileTaskName("Kotlin")] as KotlinCompile).apply {
    kotlinJavaToolchain.toolchain.use(launcher)
  }
}

val currentJdkSourceSet: SourceSet = if (JavaVersion.current() >= JavaVersion.VERSION_17) {
  createSourceSetForJavaVersion(17)
}else {
  createSourceSetForJavaVersion(11)
}

val jdkCommonSourceSet: SourceSet = sourceSets.create("jdkCommon")

val jdkCommonImplementation: Configuration by configurations.getting
val currentJdkImplementation: Configuration = configurations["${currentJdkSourceSet.name}Implementation"]

val kotlinVersion: String by project
val projectorClientVersion: String by project
val projectorClientGroup: String by project
version = project(":projector-server").version

tasks.withType<Jar> {
  val dependentOutputs = listOf(
    jdkCommonSourceSet,
    currentJdkSourceSet,
  ).flatMap {
    it.output.classesDirs.files
  }
  from(dependentOutputs)
}

dependencies {
  jdkCommonImplementation("$projectorClientGroup:projector-util-logging:$projectorClientVersion")
  currentJdkImplementation(jdkCommonSourceSet.output)
  api(jdkCommonSourceSet.output)
  api(currentJdkSourceSet.output)

  testImplementation(kotlin("test", kotlinVersion))
}
