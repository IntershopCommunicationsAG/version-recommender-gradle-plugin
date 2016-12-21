# Introduction

This gradle plugin allows to automate dependency updates. Therefore the project can define an update strategy.
This plugin works together with the nebula-dependency-recommender and nebula-publishing-plugin to inject the resolved versions in to the build configuration.

This plugin doesn't use the standard gradle version resolver. The version.properties contains the current version of the project, so no pattern are used, like (1.3.+).
The resolver uses the current version, the defined update strategy and a list of available versions to use calculate the new recommended version.
Therefore an additional lock file is not necessary.

# Update strategy

Dependent on the context of development, stabilization or feature development, different update strategies for
update of third party libraries are necessary. The available strategy are derived from semantic versioning (http://semver.org/)

| strategy  | description                      |
|-----------|----------------------------------|
| MAJOR     | all new releases can be used     |
| MINOR     | new minor releases can be used   |
| PATCH     | new patch releases can be used   |
| INC       | new increments can be used (soon)|
| STICK     | uses current version             |

# Supported version patterns and extensions

Unfortunally, not all open source projects using semantic versioning or derived version patterns. Therefore the plugin tries to imagine major, minor, patch, increments and release extensions.

Following patterns are recognized:

| pattern       | example      | description                                                                                            |
|---------------|--------------|--------------------------------------------------------------------------------------------------------|
| \d+\.\d+\.\d+ | 1.2.3        | standard three numbers semantic versions                                                               |
| \d+\.\d+\.\d+\.\d+ | 1.2.3.4 | four numbers (fourth number is interpreted as increments (first three following the semantic versions) |
| \d+\.\d+      | 1.2          | two numbers (mainly used for projects without separate patch development) interpreted as 1.2.0         |
| \d+           | 1            | one number (seldom used for projects, which providing an API) interpreted as 1.0.0                     |
| .FINAL        | 5.1.3.Final  | several GA extensions separated with a dot (the lowercase of the extension must be 'final' or ('ga')   |
| -GA           | 1.2.3-ga     | several GA extensions separated with a dash (the lowercase of the extension must be 'final' or ('ga')  |
| .v\d{8}       | 1.2.3.v20160101 | jetty GA extensions with date                                                                       |
| -RC\d+        | 1.2.3-rc1    | release candidates (will be supported by INC strategy)                                                 |
| -DEV\d+       | 1.2.3-dev1   | internal development release (will be supported by INC strategy)                                       |

In case the number of "semantic" version (three, two or one number) is higher than 1000, the version is marked as non-semantic.

# Integration

Usage as gradle plugin, adapt build.gradle:
<pre>
buildscript {
    dependencies {
        classpath 'com.netflix.nebula:nebula-dependency-recommender:3.6.3'
        classpath 'com.netflix.nebula:nebula-publishing-plugin:4.9.1'
        classpath 'com.intershop.gradle.version-recommender:version-recommender-gradle-plugin:1.2.0'
    }
}

apply plugin: 'nebula.dependency-recommender'
apply plugin: 'nebula.maven-resolved-dependencies'
apply plugin: 'com.intershop.gradle.version.recommender'

versionRecommender {
    strategy = 'MAJOR'
    propertiesFile = new File(project.projectDir, 'version.properties')
    versionCache = new File(rootProject.buildDir, 'versions.xml')
    exceptions = [:] // e.g. [ 'ch.qos.logback' : 'STICK', 'ch.qos.logback:logback-classic' : 'PATCH' ]
}

// nebula dependency recommender
dependencyRecommendations {
    propertiesFile uri: new File(project.projectDir, 'version.properties').toURI().toString()
    add { org, name -> project.tasks.findByName('updateDependencies').getNextVersion(org, name) }
}
</pre>

# Tasks

| task                     | description                                                             |
|--------------------------|-------------------------------------------------------------------------|
| recommendDependencies    | prints new versions of dependencies (uses defined update strategy)      |
| updateDependencies       | enable new versions for further tasks, without storing the new versions |
| storeDependencies        | stores resolved versions                                                |

Example:

<pre>
./gradlew recommendDependencies
./gradlew updateDependencies test storeDependencies
</pre>

# Output

<pre>
com.google.code.findbugs:jsr305:2.0.1:2.0.3
com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:r136:(nonsemantic)
</pre>

| column    | description                            |
|-----------|----------------------------------------|
| 1         | maven artifact - group and artifact id |
| 2         | currently used version                 |
| 3         | available newer version (or (nonsemantic) in case manually update necessary, because artifact doesn't follow the semantic versioning concept |

# Thanks

Special thanks for the ideas to write this plugin:

https://github.com/nebula-plugins/nebula-dependency-recommender-plugin

https://github.com/nebula-plugins/gradle-dependency-lock-plugin

https://github.com/nebula-plugins/nebula-publishing-plugin

https://github.com/stempler/gradle-versioneye-plugin

# License

Copyright 2014-2017 Intershop Communications.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
