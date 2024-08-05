package com.example.design.strage.commonStrage;

public class Test {
    public static void main(String[] args) {
        User user =new User();
        user.setUserType(1);

        System.out.println("打折之后，费用为："+charge(user));
    }

    /**
     * 有其他会员在这里继续加Strategy的实现类，增加case语句
     * 这样导致代码里面有大量的判断存在，如果有新的用户类型及对应的计算规则，需要修改这部分的代码 ，繁琐异常。
     *
     * 接下来利用SpringBoot对上述流程进行些微的改造：springStrage包下面
     * @param user
     * @return
     */

    public static double charge(User user){
        Charge charge;
        double cost =100;
        switch (user.getUserType()){
            case 1:
                charge = new Charge(new SeniorVIPStrategy());
                break;
            case 2:
                charge = new Charge(new IntermediateVIPStrategy());
                break;
            case 3:
                charge = new Charge(new InitialVIPStrategy());
                break;
            case 4:
                charge = new Charge(new UserStrategy());
                break;

            default:
                throw new RuntimeException("不支持的用户类型");
        }

        return charge.price(cost);
    }
}
