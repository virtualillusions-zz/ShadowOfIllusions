package test.IO.blog.json.example;

import javax.xml.bind.JAXBContext;
import test.IO.blog.json.example.bar.Bar;
import test.IO.blog.json.example.foo.Foo;

public class Demo {

    public static void main(String[] args) throws Exception {
        System.out.println(JAXBContext.newInstance(Foo.class).getClass());
        System.out.println(JAXBContext.newInstance(Bar.class).getClass());
        System.out.println(JAXBContext.newInstance(Foo.class, Bar.class).getClass());
    }
}