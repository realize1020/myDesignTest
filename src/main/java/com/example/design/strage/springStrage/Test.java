package com.example.design.strage.springStrage;

import com.example.design.strage.commonStrage.*;

/**
 * 因为是Spring方式启动，所以逻辑放到spring启动类里
 */
public class Test {
    public static void main(String[] args) {
        User user =new User();
        user.setUserType(1);
        System.out.println("打折之后，费用为："+charge(user));
    }


    /**
     * 这样当有新的user_type及价格计算方式出现时，
     * 只需要实现PriceStrategy接口并在对应的实现类上面加入@Component和@UserTypeAnnotation注解即可，无需修改业务代码 !
     * @param user
     * @return
     */
    public static double charge(User user){

        double cost = 100.0;
        return StrategyFactory.getPriceStrategyByUserType(user.getUserType()).calculatePrice(cost);
    }
}
