package service;

import javax.inject.Singleton;

@Singleton
public class HelloWorldPL implements HelloWorld {

    @Override
    public String say() {
        return "Witaj ?wiecie";
    }
}
