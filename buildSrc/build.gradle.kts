plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    maven {
        name = "MinecraftForge Maven"
        url = uri("http://files.minecraftforge.net/maven/")
    }
}

dependencies {
    implementation("com.google.guava:guava:30.1.1-jre")
    implementation("commons-io:commons-io:2.8.0")
    implementation("net.minecraftforge:srgutils:0.4.1")
}

gradlePlugin {
    plugins.register("mcp-plugin") {
        id = "mcp-plugin"
        implementationClass = "mcp.MCPPlugin"
    }
}