
package com.example.eventservice.config;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
class PrototypeBean {
    public PrototypeBean() { System.out.println("PrototypeBean created: " + this); }
}

@Component
class SingletonBean {
    public SingletonBean() { System.out.println("SingletonBean created: " + this); }
}
