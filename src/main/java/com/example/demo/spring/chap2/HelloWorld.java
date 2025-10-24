package com.example.demo.spring.chap2;

public class HelloWorld {
    public static void main(String... args) {
        MessageRenderer mr =
                MessageSupportFactory.getInstance().getMessageRenderer();
        MessageProvider mp =
                MessageSupportFactory.getInstance().getMessageProvider();
        mr.setMessageProvider(mp);
        mr.render();
    }

    public static void main2(String[] args) {
        MessageRenderer mr = new StandardOutMessageRenderer();
        MessageProvider mp = new HelloWorldMessageProvider();
        mr.setMessageProvider(mp);
        mr.render();
    }

    public static void main1(String[] args) {
        if (args.length > 0) {
            System.out.println(args[0]);
        } else {
            System.out.println("Hello World!");
        }
    }
}
