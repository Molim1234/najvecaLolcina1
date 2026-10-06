package com.example.najvecaLolcina.mapper;

import com.example.najvecaLolcina.entity.Coupon;
import com.example.najvecaLolcina.entity.CouponDTO;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public Coupon toCoupon(CouponDTO couponDTO){
        return new Coupon(couponDTO.getCoupon(), couponDTO.getPercentOfDscount(), couponDTO.getUses(),
                couponDTO.getExpiringDate()
                );
    }

}
