package scripts

import com.artur114.srptowerdefense.common.util.groovy.IGroovyEngine

import java.nio.file.Path
import java.nio.file.Paths

IGroovyEngine shell = shellIn
Path path = Paths.get("..", "src/test/groovy/scripts").toAbsolutePath().normalize()

shell.loadClass(path.resolve("classes/BaseDevScript.groovy"))