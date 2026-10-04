package com.example.najvecaLolcina.Mapper;

import com.example.najvecaLolcina.Entity.Coupon;
import com.example.najvecaLolcina.Entity.CouponDTO;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public Coupon toCoupon(CouponDTO couponDTO){
        return new Coupon(couponDTO.getCoupon(), couponDTO.getPercentOfDscount(), couponDTO.getUses(),
                couponDTO.getExpiringDate()
                );
    }

}
