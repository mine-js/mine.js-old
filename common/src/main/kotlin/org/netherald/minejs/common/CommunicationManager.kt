package org.netherald.minejs.common

interface CommunicationManager {
    fun tryCommunicate(plugin: String, args: ArrayList<Any>)
}