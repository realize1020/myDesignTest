package com.example.design.reflect.isAssignableFrom和instanceof的区别;

/**
 * isAssignableFrom vs instanceof 区别示例
 * 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 * 公众号：bugstack虫洞栈
 * Create by 小傅哥(fustack)
 */
public class TypeCheckExample {
    
    // 父类
    static class Animal {
        public void eat() {
            System.out.println("Animal is eating");
        }
    }
    
    // 子类
    static class Dog extends Animal {
        public void bark() {
            System.out.println("Dog is barking");
        }
    }
    
    // 接口
    interface Runnable {
        void run();
    }
    
    // 实现类
    static class Cat extends Animal implements Runnable {
        public void run() {
            System.out.println("Cat is running");
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== isAssignableFrom vs instanceof 区别示例 ===\n");
        
        // 创建对象实例
        Animal animal = new Animal();
        Dog dog = new Dog();
        Cat cat = new Cat();
        
        // 1. instanceof 操作符示例
        System.out.println("1. instanceof 操作符（检查对象实例的类型关系）：");
        System.out.println("dog instanceof Animal: " + (dog instanceof Animal)); // true
        System.out.println("dog instanceof Dog: " + (dog instanceof Dog));       // true
        System.out.println("cat instanceof Runnable: " + (cat instanceof Runnable)); // true
        System.out.println("animal instanceof Dog: " + (animal instanceof Dog)); // false
        System.out.println("null instanceof Animal: " + (null instanceof Animal)); // false
        
        System.out.println("\n2. isAssignableFrom 方法（检查类之间的继承关系）：");
        // 2. isAssignableFrom 方法示例
        System.out.println("Animal.class.isAssignableFrom(Dog.class): " + 
            Animal.class.isAssignableFrom(Dog.class)); // true
        System.out.println("Dog.class.isAssignableFrom(Animal.class): " + 
            Dog.class.isAssignableFrom(Animal.class)); // false
        System.out.println("Runnable.class.isAssignableFrom(Cat.class): " + 
            Runnable.class.isAssignableFrom(Cat.class)); // true
        System.out.println("Object.class.isAssignableFrom(String.class): " + 
            Object.class.isAssignableFrom(String.class)); // true
        
        System.out.println("\n3. 关键区别对比：");
        
        // 区别1：操作对象不同
        System.out.println("区别1 - 操作对象不同：");
        System.out.println("   instanceof: 左边是对象实例，右边是类名");
        System.out.println("   isAssignableFrom: 左边是Class对象，右边是Class对象");
        
        // 区别2：null值处理
        System.out.println("\n区别2 - null值处理：");
        System.out.println("   null instanceof Animal: " + (null instanceof Animal)); // false
        try {
            System.out.println("   Animal.class.isAssignableFrom(null): 会抛出NullPointerException");
            Animal.class.isAssignableFrom(null);
        } catch (NullPointerException e) {
            System.out.println("   实际抛出: " + e.getClass().getSimpleName());
        }
        
        // 区别3：编译时检查
        System.out.println("\n区别3 - 编译时检查：");
        System.out.println("   instanceof: 编译时会检查类型兼容性");
        System.out.println("   isAssignableFrom: 运行时检查，更灵活");
        
        // 区别4：使用场景
        System.out.println("\n区别4 - 使用场景：");
        System.out.println("   instanceof: 检查具体对象实例的类型");
        System.out.println("   isAssignableFrom: 检查类之间的继承关系，常用于反射和框架");
        
        System.out.println("\n4. 实际应用示例：");
        
        // 实际应用：Spring框架中的类型检查
        checkBeanType(animal, "animal");
        checkBeanType(dog, "dog");
        checkBeanType(cat, "cat");
        
        // 泛型类型检查示例
        checkGenericType();
    }
    
    /**
     * 模拟Spring框架中的Bean类型检查
     */
    public static void checkBeanType(Object bean, String beanName) {
        System.out.println("\n检查Bean '" + beanName + "' 的类型：");
        
        // 使用instanceof检查具体类型
        if (bean instanceof Dog) {
            System.out.println("   instanceof Dog: 这是一个Dog对象");
            ((Dog) bean).bark();
        }
        
        if (bean instanceof Runnable) {
            System.out.println("   instanceof Runnable: 实现了Runnable接口");
            ((Runnable) bean).run();
        }
        
        // 使用isAssignableFrom检查类关系
        Class<?> beanClass = bean.getClass();
        if (Animal.class.isAssignableFrom(beanClass)) {
            System.out.println("   isAssignableFrom Animal: 是Animal或其子类");
        }
        
        if (Runnable.class.isAssignableFrom(beanClass)) {
            System.out.println("   isAssignableFrom Runnable: 实现了Runnable接口");
        }
    }
    
    /**
     * 泛型类型检查示例
     */
    public static void checkGenericType() {
        System.out.println("\n5. 泛型类型检查示例：");
        
        // 创建不同类型的List
        java.util.List<String> stringList = new java.util.ArrayList<>();
        java.util.List<Integer> integerList = new java.util.ArrayList<>();
        
        // 使用isAssignableFrom检查集合类型
        System.out.println("List.class.isAssignableFrom(ArrayList.class): " + 
            java.util.List.class.isAssignableFrom(java.util.ArrayList.class)); // true
        
        System.out.println("ArrayList.class.isAssignableFrom(LinkedList.class): " + 
            java.util.ArrayList.class.isAssignableFrom(java.util.LinkedList.class)); // false
        
        // 运行时类型擦除后的检查
        System.out.println("stringList instanceof List: " + (stringList instanceof java.util.List)); // true
        System.out.println("integerList instanceof List: " + (integerList instanceof java.util.List)); // true
        
        // 但无法检查泛型参数类型（类型擦除）
        System.out.println("注意：由于类型擦除，无法在运行时检查List<String>和List<Integer>的区别");
    }
}