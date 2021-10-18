import ru.sber.filesystem.VFilesystem
import ru.sber.filesystem.VPath
import java.io.IOException
import java.io.PrintWriter
import java.net.ServerSocket
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
                val s = it.accept()
                handle(s, fs)
            }
        }
    }

    private fun handle(socket: Socket, fs: VFilesystem) {
        socket.use { s ->
            val reader = s.getInputStream().bufferedReader()
            val clientRequest = reader.readLine()

            val writer = PrintWriter(s.getOutputStream())
            val serverResponse = getResponse(clientRequest, fs)

            writer.println(serverResponse)
            writer.flush()

        }
    }

    private fun getResponse(clientRequest: String, fs: VFilesystem): String {
        val typeRequest = clientRequest.subSequence(0, 3)

        if (typeRequest == "GET") {
            val path = clientRequest.substringAfter(" ").substringBefore(" ")

            val fileContent = fs.readFile(VPath(path))

            if (fileContent != null)
                return buildOkResponse(fileContent)
        }

        return buildNotFoundErrorResponse()
    }

    private fun buildNotFoundErrorResponse(): String = "HTTP/1.0 404 Not Found\r\n\n" +
            "Server: FileServer\r\n\n" +
            "\r\n"

    private fun buildOkResponse(fileContent: String): String = "HTTP/1.0 200 OK\r\n" +
            "Server: FileServer\r\n" +
            "\r\n" +
            "$fileContent\r\n"
}