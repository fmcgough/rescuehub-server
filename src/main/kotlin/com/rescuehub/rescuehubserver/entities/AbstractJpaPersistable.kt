package com.rescuehub.rescuehubserver.entities

import javax.persistence.MappedSuperclass
import java.io.Serializable
import org.springframework.data.util.ProxyUtils


@MappedSuperclass
abstract class AbstractJpaPersistable<T : Serializable> {

    companion object {
        private val serialVersionUID = -5554308939380869754L
    }

    abstract val id: T

    override final fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true

        if (javaClass != ProxyUtils.getUserClass(other)) return false

        other as AbstractJpaPersistable<*>

        return this.id === other.id
    }

    override final fun hashCode(): Int {
        return 31
    }

}
