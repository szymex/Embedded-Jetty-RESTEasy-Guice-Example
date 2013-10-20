package service;

import javax.inject.Singleton;

@Singleton
public class HelloWorldFI implements HelloWorld {

    @Override
    public String say() {
        return "Hei maailma!";
    }
}
