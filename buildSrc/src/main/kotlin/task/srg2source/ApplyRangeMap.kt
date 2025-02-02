package task.srg2source

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import task.base.MavenJarExec

import java.io.File

open class ApplyRangeMap : MavenJarExec() {

    @InputDirectory
    var input: File? = null

    @InputFile
    var range: File? = null

    @OutputFile
    var output: File? = null

    @InputFile
    var mappings: File? = null

//    @InputFile
//    var exc: File? = null

    @Input
    var keepImports = true

    init {
        toolJar = "net.minecraftforge:Srg2Source:5.+:fatjar"
        args = arrayOf( "--apply", "--input", "{input}", "--range", "{range}", "--srg", "{mappings}", /* "--exc", "{exc}", */ "--output", "{output}", "--keepImports", "{keepImports}")
    }

    override fun filterArgs(): List<String> {
        val replace = mapOf(
                "{input}" to (input?.absolutePath ?: ""),
                "{range}" to (range?.absolutePath ?: ""),
                "{mappings}" to (mappings?.absolutePath ?: ""),
//                "{exc}" to (exc?.absolutePath ?: ""),
                "{output}" to (output?.absolutePath ?: ""),
                "{keepImports}" to (if (keepImports) "true" else "false")
        )

        return args.map { arg -> replace.getOrDefault(arg, arg) }
    }

}