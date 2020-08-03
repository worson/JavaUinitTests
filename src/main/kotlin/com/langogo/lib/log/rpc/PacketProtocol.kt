package com.langogo.lib.log.rpc

/**
 * 说明:
 * @author wangshengxing  08.01 2020
 * 协议结构 [starts flag(4byte)] + [dateLength(4 byte)]+[data] + [data crc [4 byte]]
 */
object PacketProtocol {

    const val LENGTH_CRC_SIZE = 8

    const val INDEX_FLAG_START = 0

    const val INDEX_DATA_LENGTH = 4

    const val INDEX_DATA_START = 4


    const val MAX_PACKET_SIZE: Int = 2048

    /**
     * 包非数据的长度
     */
    const val PACKET_WRAPPER_SIZE: Int = (8+LENGTH_CRC_SIZE)

    const val MAX_PACKET_DATA_SIZE: Int = (MAX_PACKET_SIZE - PACKET_WRAPPER_SIZE)

    /**
     * 默认端口号
     */
    const val DEFAULT_SERVER_PORT: Int = 22612

    const val DEFAULT_SERVER_IP = "127.0.0.1"

    /**
     * 包的起始标志
     */
    const val FLAG_PACKET_START: Int = 0x0f0f0f0f





}