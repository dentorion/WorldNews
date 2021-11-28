package com.entin.worldnews.presentation.util.connection

import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

object PingGoogle {

    // Important to execute this on a background thread.
    fun execute(socketFactory: SocketFactory): Boolean {
        return try{
            // PINGING google
            val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            // PING success
            true
        }catch (e: IOException){
            // No internet connection
            false
        }
    }
}