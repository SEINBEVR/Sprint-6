import ru.sber.filesystem.VFilesystem
import java.io.IOException
import java.net.ServerSocket
import ru.sber.filesystem.VPath
import java.io.PrintWriter
import java.net.Socket

/**
 * A basic and very limited implementation of a file server that responds to GET
 * requests from HTTP clients.
 */
class FileServer {

    /**
     * Main entrypoint for the basic file server.
     *
     * @param socket Provided socket to accept connections on.
     * @param fs     A proxy filesystem to serve files from. See the VFilesystem
     *               class for more detailed documentation of its usage.
     * @throws IOException If an I/O error is detected on the server. This
     *                     should be a fatal error, your file server
     *                     implementation is not expected to ever throw
     *                     IOExceptions during normal operation.
     */
    @Throws(IOException::class)
    fun run(socket: ServerSocket, fs: VFilesystem) {

        /**
         * Enter a spin loop for handling client requests to the provided
         * ServerSocket object.
         */

            socket.use {
                while (true) {

                    val clientSocket = it.accept()
                    handle(clientSocket, fs)
                }
            }
        }

    private fun handle(socket: Socket, fs: VFilesystem) {
        socket.use { s ->
            s.getInputStream().bufferedReader().use { r ->
                val clientRequest = r.readLine()
                val path = clientRequest.drop(4).substringBefore(' ')
                val serverResponse = getStatus(path, fs)
                returnAnswer(socket, serverResponse)
            }
        }
    }

    private fun returnAnswer(socket: Socket, serverResponse: String) {
        PrintWriter(socket.getOutputStream()).use { w ->
            w.println(serverResponse)
            w.flush()
        }
    }

    private fun getStatus(path: String, fs: VFilesystem): String {
        val vPath = VPath(path)

        return if(fs.readFile(vPath) != null) {
            HTTPStatus.OK_200.answer + fs.readFile(vPath)
        } else {
            HTTPStatus.NOTFOUND_404.answer
        }
    }

    enum class HTTPStatus(val answer: String) {
        OK_200(
            "HTTP/1.0 200 OK\r\n" +
                    "Server: FileServer\r\n" +
                    "\r\n"
        ),
        NOTFOUND_404(
            "HTTP/1.0 404 Not Found\r\n\n" +
                    "Server: FileServer\r\n\n" +
                    "\r\n"
        )
    }
}
/*
    * 2) Using Socket.getInputStream(), parse the received HTTP
    * packet. In particular, we are interested in confirming this
    * message is a GET and parsing out the path to the file we are
    * GETing. Recall that for GET HTTP packets, the first line of the
    * received packet will look something like:
    *
    *     GET /path/to/file HTTP/1.1
    */


/*
* 3) Using the parsed path to the target file, construct an
* HTTP reply and write it to Socket.getOutputStream(). If the file
* exists, the HTTP reply should be formatted as follows:
*
*   HTTP/1.0 200 OK\r\n
*   Server: FileServer\r\n
*   \r\n
*   FILE CONTENTS HERE\r\n
*
* If the specified file does not exist, you should return a reply
* with an error code 404 Not Found. This reply should be formatted
* as:
*
*   HTTP/1.0 404 Not Found\r\n
*   Server: FileServer\r\n
*   \r\n
*
* Don't forget to close the output stream.
*/