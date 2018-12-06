package com.rescuehub.rescuehubserver.security

import org.springframework.security.core.annotation.AuthenticationPrincipal

/**
 * This meta-annotation can be used to inject the currently authenticated user principal in
 * the controllers.
 */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@AuthenticationPrincipal
annotation class CurrentUser {

}
