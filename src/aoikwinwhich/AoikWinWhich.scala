//
package aoikwinwhich
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import scala.collection.mutable.ListBuffer

//
object AoikWinWhich {

    def find_executable(prog: String): List[String] = {
        // 8f1kRCu
        val env_var_PATHEXT = System.getenv("PATHEXT")
        /// can be null

        // 6qhHTHF
        // split into a list of extensions
        var ext_s =
            if (env_var_PATHEXT == null)
                List[String]()
            else
                env_var_PATHEXT.split(File.pathSeparator).toList

        // 2pGJrMW
        // strip
        ext_s = ext_s.map(_.trim)

        // 2gqeHHl
        // remove empty
        ext_s = ext_s.filter(_ != "")

        // 2zdGM8W
        // convert to lowercase
        ext_s = ext_s.map(_.toLowerCase)

        // 2fT8aRB
        // uniquify
        ext_s = ext_s.distinct
        /// |distinct| keeps the original order.

        // 4ysaQVN
        val env_var_PATH = System.getenv("PATH")
        /// can be null

        // 6mPI0lg
        var dir_path_s =
            if (env_var_PATH == null)
                ListBuffer[String]()
            else
                env_var_PATH.split(File.pathSeparator).to[ListBuffer]

        // 5rT49zI
        // insert empty dir path to the beginning
        //
        // Empty dir handles the case that |prog| is a path, either relative or
        //  absolute. See code 7rO7NIN.
        "" +=: dir_path_s

        // 2klTv20
        // uniquify
        dir_path_s = dir_path_s.distinct
        /// |distinct| keeps the original order.

        // 6bFwhbv
        val exe_path_s = ListBuffer[String]()

        for (dir_path <- dir_path_s) {
            // 7rO7NIN
            // synthesize a path with the dir and prog
            val path =
                if (dir_path == "")
                    prog
                else
                    Paths.get(dir_path, prog).toString

            // 6kZa5cq
            // assume the path has extension, check if it is an executable
            if (ext_s.exists(path.endsWith _)) {
                if (Files.isRegularFile(Paths.get(path))) {
                    exe_path_s += path
                }
            }

            // 2sJhhEV
            // assume the path has no extension
            for (ext <- ext_s) {
                // 6k9X6GP
                // synthesize a new path with the path and the executable extension
                val path_plus_ext = path + ext

                // 6kabzQg
                // check if it is an executable
                if (Files.isRegularFile(Paths.get(path_plus_ext))) {
                    exe_path_s += path_plus_ext
                }
            }
        }

        //
        return exe_path_s.toList
    }

    def main(args: Array[String]) {
        // 9mlJlKg
        // check if one cmd arg is given
        if (args.length != 1) {
            // 7rOUXFo
            // print program usage
            println("Usage: aoikwinwhich PROG")
            println("")
            println("#/ PROG can be either name or path")
            println("aoikwinwhich notepad.exe")
            println("aoikwinwhich C:\\Windows\\notepad.exe")
            println("")
            println("#/ PROG can be either absolute or relative")
            println("aoikwinwhich C:\\Windows\\notepad.exe")
            println("aoikwinwhich Windows\\notepad.exe")
            println("")
            println("#/ PROG can be either with or without extension")
            println("aoikwinwhich notepad.exe")
            println("aoikwinwhich notepad")
            println("aoikwinwhich C:\\Windows\\notepad.exe")
            println("aoikwinwhich C:\\Windows\\notepad")

            // 3nqHnP7
            return
        }

        // 9m5B08H
        // get name or path of a program from cmd arg
        val prog = args(0)

        // 8ulvPXM
        // find executables
        val path_s = find_executable(prog)

        // 5fWrcaF
        // has found none, exit
        if (path_s.size == 0) {
            // 3uswpx0
            return
        }

        // 9xPCWuS
        // has found some, output
        val txt = path_s.mkString("\n")

        println(txt)

        //
        return
    }
}
