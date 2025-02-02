plugins {
    `java-library`
}

apply(plugin = "mcp-plugin")

group = "org.example"
version = "1.0-SNAPSHOT"

//----------------------------------------------------------------------------------------------------------------------
// Mappings
//----------------------------------------------------------------------------------------------------------------------

val generateMcp2Srg by project(":_mappings").tasks.getting(task.srg.TransformSrg::class)
val generateMcp2Obf by project(":_mappings").tasks.getting(task.srg.GenerateRemappingSrg::class)

//----------------------------------------------------------------------------------------------------------------------
// MCP
//----------------------------------------------------------------------------------------------------------------------

val jar by tasks.getting(Jar::class) {

}

val reobfJar by tasks.creating(task.ApplySpecialSource::class) {
    group = "mcp"
    dependsOn(generateMcp2Obf)

    val jarFile = jar.archiveFile.get().asFile

    input = jarFile
    output = file(jarFile.toPath().resolveSibling("${jarFile.nameWithoutExtension}-reobf.jar"))
    mappings = generateMcp2Obf.output
}

jar.finalizedBy(reobfJar)

//----------------------------------------------------------------------------------------------------------------------
// Dependencies
//----------------------------------------------------------------------------------------------------------------------

repositories {
    // Maven Central + Mojang Maven are both provided by mcp-plugin

    maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
}

val api : Configuration by configurations.getting

dependencies {
    api(project(":client"))

    //------------------------------------------------------------------------------------------------------------------
    // Common Dev
    //------------------------------------------------------------------------------------------------------------------
    api(group = "org.jetbrains", name = "annotations", version = "20.1.0")

    api(group = "org.apache.logging.log4j", name = "log4j-api", version = "2.11.2")
    runtimeOnly(group = "org.apache.logging.log4j", name = "log4j-core", version = "2.11.2")

    //------------------------------------------------------------------------------------------------------------------
    // Launcher Implementation
    //------------------------------------------------------------------------------------------------------------------

    implementation(group = "cpw.mods", name = "modlauncher", version = "8.0.9")
    implementation(group = "cpw.mods", name = "grossjava9hacks", version = "1.3.3")
    implementation(group = "net.sf.jopt-simple", name = "jopt-simple", version = "5.0.4")
    implementation(group = "com.google.guava", name = "guava", version = "30.1.1-jre")
    runtimeOnly(group = "com.google.code.gson", name = "gson", version = "2.2.4")
    runtimeOnly(group = "org.ow2.asm", name = "asm-analysis", version = "7.2")
    runtimeOnly(group = "org.ow2.asm", name = "asm-commons", version = "7.2")
    runtimeOnly(group = "org.ow2.asm", name = "asm-tree", version = "7.2")
    runtimeOnly(group = "org.ow2.asm", name = "asm-util", version = "7.2")

    //------------------------------------------------------------------------------------------------------------------
    // Mixins
    //------------------------------------------------------------------------------------------------------------------

    api(group = "org.spongepowered", name = "mixin", version = "0.8.1")
    annotationProcessor(group = "org.spongepowered", name = "mixin", version = "0.8.1", classifier = "processor")
}

//----------------------------------------------------------------------------------------------------------------------
// Mixins
//----------------------------------------------------------------------------------------------------------------------

val compileJava by tasks.getting(JavaCompile::class) {
    dependsOn(generateMcp2Srg)

    //TODO: refmap -> into Jar
    options.compilerArgs.addAll(listOf(
        "-AreobfTsrgFile=${generateMcp2Srg.output!!.canonicalPath}",
        "-AoutTsrgFile=${file("$temporaryDir/$name-mappings.tsrg").canonicalPath}",
        "-AoutRefMapFile=${file("build/temp/refmap.json").canonicalPath}", // ${file("$temporaryDir/$name-refmap.json").canonicalPath}",
        "-AmappingTypes=tsrg"
    ))
}
