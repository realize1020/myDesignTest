package com.example.design;

import com.example.design.strage.commonStrage.User;
import com.example.design.strage.springStrage.StrategyFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringAndDesignApplication{

    public static void main(String[] args) {
        SpringApplication.run(SpringAndDesignApplication.class, args);


        //***********spring策略模式**********************************
        User user =new User();
        user.setUserType(1);
        System.out.println("打折之后，费用为："+charge(user));
        //***********spring策略模式**********************************
    }

    /**
     * spring策略模式
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
