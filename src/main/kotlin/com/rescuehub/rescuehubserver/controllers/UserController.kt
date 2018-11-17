package com.rescuehub.rescuehubserver.entities

import com.rescuehub.rescuehubserver.entities.User
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import com.naturalprogrammer.spring.lemon.LemonController

@RestController
@RequestMapping("/core")
public class MyController : LemonController<User, Long>() {

}
