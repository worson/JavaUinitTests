package com.langogo.lib.log.rpc

import com.langogo.lib.log.L
import com.langogo.lib.log.rpc.PacketProtocol.INDEX_DATA_LENGTH
import com.langogo.lib.log.rpc.PacketProtocol.INDEX_FLAG_START
import com.langogo.lib.log.rpc.PacketProtocol.LENGTH_CRC_SIZE
import com.langogo.lib.log.rpc.PacketProtocol.MAX_PACKET_DATA_SIZE
import com.langogo.lib.log.rpc.PacketProtocol.MAX_PACKET_SIZE
import com.langogo.lib.log.rpc.PacketProtocol.PACKET_WRAPPER_SIZE
import java.nio.ByteBuffer
import java.util.zip.CRC32

/**
 * 说明:
 * @author wangshengxing  08.01 2020
 */
object SocketPacketUtil {

    val  TAG = "SocketPacketUtil"

    /**
     * 打包数据
     */
    fun pack(msg:String):ByteArray{
        val buffer = ByteBuffer.allocate(msg.length+PacketProtocol.PACKET_WRAPPER_SIZE)
        buffer.putInt(PacketProtocol.FLAG_PACKET_START)
        buffer.putInt(msg.length)
        val bar=msg.toByteArray(Charsets.UTF_8)
        buffer.put(bar)
        val crc = CRC32()
        crc.update(bar)
        val crcValue=crc.value
        buffer.putLong(crcValue)
        return ByteArray(buffer.position()).apply {
            buffer.flip()
            buffer.get(this)
        }

    }

    /**
     * 数据解包
     * 数据在读取前ByteBuffer需求读就绪
     */
    fun unpack(bufffer:ByteBuffer):String?{
        val startIndex=bufffer.position()

        if ((PACKET_WRAPPER_SIZE) >= (bufffer.limit()-startIndex) ){
            L.i(TAG, "unpack: packet length error , position=${bufffer.position()} , limit =${bufffer.limit()}")
            return null
        }

        val totalSize=bufffer.remaining()
        val flag_start=bufffer.getInt(startIndex+INDEX_FLAG_START)
        if (flag_start!=PacketProtocol.FLAG_PACKET_START) {
            L.i(TAG, "unpack: real flag start is ${flag_start}")
            bufffer.getInt()
            return null
        }
        val data_length=bufffer.getInt(startIndex+INDEX_DATA_LENGTH)
        if (data_length<=0){
            L.i(TAG, "unpack: data length error = $data_length")
            return null
        }
        if (totalSize<(data_length+PACKET_WRAPPER_SIZE)){
            L.i(TAG, "unpack: data packet not complete received")
            return null
        }
        //支持连续解包，不需要再处理
        /*if (totalSize>=MAX_PACKET_DATA_SIZE){
            L.i(TAG, "unpack: data length too big ${totalSize}")
            return null
        }*/

        //skip header
        bufffer.getInt()
        bufffer.getInt()

        val strBytes=ByteArray(data_length)
        bufffer.get(strBytes)

        val crcGet=bufffer.getLong()
        val crc = CRC32()
        crc.update(strBytes)
        val crcValue=crc.value
        if (crcGet!=crcValue){
            L.i(TAG, "unpack: crc error , read value=${crcGet}, packet check sum =${crcValue}")
            return null
        }

        return String(strBytes)


    }
}