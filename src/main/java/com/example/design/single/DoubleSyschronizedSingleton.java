package com.example.design.single;

public class DoubleSyschronizedSingleton {
    private static DoubleSyschronizedSingleton instance;

    private DoubleSyschronizedSingleton(){}

    public  DoubleSyschronizedSingleton getInstance(){
        if(instance!=null){
            return instance;
        }
        synchronized(DoubleSyschronizedSingleton.class){
            if (instance==null){
                instance = new DoubleSyschronizedSingleton();
            }
        }
        return instance;
    }



    /**
     * 有的代码中会将同步锁synchronized写在方法中，例如：
     *    public static synchronized SingletonMode getInstance(){.....}
     * 造成的弊端就是：多线程每次在调用getInstance()时都会产生一个同步，造成损耗。
     * 相应的我们需要保持同步的代码块仅仅就是：
     *      singletonMode = new SingletonMode();
     * 所以只要在该代码处添加同步锁就可以了
     *
     */
//    public static SingletonMode getInstance() {
//        /**
//         * 此处检测singletonMode == null,是为了防止当singletonMode已经初始化后，
//         * 还会继续调用同步锁，造成不必要的损耗
//         */
//        if (singletonMode == null) {
//            // 加锁目的，防止多线程同时进入造成对象多次实例化
//            synchronized (SingletonMode.class) {
//                // 为了在null的情况下创建实例，防止不必要的损耗
//                if (singletonMode == null) {
//                    singletonMode = new SingletonMode();
//                }
//            }
//        }
//        // 返回实例对象
//        return singletonMode;
//    }

}
